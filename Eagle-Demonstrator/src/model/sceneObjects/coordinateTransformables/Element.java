package model.sceneObjects.coordinateTransformables;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import model.BoundingBox;
import model.Library;
import model.sceneObjects.EagleBoard;
import utils.CoordinateTransformer;

/**
 * The type Element.
 */
public class Element implements ICoordinateTransformable {

    private static final double STROKE_WIDTH = 0.05;
    private static final double CROSS_HALF_LINE_LENGTH = 0.5;

    private Point2D bboxLowerLeftVector;
    private Point2D bboxUpperRightVector;
    private int bboxCount;

    // required
    private EagleBoard board;
    private String name;
    private String libraryName;
    private String packageName;
    private String value;
    private double x1;
    private double y1;

    // optional
    private String description;
    private boolean locked;
    private boolean smashed;
    private int rot;

    private boolean isSetDescription;
    private boolean isSetLocked;
    private boolean isSetSmashed;
    private boolean isSetRot;

    /**
     * Instantiates a new Element.
     *
     * @param board       the board
     * @param name        the name
     * @param libraryName the library name
     * @param packageName the package name
     * @param value       the value
     * @param x1          the x 1
     * @param y1          the y 1
     */
    public Element(EagleBoard board, String name, String libraryName, String packageName, String value, double x1, double y1) {
        this.board = board;
        this.name = name;
        this.libraryName = libraryName;
        this.packageName = packageName;
        this.value = value;
        this.x1 = x1;
        this.y1 = y1;

        this.description = null;
        this.locked = false;
        this.smashed = false;
        this.rot = 0;

        this.isSetDescription = false;
        this.isSetLocked = false;
        this.isSetSmashed = false;
        this.isSetRot = false;

        this.bboxCount = 0;
    }


    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        // smashed and locked are ignored at the moment

        Pane sceneNode = new Pane();

        if(getBoard().getIsSetLibraries()) {
            Library lib = getBoard().getLibraries().get(getLibraryName());

            if(lib != null && lib.getIsSetPackages()) {
                EaglePackage pack = lib.getPackages().get(getPackageName());

                if(pack != null) {
                    sceneNode.getChildren().add(pack.getSceneNode(getX1(), getY1(), getName(), getValue(), getRot()));
                }
            }
        }

        // draw cross for the element coordinate system
        Line vertical = new Line(getX1(), getY1() + CROSS_HALF_LINE_LENGTH, getX1(), getY1() - CROSS_HALF_LINE_LENGTH);
        vertical.setStrokeWidth(STROKE_WIDTH);
        vertical.setStroke(Color.BLACK);

        Line horizontal = new Line(getX1() - CROSS_HALF_LINE_LENGTH, getY1(), getX1() + CROSS_HALF_LINE_LENGTH, getY1());
        horizontal.setStrokeWidth(STROKE_WIDTH);
        horizontal.setStroke(Color.BLACK);

        sceneNode.getChildren().addAll(vertical, horizontal);
        sceneNode.getTransforms().add(new Rotate(-getRot(), getX1(), getY1()));
        sceneNode.setOnMouseClicked(event -> getElementInformation());

        return sceneNode;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        BoundingBox bbox;

        // if we already computed the bbox we use the edge vectors and the current position to get the bbox
        // bboxCount is needed because the first time we calculate the bbox the coordinate transformation has not
        // been done, so we wait for the next computed values.
        if(bboxLowerLeftVector != null && bboxUpperRightVector != null && bboxCount >= 2) {
            Point2D position = new Point2D(getX1(), getY1());

            bbox = new BoundingBox(position.add(bboxLowerLeftVector),
                                   position.add(bboxUpperRightVector));
        } // otherwise we have to compute the bbox and save the vectors
        else
        {
            bbox = new BoundingBox(getX1() - CROSS_HALF_LINE_LENGTH,
                    getY1() - CROSS_HALF_LINE_LENGTH,
                    getX1() + CROSS_HALF_LINE_LENGTH,
                    getY1() + CROSS_HALF_LINE_LENGTH);

            if(getBoard().getIsSetLibraries()) {
                Library lib = getBoard().getLibraries().get(getLibraryName());

                if(lib != null && lib.getIsSetPackages()) {
                    EaglePackage pack = lib.getPackages().get(getPackageName());

                    if(pack != null) {
                        BoundingBox packBbox = pack.getBoundingBox(getX1(), getY1());
                        bbox = bbox.merge(packBbox);
                    }
                }
            }

            Point2D position = new Point2D(getX1(), getY1());
            bboxLowerLeftVector = bbox.getLowerLeftEdge().subtract(position);
            bboxUpperRightVector = bbox.getUpperRightEdge().subtract(position);
            bboxCount++;
        }

        bbox = bbox.rotate(getRot(), new Point2D(getX1(), getY1()));

        return bbox;
    }

    @Override
    public void transformCoordinates(CoordinateTransformer ct, boolean signTransformation) {
        this.y1 = signTransformation ? ct.transformSign(getY1()) : ct.transformOrigin(getY1());
    }


    /**
     * Gets element information.
     *
     * @return the element information
     */
    public String getElementInformation() {
        CoordinateTransformer ct = new CoordinateTransformer(getBoard().getHeight());
        double y = ct.transformOrigin(getY1());

        String information = "Element: \t\t" + getName() + "\n";
        information += "Library: \t\t" + getLibraryName() + "\n";
        information += "Package: \t" + getPackageName() + "\n";
        information += "Value: \t\t" + getValue() + "\n";
        information += "X: \t\t\t" + getX1() + "\n";
        information += "Y: \t\t\t" + y + "\n";
        information += "Rotation: \t" + getRot();

        return information;
    }

    /**
     * Sets bounding box.
     *
     * @param bbox the bbox
     */
    public void setBoundingBox(BoundingBox bbox) {
        Point2D position = new Point2D(getX1(), getY1());
        bboxLowerLeftVector = bbox.getLowerLeftEdge().subtract(position);
        bboxUpperRightVector = bbox.getUpperRightEdge().subtract(position);
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
        this.isSetDescription = true;
    }

    /**
     * Sets locked.
     *
     * @param locked the locked
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
        this.isSetLocked = true;
    }

    /**
     * Sets smashed.
     *
     * @param smashed the smashed
     */
    public void setSmashed(boolean smashed) {
        this.smashed = smashed;
        this.isSetSmashed = true;
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
     * Sets x 1.
     *
     * @param x1 the x 1
     */
// the two following setter are only used to place the element randomly
    public void setX1(double x1) {
        this.x1 = x1;
    }

    /**
     * Sets y 1.
     *
     * @param y1 the y 1
     */
    public void setY1(double y1) {
        this.y1 = y1;
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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets library name.
     *
     * @return the library name
     */
    public String getLibraryName() {
        return libraryName;
    }

    /**
     * Gets package name.
     *
     * @return the package name
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
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

    /**
     * Gets locked.
     *
     * @return the locked
     */
    public boolean getLocked() {
        return locked;
    }

    /**
     * Gets smashed.
     *
     * @return the smashed
     */
    public boolean getSmashed() {
        return smashed;
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
     * Gets is set description.
     *
     * @return the is set description
     */
    public boolean getIsSetDescription() {
        return isSetDescription;
    }

    /**
     * Gets is set locked.
     *
     * @return the is set locked
     */
    public boolean getIsSetLocked() {
        return isSetLocked;
    }

    /**
     * Gets is set smashed.
     *
     * @return the is set smashed
     */
    public boolean getIsSetSmashed() {
        return isSetSmashed;
    }

    /**
     * Gets is set rot.
     *
     * @return the is set rot
     */
    public boolean getIsSetRot() {
        return isSetRot;
    }
}
