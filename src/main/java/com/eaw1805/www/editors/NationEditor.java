package empire.webapp.editors;

import empire.data.managers.beans.NationManagerBean;
import empire.data.model.Nation;

import java.beans.PropertyEditorSupport;

/**
 * This class provides a means to customize how String values
 * are mapped to the non-String class Nation.
 */
@SuppressWarnings("restriction")
public class NationEditor extends PropertyEditorSupport {

    /**
     * This method return a String representation of the
     * Nation's id value.
     *
     * @return String the representation of a Nation's id value.
     */
    public String getAsText() {
        String outputString;
        outputString = "";
        if (getValue() != null) {
            outputString = String.valueOf(((Nation) getValue()).getId());
        }
        return outputString;
    }

    /**
     * Sets a bean property value from the string value passed in.
     *
     * @param value the string value passed for the object Nation.
     */
    public void setAsText(final String value) {
        setValue(nationManager.getByID(Integer.parseInt(value)));
    }

    /**
     * Instance NationManager class to perform queries
     * about nation objects.
     */
    private transient NationManagerBean nationManager;

    /**
     * Setter method used by spring to inject a nationManager bean.
     *
     * @param injNationManager a nationManager bean.
     */
    public void setNationManager(final NationManagerBean injNationManager) {
        nationManager = injNationManager;
    }
}