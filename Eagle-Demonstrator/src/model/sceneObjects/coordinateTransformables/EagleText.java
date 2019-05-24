package model.sceneObjects.coordinateTransformables;

import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.scene.transform.Rotate;
import model.BoundingBox;
import model.enums.Align;
import model.enums.TextFont;
import model.sceneObjects.EagleBoard;
import utils.ColorProvider;
import utils.CoordinateTransformer;

/**
 * The type Eagle text.
 */
public class EagleText implements ICoordinateTransformable, ILayerable {

    private static final double STROKE_WIDTH = 0.02;
    private static final double CROSS_HALF_LINE_LENGTH = 0.2;

    // required
    private EagleBoard board;
    private double x1;
    private double y1;
    private int layerNumber;
    private double size;
    private String value;

    // optional
    private TextFont font;
    private int ratio;
    private int rot;
    private Align align;
    private int distance;

    private boolean isSetFont;
    private boolean isSetRatio;
    private boolean isSetRot;
    private boolean isSetAlign;
    private boolean isSetDistance;

    private Integer centerRotation;

    /**
     * Instantiates a new Eagle text.
     *
     * @param board       the board
     * @param value       the value
     * @param x1          the x 1
     * @param y1          the y 1
     * @param layerNumber the layer number
     * @param size        the size
     */
    public EagleText(EagleBoard board, String value, double x1, double y1, int layerNumber, double size) {
        this.board = board;
        this.value = value;
        this.x1 = x1;
        this.y1 = y1;
        this.layerNumber = layerNumber;
        this.size = size;

        this.font = TextFont.Proportional;
        this.ratio = 8;
        this.rot = 0;
        this.align = Align.BottomLeft;
        this.distance = 50;

        this.isSetFont = false;
        this.isSetRatio = false;
        this.isSetRot = false;
        this.isSetAlign = false;
        this.isSetDistance = false;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        // textFont, ratio and distance are ignored at the moment

        Pane sceneNode = new Pane();
        double x = getX1() + referenceX;
        double y = getY1() + referenceY;

        Color textColor = ColorProvider.getColorByLayer(getBoard(), getLayerNumber());
        TextAlignment textAlignment = TextAlignment.LEFT;
        VPos vPos = VPos.BASELINE;

        switch (getAlign()) {
            case BottomLeft:
                break;
            case CenterLeft:
                vPos = VPos.CENTER;
                break;
            case TopLeft:
                vPos = VPos.TOP;
                break;
            case Center:
                vPos = VPos.CENTER;
                break;
            case TopCenter:
                textAlignment = TextAlignment.CENTER;
                vPos = VPos.TOP;
                break;
            case BottomCenter:
                textAlignment = TextAlignment.CENTER;
                break;
            case CenterRight:
                textAlignment = TextAlignment.RIGHT;
                vPos = VPos.CENTER;
                break;
            case TopRight:
                textAlignment = TextAlignment.RIGHT;
                vPos = VPos.TOP;
                break;
            case BottomRight:
                textAlignment = TextAlignment.RIGHT;
                break;
        }

        // create the text
        Text text = new Text(x, y, getValue());
        text.setFont(Font.font(getSize()));
        text.setFill(textColor);
        text.setTextOrigin(vPos);
        text.setTextAlignment(textAlignment);
        
        if(getCenterRotation() != null && getCenterRotation() != 90 && getCenterRotation() != 270) {
            text.setBoundsType(TextBoundsType.VISUAL);
            text.getTransforms().add(new Rotate(getCenterRotation(),
                                            x + text.getLayoutBounds().getWidth() / 2,
                                            y - text.getLayoutBounds().getHeight() / 2));
        }

        // draw cross for the text coordinate system
        Line vertical = new Line(x, y + CROSS_HALF_LINE_LENGTH, x, y - CROSS_HALF_LINE_LENGTH);
        vertical.setStrokeWidth(STROKE_WIDTH);
        vertical.setStroke(Color.GREY);

        Line horizontal = new Line(x - CROSS_HALF_LINE_LENGTH, y, x + CROSS_HALF_LINE_LENGTH, y);
        horizontal.setStrokeWidth(STROKE_WIDTH);
        horizontal.setStroke(Color.GREY);

        // add all to the returning scene node and rotate it
        sceneNode.getChildren().addAll(text, vertical, horizontal);
        sceneNode.getTransforms().add(new Rotate(-getRot(), x, y));

        return sceneNode;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        Node sceneNode = getSceneNode(referenceX, referenceY);
        BoundingBox bbox = new BoundingBox(sceneNode.getLayoutBounds().getMinX(),
                sceneNode.getLayoutBounds().getMinY(),
                sceneNode.getLayoutBounds().getMaxX(),
                sceneNode.getLayoutBounds().getMaxY()).rotate(getRot());

        return bbox;
    }

    @Override
    public void transformCoordinates(CoordinateTransformer ct, boolean signTransformation) {
        this.y1 = signTransformation ? ct.transformSign(getY1()) : ct.transformOrigin(getY1());
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
// special case if text is used in package for element name and value
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets font.
     *
     * @param font the font
     */
    public void setFont(TextFont font) {
        this.font = font;
        this.isSetFont = true;
    }

    /**
     * Sets ratio.
     *
     * @param ratio the ratio
     */
    public void setRatio(int ratio) {
        this.ratio = ratio;
        this.isSetRatio = true;
    }

    /**
     * Sets rot.
     *
     * @param rot the rot
     */
    public void setRot(int rot) {
        this.rot = rot;
        this.isSetRot = true;
    }

    /**
     * Sets align.
     *
     * @param align the align
     */
    public void setAlign(Align align) {
        this.align = align;
        this.isSetAlign = true;
    }

    /**
     * Sets distance.
     *
     * @param distance the distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
        this.isSetDistance = true;
    }

    /**
     * Sets center rotation. Is set for element names and values.
     *
     * @param centerRotation the center rotation
     */
    public void setCenterRotation(Integer centerRotation) {
        this.centerRotation = centerRotation;
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
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
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

    public int getLayerNumber() {
        return layerNumber;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public double getSize() {
        return size;
    }

    /**
     * Gets font.
     *
     * @return the font
     */
    public TextFont getFont() {
        return font;
    }

    /**
     * Gets ratio.
     *
     * @return the ratio
     */
    public int getRatio() {
        return ratio;
    }

    /**
     * Gets rot.
     *
     * @return the rot
     */
    public int getRot() {
        return rot;
    }

    /**
     * Gets align.
     *
     * @return the align
     */
    public Align getAlign() {
        return align;
    }

    /**
     * Gets distance.
     *
     * @return the distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Gets is set font.
     *
     * @return the is set font
     */
    public boolean getIsSetFont() {
        return isSetFont;
    }

    /**
     * Gets is set ratio.
     *
     * @return the is set ratio
     */
    public boolean getIsSetRatio() {
        return isSetRatio;
    }

    /**
     * Gets is set rot.
     *
     * @return the is set rot
     */
    public boolean getIsSetRot() {
        return isSetRot;
    }

    /**
     * Gets is set align.
     *
     * @return the is set align
     */
    public boolean getIsSetAlign() {
        return isSetAlign;
    }

    /**
     * Gets is set distance.
     *
     * @return the is set distance
     */
    public boolean getIsSetDistance() {
        return isSetDistance;
    }

    /**
     * Gets center rotation. Is used for element values and names.
     *
     * @return the center rotation
     */
    public Integer getCenterRotation() {
        return centerRotation;
    }
}
