package model;

import model.enums.VerticalText;

/**
 * The type Setting.
 */
public class Setting {

    // standard value up
    private VerticalText verticalText;

    // implied
    private boolean alwaysVectorFont;

    private boolean isSetAlwaysVectorFont;
    private boolean isSetVerticalText;

    /**
     * Instantiates a new Setting.
     */
    public Setting() {
        this.verticalText = VerticalText.Up;
        this.isSetAlwaysVectorFont = false;
        this.isSetVerticalText = false;
    }

    /**
     * Sets always vector font.
     *
     * @param alwaysVectorFont the always vector font
     */
    public void setAlwaysVectorFont(boolean alwaysVectorFont) {
        this.alwaysVectorFont = alwaysVectorFont;
        this.isSetAlwaysVectorFont = true;
    }

    /**
     * Sets vertical text.
     *
     * @param verticalText the vertical text
     */
    public void setVerticalText(VerticalText verticalText) {
        this.verticalText = verticalText;
        this.isSetVerticalText = true;
    }

    /**
     * Gets always vector font.
     *
     * @return the always vector font
     */
    public boolean getAlwaysVectorFont() {
        return alwaysVectorFont;
    }

    /**
     * Gets vertical text.
     *
     * @return the vertical text
     */
    public VerticalText getVerticalText() {
        return verticalText;
    }

    /**
     * Gets is set always vector font.
     *
     * @return the is set always vector font
     */
    public boolean getIsSetAlwaysVectorFont() {
        return isSetAlwaysVectorFont;
    }

    /**
     * Gets is set vertical text.
     *
     * @return the is set vertical text
     */
    public boolean getIsSetVerticalText() {
        return isSetVerticalText;
    }
}
