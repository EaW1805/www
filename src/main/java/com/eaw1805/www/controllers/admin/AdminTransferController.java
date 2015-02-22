package com.eaw1805.www.controllers.admin;

import com.eaw1805.data.managers.beans.PaymentHistoryManagerBean;
import com.eaw1805.data.model.Follow;
import com.eaw1805.data.model.PaymentHistory;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.WatchGame;
import com.eaw1805.www.commands.AdminTransferCommand;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.validators.AdminTransferValidator;
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
public class AdminTransferController
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(AdminTransferController.class);

    protected static final String MODEL_JSTL_KEY = "adminTransferCommand";

    @ModelAttribute(MODEL_JSTL_KEY)
    public AdminTransferCommand populateUser() {
        return new AdminTransferCommand();
    }

    @ModelAttribute("page")
    public String populatePage() {
        return getPage();
    }

    @ModelAttribute("followingListAll")
    public Set<User> getFollowingAll() {
        ScenarioContextHolder.defaultScenario();
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
            Calendar thatCal = calendar(game.getGame());
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

    @RequestMapping(method = RequestMethod.GET, value = "/adminTransfer")
    public String setupForm(final ModelMap model) {
        return "user/settings";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/adminTransfer")
    public String processAdminTransferSubmit(@ModelAttribute(MODEL_JSTL_KEY) AdminTransferCommand transferCommand,
                                             final BindingResult result, final SessionStatus status) {
        ScenarioContextHolder.defaultScenario();
        LOGGER.debug("Admin processing transfer");

        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() <= 0 || thisUser.getUserType() != 3) {
            return "user/login";
        }

        final User receiverUser = userManager.getByUserName(transferCommand.getReceiver());
        if (receiverUser == null) {
            transferCommand.setReceiver("");
        }

        final AdminTransferValidator validator = new AdminTransferValidator();
        validator.validate(transferCommand, result);
        if (result.hasErrors()) {
            return "redirect:user/" + receiverUser.getUsername();
        }

        final User admin = userManager.getByUserName("admin");

        final int freeCreditsTransferred = Integer.decode(transferCommand.getFreeCreditsAmount());
        final int boughtCreditsTransferred = Integer.decode(transferCommand.getBoughtCreditsAmount());
        final int transferredCreditsTransferred = Integer.decode(transferCommand.getTransferredCreditsAmount());

        final int freeCreditsAmount = receiverUser.getCreditFree();
        final int boughtCreditsAmount = receiverUser.getCreditBought();
        final int transferredCreditsAmount = receiverUser.getCreditTransferred();

        final int adminFreeCreditsAmount = admin.getCreditFree();
        final int adminBoughtCreditsAmount = admin.getCreditBought();
        final int adminTransferredCreditsAmount = admin.getCreditTransferred();

        LOGGER.debug("receiver:" + transferCommand.getReceiver() +
                " freeCreditsAmount : " + freeCreditsTransferred +
                " boughtCreditsAmount : " + boughtCreditsTransferred +
                " transferredCreditsAmount : " + transferredCreditsTransferred);

        if (freeCreditsTransferred != 0) {

            receiverUser.setCreditFree(freeCreditsAmount + freeCreditsTransferred);
            admin.setCreditFree(adminFreeCreditsAmount - freeCreditsTransferred);

            final PaymentHistory receiverUserPayment = new PaymentHistory();
            receiverUserPayment.setUser(receiverUser);

            if (freeCreditsTransferred > 0) {
                receiverUserPayment.setComment("Administrator credited "
                        + freeCreditsTransferred + " free credits to " + receiverUser.getUsername()
                        + " (" + transferCommand.getComment() + " )");
            } else {
                receiverUserPayment.setComment("Administrator debited "
                        + -freeCreditsTransferred + " free credits to " + receiverUser.getUsername()
                        + " (" + transferCommand.getComment() + ")");
            }

            receiverUserPayment.setDate(new Date());
            receiverUserPayment.setAgent(thisUser);
            receiverUserPayment.setType(PaymentHistory.TYPE_TRANSFER);
            receiverUserPayment.setCreditFree(receiverUser.getCreditFree());
            receiverUserPayment.setCreditBought(receiverUser.getCreditBought());
            receiverUserPayment.setCreditTransferred(receiverUser.getCreditTransferred());
            receiverUserPayment.setChargeBought(0);
            receiverUserPayment.setChargeFree(freeCreditsTransferred);
            receiverUserPayment.setChargeTransferred(0);

            pmHistoryManager.add(receiverUserPayment);

            final PaymentHistory adminUserPayment = new PaymentHistory();
            adminUserPayment.setUser(admin);

            if (freeCreditsTransferred > 0) {
                adminUserPayment.setComment(thisUser.getUsername() + " credited "
                        + freeCreditsTransferred + " free credits to " + receiverUser.getUsername()
                        + " (" + transferCommand.getComment() + " )");
            } else {
                adminUserPayment.setComment(thisUser.getUsername() + " debited "
                        + -freeCreditsTransferred + " free credits to " + receiverUser.getUsername()
                        + " (" + transferCommand.getComment() + ")");
            }

            adminUserPayment.setDate(new Date());
            adminUserPayment.setAgent(receiverUser);
            adminUserPayment.setType(PaymentHistory.TYPE_TRANSFER);
            adminUserPayment.setCreditFree(admin.getCreditFree());
            adminUserPayment.setCreditBought(admin.getCreditBought());
            adminUserPayment.setCreditTransferred(admin.getCreditTransferred());
            adminUserPayment.setChargeBought(0);
            adminUserPayment.setChargeFree(-freeCreditsTransferred);
            adminUserPayment.setChargeTransferred(0);

            pmHistoryManager.add(adminUserPayment);


        }

        if (boughtCreditsTransferred != 0) {
            receiverUser.setCreditBought(boughtCreditsAmount + boughtCreditsTransferred);
            admin.setCreditBought(adminBoughtCreditsAmount - boughtCreditsTransferred);

            final PaymentHistory receiverUserPayment = new PaymentHistory();
            receiverUserPayment.setUser(receiverUser);

            if (boughtCreditsTransferred > 0) {
                receiverUserPayment.setComment("Administrator credited "
                        + boughtCreditsTransferred + " credits to " + receiverUser.getUsername()
                        + " (" + transferCommand.getComment() + " )");
            } else {
                receiverUserPayment.setComment("Administrator debited "
                        + -boughtCreditsTransferred + " credits to " + receiverUser.getUsername()
                        + " (" + transferCommand.getComment() + ")");
            }

            receiverUserPayment.setDate(new Date());
            receiverUserPayment.setAgent(thisUser);
            receiverUserPayment.setType(PaymentHistory.TYPE_TRANSFER);
            receiverUserPayment.setCreditFree(receiverUser.getCreditFree());
            receiverUserPayment.setCreditBought(receiverUser.getCreditBought());
            receiverUserPayment.setCreditTransferred(receiverUser.getCreditTransferred());
            receiverUserPayment.setChargeBought(boughtCreditsTransferred);
            receiverUserPayment.setChargeFree(0);
            receiverUserPayment.setChargeTransferred(0);

            pmHistoryManager.add(receiverUserPayment);

            final PaymentHistory adminUserPayment = new PaymentHistory();
            adminUserPayment.setUser(admin);

            if (boughtCreditsTransferred > 0) {
                adminUserPayment.setComment(thisUser.getUsername() + " credited "
                        + boughtCreditsTransferred + " credits to " + receiverUser.getUsername()
                        + " (" + transferCommand.getComment() + " )");
            } else {
                adminUserPayment.setComment(thisUser.getUsername() + " debited "
                        + -boughtCreditsTransferred + " credits to " + receiverUser.getUsername()
                        + " (" + transferCommand.getComment() + ")");
            }

            adminUserPayment.setDate(new Date());
            adminUserPayment.setAgent(receiverUser);
            adminUserPayment.setType(PaymentHistory.TYPE_TRANSFER);
            adminUserPayment.setCreditFree(admin.getCreditFree());
            adminUserPayment.setCreditBought(admin.getCreditBought());
            adminUserPayment.setCreditTransferred(admin.getCreditTransferred());
            adminUserPayment.setChargeBought(-boughtCreditsTransferred);
            adminUserPayment.setChargeFree(0);
            adminUserPayment.setChargeTransferred(0);

            pmHistoryManager.add(adminUserPayment);
        }

        if (transferredCreditsTransferred != 0) {

            receiverUser.setCreditTransferred(transferredCreditsAmount + transferredCreditsTransferred);
            admin.setCreditTransferred(adminTransferredCreditsAmount - transferredCreditsTransferred);

            final PaymentHistory receiverUserPayment = new PaymentHistory();
            receiverUserPayment.setUser(receiverUser);

            if (transferredCreditsTransferred > 0) {
                receiverUserPayment.setComment("Administrator credited "
                        + transferredCreditsTransferred + " transferred credits to " + receiverUser.getUsername()
                        + " (" + transferCommand.getComment() + " )");
            } else {
                receiverUserPayment.setComment("Administrator debited "
                        + -transferredCreditsTransferred + " transferred credits to " + receiverUser.getUsername()
                        + " (" + transferCommand.getComment() + ")");
            }

            receiverUserPayment.setDate(new Date());
            receiverUserPayment.setAgent(thisUser);
            receiverUserPayment.setType(PaymentHistory.TYPE_TRANSFER);
            receiverUserPayment.setCreditFree(receiverUser.getCreditFree());
            receiverUserPayment.setCreditBought(receiverUser.getCreditBought());
            receiverUserPayment.setCreditTransferred(receiverUser.getCreditTransferred());
            receiverUserPayment.setChargeBought(0);
            receiverUserPayment.setChargeFree(0);
            receiverUserPayment.setChargeTransferred(transferredCreditsTransferred);

            pmHistoryManager.add(receiverUserPayment);

            final PaymentHistory adminUserPayment = new PaymentHistory();
            adminUserPayment.setUser(admin);

            if (transferredCreditsTransferred > 0) {
                adminUserPayment.setComment(thisUser.getUsername() + " credited "
                        + transferredCreditsTransferred + " transferred credits to " + receiverUser.getUsername()
                        + " (" + transferCommand.getComment() + " )");
            } else {
                adminUserPayment.setComment(thisUser.getUsername() + " debited "
                        + -transferredCreditsTransferred + " transferred credits to " + receiverUser.getUsername()
                        + " (" + transferCommand.getComment() + ")");
            }

            adminUserPayment.setDate(new Date());
            adminUserPayment.setAgent(receiverUser);
            adminUserPayment.setType(PaymentHistory.TYPE_TRANSFER);
            adminUserPayment.setCreditFree(admin.getCreditFree());
            adminUserPayment.setCreditBought(admin.getCreditBought());
            adminUserPayment.setCreditTransferred(admin.getCreditTransferred());
            adminUserPayment.setChargeBought(0);
            adminUserPayment.setChargeFree(0);
            adminUserPayment.setChargeTransferred(-transferredCreditsTransferred);

            pmHistoryManager.add(adminUserPayment);
        }

        userManager.update(admin);
        userManager.update(receiverUser);

        return "redirect:user/" + receiverUser.getUsername();
    }

    /**
     * Instance of PaymentHistoryManager class.
     */
    protected transient PaymentHistoryManagerBean pmHistoryManager;

    public void setPmHistoryManager(PaymentHistoryManagerBean pmHistoryManager) {
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
