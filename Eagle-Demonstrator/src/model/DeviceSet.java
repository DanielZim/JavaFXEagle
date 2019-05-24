package model;

import model.sceneObjects.Gate;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Device set.
 */
public class DeviceSet {

    // required
    private String name;

    // optional
    private String description;
    private String prefix;
    private boolean userValue;

    private Map<String, Gate> gates;
    private Map<String, Device> devices;

    private boolean isSetDescription;
    private boolean isSetPrefix;
    private boolean isSetUserValue;
    private boolean isSetGates;
    private boolean isSetDevices;

    /**
     * Instantiates a new Device set.
     *
     * @param name the name
     */
    public DeviceSet(String name) {
        this.name = name;

        this.description = "";
        this.prefix = "";
        this.userValue = false;

        this.gates = new HashMap<>();
        this.devices = new HashMap<>();

        this.isSetDescription = false;
        this.isSetPrefix = false;
        this.isSetUserValue = false;
        this.isSetGates = false;
        this.isSetDevices = false;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
        this.isSetDescription = true;
    }

    /**
     * Sets prefix.
     *
     * @param prefix the prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.isSetPrefix = true;
    }

    /**
     * Sets user value.
     *
     * @param userValue the user value
     */
    public void setUserValue(boolean userValue) {
        this.userValue = userValue;
        this.isSetUserValue = true;
    }

    /**
     * Sets gates.
     *
     * @param gates the gates
     */
    public void setGates(Map<String, Gate> gates) {
        this.gates = gates;
        this.isSetGates = true;
    }

    /**
     * Sets devices.
     *
     * @param devices the devices
     */
    public void setDevices(Map<String, Device> devices) {
        this.devices = devices;
        this.isSetDevices = true;
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
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets prefix.
     *
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Gets user value.
     *
     * @return the user value
     */
    public boolean getUserValue() {
        return userValue;
    }

    /**
     * Gets gates.
     *
     * @return the gates
     */
    public Map<String, Gate> getGates() {
        return gates;
    }

    /**
     * Gets devices.
     *
     * @return the devices
     */
    public Map<String, Device> getDevices() {
        return devices;
    }

    /**
     * Gets is set description.
     *
     * @return the is set description
     */
    public boolean getIsSetDescription() {
        return isSetDescription;
    }

    /**
     * Gets is set prefix.
     *
     * @return the is set prefix
     */
    public boolean getIsSetPrefix() {
        return isSetPrefix;
    }

    /**
     * Gets is set user value.
     *
     * @return the is set user value
     */
    public boolean getIsSetUserValue() {
        return isSetUserValue;
    }

    /**
     * Gets is set gates.
     *
     * @return the is set gates
     */
    public boolean getIsSetGates() {
        return isSetGates;
    }

    /**
     * Gets is set devices.
     *
     * @return the is set devices
     */
    public boolean getIsSetDevices() {
        return isSetDevices;
    }
}
