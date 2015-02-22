package empire.webapp.commands;

/**
 * Bean for request help pages.
 */
public class HelpCommand {

    /**
     * Hold the page name.
     */
    private String page;

    /**
     * Default constructor.
     */
    public HelpCommand() {
        page = null;
    }

    /**
     * Get the page name.
     *
     * @return the page name.
     */
    public String getPage() {
        return page;
    }

    /**
     * Set the page name.
     *
     * @param value the page name.
     */
    public void setPage(final String value) {
        this.page = value;
    }
}
