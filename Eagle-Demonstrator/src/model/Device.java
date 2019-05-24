package model;

import java.util.*;

/**
 * The type Device.
 */
public class Device {

    // implied
    private String name;
    private String packageName;

    // optional
    private List<Connect> connects;
    private Map<String, Technology> technologies;

    private boolean isSetName;
    private boolean isSetPackageName;
    private boolean isSetConnects;
    private boolean isSetTechnologies;

    /**
     * Instantiates a new Device.
     */
    public Device() {
        this.name = "";
        this.packageName = null;

        this.connects = new ArrayList<>();
        this.technologies = new HashMap<>();

        this.isSetName = false;
        this.isSetPackageName = false;
        this.isSetConnects = false;
        this.isSetTechnologies = false;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
        this.isSetName = true;
    }

    /**
     * Sets package name.
     *
     * @param packageName the package name
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
        this.isSetPackageName = true;
    }

    /**
     * Sets connects.
     *
     * @param connects the connects
     */
    public void setConnects(List<Connect> connects) {
        this.connects = connects;
        this.isSetConnects = true;
    }

    /**
     * Sets technologies.
     *
     * @param technologies the technologies
     */
    public void setTechnologies(Map<String, Technology> technologies) {
        this.technologies = technologies;
        this.isSetTechnologies = true;
    }

    /**
     * Gets package name.
     *
     * @return the package name
     */
    public String getPackageName() {
        return packageName;
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
     * Gets connects.
     *
     * @return the connects
     */
    public List<Connect> getConnects() {
        return connects;
    }

    /**
     * Gets technologies.
     *
     * @return the technologies
     */
    public Map<String, Technology> getTechnologies() {
        return technologies;
    }

    /**
     * Gets is set name.
     *
     * @return the is set name
     */
    public boolean getIsSetName() {
        return isSetName;
    }

    /**
     * Gets is set package name.
     *
     * @return the is set package name
     */
    public boolean getIsSetPackageName() {
        return isSetPackageName;
    }

    /**
     * Gets is set connects.
     *
     * @return the is set connects
     */
    public boolean getIsSetConnects() {
        return isSetConnects;
    }

    /**
     * Gets is set technologies.
     *
     * @return the is set technologies
     */
    public boolean getIsSetTechnologies() {
        return isSetTechnologies;
    }
}
