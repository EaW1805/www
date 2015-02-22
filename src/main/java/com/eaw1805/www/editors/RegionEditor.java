package empire.webapp.editors;

import empire.data.managers.beans.RegionManagerBean;
import empire.data.model.map.Region;

import java.beans.PropertyEditorSupport;

/**
 * This class provides a means to customize how String values
 * are mapped to the non-String class Region.
 */
@SuppressWarnings("restriction")
public class RegionEditor extends PropertyEditorSupport {

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
            outputString = String.valueOf(((Region) getValue()).getId());
        }
        return outputString;
    }

    /**
     * Sets a bean property value from the string value passed in.
     *
     * @param value the string value passed for the object Nation.
     */
    public void setAsText(final String value) {
        setValue(regionManager.getByID(Integer.parseInt(value)));
    }

    /**
     * Instance RegionManagerBean class to perform queries
     * about region objects.
     */
    private transient RegionManagerBean regionManager;

    /**
     * Setter method used by spring to inject a RegionManager bean.
     *
     * @param injRegionManager a RegionManager bean.
     */
    public void setRegionManager(final RegionManagerBean injRegionManager) {
        regionManager = injRegionManager;
    }
}
