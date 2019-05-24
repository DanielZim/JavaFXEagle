package model.sceneObjects.coordinateTransformables;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import model.BoundingBox;
import model.Vertex;
import model.enums.PolygonPour;
import model.sceneObjects.EagleBoard;
import utils.ColorProvider;
import utils.CoordinateTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Eagle polygon.
 */
public class EaglePolygon implements ICoordinateTransformable, ILayerable {

    private static final double DEFAULT_ISOLATE = 0.3;

    // required
    private EagleBoard board;
    private double width;
    private int layerNumber;

    // implied
    private double spacing;
    private double isolate;

    // optional
    private PolygonPour pour;
    private boolean orphans;
    private boolean thermals;
    private int rank;

    private List<Vertex> vertices;

    private boolean isSetSpacing;
    private boolean isSetIsolate;
    private boolean isSetPour;
    private boolean isSetOrphans;
    private boolean isSetThermals;
    private boolean isSetRank;

    private boolean isSetVertices;

    /**
     * Instantiates a new Eagle polygon.
     *
     * @param board       the board
     * @param width       the width
     * @param layerNumber the layer number
     */
    public EaglePolygon(EagleBoard board, double width, int layerNumber) {
        this.board = board;
        this.width = width;
        this.layerNumber = layerNumber;

        this.spacing = 0.0;
        this.isolate = 0.0;

        this.pour = PolygonPour.Solid;
        this.orphans = false;
        this.thermals = true;
        this.rank = 0;

        this.vertices = new ArrayList<>();

        this.isSetSpacing = false;
        this.isSetIsolate = false;
        this.isSetPour = false;
        this.isSetOrphans = false;
        this.isSetThermals = false;
        this.isSetRank = false;

        this.isSetVertices = false;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        // spacing, pour, orphans, thermals, rank are ignored at the moment
        Group sceneNode = new Group();
        List<Double> coordinates = new ArrayList<>();

        Color color = ColorProvider.getColorByLayer(getBoard(), getLayerNumber());

        if(getIsSetVertices()) {
            for(Vertex vertex : getVertices()) {
                coordinates.add(referenceX + vertex.getX1());
                coordinates.add(referenceY + vertex.getY1());
            }

            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(coordinates);
            polygon.setFill(Color.TRANSPARENT);
            polygon.setStrokeWidth(getWidth());
            polygon.setStroke(color);
            polygon.setStrokeLineCap(StrokeLineCap.ROUND);

            double isolate = DEFAULT_ISOLATE;

            if(getIsSetIsolate() && getIsolate() != 0) {
                isolate = getIsolate();
            }

            polygon.getStrokeDashArray().addAll(isolate, isolate * 2);

            sceneNode.getChildren().add(polygon);
        }

        return sceneNode;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        BoundingBox bbox = null;

        if(getIsSetVertices()) {
            for (Vertex vertex : getVertices()) {
                minX = Math.min(minX, vertex.getX1());
                minY = Math.min(minY, vertex.getY1());
                maxX = Math.max(maxX, vertex.getX1());
                maxY = Math.max(maxY, vertex.getY1());
            }

            bbox = new BoundingBox(minX + referenceX, minY + referenceY, maxX + referenceX, minY + referenceY);
        }

        return bbox;
    }

    @Override
    public void transformCoordinates(CoordinateTransformer ct, boolean signTransformation) {
        for (Vertex vertex : this.vertices) {
            vertex.setY1(signTransformation ? ct.transformSign(vertex.getY1()) : ct.transformOrigin(vertex.getY1()));
        }
    }

    /**
     * Add vertex.
     *
     * @param vertex the vertex
     */
    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
        this.isSetVertices = true;
    }

    /**
     * Sets vertices.
     *
     * @param vertices the vertices
     */
    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
        this.isSetVertices = true;
    }

    /**
     * Sets pour.
     *
     * @param pour the pour
     */
    public void setPour(PolygonPour pour) {
        this.pour = pour;
        this.isSetPour = true;
    }

    /**
     * Sets orphans.
     *
     * @param orphans the orphans
     */
    public void setOrphans(boolean orphans) {
        this.orphans = orphans;
        this.isSetOrphans = true;
    }

    /**
     * Sets thermals.
     *
     * @param thermals the thermals
     */
    public void setThermals(boolean thermals) {
        this.thermals = thermals;
         this.isSetThermals = true;
    }

    /**
     * Sets rank.
     *
     * @param rank the rank
     */
    public void setRank(int rank) {
        this.rank = rank;
        this.isSetRank = true;
    }

    /**
     * Sets spacing.
     *
     * @param spacing the spacing
     */
    public void setSpacing(double spacing) {
        this.spacing = spacing;
        this.isSetSpacing = true;
    }

    /**
     * Sets isolate.
     *
     * @param isolate the isolate
     */
    public void setIsolate(double isolate) {
        this.isolate = isolate;
        this.isSetIsolate = true;
    }

    /**
     * Gets vertices.
     *
     * @return the vertices
     */
    public List<Vertex> getVertices() {
        return vertices;
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
     * Gets width.
     *
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    public int getLayerNumber() {
        return layerNumber;
    }

    /**
     * Gets spacing.
     *
     * @return the spacing
     */
    public double getSpacing() {
        return spacing;
    }

    /**
     * Gets isolate.
     *
     * @return the isolate
     */
    public double getIsolate() {
        return isolate;
    }

    /**
     * Gets pour.
     *
     * @return the pour
     */
    public PolygonPour getPour() {
        return pour;
    }

    /**
     * Gets orphans.
     *
     * @return the orphans
     */
    public boolean getOrphans() {
        return orphans;
    }

    /**
     * Gets thermals.
     *
     * @return the thermals
     */
    public boolean getThermals() {
        return thermals;
    }

    /**
     * Gets rank.
     *
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Gets is set spacing.
     *
     * @return the is set spacing
     */
    public boolean getIsSetSpacing() {
        return isSetSpacing;
    }

    /**
     * Gets is set isolate.
     *
     * @return the is set isolate
     */
    public boolean getIsSetIsolate() {
        return isSetIsolate;
    }

    /**
     * Gets is set pour.
     *
     * @return the is set pour
     */
    public boolean getIsSetPour() {
        return isSetPour;
    }

    /**
     * Gets is set orphans.
     *
     * @return the is set orphans
     */
    public boolean getIsSetOrphans() {
        return isSetOrphans;
    }

    /**
     * Gets is set thermals.
     *
     * @return the is set thermals
     */
    public boolean getIsSetThermals() {
        return isSetThermals;
    }

    /**
     * Gets is set rank.
     *
     * @return the is set rank
     */
    public boolean getIsSetRank() {
        return isSetRank;
    }

    /**
     * Gets is set vertices.
     *
     * @return the is set vertices
     */
    public boolean getIsSetVertices() {
        return isSetVertices;
    }
}
