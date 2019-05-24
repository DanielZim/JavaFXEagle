package model;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Pass.
 */
public class Pass {

    // required
    private String name;

    // implied
    private String refer;

    // optional
    private boolean active;
    private Map<String, Param> params;

    private boolean isSetRefer;
    private boolean isSetActive;
    private boolean isSetParams;

    /**
     * Instantiates a new Pass.
     *
     * @param name the name
     */
    public Pass(String name) {
        this.name = name;

        this.refer = null;
        this.active = true;
        this.params = new HashMap<>();

        this.isSetRefer = false;
        this.isSetActive = false;
        this.isSetParams = false;
    }

    /**
     * Add param.
     *
     * @param param the param
     */
    public void addParam(Param param) {
        this.params.put(param.getName(), param);
        this.isSetParams = true;
    }

    /**
     * Sets active.
     *
     * @param active the active
     */
    public void setActive(boolean active) {
        this.active = active;
        this.isSetActive = true;
    }

    /**
     * Sets refer.
     *
     * @param refer the refer
     */
    public void setRefer(String refer) {
        this.refer = refer;
        this.isSetRefer = true;
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
     * Gets refer.
     *
     * @return the refer
     */
    public String getRefer() {
        return refer;
    }

    /**
     * Gets active.
     *
     * @return the active
     */
    public boolean getActive() {
        return active;
    }

    /**
     * Gets params.
     *
     * @return the params
     */
    public Map<String, Param> getParams() {
        return params;
    }

    /**
     * Gets is set refer.
     *
     * @return the is set refer
     */
    public boolean getIsSetRefer() {
        return isSetRefer;
    }

    /**
     * Gets is set active.
     *
     * @return the is set active
     */
    public boolean getIsSetActive() {
        return isSetActive;
    }

    /**
     * Gets is set params.
     *
     * @return the is set params
     */
    public boolean getIsSetParams() {
        return isSetParams;
    }
}
