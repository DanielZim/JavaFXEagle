package model;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.Light;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * The type Bounding box.
 */
public class BoundingBox {

    private Point2D lowerLeftEdge;
    private Point2D upperRightEdge;

    /**
     * Instantiates a new Bounding box.
     *
     * @param firstEdge  the first edge
     * @param secondEdge the second edge
     */
    public BoundingBox(Point2D firstEdge, Point2D secondEdge) {
        this(firstEdge.getX(), firstEdge.getY(), secondEdge.getX(), secondEdge.getY());
    }

    /**
     * Instantiates a new Bounding box.
     *
     * @param x1 the x 1
     * @param y1 the y 1
     * @param x2 the x 2
     * @param y2 the y 2
     */
    public BoundingBox(double x1, double y1, double x2, double y2) {
        double minX = Math.min(x1, x2);
        double maxX = Math.max(x1, x2);
        double minY = Math.min(y1, y2);
        double maxY = Math.max(y1, y2);

        this.lowerLeftEdge = new Point2D(minX, minY);
        this.upperRightEdge = new Point2D(maxX, maxY);
    }

    /**
     * Is overlapping boolean.
     *
     * @param bbox the bbox
     * @return the boolean
     */
    public boolean isOverlapping(BoundingBox bbox) {

        if (bbox == null) return false;
        if (getUpperRightEdge().getX() < bbox.getLowerLeftEdge().getX()) return false; // this is left of bbox
        if (getLowerLeftEdge().getX() > bbox.getUpperRightEdge().getX()) return false; // this is right of bbox
        if (getUpperRightEdge().getY() < bbox.getLowerLeftEdge().getY()) return false; // this is above bbox
        if (getLowerLeftEdge().getY() > bbox.getUpperRightEdge().getY()) return false; // this is below bbox

        return true; // boxes overlap
    }

    /**
     * Checks if the given point is in the bounding box area.
     *
     * @param point the point to be checked
     * @return the result
     */
    public boolean isIncluded(Point2D point) {
        return point.getX() >= getLowerLeftEdge().getX()
                && point.getX() <= getUpperRightEdge().getX()
                && point.getY() >= getLowerLeftEdge().getY()
                && point.getY() <= getUpperRightEdge().getY();
    }

    /**
     * Gets overlapping area.
     *
     * @param bbox the bbox
     * @return the overlapping area
     */
    public double getOverlappingArea(BoundingBox bbox) {

        if (isOverlapping(bbox)) {
            double xLowerLeft = Math.max(getLowerLeftEdge().getX(), bbox.getLowerLeftEdge().getX());
            double yLowerLeft = Math.max(getLowerLeftEdge().getY(), bbox.getLowerLeftEdge().getY());
            double xUpperRight = Math.min(getUpperRightEdge().getX(), bbox.getUpperRightEdge().getX());
            double yUpperRight = Math.min(getUpperRightEdge().getY(), bbox.getUpperRightEdge().getY());

            return Math.abs(xUpperRight - xLowerLeft) * Math.abs(yUpperRight - yLowerLeft);
        }

        return 0.0;
    }

    /**
     * Merge bounding box.
     *
     * @param bbox the bbox
     * @return the bounding box
     */
    public BoundingBox merge(BoundingBox bbox) {

        if (bbox == null) return this;

        double xLowerLeft = Math.min(getLowerLeftEdge().getX(), bbox.getLowerLeftEdge().getX());
        double yLowerLeft = Math.min(getLowerLeftEdge().getY(), bbox.getLowerLeftEdge().getY());
        double xUpperRight = Math.max(getUpperRightEdge().getX(), bbox.getUpperRightEdge().getX());
        double yUpperRight = Math.max(getUpperRightEdge().getY(), bbox.getUpperRightEdge().getY());

        return new BoundingBox(xLowerLeft, yLowerLeft, xUpperRight, yUpperRight);
    }

    /**
     * Rotate bounding box.
     *
     * @param angle the angle
     * @return the bounding box
     */
    public BoundingBox rotate(int angle) {
        return rotate(angle, getCenterPoint());
    }

    /**
     * Rotate bounding box.
     *
     * @param angle         the angle
     * @param rotationPoint the rotation point
     * @return the bounding box
     */
    public BoundingBox rotate(int angle, Point2D rotationPoint) {
        double rad = -Math.PI * (angle / 180.0);

        // translate the edges of the box so that the center is located at (0, 0)
        double translationX = -rotationPoint.getX();
        double translationY = -rotationPoint.getY();

        Point2D edge1 = translatePoint(getLowerLeftEdge(), translationX, translationY);
        Point2D edge2 = translatePoint(getLowerRightEdge(), translationX, translationY);
        Point2D edge3 = translatePoint(getUpperRightEdge(), translationX, translationY);
        Point2D edge4 = translatePoint(getUpperLeftEdge(), translationX, translationY);

        // rotate the edges
        Point2D rotatedEdge1 = rotatePoint(edge1, rad);
        Point2D rotatedEdge2 = rotatePoint(edge2, rad);
        Point2D rotatedEdge3 = rotatePoint(edge3, rad);
        Point2D rotatedEdge4 = rotatePoint(edge4, rad);

        // translate the edges back
        Point2D resultEdge1 = translatePoint(rotatedEdge1, -translationX, -translationY);
        Point2D resultEdge2 = translatePoint(rotatedEdge2, -translationX, -translationY);
        Point2D resultEdge3 = translatePoint(rotatedEdge3, -translationX, -translationY);
        Point2D resultEdge4 = translatePoint(rotatedEdge4, -translationX, -translationY);

        double minX = Math.min(Math.min(Math.min(resultEdge1.getX(), resultEdge2.getX()), resultEdge3.getX()), resultEdge4.getX());
        double maxX = Math.max(Math.max(Math.max(resultEdge1.getX(), resultEdge2.getX()), resultEdge3.getX()), resultEdge4.getX());
        double minY = Math.min(Math.min(Math.min(resultEdge1.getY(), resultEdge2.getY()), resultEdge3.getY()), resultEdge4.getY());
        double maxY = Math.max(Math.max(Math.max(resultEdge1.getY(), resultEdge2.getY()), resultEdge3.getY()), resultEdge4.getY());

        return new BoundingBox(minX, minY, maxX, maxY);
    }


    /**
     * Gets scene node.
     *
     * @return the scene node
     */
    public Node getSceneNode() {
        Group sceneNode = new Group();

        Line line1 = new Line(getLowerLeftEdge().getX(), getLowerLeftEdge().getY(),
                getUpperLeftEdge().getX(), getUpperLeftEdge().getY());
        line1.setStroke(Color.PURPLE);
        line1.setStrokeWidth(0.1);

        Line line2 = new Line(getLowerLeftEdge().getX(), getLowerLeftEdge().getY(),
                getLowerRightEdge().getX(), getLowerRightEdge().getY());
        line2.setStroke(Color.PURPLE);
        line2.setStrokeWidth(0.1);

        Line line3 = new Line(getUpperLeftEdge().getX(), getUpperLeftEdge().getY(),
                getUpperRightEdge().getX(), getUpperRightEdge().getY());
        line3.setStroke(Color.PURPLE);
        line3.setStrokeWidth(0.1);

        Line line4 = new Line(getUpperRightEdge().getX(), getUpperRightEdge().getY(),
                getLowerRightEdge().getX(), getLowerRightEdge().getY());
        line4.setStroke(Color.PURPLE);
        line4.setStrokeWidth(0.1);

        sceneNode.getChildren().addAll(line1, line2, line3, line4);

        return sceneNode;
    }


    /**
     * Rotates a given point with an angle.
     *
     * @param point the point to rotate
     * @param angle the angle
     * @return the rotated point
     */
    private Point2D rotatePoint(Point2D point, double angle) {
        double x = point.getX() * Math.cos(angle) - point.getY() * Math.sin(angle);
        double y = point.getX() * Math.sin(angle) + point.getY() * Math.cos(angle);

        return new Point2D(x, y);
    }

    /**
     * Translates a given point.
     *
     * @param point        the point to translate
     * @param translationX the x translation
     * @param translationY the y translation
     * @return the translated point
     */
    private Point2D translatePoint(Point2D point, double translationX, double translationY) {
        return new Point2D(point.getX() + translationX, point.getY() + translationY);
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "lowerLeftEdge=" + getLowerLeftEdge() +
                ", upperRightEdge=" + getUpperRightEdge() +
                '}';
    }

    /**
     * Gets min x.
     *
     * @return the min x
     */
    public double getMinX() {
        return lowerLeftEdge.getX();
    }

    /**
     * Gets min y.
     *
     * @return the min y
     */
    public double getMinY() {
        return lowerLeftEdge.getY();
    }

    /**
     * Gets max x.
     *
     * @return the max x
     */
    public double getMaxX() {
        return upperRightEdge.getX();
    }

    /**
     * Gets max y.
     *
     * @return the max y
     */
    public double getMaxY() {
        return upperRightEdge.getY();
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public double getHeight() {
        return Math.abs(getUpperRightEdge().getY() - getLowerLeftEdge().getY());
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public double getWidth() {
        return Math.abs(getUpperRightEdge().getX() - getLowerLeftEdge().getX());
    }

    /**
     * Gets lower left edge.
     *
     * @return the lower left edge
     */
    public Point2D getLowerLeftEdge() {
        return lowerLeftEdge;
    }

    /**
     * Gets upper right edge.
     *
     * @return the upper right edge
     */
    public Point2D getUpperRightEdge() {
        return upperRightEdge;
    }

    /**
     * Gets lower right edge.
     *
     * @return the lower right edge
     */
    public Point2D getLowerRightEdge() {
        return new Point2D(getUpperRightEdge().getX(), getLowerLeftEdge().getY());
    }

    /**
     * Gets upper left edge.
     *
     * @return the upper left edge
     */
    public Point2D getUpperLeftEdge() {
        return new Point2D(getLowerLeftEdge().getX(), getUpperRightEdge().getY());
    }

    /**
     * Gets center point.
     *
     * @return the center point
     */
    public Point2D getCenterPoint() {
        double x = (getLowerLeftEdge().getX() + getUpperRightEdge().getX()) / 2;
        double y = (getLowerLeftEdge().getY() + getUpperRightEdge().getY()) / 2;

        return new Point2D(x, y);
    }
}
