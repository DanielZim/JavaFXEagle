package model;

/**
 * The type Design rules.
 */
public class DesignRules {

    private String name;
    private String content;

    /**
     * Instantiates a new Design rules.
     *
     * @param name    the name
     * @param content the content
     */
    public DesignRules(String name, String content) {
        this.name = name;
        this.content = content;
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
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }
}
