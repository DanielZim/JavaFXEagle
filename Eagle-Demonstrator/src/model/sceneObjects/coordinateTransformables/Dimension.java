package model.sceneObjects.coordinateTransformables;

import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.transform.Rotate;
import model.BoundingBox;
import model.enums.DimensionType;
import model.enums.GridUnit;
import model.sceneObjects.EagleBoard;
import utils.ColorProvider;
import utils.CoordinateTransformer;

/**
 * The type Dimension.
 */
public class Dimension implements ICoordinateTransformable, ILayerable {

    private static final double DEFAULT_WIDTH = 0.1; // 0.1 standard
    private static final double ARROW_HEIGHT = 0.6; // 0.6 standard
    private static final double ARROW_LENGTH = 2; // 2 standard

    // required
    private EagleBoard board;
    private double x1;
    private double y1;
    private int layerNumber;
    private double x2;
    private double y2;
    private double x3;
    private double y3;
    private double textSize;

    // optional
    private double width; // line width
    private DimensionType dtype;
    private double extWidth;
    private double extLength;
    private double extOffset;
    private double textRatio;
    private GridUnit unit;
    private int precision;
    private boolean visible;

    private boolean isSetWidth;
    private boolean isSetDtype;
    private boolean isSetExtWidth;
    private boolean isSetExtLength;
    private boolean isSetExtOffset;
    private boolean isSetTextRatio;
    private boolean isSetUnit;
    private boolean isSetPrecision;
    private boolean isSetVisible;

    /**
     * Instantiates a new Dimension.
     *
     * @param board       the board
     * @param x1          the x 1
     * @param y1          the y 1
     * @param layerNumber the layer number
     * @param x2          the x 2
     * @param y2          the y 2
     * @param x3          the x 3
     * @param y3          the y 3
     * @param textSize    the text size
     */
    public Dimension(EagleBoard board, double x1, double y1, int layerNumber, double x2, double y2, double x3, double y3, double textSize) {
        this.board = board;
        this.x1 = x1;
        this.y1 = y1;
        this.layerNumber = layerNumber;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.textSize = textSize;

        this.dtype = DimensionType.Parallel;
        this.textRatio = 8;
        this.unit = GridUnit.Mm;
        this.precision = 2;
        this.visible = false;

        this.isSetWidth = false;
        this.isSetDtype = false;
        this.isSetExtWidth = false;
        this.isSetExtLength = false;
        this.isSetExtOffset = false;
        this.isSetTextRatio = false;
        this.isSetUnit = false;
        this.isSetPrecision = false;
        this.isSetVisible = false;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        Group sceneNode = new Group();
        double x1 = referenceX + getX1();
        double y1 = referenceY + getY1();
        double x2 = referenceX + getX2();
        double y2 = referenceY + getY2();
        double x3 = referenceX + getX3();
        double y3 = referenceY + getY3();
        Point2D p1 = new Point2D(x1, y1);
        Point2D p2 = new Point2D(x2, y2);
        Point2D p3 = new Point2D(x3, y3);
        double strokeWidth = getIsSetWidth() ? getWidth() : DEFAULT_WIDTH;

        Color color = ColorProvider.getColorByLayer(getBoard(), getLayerNumber());

        switch(getDtype()) {
            case Parallel:
            case Leader:
            case Radius:
            case Diameter:
            case Vertical:
            case Horizontal:
                drawParallelDimension(sceneNode, p1, p2, p3, color, strokeWidth);
        }

        return sceneNode;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        double minX = Math.min(Math.min(getX1(), getX2()), getX3());
        double minY = Math.min(Math.min(getY1(), getY2()), getY3());
        double maxX = Math.max(Math.max(getX1(), getX2()), getX3());
        double maxY = Math.max(Math.max(getY1(), getY2()), getY3());

        BoundingBox bbox = new BoundingBox(minX + referenceX, minY + referenceY, maxX + referenceX, maxY + referenceY);

        return bbox;
    }

    /**
     * Draws the parallel dimensions.
     *
     * @param sceneNode     the scene node
     * @param p1            point 1
     * @param p2            point 2
     * @param p3            point 3
     * @param color         the color
     * @param strokeWidth   the stroke width
     */
    private void drawParallelDimension(Group sceneNode, Point2D p1, Point2D p2, Point2D p3, Color color, double strokeWidth) {
        double dimensionLineLength = p1.distance(p2);

        Point2D middle = p2.midpoint(p1);
        Point2D directionVector = p3.subtract(middle);
        Point2D dimensionLineStart = p1.add(directionVector);
        Point2D dimensionLineEnd = p2.add(directionVector);
        Point2D startToEndVector = dimensionLineEnd.subtract(dimensionLineStart);

        // draw dimension line
        Line dimensionLine = new Line(dimensionLineStart.getX(), dimensionLineStart.getY(),
                dimensionLineEnd.getX(), dimensionLineEnd.getY());
        dimensionLine.setStrokeWidth(strokeWidth);
        dimensionLine.setStroke(color);
        sceneNode.getChildren().add(dimensionLine);

        // draw arrows
        drawArrowAtLineEnd(sceneNode, dimensionLineStart, startToEndVector.multiply(-1), color);
        drawArrowAtLineEnd(sceneNode, dimensionLineEnd, startToEndVector, color);

        // draw side lines
        Line side1 = new Line(p1.getX(), p1.getY(), dimensionLineStart.getX(), dimensionLineStart.getY());
        side1.setStrokeWidth(strokeWidth);
        side1.setStroke(color);
        Line side2 = new Line(p2.getX(), p2.getY(), dimensionLineEnd.getX(), dimensionLineEnd.getY());
        side2.setStrokeWidth(strokeWidth);
        side2.setStroke(color);
        sceneNode.getChildren().addAll(side1, side2);

        // draw text label
        drawLabel(sceneNode, p3, dimensionLineLength, startToEndVector, color, directionVector);
    }

    /**
     * Draws an arrow at the end of the line.
     *
     * @param sceneNode         the scene node
     * @param lineEnd           the point representing the end of the line
     * @param arrowDirection    the direction of the arrow
     * @param color             the color
     */
    private void drawArrowAtLineEnd(Group sceneNode, Point2D lineEnd, Point2D arrowDirection, Color color) {
        Point2D normalVector = new Point2D(-arrowDirection.getY(), arrowDirection.getX()).normalize();

        Point2D arrowEdge1 = lineEnd.add(normalVector.multiply(ARROW_HEIGHT / 2));
        arrowEdge1 = arrowEdge1.add(arrowDirection.normalize().multiply(-ARROW_LENGTH));

        Point2D arrowEdge2 = lineEnd.subtract(normalVector.multiply(ARROW_HEIGHT / 2));
        arrowEdge2 = arrowEdge2.add(arrowDirection.normalize().multiply(-ARROW_LENGTH));

        Polygon arrow = new Polygon(lineEnd.getX(), lineEnd.getY(),
                arrowEdge1.getX(), arrowEdge1.getY(),
                arrowEdge2.getX(), arrowEdge2.getY());
        arrow.setFill(color);

        sceneNode.getChildren().add(arrow);
    }

    /**
     * Draws the label.
     *
     * @param sceneNode             the scene node
     * @param p3                    the position
     * @param dimensionLineLength   the length of the dimension line
     * @param lineDirectionVector   the direction of the line vector
     * @param color                 the color
     */
    private void drawLabel(Group sceneNode, Point2D p3, double dimensionLineLength, Point2D lineDirectionVector,
                           Color color, Point2D textMoveVector) {
        String lengthString = String.format("%.2f", dimensionLineLength);
        Text text = new Text(p3.getX(), p3.getY(), lengthString);
        text.setFont(new Font(getTextSize()));
        text.setFill(color);
        text.setTextOrigin(VPos.CENTER);
        text.setBoundsType(TextBoundsType.VISUAL);
        double textHeight = text.getLayoutBounds().getHeight();

        // use the move vector an the text height to move the label away from the line
        Point2D moveVec = textMoveVector.normalize().multiply(textHeight);
        Point2D position = new Point2D(p3.getX() + moveVec.getX(), p3.getY() - moveVec.getY());
        text.setX(position.getX());
        text.setY(position.getY());

        // rotate text around its center
        Point2D xAxis = new Point2D(1, 0);
        double rotRad = Math.acos(xAxis.dotProduct(lineDirectionVector) / dimensionLineLength);
        double rotDegree = -rotRad * 180 / Math.PI;
        text.getTransforms().add(new Rotate(rotDegree, position.getX(), position.getY()));

        sceneNode.getChildren().addAll(text);
    }

    /**
     * Sets width.
     *
     * @param width the width
     */
    public void setWidth(double width) {
        this.width = width;
        this.isSetWidth = true;
    }

    /**
     * Sets dtype.
     *
     * @param dtype the dtype
     */
    public void setDtype(DimensionType dtype) {
        this.dtype = dtype;
        this.isSetDtype = true;
    }

    /**
     * Sets ext width.
     *
     * @param extWidth the ext width
     */
    public void setExtWidth(double extWidth) {
        this.extWidth = extWidth;
        this.isSetExtWidth = true;
    }

    /**
     * Sets ext length.
     *
     * @param extLength the ext length
     */
    public void setExtLength(double extLength) {
        this.extLength = extLength;
        this.isSetExtLength = true;
    }

    /**
     * Sets ext offset.
     *
     * @param extOffset the ext offset
     */
    public void setExtOffset(double extOffset) {
        this.extOffset = extOffset;
        this.isSetExtOffset = true;
    }

    /**
     * Sets text ratio.
     *
     * @param textRatio the text ratio
     */
    public void setTextRatio(double textRatio) {
        this.textRatio = textRatio;
        this.isSetTextRatio = true;
    }

    /**
     * Sets unit.
     *
     * @param unit the unit
     */
    public void setUnit(GridUnit unit) {
        this.unit = unit;
        this.isSetUnit = true;
    }

    /**
     * Sets precision.
     *
     * @param precision the precision
     */
    public void setPrecision(int precision) {
        this.precision = precision;
        this.isSetPrecision = true;
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

    public int getLayerNumber() {
        return layerNumber;
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

    /**
     * Gets x 3.
     *
     * @return the x 3
     */
    public double getX3() {
        return x3;
    }

    /**
     * Gets y 3.
     *
     * @return the y 3
     */
    public double getY3() {
        return y3;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets text size.
     *
     * @return the text size
     */
    public double getTextSize() {
        return textSize;
    }

    /**
     * Gets dtype.
     *
     * @return the dtype
     */
    public DimensionType getDtype() {
        return dtype;
    }

    /**
     * Gets ext width.
     *
     * @return the ext width
     */
    public double getExtWidth() {
        return extWidth;
    }

    /**
     * Gets ext length.
     *
     * @return the ext length
     */
    public double getExtLength() {
        return extLength;
    }

    /**
     * Gets ext offset.
     *
     * @return the ext offset
     */
    public double getExtOffset() {
        return extOffset;
    }

    /**
     * Gets text ratio.
     *
     * @return the text ratio
     */
    public double getTextRatio() {
        return textRatio;
    }

    /**
     * Gets unit.
     *
     * @return the unit
     */
    public GridUnit getUnit() {
        return unit;
    }

    /**
     * Gets precision.
     *
     * @return the precision
     */
    public int getPrecision() {
        return precision;
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
     * Gets is set dtype.
     *
     * @return the is set dtype
     */
    public boolean getIsSetDtype() {
        return isSetDtype;
    }

    /**
     * Gets is set ext width.
     *
     * @return the is set ext width
     */
    public boolean getIsSetExtWidth() {
        return isSetExtWidth;
    }

    /**
     * Gets is set ext length.
     *
     * @return the is set ext length
     */
    public boolean getIsSetExtLength() {
        return isSetExtLength;
    }

    /**
     * Gets is set ext offset.
     *
     * @return the is set ext offset
     */
    public boolean getIsSetExtOffset() {
        return isSetExtOffset;
    }

    /**
     * Gets is set text ratio.
     *
     * @return the is set text ratio
     */
    public boolean getIsSetTextRatio() {
        return isSetTextRatio;
    }

    /**
     * Gets is set unit.
     *
     * @return the is set unit
     */
    public boolean getIsSetUnit() {
        return isSetUnit;
    }

    /**
     * Gets is set precision.
     *
     * @return the is set precision
     */
    public boolean getIsSetPrecision() {
        return isSetPrecision;
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
     * Get is set width boolean.
     *
     * @return the boolean
     */
    public boolean getIsSetWidth(){return isSetWidth;}


    @Override
    public void transformCoordinates(CoordinateTransformer ct, boolean signTransformation) {
        if (signTransformation) {
            this.y1 = ct.transformSign(getY1());
            this.y2 = ct.transformSign(getY2());
            this.y3 = ct.transformSign(getY3());
        }
        else {
            this.y1 = ct.transformOrigin(getY1());
            this.y2 = ct.transformOrigin(getY2());
            this.y3 = ct.transformOrigin(getY3());
        }
    }
}
