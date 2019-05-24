package main;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import utils.ColorProvider;

/**
 * The type Scene settings is used in the EagleSceneBuilder for settings which can be changed by the user.
 */
public class SceneSettings {

    private boolean drawBoundingBoxes;
    private Color backgroundColor;

    /**
     * Instantiates a new Scene settings with default values.
     */
    public SceneSettings() {
        this.drawBoundingBoxes = false;
        this.backgroundColor = ColorProvider.WHITE_BACKGROUND;
    }

    /**
     * Gets the user dialog.
     *
     * @return the dialog
     */
    public Dialog<SceneSettings> getDialog() {
        // build the dialog
        Dialog<SceneSettings> dialog = new Dialog<>();
        dialog.setTitle("Settings");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);

        // setup the input fields with labels
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox bgComboBox = new ComboBox();
        bgComboBox.getItems().addAll("White", "Black", "Color");
        bgComboBox.setValue(this.getBackgroundColorString());

        CheckBox drawBboxesCheckBox = new CheckBox();
        drawBboxesCheckBox.setSelected(this.isDrawBoundingBoxes());

        grid.add(new Label("Background color:"), 0, 0);
        grid.add(bgComboBox, 1, 0);
        grid.add(new Label("Show bounding boxes:"), 0, 1);
        grid.add(drawBboxesCheckBox, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // convert the result to a list
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.APPLY) {
                SceneSettings result = new SceneSettings();
                result.setBackgroundColor(bgComboBox.getValue().toString());
                result.setDrawBoundingBoxes(drawBboxesCheckBox.isSelected());

                return result;
            }

            return null;
        });

        return dialog;
    }

    /**
     * Is draw bounding boxes boolean.
     *
     * @return the boolean
     */
    public boolean isDrawBoundingBoxes() {
        return drawBoundingBoxes;
    }

    /**
     * Sets draw bounding boxes.
     *
     * @param showBoundingBoxes the show bounding boxes
     */
    public void setDrawBoundingBoxes(boolean showBoundingBoxes) {
        this.drawBoundingBoxes = showBoundingBoxes;
    }

    /**
     * Gets background color.
     *
     * @return the background color
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Gets background color string.
     *
     * @return the background color string
     */
    public String getBackgroundColorString() {
        return getBackgroundColor() == ColorProvider.WHITE_BACKGROUND ? "White"
                : getBackgroundColor() == ColorProvider.BLACK_BACKGROUND ? "Black" : "Color";
    }

    /**
     * Sets background color.
     *
     * @param backgroundColor the background color
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Sets background color by the string returned from the dialog combobox.
     *
     * @param colorString the color string
     */
    public void setBackgroundColor(String colorString) {
        Color color = colorString.equals("White") ? ColorProvider.WHITE_BACKGROUND :
                colorString.equals("Black") ? ColorProvider.BLACK_BACKGROUND : ColorProvider.COLOR_BACKGROUND;
        setBackgroundColor(color);
    }
}
