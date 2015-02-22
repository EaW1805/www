package com.eaw1805.www.controllers.user;

import com.eaw1805.data.managers.beans.PaymentHistoryManagerBean;
import com.eaw1805.data.model.Follow;
import com.eaw1805.data.model.PaymentHistory;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.WatchGame;
import com.eaw1805.www.commands.PaypalCommand;
import com.eaw1805.www.commands.TransferCommand;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.validators.TransferValidator;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Controller
public class TransferCreditsController
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(TransferCreditsController.class);

    protected static final String MODEL_JSTL_KEY = "transferCommand";

    @ModelAttribute(MODEL_JSTL_KEY)
    public TransferCommand populateUser() {
        return new TransferCommand();
    }

    @ModelAttribute("page")
    public String populatePage() {
        return getPage();
    }

    @ModelAttribute("paypalCommand")
    public PaypalCommand paypalCommand() {
        return new PaypalCommand();
    }

    @ModelAttribute("followingListAll")
    public Set<User> getFollowingAll() {
        // get this user leaders.
        final List<Follow> leaders = followManager.listByFollower(getUser(), true);
        final TreeSet<User> leadersSet = new TreeSet<User>(new Comparator<User>() {
            @Override
            public int compare(final User thisUser, final User otherUser) {
                return thisUser.getUsername().compareToIgnoreCase(otherUser.getUsername());
            }
        });
        for (final Follow leader : leaders) {
            leadersSet.add(leader.getLeader());
        }
        return leadersSet;
    }

    @ModelAttribute("providerIds")
    public Set<String> getProviderIds() {
        return conFactoryLocator.registeredProviderIds();
    }

    @ModelAttribute("connectionMap")
    public Map<String, List<Connection<?>>> getConnectionMap() {
        return conRepository.findAllConnections();
    }

    @ModelAttribute("history")
    public List<PaymentHistory> getPaymentHistory() {
        return pmHistoryManager.list(getUser());
    }

    @ModelAttribute("dates")
    public Map<Integer, String> getGamesDates() {
        ScenarioContextHolder.defaultScenario();
        // Calculate calendar dates for each game
        final Map<Integer, String> dates = new HashMap<Integer, String>();

        for (final WatchGame game : watchGameManager.listByUser(getUser())) {
            final StringBuilder strBuilder = new StringBuilder();
            final Calendar thatCal = calendar(game.getGame());
            strBuilder.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
            strBuilder.append(" ");
            strBuilder.append(thatCal.get(Calendar.YEAR));
            dates.put(game.getWatchGameId(), strBuilder.toString());
        }
        return dates;
    }

    /**
     * Get the current page of settings to display.
     *
     * @return A string that shows the page.
     */
    public String getPage() {
        return "transfer";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/transfer")
    public String setupForm(final ModelMap model) {
        return "user/settings";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/transfer")
    public String processTransferCredits(@ModelAttribute(MODEL_JSTL_KEY) final TransferCommand transferCommand,
                                         final BindingResult result, final SessionStatus status) {
        LOGGER.debug("processing transfer");

        //get http request object.
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() <= 0) {
            return "user/login";
        }

        LOGGER.debug("user " + thisUser.getUsername());

        //read inputs from request
        final String receiverStr = request.getParameter("receiverUserName");
        final User receiverUser = userManager.getByUserName(receiverStr);
        if (transferCommand.getCreditsAmount() == null || transferCommand.getCreditsAmount().isEmpty()) {
            transferCommand.setCreditsAmount("0");
        }
        final int amount = Integer.parseInt(transferCommand.getCreditsAmount());

        //Construct a transfer Command
        transferCommand.setSender(thisUser);
        transferCommand.setReceiver(receiverUser);

        final TransferValidator transferValidator = new TransferValidator();
        transferValidator.validate(transferCommand, result);
        if (result.hasErrors()) {
            return "user/settings";
        }

        LOGGER.debug("sender: + " + thisUser.getUsername() + " receiver:" + receiverStr + " : amount : " + transferCommand.getCreditsAmount());

        thisUser.setCreditBought(thisUser.getCreditBought() - amount);
        receiverUser.setCreditTransferred(receiverUser.getCreditTransferred() + amount);
        final PaymentHistory thisUserPayment = new PaymentHistory();
        thisUserPayment.setUser(thisUser);
        thisUserPayment.setComment("User " + thisUser.getUsername() + " transfers "
                + amount + " credits to " + receiverUser.getUsername());
        thisUserPayment.setDate(new Date());
        thisUserPayment.setAgent(receiverUser);
        thisUserPayment.setType(PaymentHistory.TYPE_OFFER);
        thisUserPayment.setCreditFree(thisUser.getCreditFree());
        thisUserPayment.setCreditBought(thisUser.getCreditBought());
        thisUserPayment.setCreditTransferred(thisUser.getCreditTransferred());
        thisUserPayment.setChargeBought(-amount);
        thisUserPayment.setChargeFree(0);
        thisUserPayment.setChargeTransferred(0);

        final PaymentHistory receiverUserPayment = new PaymentHistory();
        receiverUserPayment.setUser(receiverUser);
        receiverUserPayment.setComment("User " + receiverUser.getUsername() + " received "
                + amount + " credits from " + thisUser.getUsername());
        receiverUserPayment.setDate(new Date());
        receiverUserPayment.setAgent(thisUser);
        receiverUserPayment.setType(PaymentHistory.TYPE_OFFER);
        receiverUserPayment.setCreditFree(receiverUser.getCreditFree());
        receiverUserPayment.setCreditBought(receiverUser.getCreditBought());
        receiverUserPayment.setCreditTransferred(receiverUser.getCreditTransferred());
        receiverUserPayment.setChargeBought(amount);
        receiverUserPayment.setChargeFree(0);
        receiverUserPayment.setChargeTransferred(0);

        userManager.update(thisUser);
        userManager.update(receiverUser);
        pmHistoryManager.add(thisUserPayment);
        pmHistoryManager.add(receiverUserPayment);

        return "redirect:settings";
    }

    /**
     * Instance of PaymentHistoryManager class.
     */
    protected transient PaymentHistoryManagerBean pmHistoryManager;

    public void setPmHistoryManager(final PaymentHistoryManagerBean pmHistoryManager) {
        this.pmHistoryManager = pmHistoryManager;
    }

    /**
     * Instance of ConnectionRepository class.
     */
    protected transient ConnectionRepository conRepository;

    /**
     * Instance of ConnectionFactoryLocator class..
     */
    protected transient ConnectionFactoryLocator conFactoryLocator;

    public void setConFactoryLocator(final ConnectionFactoryLocator conFactoryLocator) {
        this.conFactoryLocator = conFactoryLocator;
    }

    public void setConRepository(final ConnectionRepository conRepository) {
        this.conRepository = conRepository;
    }
}
