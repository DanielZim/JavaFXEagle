package utils;

import javafx.scene.paint.Color;
import model.Layer;
import model.sceneObjects.EagleBoard;

/**
 * The type Color provider.
 */
public class ColorProvider {

    private static double ALPHA = 0.8;

    public static Color WHITE_BACKGROUND = Color.web("#ffffff");
    public static Color BLACK_BACKGROUND = Color.web("#000000");
    public static Color COLOR_BACKGROUND = Color.web("#eeeece");

    /**
     * The constant THT_PAD_COLOR.
     */
    public static Color THT_PAD_COLOR = Color.web("#46a03d", ALPHA);
    /**
     * The constant HOLE_COLOR.
     */
    public static Color HOLE_COLOR = Color.web("#a5a5a5", ALPHA);
    /**
     * The constant VIA_COLOR.
     */
    public static Color VIA_COLOR = Color.web("#4b5a4b", ALPHA);

    /**
     * Gets color by the given layer.
     *
     * @param board       the board
     * @param layerNumber the layer number
     * @return the color
     */
    public static Color getColorByLayer(EagleBoard board, int layerNumber) {
        Layer layer = board.getLayers().get(layerNumber);
        int colorValue = layer != null ? layer.getColor() : -1;

        // we use the color number from the layer definition here
        switch (colorValue) {
            case 1: return Color.web("#7979af", ALPHA);
            case 2: return Color.web("#4ba54b", 0.25);
            case 3: return Color.web("#4ba5a5", ALPHA);
            case 4: return Color.web("#a0463d", ALPHA);
            case 6: return Color.web("#a0a03d", ALPHA);
            case 7: return Color.web("#a0a097", ALPHA);
            case 14: return Color.web("#ffff4b", ALPHA);
            case 15: return Color.web("#000000", ALPHA);
            case 16: return Color.web("#a0a097", ALPHA);
            default: return Color.web("#000000", ALPHA);
        }
    }
}
