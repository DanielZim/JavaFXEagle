package model;

/**
 * The type Param.
 */
public class Param {

    // required
    private String name;
    private String value;

    /**
     * Instantiates a new Param.
     *
     * @param name  the name
     * @param value the value
     */
    public Param(String name, String value) {
        this.name = name;
        this.value = value;
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
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
