package model.sceneObjects.coordinateTransformables;

import javafx.scene.Group;
import javafx.scene.Node;
import model.BoundingBox;
import model.ContactRef;
import model.sceneObjects.EagleBoard;
import utils.CoordinateTransformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type Signal.
 */
public class Signal implements ICoordinateTransformable {

    // required
    private EagleBoard board;
    private String name;

    // optional
    private int classNumber;
    private boolean airWiresHidden;

    private List<ContactRef> contactRefs;
    private List<EaglePolygon> polygons;
    private List<Wire> wires;
    private List<Via> vias;

    private boolean isSetClassNumber;
    private boolean isSetAirWiresHidden;

    private boolean isSetContactRefs;
    private boolean isSetPolygons;
    private boolean isSetWires;
    private boolean isSetVias;

    /**
     * Instantiates a new Signal.
     *
     * @param board the board
     * @param name  the name
     */
    public Signal(EagleBoard board, String name) {
        this.board = board;
        this.name = name;

        this.classNumber = 0;
        this.airWiresHidden = false;

        this.contactRefs = new ArrayList<>();
        this.polygons = new ArrayList<>();
        this.wires = new ArrayList<>();
        this.vias = new ArrayList<>();

        this.isSetClassNumber = false;
        this.isSetAirWiresHidden = false;
        this.isSetContactRefs = false;
        this.isSetPolygons = false;
        this.isSetWires = false;
        this.isSetVias = false;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        Group sceneNode = new Group();

        if(getIsSetPolygons()) {
            for (EaglePolygon poly : getPolygons()) {
                sceneNode.getChildren().add(poly.getSceneNode());
            }
        }

        if(getIsSetWires()) {
            for(Wire wire : getWires()) {
                sceneNode.getChildren().add(wire.getSceneNode());
            }
        }

        if(getIsSetVias()) {
            for(Via via : getVias()) {
                sceneNode.getChildren().add(via.getSceneNode());
            }
        }

        return sceneNode;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        BoundingBox bbox = null;

        if(getIsSetPolygons()) {
            for (EaglePolygon poly : getPolygons()) {
                BoundingBox polyBbox = poly.getBoundingBox(referenceX, referenceY);

                if(polyBbox != null) {
                    bbox = polyBbox.merge(bbox);
                }
            }
        }

        if(getIsSetWires()) {
            for(Wire wire : getWires()) {
                BoundingBox wireBbox = wire.getBoundingBox(referenceX, referenceY);

                if(wireBbox != null) {
                    bbox = wireBbox.merge(bbox);
                }
            }
        }

        if(getIsSetVias()) {
            for(Via via : getVias()) {
                BoundingBox viaBbox = via.getBoundingBox(referenceX, referenceY);

                if(viaBbox != null) {
                    bbox = viaBbox.merge(bbox);
                }
            }
        }

        return bbox;
    }

    @Override
    public void transformCoordinates(CoordinateTransformer ct, boolean signTransformation) {
        for (EaglePolygon polygon : getPolygons()) {
            polygon.transformCoordinates(ct, signTransformation);
        }
        for (Wire wire : getWires()) {
            wire.transformCoordinates(ct, signTransformation);
        }
        for (Via via : getVias()) {
            via.transformCoordinates(ct, signTransformation);
        }
    }

    /**
     * Add contact ref.
     *
     * @param ref the ref
     */
    public void addContactRef(ContactRef ref) {
        this.contactRefs.add(ref);
        this.isSetContactRefs = true;
    }

    /**
     * Add contact refs.
     *
     * @param contactRefs the contact refs
     */
    public void addContactRefs(Collection<? extends ContactRef> contactRefs) {
        this.contactRefs.addAll(contactRefs);
        this.isSetContactRefs = true;
    }

    /**
     * Add polygon.
     *
     * @param polygon the polygon
     */
    public void addPolygon(EaglePolygon polygon) {
        this.polygons.add(polygon);
        this.isSetPolygons = true;
    }

    /**
     * Add polygons.
     *
     * @param polygons the polygons
     */
    public void addPolygons(Collection<? extends EaglePolygon> polygons) {
        this.polygons.addAll(polygons);
        this.isSetPolygons = true;
    }

    /**
     * Add wire.
     *
     * @param wire the wire
     */
    public void addWire(Wire wire) {
        this.wires.add(wire);
        this.isSetWires = true;
    }

    /**
     * Add wires.
     *
     * @param wires the wires
     */
    public void addWires(Collection<? extends Wire> wires) {
        this.wires.addAll(wires);
        this.isSetWires = true;
    }

    /**
     * Add via.
     *
     * @param via the via
     */
    public  void addVia(Via via) {
        this.vias.add(via);
        this.isSetVias = true;
    }

    /**
     * Add vias.
     *
     * @param vias the vias
     */
    public void addVias(Collection<? extends Via> vias) {
        this.vias.addAll(vias);
        this.isSetVias = true;
    }

    /**
     * Sets class number.
     *
     * @param classNumber the class number
     */
    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
        this.isSetClassNumber = true;
    }

    /**
     * Sets air wires hidden.
     *
     * @param airWiresHidden the air wires hidden
     */
    public void setAirWiresHidden(boolean airWiresHidden) {
        this.airWiresHidden = airWiresHidden;
        this.isSetAirWiresHidden = true;
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
     * Gets class number.
     *
     * @return the class number
     */
    public int getClassNumber() {
        return classNumber;
    }

    /**
     * Gets air wires hidden.
     *
     * @return the air wires hidden
     */
    public boolean getAirWiresHidden() {
        return airWiresHidden;
    }

    /**
     * Gets contact refs.
     *
     * @return the contact refs
     */
    public List<ContactRef> getContactRefs() {
        return contactRefs;
    }

    /**
     * Gets polygons.
     *
     * @return the polygons
     */
    public List<EaglePolygon> getPolygons() {
        return polygons;
    }

    /**
     * Gets wires.
     *
     * @return the wires
     */
    public List<Wire> getWires() {
        return wires;
    }

    /**
     * Gets vias.
     *
     * @return the vias
     */
    public List<Via> getVias() {
        return vias;
    }

    /**
     * Gets is set class number.
     *
     * @return the is set class number
     */
    public boolean getIsSetClassNumber() {
        return isSetClassNumber;
    }

    /**
     * Gets is set air wires hidden.
     *
     * @return the is set air wires hidden
     */
    public boolean getIsSetAirWiresHidden() {
        return isSetAirWiresHidden;
    }

    /**
     * Gets is set contact refs.
     *
     * @return the is set contact refs
     */
    public boolean getIsSetContactRefs() {
        return isSetContactRefs;
    }

    /**
     * Gets is set polygons.
     *
     * @return the is set polygons
     */
    public boolean getIsSetPolygons() {
        return isSetPolygons;
    }

    /**
     * Gets is set wires.
     *
     * @return the is set wires
     */
    public boolean getIsSetWires() {
        return isSetWires;
    }

    /**
     * Gets is set vias.
     *
     * @return the is set vias
     */
    public boolean getIsSetVias() {
        return isSetVias;
    }
}
