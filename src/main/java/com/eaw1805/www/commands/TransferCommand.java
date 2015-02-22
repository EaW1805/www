package empire.webapp.commands;

import empire.data.model.User;

/**
 * Implementation of the Transfer Command.
 */
public class TransferCommand {

    /**
     * The transfer sender.
     */
    private User sender;

    /**
     * The transfer receiver.
     */
    private User receiver;

    /**
     * The Credits amount.
     */
    private String creditsAmount;

    /**
     * The Form given password of the Sender.
     */
    private String givenPassword;

    /**
     * Default Constructor.
     */
    public TransferCommand() {
        sender = null;
        receiver = null;
        creditsAmount = null;
        givenPassword = null;
    }

    /**
     * Returns the Sender.
     *
     * @return the User.
     */
    public User getSender() {
        return sender;
    }

    /**
     * Defines the Sender.
     *
     * @param sender a User
     */
    public void setSender(final User sender) {
        this.sender = sender;
    }

    /**
     * Returns the Receiver,
     *
     * @return a User object.
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * Defines the receiver User.
     *
     * @param receiver a User object
     */
    public void setReceiver(final User receiver) {
        this.receiver = receiver;
    }

    /**
     * Returns the credits amount of the transfer.
     *
     * @return a String with the amount
     */
    public String getCreditsAmount() {
        return creditsAmount;
    }

    /**
     * Defines the Credits amount of the specific tranfer.
     *
     * @param creditsAmount a String object
     */
    public void setCreditsAmount(final String creditsAmount) {
        this.creditsAmount = creditsAmount;
    }

    /**
     * Returns the given password.
     *
     * @return a String Object
     */
    public String getGivenPassword() {
        return givenPassword;
    }

    /**
     * Defiens the Sender's password.
     *
     * @param givenPassword a String with the password.
     */
    public void setGivenPassword(final String givenPassword) {
        this.givenPassword = givenPassword;
    }
}
