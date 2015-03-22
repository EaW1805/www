package com.eaw1805.www.commands;

/**
 * Implementation of the Administrator Transfer Command.
 */
public class AdminTransferCommand {

    /**
     * The transfer receiver.
     */
    private String receiver;

    /**
     * The Free Credits amount.
     */
    private String freeCreditsAmount;

    /**
     * The Bought Credits amount.
     */
    private String boughtCreditsAmount;

    /**
     * The Transferred Credits amount.
     */
    private String transferredCreditsAmount;

    /**
     * Comment on Transfer.
     */
    private String comment;

    /**
     * Default Constructor.
     */
    public AdminTransferCommand() {
        receiver = null;
        freeCreditsAmount = null;
        transferredCreditsAmount = null;
        boughtCreditsAmount = null;
        comment = null;
    }

    /**
     * Returns the Receiver,
     *
     * @return a User object.
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Defines the receiver User.
     *
     * @param receiver a User object
     */
    public void setReceiver(final String receiver) {
        this.receiver = receiver;
    }

    /**
     * Returns the free credits amount of the transfer.
     *
     * @return a String with the amount
     */
    public String getFreeCreditsAmount() {
        return freeCreditsAmount;
    }

    /**
     * Defines the Free Credits amount of the specific tranfer.
     *
     * @param creditsAmount a String object
     */
    public void setFreeCreditsAmount(final String creditsAmount) {
        this.freeCreditsAmount = creditsAmount;
    }

    /**
     * Returns the bought credits amount of the transfer.
     *
     * @return a String with the amount
     */
    public String getBoughtCreditsAmount() {
        return boughtCreditsAmount;
    }

    /**
     * Defines the Bought Credits amount of the specific tranfer.
     *
     * @param creditsAmount a String object
     */
    public void setBoughtCreditsAmount(final String creditsAmount) {
        this.boughtCreditsAmount = creditsAmount;
    }

    /**
     * Returns the Transferred credits amount of the transfer.
     *
     * @return a String with the amount
     */
    public String getTransferredCreditsAmount() {
        return transferredCreditsAmount;
    }

    /**
     * Defines the Transferred Credits amount of the specific tranfer.
     *
     * @param creditsAmount a String object
     */
    public void setTransferredCreditsAmount(final String creditsAmount) {
        this.transferredCreditsAmount = creditsAmount;
    }

    /**
     * Returns the Comment of this transfer.
     *
     * @return a String with the comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Defines the Comment on this transfer.
     *
     * @param value a String object
     */
    public void setComment(final String value) {
        this.comment = value;
    }
}
