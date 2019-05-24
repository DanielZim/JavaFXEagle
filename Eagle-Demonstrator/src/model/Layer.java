package model;

/**
 * The type Layer.
 */
public class Layer {

    // required
    private int number;
    private String name;
    private int color;
    private int fill;

    // optional
    private boolean visible;
    private boolean active;

    private boolean isSetVisible;
    private boolean isSetActive;

    /**
     * Instantiates a new Layer.
     *
     * @param number the number
     * @param name   the name
     * @param color  the color
     * @param fill   the fill
     */
    public Layer(int number, String name, int color, int fill) {
        this.number = number;
        this.name = name;
        this.color = color;
        this.fill = fill;

        this.visible = true;
        this.active = true;

        this.isSetVisible = false;
        this.isSetActive = false;
    }

    /**
     * Sets visible.
     *
     * @param visible the visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
        this.isSetVisible = true;
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
     * Gets number.
     *
     * @return the number
     */
    public int getNumber() {
        return number;
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
     * Gets fill.
     *
     * @return the fill
     */
    public int getFill() {
        return fill;
    }

    /**
     * Gets visible.
     *
     * @return the visible
     */
    public boolean getVisible() {
        return visible;
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
     * Gets color.
     *
     * @return the color
     */
    public int getColor() {
        return color;
    }

    /**
     * Gets is set visible.
     *
     * @return the is set visible
     */
    public boolean getIsSetVisible() {
        return isSetVisible;
    }

    /**
     * Gets is set active.
     *
     * @return the is set active
     */
    public boolean getIsSetActive() {
        return isSetActive;
    }
}
