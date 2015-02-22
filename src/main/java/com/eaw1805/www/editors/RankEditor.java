package empire.webapp.editors;

import empire.data.managers.beans.RankManagerBean;
import empire.data.model.army.Rank;

import java.beans.PropertyEditorSupport;

/**
 * This class provides a means to customize how String values
 * are mapped to the non-String class Rank.
 */
@SuppressWarnings("restriction")
public class RankEditor extends PropertyEditorSupport {

    /**
     * This method return a String representation of the
     * Terrain's id value.
     *
     * @return String the representation of a Terrain's id value.
     */
    public String getAsText() {
        String outputString;
        outputString = "";
        if (getValue() != null) {
            outputString = String.valueOf(((Rank) getValue()).getRankId());
        }
        return outputString;
    }

    /**
     * Sets a bean property value from the string value passed in.
     *
     * @param value the string value passed for the object Nation.
     */
    public void setAsText(final String value) {
        setValue(rankManager.getByID(Integer.parseInt(value)));
    }

    /**
     * Instance RankManagerBean class to perform queries
     * about rank objects.
     */
    private transient RankManagerBean rankManager;

    /**
     * Setter method used by spring to inject a RankManager bean.
     *
     * @param injRankManager a RankManager bean.
     */
    public void setRankManager(final RankManagerBean injRankManager) {
        rankManager = injRankManager;
    }
}
