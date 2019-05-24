package model;

import model.sceneObjects.Attribute;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Technology.
 */
public class Technology {

    // required
    private String name;

    // optional
    private Map<String, Attribute> attributes;

    private boolean isSetAttributes;

    /**
     * Instantiates a new Technology.
     *
     * @param name the name
     */
    public Technology(String name) {
        this.name = name;

        this.attributes = new HashMap<>();
        this.isSetAttributes = false;
    }

    /**
     * Add attribute.
     *
     * @param attr the attr
     */
    public void addAttribute(Attribute attr) {
        this.attributes.put(attr.getName(), attr);
        this.isSetAttributes = true;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets attributes.
     *
     * @return the attributes
     */
    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Gets is set attributes.
     *
     * @return the is set attributes
     */
    public boolean getIsSetAttributes() {
        return isSetAttributes;
    }
}
