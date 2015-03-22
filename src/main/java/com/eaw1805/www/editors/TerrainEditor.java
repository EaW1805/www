package com.eaw1805.www.editors;

import com.eaw1805.data.managers.beans.TerrainManagerBean;
import com.eaw1805.data.model.map.Terrain;

import java.beans.PropertyEditorSupport;

/**
 * This class provides a means to customize how String values
 * are mapped to the non-String class Terrain.
 */
@SuppressWarnings("restriction")
public class TerrainEditor extends PropertyEditorSupport {

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
            outputString = String.valueOf(((Terrain) getValue()).getId());
        }
        return outputString;
    }

    /**
     * Sets a bean property value from the string value passed in.
     *
     * @param value the string value passed for the object Nation.
     */
    public void setAsText(final String value) {
        setValue(terrainManager.getByID(Integer.parseInt(value)));
    }

    /**
     * Instance TerrainManagerBean class to perform queries
     * about nation objects.
     */
    private transient TerrainManagerBean terrainManager;

    /**
     * Setter method used by spring to inject a TerrainManager bean.
     *
     * @param injTerrainManager a TerrainManager bean.
     */
    public void setTerrainManager(final TerrainManagerBean injTerrainManager) {
        terrainManager = injTerrainManager;
    }
}
