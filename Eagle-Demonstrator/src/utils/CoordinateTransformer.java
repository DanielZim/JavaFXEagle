package utils;

/**
 * The type Coordinate transformer.
 */
public class CoordinateTransformer {

    private double boardHeight;

    /**
     * Instantiates a new Coordinate transformer.
     */
    public CoordinateTransformer() {
        this.boardHeight = 0;
    }

    /**
     * Instantiates a new Coordinate transformer.
     *
     * @param height the height
     */
    public CoordinateTransformer(double height) {
        this.boardHeight = height;
    }

    /**
     * Transform origin double.
     *
     * @param toTransform the to transform
     * @return the double
     */
    public double transformOrigin(double toTransform) {
        return this.boardHeight - toTransform;
    }

    /**
     * Transform sign double.
     *
     * @param toTransform the to transform
     * @return the double
     */
    public double transformSign(double toTransform) {
        return -toTransform;
    }

    /**
     * Sets board height.
     *
     * @param boardHeight the board height
     */
    public void setBoardHeight(double boardHeight) {
        this.boardHeight = boardHeight;
    }
}
