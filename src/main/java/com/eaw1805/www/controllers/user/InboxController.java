package com.eaw1805.www.controllers.user;

import com.eaw1805.data.model.Message;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.antisamy.AntisamyManager;
import com.eaw1805.www.controllers.cache.async.EawAsync;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.controllers.validators.InboxValidator;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.beans.support.PagedListHolder;
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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Display the messages of the user account.
 */
@org.springframework.stereotype.Controller
public class InboxController
        extends BaseController {

    private static final String MODEL_JSTL_KEY = "message";

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(InboxController.class);

    /**
     * Instance of AntisamManager class.
     */
    protected transient AntisamyManager antisamyManager;

    @ModelAttribute(MODEL_JSTL_KEY)
    public Message getCommandObject() {
        return new Message();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/inbox")
    public String processInboxSubmit(
            @ModelAttribute(MODEL_JSTL_KEY) Message message, final BindingResult result,
            final SessionStatus status, final ModelMap model) throws InvalidPageException {
        ScenarioContextHolder.defaultScenario();

        //get http request object.
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        final User thisUser = getUser();
        if (thisUser == null || thisUser.getUserId() == -1) {
            throw new InvalidPageException("Access Denied");
        }
        model.addAllAttributes(prepareInbox(thisUser));

        final String recipient = request.getParameter("recipient");

        //check that recipient is given.
        if (recipient == null || recipient.isEmpty()) {
            result.rejectValue("receiver", "invalid.receiver");
            return "user/inbox";
        }

        final User endUser = userManager.getByUserName(recipient);

        //check that users are valid.
        if (endUser == null || thisUser.equals(endUser)) {
            result.rejectValue("receiver", "invalid.receiver");
            return "user/inbox";
        }

        //Clear html code.
        message.setBodyMessage(antisamyManager.scanWithOutImgTag(message.getBodyMessage()));
        message.setSubject(antisamyManager.scanWithOutImgTag(message.getSubject()));

        final InboxValidator inboxValidator = new InboxValidator();
        inboxValidator.validate(message, result);
        if (result.hasErrors()) {
            return "user/inbox";
        }

        final Message rootMessage =
                messageManager.getRootMessage(thisUser.getUserId(), endUser.getUserId(), message.getSubject());
        //This is a new root message
        if (rootMessage == null) {
            message.setRootId(0);
        } else {
            message.setRootId(rootMessage.getMessageId());
        }

        message.setSender(thisUser);
        message.setReceiver(endUser);
        message.setDate(new Date());
        message.setOpened(false);

        if (!message.getSubject().isEmpty()
                && message.getSubject().trim().length() > 0) {
            messageManager.add(message);
            if (message.getReceiver().getEnableNotifications()) {
                if (message.getRootId() == 0) {
                    final Message thisMessage =
                            messageManager.getRootMessage(thisUser.getUserId(), endUser.getUserId(), message.getSubject());
                    message.setRootId(thisMessage.getMessageId());
                }

                sendMessageNotification(message);
            }
        } else {
            result.rejectValue("subject", "required.subject");
            return "user/inbox";
        }

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Inbox Post");
        model.addAllAttributes(prepareInbox(thisUser));
        model.addAttribute("message", new Message());
        return "user/inbox";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/inbox")
    public String setupInbox(final ModelMap model) throws InvalidPageException {

        final User thisUser = getUser();
        //be sure user is logged in.
        if (thisUser == null || thisUser.getUserId() == -1) {
            throw new InvalidPageException("Access Denied");
        }

        model.addAllAttributes(prepareInbox(thisUser));

        messageManager.updateAllMessagesToOpened(thisUser.getUserId());
        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Inbox");
        return "user/inbox";
    }

    /**
     * Formats message date.
     *
     * @param userMessages a List with the messages,
     * @return the formatted dates.
     */
    private HashMap<Integer, String> formatMessageDate(final List<Message> userMessages) {
        final HashMap<Integer, String> messageToTime = new HashMap<Integer, String>();

        final Calendar now = Calendar.getInstance();
        long nowLong = now.getTimeInMillis();

        final Calendar theBeginning = Calendar.getInstance();
        theBeginning.setTimeInMillis(0);

        final Calendar msgCal = Calendar.getInstance();
        for (final Message message : userMessages) {
            message.setOpened(true);
            msgCal.setTime(message.getDate());
            final StringBuilder timePassedStr = new StringBuilder();

            long msgLong = msgCal.getTimeInMillis();
            long timePassed = nowLong - msgLong;
            Calendar distance = Calendar.getInstance();
            distance.setTimeInMillis(timePassed);
            final int years = distance.get(Calendar.YEAR) - theBeginning.get(Calendar.YEAR);
            final int months = years * 12
                    + (distance.get(Calendar.MONTH) - theBeginning.get(Calendar.MONTH));
            final int days = months * 30
                    + (distance.get(Calendar.DAY_OF_YEAR) - theBeginning.get(Calendar.DAY_OF_YEAR));
            final int hours = days * 24 + distance.get(Calendar.HOUR_OF_DAY) - theBeginning.get(Calendar.HOUR_OF_DAY);
            final int minutes = hours * 60 + distance.get(Calendar.MINUTE) - theBeginning.get(Calendar.MINUTE);

            if (years > 0) {
                if (years == 1) {
                    timePassedStr.append(years).append(" year");
                } else {
                    timePassedStr.append(years).append(" years");
                }
            } else if (months > 0) {
                if (months == 1) {
                    timePassedStr.append(months).append(" month");
                } else {
                    timePassedStr.append(months).append(" months");
                }
            } else if (days > 0) {
                if (days == 1) {
                    timePassedStr.append(days).append(" day");
                } else {
                    timePassedStr.append(days).append(" days");
                }
            } else if (hours > 0) {
                if (hours == 1) {
                    timePassedStr.append(hours).append(" hour");
                } else {
                    timePassedStr.append(hours).append(" hours");
                }
            } else if (minutes > 0) {
                if (minutes == 1) {
                    timePassedStr.append(minutes).append(" minute");
                } else {
                    timePassedStr.append(minutes).append(" minutes");
                }
            } else {
                timePassedStr.append("seconds");
            }
            timePassedStr.append(" ago");
            messageToTime.put(message.getMessageId(), timePassedStr.toString());
        }
        return messageToTime;
    }


    /**
     * Returns a sorted, by date and subject, list of the given messages.
     *
     * @param messages the list of the messages.
     * @return a sorted list.
     */
    private LinkedList<LinkedList<Message>> getMessages(final List<Message> messages) {

        final LinkedHashMap<Integer, LinkedList<Message>> groupedMessages
                = new LinkedHashMap<Integer, LinkedList<Message>>();

        for (final Message message : messages) {
            int thisMessageID;
            if (message.getRootId() == 0) {
                thisMessageID = message.getMessageId();
            } else {
                thisMessageID = message.getRootId();
            }

            if (groupedMessages.containsKey(thisMessageID)) {
                groupedMessages.get(thisMessageID).add(message);

            } else {
                final LinkedList<Message> tmpList = new LinkedList<Message>();
                tmpList.add(message);
                groupedMessages.put(thisMessageID, tmpList);
            }
        }

        for (final LinkedList<Message> groupedList : groupedMessages.values()) {
            int bigMessages = 0;
            int index = 0;
            int length = 0;
            for (final Message message : groupedList) {
                message.setSubject(WordUtils.wrap(antisamyManager.scanWithOutImgTag(message.getSubject()), 15, " ", true));
                index++;
                if (message.getBodyMessage().length() > 99) {

                    final String text = message.getBodyMessage().substring(0, 99);
                    final String content = WordUtils.wrap(text, 28, " ", true);

                    message.setBodyMessage(antisamyManager.scanWithOutImgTag(content) + "... ");
                    length += content.length() + 28;

                    final String url = "http://www.eaw1805.com/inbox/private/" + message.getMessageId() + "/view";
                    final String href = "<a href='" + url + "'>Read More</a>";

                    message.setBodyMessage(message.getBodyMessage() + href);

                    bigMessages++;

                } else {
                    length += message.getBodyMessage().length() + 28;
                    message.setBodyMessage(antisamyManager.scanWithOutImgTag(message.getBodyMessage()));
                }

                if (bigMessages == 2 && length / 28 > 13) {
                    break;

                } else if (bigMessages != 2 && length / 28 > 10) {
                    break;
                }
            }

            if (bigMessages == 2 && length / 28 > 13) {
                final List<Message> temp = new LinkedList<Message>();
                for (int i = 0; i < index - 1; i++) {
                    temp.add(groupedList.get(i));
                }
                groupedList.clear();
                groupedList.addAll(temp);

            } else if (bigMessages != 2 && length / 28 > 10) {
                final List<Message> temp = new LinkedList<Message>();
                for (int i = 0; i < index - 1; i++) {
                    temp.add(groupedList.get(i));
                }
                groupedList.clear();
                groupedList.addAll(temp);
            }
        }

        final LinkedList<LinkedList<Message>> groupedMessagesList = new LinkedList<LinkedList<Message>>();
        groupedMessagesList.addAll(groupedMessages.values());

        return groupedMessagesList;
    }

    /**
     * Prepare Users inbox.
     *
     * @param thisUser the current User.
     * @return a HashMap with the Data.
     */
    private HashMap<String, Object> prepareInbox(final User thisUser) {
        //get http request object.
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        PagedListHolder pagedListHolder = (PagedListHolder) request.getSession().getAttribute("messagesList");

        //User preferences for inbox thread size.
        final int usersInboxSize = thisUser.getInboxThreadSize();

        final List<Message> messages = messageManager.retrieveUserHistory(thisUser.getUserId());

        if (pagedListHolder == null) {

            //initialize a new pageListHolder.
            pagedListHolder = new PagedListHolder(getMessages(messages));

            //Show all messages if inbox size equals -1
            if (usersInboxSize == -1) {
                pagedListHolder.setPageSize(pagedListHolder.getNrOfElements());

            } else {
                pagedListHolder.setPageSize(usersInboxSize);
            }

        } else {
            if (pagedListHolder.getNrOfElements() != messages.size()) {
                pagedListHolder.setSource(getMessages(messages));
            }

            //Check if user's inbox thread size is different from pageholder's page size.
            if (pagedListHolder.getPageSize() != pagedListHolder.getNrOfElements()
                    && pagedListHolder.getPageSize() != usersInboxSize) {
                //Show all messages if inbox size equals -1
                if (usersInboxSize == -1) {
                    pagedListHolder.setPageSize(pagedListHolder.getNrOfElements());
                } else {
                    pagedListHolder.setPageSize(usersInboxSize);
                }

            } else {
                final String page = (String) request.getParameter("page");
                if ("next".equals(page)) {
                    pagedListHolder.nextPage();

                } else if ("previous".equals(page)) {
                    pagedListHolder.previousPage();

                } else {
                    try {
                        final int goToPage = Integer.valueOf(page);
                        if (goToPage <= pagedListHolder.getPageCount() && goToPage >= 0) {
                            pagedListHolder.setPage(goToPage);
                        }
                    } catch (Exception e) {
                        //eat it
                    }
                }
            }
        }

        final HashMap<String, Object> model = new HashMap<String, Object>();
        request.getSession().setAttribute("messagesList", pagedListHolder);
        model.put("messagesList", pagedListHolder);
        model.put("messageToTime", formatMessageDate(messages));
        return model;
    }

    /**
     * Getter method used to access the antisamyManager bean.
     *
     * @return the antisamyManager bean.
     */
    public AntisamyManager getAntisamyManager() {
        return antisamyManager;
    }

    /**
     * Setter method used by spring to inject a AntisamyManager bean.
     *
     * @param value a antisamyManager bean.
     */
    public void setAntisamyManager(final AntisamyManager value) {
        this.antisamyManager = value;
    }
}
