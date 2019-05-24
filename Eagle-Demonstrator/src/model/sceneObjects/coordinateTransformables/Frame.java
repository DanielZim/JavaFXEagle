package model.sceneObjects.coordinateTransformables;

import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.BoundingBox;
import model.sceneObjects.EagleBoard;
import utils.ColorProvider;
import utils.CoordinateTransformer;

/**
 * The type Frame.
 */
public class Frame implements ICoordinateTransformable, ILayerable {

    private static final double OUTER_STROKE_WIDTH = 0.2;
    private static final double INNER_STROKE_WIDTH = 0.1;
    private static final double BORDER_WIDTH = 4;
    private static final double TEXT_SIZE = 3;

    private EagleBoard board;
    private double x1;
    private double y1;
    private int layerNumber;
    private double x2;
    private double y2;
    private int columns;
    private int rows;

    private boolean borderLeft;
    private boolean borderTop;
    private boolean borderRight;
    private boolean borderBottom;

    /**
     * Instantiates a new Frame.
     *
     * @param board       the board
     * @param x1          the x 1
     * @param y1          the y 1
     * @param layerNumber the layer number
     * @param x2          the x 2
     * @param y2          the y 2
     * @param columns     the columns
     * @param rows        the rows
     */
    public Frame(EagleBoard board, double x1, double y1, int layerNumber, double x2, double y2, int columns, int rows) {
        this.board = board;
        this.x1 = x1;
        this.y1 = y1;
        this.layerNumber = layerNumber;
        this.x2 = x2;
        this.y2 = y2;
        this.columns = columns;
        this.rows = rows;

        this.borderBottom = true;
        this.borderLeft = true;
        this.borderTop = true;
        this.borderRight = true;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        Group sceneNode = new Group();

        double x1 = getX1() + referenceX;
        double y1 = getY1() + referenceY;
        double x2 = getX2() + referenceX;
        double y2 = getY2() + referenceY;
        Color color = ColorProvider.getColorByLayer(getBoard(), getLayerNumber());

        // get to know which coordinate belongs to which frame edge
        double minX = Math.min(x1, x2);
        double maxX = Math.max(x1, x2);
        double minY = Math.min(y1, y2);
        double maxY = Math.max(y1, y2);

        // draw outer line
        Polygon outerLine = new Polygon(minX, minY,
                                        minX, maxY,
                                        maxX, maxY,
                                        maxX, minY);
        outerLine.setFill(Color.TRANSPARENT);
        outerLine.setStroke(color);
        outerLine.setStrokeWidth(OUTER_STROKE_WIDTH);
        sceneNode.getChildren().add(outerLine);

        // draw inner lines if required
        if(getBorderTop()) {
            double xLineStart = getBorderLeft() ? minX + BORDER_WIDTH : minX;
            double xLineEnd = getBorderRight() ? maxX - BORDER_WIDTH : maxX;
            double yLine = maxY - BORDER_WIDTH;

            drawInnerLine(sceneNode, xLineStart, yLine, xLineEnd, yLine, color);
        }

        if(getBorderLeft()) {
            double xLine = minX + BORDER_WIDTH;
            double yLineStart = getBorderBottom() ? minY + BORDER_WIDTH : minY;
            double yLineEnd = getBorderTop() ? maxY - BORDER_WIDTH : maxY;

            drawInnerLine(sceneNode, xLine, yLineStart, xLine, yLineEnd, color);
        }

        if(getBorderRight()) {
            double xLine = maxX - BORDER_WIDTH;
            double yLineStart = getBorderBottom() ? minY + BORDER_WIDTH : minY;
            double yLineEnd = getBorderTop() ? maxY - BORDER_WIDTH : maxY;

            drawInnerLine(sceneNode, xLine, yLineStart, xLine, yLineEnd, color);
        }

        if(getBorderBottom()) {
            double xLineStart = getBorderLeft() ? minX + BORDER_WIDTH : minX;
            double xLineEnd = getBorderRight() ? maxX - BORDER_WIDTH : maxX;
            double yLine = minY + BORDER_WIDTH;

            drawInnerLine(sceneNode, xLineStart, yLine, xLineEnd, yLine, color);
        }

        // if rows and columns are specified, then draw parting line and labels
        // rows are labeled with letters (A, B, C, ...)
        if(getRows() > 0) {
            drawRows(sceneNode, minX, maxX, minY, maxY, color);
        }

        // columns are label with numbers (1, 2, 3, ...)
        if(getColumns() > 0) {
            drawColumns(sceneNode, minX, maxX, minY, maxY, color);
        }

        return sceneNode;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        BoundingBox bbox = new BoundingBox(getX1() + referenceX, getY1() + referenceY,
                getX2() + referenceX, getY2() + referenceY);

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
     * Draws the rows.
     *
     * @param sceneNode the scene node
     * @param minX      minimum x value
     * @param maxX      maximum x value
     * @param minY      minimum y value
     * @param maxY      maximum y value
     * @param color     the color
     */
    private void drawRows(Group sceneNode, double minX, double maxX, double minY, double maxY, Color color) {
        double frameHeight = maxY - minY;

        if(getRows() > 1) {
            double rowHeight = frameHeight / getRows();

            // parting lines
            for(int i = 1; i < getRows(); i++) {
                if(getBorderRight()) {
                    double xRowLineStart = maxX - BORDER_WIDTH;
                    double xRowLineEnd = maxX;
                    double yRowLine = minY + i * rowHeight;

                    drawInnerLine(sceneNode, xRowLineStart, yRowLine, xRowLineEnd, yRowLine, color);
                }

                if(getBorderLeft()) {
                    double xRowLineStart = minX;
                    double xRowLineEnd = minX + BORDER_WIDTH;
                    double yRowLine = minY + i * rowHeight;

                    drawInnerLine(sceneNode, xRowLineStart, yRowLine, xRowLineEnd, yRowLine, color);
                }
            }

            // labels
            for (int i = 0; i < getRows(); i++) {
                if(getBorderRight()) {
                    double xTextPosition = maxX - BORDER_WIDTH / 2 - 1;
                    double yTextPosition = minY + i * rowHeight + (rowHeight / 2);
                    String value = getCharForNumber(i+1);

                    drawLabel(sceneNode, xTextPosition, yTextPosition, value, color);
                }

                if(getBorderLeft()) {
                    double xTextPosition = minX + BORDER_WIDTH / 2 - 1;
                    double yTextPosition = minY + i * rowHeight + (rowHeight / 2);
                    String value = getCharForNumber(i+1);

                    drawLabel(sceneNode, xTextPosition, yTextPosition, value, color);
                }
            }
        } else {
            // here we need no parting line
            if(getBorderRight()) {
                double xTextPosition = maxX - BORDER_WIDTH / 2 - 1;
                double yTextPosition = minY + (frameHeight / 2);
                String value = getCharForNumber(1);

                drawLabel(sceneNode, xTextPosition, yTextPosition, value, color);
            }

            if(getBorderLeft()) {
                double xTextPosition = minX + BORDER_WIDTH / 2 - 1;
                double yTextPosition = minY + (frameHeight / 2);
                String value = getCharForNumber(1);

                drawLabel(sceneNode, xTextPosition, yTextPosition, value, color);
            }
        }
    }

    /**
     * Draws the columns.
     *
     * @param sceneNode the scene node
     * @param minX      the minimum x value
     * @param maxX      the maximum x value
     * @param minY      the minimum y value
     * @param maxY      the maximum y value
     * @param color     the color
     */
    private void drawColumns(Group sceneNode, double minX, double maxX, double minY, double maxY, Color color) {
        double frameWidth = maxX - minX;

        if(getColumns() > 1) {
            double columnWidth = frameWidth / getColumns();

            // parting lines
            for(int i = 1; i < getRows(); i++) {
                // top
                if(getBorderTop()) {
                    double xColumnLine = minX + i * columnWidth;
                    double yColumnLineStart = maxY - BORDER_WIDTH;
                    double yColumnLineEnd = maxY;

                    drawInnerLine(sceneNode, xColumnLine, yColumnLineStart, xColumnLine, yColumnLineEnd, color);
                }

                // bottom
                if(getBorderBottom()) {
                    double xColumnLine = minX + i * columnWidth;
                    double yColumnLineStart = minY;
                    double yColumnLineEnd = minY + BORDER_WIDTH;

                    drawInnerLine(sceneNode, xColumnLine, yColumnLineStart, xColumnLine, yColumnLineEnd, color);
                }
            }

            // labels
            for (int i = 0; i < getColumns(); i++) {
                if (getBorderTop()) {
                    double xTextPosition = minX + i * columnWidth + (columnWidth / 2);
                    double yTextPosition = maxY - BORDER_WIDTH / 2;
                    String value = String.valueOf(i + 1);

                    drawLabel(sceneNode, xTextPosition, yTextPosition, value, color);
                }

                if (getBorderBottom()) {
                    double xTextPosition = minX + i * columnWidth + (columnWidth / 2);
                    double yTextPosition = minY + BORDER_WIDTH / 2;
                    String value = String.valueOf(i + 1);

                    drawLabel(sceneNode, xTextPosition, yTextPosition, value, color);
                }
            }
        } else {
            // here we need no parting line
            if (getBorderTop()) {
                double xTextPosition = minX + (frameWidth / 2);
                double yTextPosition = maxY - BORDER_WIDTH / 2;
                String value = String.valueOf(1);

                drawLabel(sceneNode, xTextPosition, yTextPosition, value, color);
            }

            if (getBorderBottom()) {
                double xTextPosition = minX + (frameWidth / 2);
                double yTextPosition = minY + BORDER_WIDTH / 2;
                String value = String.valueOf(1);

                drawLabel(sceneNode, xTextPosition, yTextPosition, value, color);
            }
        }
    }

    /**
     * Draws the inner line.
     *
     * @param sceneNode the scene node
     * @param x1        x1
     * @param y1        y1
     * @param x2        x2
     * @param y2        y2
     * @param color     the color
     */
    private void drawInnerLine(Group sceneNode, double x1, double y1, double x2, double y2, Color color) {
        Line rightPartingLine = new Line(x1, y1, x2, y2);
        rightPartingLine.setStrokeWidth(INNER_STROKE_WIDTH);
        rightPartingLine.setStroke(color);
        sceneNode.getChildren().add(rightPartingLine);
    }

    /**
     * Draws the label.
     *
     * @param sceneNode the scene node
     * @param x         x
     * @param y         y
     * @param value     the value
     * @param color     the color
     */
    private void drawLabel(Group sceneNode, double x, double y, String value, Color color) {
        Text label = new Text(x, y, value);
        label.setFont(new Font(TEXT_SIZE));
        label.setFill(color);
        label.setTextOrigin(VPos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        sceneNode.getChildren().add(label);
    }

    /**
     * Gets the char for a given number.
     *
     * @param i the number to convert to a char
     * @return the char as String
     */
    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }

    /**
     * Sets border left.
     *
     * @param borderLeft the border left
     */
    public void setBorderLeft(boolean borderLeft) {
        this.borderLeft = borderLeft;
    }

    /**
     * Sets border top.
     *
     * @param borderTop the border top
     */
    public void setBorderTop(boolean borderTop) {
        this.borderTop = borderTop;
    }

    /**
     * Sets border right.
     *
     * @param borderRight the border right
     */
    public void setBorderRight(boolean borderRight) {
        this.borderRight = borderRight;
    }

    /**
     * Sets border bottom.
     *
     * @param borderBottom the border bottom
     */
    public void setBorderBottom(boolean borderBottom) {
        this.borderBottom = borderBottom;
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
     * Gets columns.
     *
     * @return the columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Gets rows.
     *
     * @return the rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets border left.
     *
     * @return the border left
     */
    public boolean getBorderLeft() {
        return borderLeft;
    }

    /**
     * Gets border top.
     *
     * @return the border top
     */
    public boolean getBorderTop() {
        return borderTop;
    }

    /**
     * Gets border right.
     *
     * @return the border right
     */
    public boolean getBorderRight() {
        return borderRight;
    }

    /**
     * Gets border bottom.
     *
     * @return the border bottom
     */
    public boolean getBorderBottom() {
        return borderBottom;
    }
}
