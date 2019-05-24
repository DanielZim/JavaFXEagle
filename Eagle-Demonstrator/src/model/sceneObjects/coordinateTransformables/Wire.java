package model.sceneObjects.coordinateTransformables;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import model.BoundingBox;
import model.enums.WireCap;
import model.enums.WireStyle;
import model.sceneObjects.EagleBoard;
import utils.ColorProvider;
import utils.CoordinateTransformer;

/**
 * The type Wire.
 */
public class Wire implements ICoordinateTransformable, ILayerable {

    private static final double DEFAULT_WIDTH = 0.07;

    // required
    private EagleBoard board;
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private int layerNumber;
    private double width;
    private String extent;

    // optional
    private WireStyle style;
    private double curve;
    private WireCap cap;

    private Boolean isSetStyle;
    private Boolean isSetCurve;
    private Boolean isSetCap;

    /**
     * Instantiates a new Wire.
     *
     * @param board       the board
     * @param x1          the x 1
     * @param y1          the y 1
     * @param x2          the x 2
     * @param y2          the y 2
     * @param layerNumber the layer number
     * @param width       the width
     * @param extent      the extent
     */
    public Wire(EagleBoard board, double x1, double y1, double x2, double y2, int layerNumber, double width, String extent) {
        this.board = board;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.layerNumber = layerNumber;
        this.width = width;
        this.extent = extent;

        this.style = WireStyle.Continous;
        this.curve = 0;
        this.cap = WireCap.Round;

        this.isSetStyle = false;
        this.isSetCurve = false;
        this.isSetCap = false;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        // curve and extent are ignored at the moment

        double xStart = getX1() + referenceX;
        double yStart = getY1() + referenceY;
        double xEnd = getX2() + referenceX;
        double yEnd = getY2() + referenceY;

        Color wireColor = ColorProvider.getColorByLayer(getBoard(), getLayerNumber());

        Line line = new Line(xStart, yStart, xEnd, yEnd);
        line.setStroke(wireColor);
        line.setStrokeWidth(getWidth());

        // TODO: fix dashed lines, values are to big here
        switch (getStyle()) {
            case DashDot:
                line.getStrokeDashArray().addAll(1d, 5d);
                break;
            case LongDash:
                line.getStrokeDashArray().addAll(10d, 5d);
                break;
            case ShortDash:
                line.getStrokeDashArray().addAll(5d, 10d);
                break;
        }

        switch (getCap()) {
            case Flat:
                line.setStrokeLineCap(StrokeLineCap.SQUARE);
                break;
            case Round:
                line.setStrokeLineCap(StrokeLineCap.ROUND);
                break;
        }

        return line;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        BoundingBox bbox = new BoundingBox(getX1() + referenceX, getY1() + referenceY,
                getX2() + referenceX + getWidth(), getY2() + referenceY + getWidth());

        return bbox;
    }

    @Override
    public void transformCoordinates(CoordinateTransformer ct, boolean signTransformation) {
        if (signTransformation) {
            this.y1 = ct.transformSign(this.y1);
            this.y2 = ct.transformSign(this.y2);
        }
        else {
            this.y1 = ct.transformOrigin(this.y1);
            this.y2 = ct.transformOrigin(this.y2);
        }
    }

    /**
     * Sets style.
     *
     * @param style the style
     */
    public void setStyle(WireStyle style) {
        this.style = style;
        this.isSetStyle = true;
    }

    /**
     * Sets curve.
     *
     * @param curve the curve
     */
    public void setCurve(double curve) {
        this.curve = curve;
        this.isSetCurve = true;
    }

    /**
     * Sets cap.
     *
     * @param cap the cap
     */
    public void setCap(WireCap cap) {
        this.cap = cap;
        this.isSetCap = true;
    }

    /**
     * Gets board.
     *
     * @return the board
     */
    public EagleBoard getBoard() {
        return board;
    }

    /**
     * Gets x 1.
     *
     * @return the x 1
     */
    public double getX1() {
        return x1;
    }

    /**
     * Gets y 1.
     *
     * @return the y 1
     */
    public double getY1() {
        return y1;
    }

    /**
     * Gets x 2.
     *
     * @return the x 2
     */
    public double getX2() {
        return x2;
    }

    /**
     * Gets y 2.
     *
     * @return the y 2
     */
    public double getY2() {
        return y2;
    }

    public int getLayerNumber() {
        return layerNumber;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public double getWidth() {
        return width > 0.0 ? width : DEFAULT_WIDTH;
    }

    /**
     * Gets extent.
     *
     * @return the extent
     */
    public String getExtent() {
        return extent;
    }

    /**
     * Gets style.
     *
     * @return the style
     */
    public WireStyle getStyle() {
        return style;
    }

    /**
     * Gets curve.
     *
     * @return the curve
     */
    public double getCurve() {
        return curve;
    }

    /**
     * Gets cap.
     *
     * @return the cap
     */
    public WireCap getCap() {
        return cap;
    }

    /**
     * Gets is set style.
     *
     * @return the is set style
     */
    public boolean getIsSetStyle() {
        return isSetStyle;
    }

    /**
     * Gets is set curve.
     *
     * @return the is set curve
     */
    public boolean getIsSetCurve() {
        return isSetCurve;
    }

    /**
     * Gets is set cap.
     *
     * @return the is set cap
     */
    public boolean getIsSetCap() {
        return isSetCap;
    }
}
