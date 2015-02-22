package empire.webapp.commands;

/**
 * Implementation of the Paypal Transfer Command.
 */
public class PaypalCommand {

    /**
     * Hold the amount.
     */
    private String amount;

    /**
     * Default constructor.
     */
    public PaypalCommand() {
        amount = null;
    }

    /**
     * Return the amount of money,
     *
     * @return a String with the amount.
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Set the amount of money.
     *
     * @param value a String with the amount.
     */
    public void setAmount(final String value) {
        this.amount = value;
    }

}
