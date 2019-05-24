package model.sceneObjects;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Light;
import javafx.scene.layout.Pane;
import model.*;
import model.sceneObjects.coordinateTransformables.EaglePackage;
import model.sceneObjects.coordinateTransformables.Element;
import model.sceneObjects.coordinateTransformables.Plain;
import model.sceneObjects.coordinateTransformables.Signal;
import utils.CoordinateTransformer;

import java.util.*;
import java.util.List;

/**
 * The type Eagle board.
 */
public class EagleBoard implements ISceneObject {

    // required
    private String eagleVersion;
    private Setting setting;
    private String name;

    // implied
    private double transformationParam;
    private double width;
    private double height;

    // optional
    private String autorouter;
    private DesignRules designRules;
    private String description;
    private String errors;
    private Plain plain;
    private Map<String, Library> libraries;
    private Map<Integer, Layer> layers;
    private Map<String, Element> elements;
    private List<Signal> signals;
    private Map<String, EagleClass> classes;
    private Grid grid;

    private double platineHeight;
    private double platineWidth;
    private BoundingBox platineBbox;
    private boolean isSetPlatineBbox;

    private TextArea informationPanel;

    private boolean isSetTransformationParam;
    private boolean isSetWidth;
    private boolean isSetHeight;
    private boolean isTransformed;
    private boolean isShuffled;

    private boolean isSetSetting;
    private boolean isSetAutorouter;
    private boolean isSetDesignRules;
    private boolean isSetDescription;
    private boolean isSetPlain;
    private boolean isSetLibraries;
    private boolean isSetLayers;
    private boolean isSetElements;
    private boolean isSetSignals;
    private boolean isSetClasses;
    private boolean isSetErrors;
    private boolean isSetGrid;

    private boolean drawBoundingBoxes;

    /**
     * Instantiates a new Eagle board.
     *
     * @param eagleVersion the eagle version
     * @param name         the name
     */
    public EagleBoard(String eagleVersion, String name) {
        this.eagleVersion = eagleVersion;
        this.setting = null;
        this.grid = null;
        this.name = name;

        this.libraries = new HashMap<>();
        this.layers = new HashMap<>();
        this.elements = new HashMap<>();
        this.signals = new ArrayList<>();
        this.classes = new HashMap<>();

        this.isSetTransformationParam = false;
        this.isSetWidth = false;
        this.isSetHeight = false;

        this.isSetSetting = false;
        this.isSetDesignRules = false;
        this.isSetDescription = false;
        this.isSetPlain = false;
        this.isSetLibraries = false;
        this.isSetLayers = false;
        this.isSetElements = false;
        this.isSetSignals = false;
        this.isSetClasses = false;
        this.isSetAutorouter = false;
        this.isSetErrors = false;
        this.isSetGrid = false;
        this.isTransformed = false;

        this.drawBoundingBoxes = false;
    }

    /**
     * Init.
     */
    public void init() {
        BoundingBox boardBbox = getBoundingBox();
        this.height = boardBbox.getHeight();
        this.width = boardBbox.getWidth();

        transformCoordinates();

        if(getIsSetPlain()) {
            this.platineBbox = getPlain().getPlatineBbox();
            this.platineHeight = platineBbox.getHeight();
            this.platineWidth = platineBbox.getWidth();
            this.isSetPlatineBbox = true;
        }
    }

    /**
     * Transform coordinates.
     */
    public void transformCoordinates() {
        setTransformed(!isTransformed());

        CoordinateTransformer ct = new CoordinateTransformer(getHeight());

        // transform plain
        if(getIsSetPlain()) {
            getPlain().transformCoordinates(ct, false);
        }

        // transform libraries
        for (Library lib : getLibraries().values()) {
            for (EaglePackage pack : lib.getPackages().values()) {
                pack.transformCoordinates(ct, true);
            }
        }

        // transform elements
        for (Element elem : getElements().values()) {
            elem.transformCoordinates(ct, false);
        }

        // transform signals
        for (Signal signal : getSignals()) {
            signal.transformCoordinates(ct, false);
        }
    }

    @Override
    public Node getSceneNode() {
        Pane sceneNode = new Pane();
        sceneNode.setMaxSize(getWidth(), getHeight());

        if(getIsSetPlain()) {
            sceneNode.getChildren().add(getPlain().getSceneNode());
        }

        if(getIsSetSignals()) {
            for(Signal signal : getSignals()) {
                sceneNode.getChildren().add(signal.getSceneNode());
            }
        }

        if(getIsSetElements()) {
            for (Element element : getElements().values()) {
                sceneNode.getChildren().add(element.getSceneNode());
            }
        }

        if(getDrawBoundingBoxes()) {
            for (Element element : getElements().values()) {
                sceneNode.getChildren().add(element.getBoundingBox().getSceneNode());
            }
        }

        return sceneNode;
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        return getSceneNode();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        BoundingBox bbox = null;

        if(getIsSetPlain()) {
            bbox = getPlain().getBoundingBox();
        }

        if(getIsSetSignals()) {
            for(Signal signal : getSignals()) {
                BoundingBox signalBbox = signal.getBoundingBox();

                if(signalBbox != null) {
                    bbox = signalBbox.merge(bbox);
                }
            }
        }

        if(getIsSetElements()) {
            for (Element element : getElements().values()) {
                BoundingBox elementBbox = element.getBoundingBox();

                if(elementBbox != null) {
                    bbox = elementBbox.merge(bbox);
                }
            }
        }

        System.out.println("EagleBoard - " + bbox);

        return bbox;
    }

    /**
     * Finds the element at the given position.
     *
     * @param point the board position
     * @return the element
     */
    public Element findElementAt(Point2D point) {
        Element result = null;

        if (getIsSetElements()) {
            for (Element element : getElements().values()) {
                BoundingBox elementBbox = element.getBoundingBox();

                // TODO: use element with higher layer
                if(elementBbox != null && elementBbox.isIncluded(point)) {
                    result = element;
                }
            }
        }

        return result;
    }

    /**
     * Gets grid.
     *
     * @return the grid
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Sets grid.
     *
     * @param grid the grid
     */
    public void setGrid(Grid grid) {
        this.grid = grid;
        this.isSetGrid = true;
    }

    /**
     * Gets is set grid.
     *
     * @return the is set grid
     */
    public boolean getIsSetGrid() {
        return isSetGrid;
    }

    /**
     * Sets setting.
     *
     * @param setting the setting
     */
    public void setSetting(Setting setting) {
        this.setting = setting;
        this.isSetSetting = true;
    }

    /**
     * Add layer.
     *
     * @param layer the layer
     */
    public void addLayer(Layer layer) {
        this.layers.put(layer.getNumber(), layer);
        this.isSetLayers = true;
    }

    /**
     * Add library.
     *
     * @param lib the lib
     */
    public void addLibrary(Library lib) {
        this.libraries.put(lib.getName(), lib);
        this.isSetLibraries = true;
    }

    /**
     * Add element.
     *
     * @param element the element
     */
    public void addElement(Element element) {
        this.elements.put(element.getName(), element);
        this.isSetElements = true;
    }

    /**
     * Add signal.
     *
     * @param signal the signal
     */
    public void addSignal(Signal signal) {
        this.signals.add(signal);
        this.isSetSignals = true;
    }

    /**
     * Add class.
     *
     * @param eClass the e class
     */
    public void addClass(EagleClass eClass) {
        this.classes.put(eClass.getName(), eClass);
        this.isSetClasses = true;
    }

    /**
     * Sets design rules.
     *
     * @param designRules the design rules
     */
    public void setDesignRules(DesignRules designRules) {
        this.designRules = designRules;
        this.isSetDesignRules = true;
    }

    /**
     * Sets transformation param.
     *
     * @param transformationParam the transformation param
     */
    public void setTransformationParam(double transformationParam) {
        this.transformationParam = transformationParam;
        this.isSetTransformationParam = true;
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
     * Sets height.
     *
     * @param height the height
     */
    public void setHeight(double height) {
        // if coordinates are already transformed: transform back, change height, transform again with new height
        if (isTransformed()) {
            transformCoordinates();
            this.height = height;
            transformCoordinates();
        }
        // if coordinates are currently not transformed: just change height
        else {
            this.height = height;
        }
        this.isSetHeight = true;
    }

    /**
     * Is transformed boolean.
     *
     * @return the boolean
     */
    public boolean isTransformed() {
        return isTransformed;
    }

    /**
     * Sets transformed.
     *
     * @param transformed the transformed
     */
    public void setTransformed(boolean transformed) {
        isTransformed = transformed;
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
     * Sets plain.
     *
     * @param plain the plain
     */
    public void setPlain(Plain plain) {
        this.plain = plain;
        this.isSetPlain = true;
    }

    /**
     * Sets libraries.
     *
     * @param libraries the libraries
     */
    public void setLibraries(Map<String, Library> libraries) {
        this.libraries = libraries;
        this.isSetLibraries = true;
    }

    /**
     * Sets layers.
     *
     * @param layers the layers
     */
    public void setLayers(Map<Integer, Layer> layers) {
        this.layers = layers;
        this.isSetLayers = true;
    }

    /**
     * Sets elements.
     *
     * @param elements the elements
     */
    public void setElements(Map<String, Element> elements) {
        this.elements = elements;
        this.isSetElements = true;
    }

    /**
     * Sets signals.
     *
     * @param signals the signals
     */
    public void setSignals(List<Signal> signals) {
        this.signals = signals;
        this.isSetSignals = true;
    }

    /**
     * Sets classes.
     *
     * @param classes the classes
     */
    public void setClasses(Map<String, EagleClass> classes) {
        this.classes = classes;
        this.isSetClasses = true;
    }

    /**
     * Sets autorouter.
     *
     * @param autorouter the autorouter
     */
    public void setAutorouter(String autorouter) {
        this.autorouter = autorouter;
        this.isSetAutorouter = true;
    }

    /**
     * Sets errors.
     *
     * @param errors the errors
     */
    public void setErrors(String errors) {
        this.errors = errors;
        this.isSetErrors = true;
    }

    /**
     * Sets draw bounding boxes.
     *
     * @param drawBoundingBoxes the draw bounding boxes
     */
    public void setDrawBoundingBoxes(boolean drawBoundingBoxes) {
        this.drawBoundingBoxes = drawBoundingBoxes;
    }

    /**
     * Sets information panel.
     *
     * @param informationPanel the information panel
     */
    public void setInformationPanel(TextArea informationPanel) {
        this.informationPanel = informationPanel;
    }

    /**
     * Sets is shuffled.
     *
     * @param shuffled the shuffled
     */
    public void setIsShuffled(boolean shuffled) {
        isShuffled = shuffled;
        getPlain().setIsBoardShuffled(shuffled);
    }

    /**
     * Gets autorouter.
     *
     * @return the autorouter
     */
    public String getAutorouter() {
        return autorouter;
    }

    /**
     * Gets is set autorouter.
     *
     * @return the is set autorouter
     */
    public boolean getIsSetAutorouter() {
        return isSetAutorouter;
    }

    /**
     * Gets is set errors.
     *
     * @return the is set errors
     */
    public boolean getIsSetErrors() {
        return isSetErrors;
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
     * Gets eagle version.
     *
     * @return the eagle version
     */
    public String getEagleVersion() {
        return eagleVersion;
    }

    /**
     * Gets setting.
     *
     * @return the setting
     */
    public Setting getSetting() {
        return setting;
    }

    /**
     * Gets transformation param.
     *
     * @return the transformation param
     */
    public double getTransformationParam() {
        return transformationParam;
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
     * Gets height.
     *
     * @return the height
     */
    public double getHeight() {
        return height;
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
     * Gets plain.
     *
     * @return the plain
     */
    public Plain getPlain() {
        return plain;
    }

    /**
     * Gets libraries.
     *
     * @return the libraries
     */
    public Map<String, Library> getLibraries() {
        return libraries;
    }

    /**
     * Gets elements.
     *
     * @return the elements
     */
    public Map<String, Element> getElements() {
        return elements;
    }

    /**
     * Gets signals.
     *
     * @return the signals
     */
    public List<Signal> getSignals() {
        return signals;
    }

    /**
     * Gets classes.
     *
     * @return the classes
     */
    public Map<String, EagleClass> getClasses() {
        return classes;
    }

    /**
     * Gets layers.
     *
     * @return the layers
     */
    public Map<Integer, Layer> getLayers() {
        return layers;
    }

    /**
     * Gets design rules.
     *
     * @return the design rules
     */
    public DesignRules getDesignRules() {
        return designRules;
    }

    /**
     * Gets is set transformation param.
     *
     * @return the is set transformation param
     */
    public boolean getIsSetTransformationParam() {
        return isSetTransformationParam;
    }

    /**
     * Gets is set width.
     *
     * @return the is set width
     */
    public boolean getIsSetWidth() {
        return isSetWidth;
    }

    /**
     * Gets is set height.
     *
     * @return the is set height
     */
    public boolean getIsSetHeight() {
        return isSetHeight;
    }

    /**
     * Gets is set design rules.
     *
     * @return the is set design rules
     */
    public boolean getIsSetDesignRules() {
        return isSetDesignRules;
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
     * Gets is set plain.
     *
     * @return the is set plain
     */
    public boolean getIsSetPlain() {
        return isSetPlain;
    }

    /**
     * Gets is set libraries.
     *
     * @return the is set libraries
     */
    public boolean getIsSetLibraries() {
        return isSetLibraries;
    }

    /**
     * Gets is set layers.
     *
     * @return the is set layers
     */
    public boolean getIsSetLayers() {
        return isSetLayers;
    }

    /**
     * Gets is set elements.
     *
     * @return the is set elements
     */
    public boolean getIsSetElements() {
        return isSetElements;
    }

    /**
     * Gets is set signals.
     *
     * @return the is set signals
     */
    public boolean getIsSetSignals() {
        return isSetSignals;
    }

    /**
     * Gets is set classes.
     *
     * @return the is set classes
     */
    public boolean getIsSetClasses() {
        return isSetClasses;
    }

    /**
     * Gets is set setting.
     *
     * @return the is set setting
     */
    public boolean getIsSetSetting() {
        return isSetSetting;
    }

    /**
     * Gets platine height.
     *
     * @return the platine height
     */
    public double getPlatineHeight() {
        return platineHeight;
    }

    /**
     * Gets platine width.
     *
     * @return the platine width
     */
    public double getPlatineWidth() {
        return platineWidth;
    }

    /**
     * Gets platine bbox.
     *
     * @return the platine bbox
     */
    public BoundingBox getPlatineBbox() {
        return platineBbox;
    }

    /**
     * Gets is set platine bbox.
     *
     * @return the is set platine bbox
     */
    public boolean getIsSetPlatineBbox() {
        return isSetPlatineBbox;
    }

    /**
     * Gets draw bounding boxes.
     *
     * @return the draw bounding boxes
     */
    public boolean getDrawBoundingBoxes() {
        return drawBoundingBoxes;
    }

    /**
     * Gets information panel.
     *
     * @return the information panel
     */
    public TextArea getInformationPanel() {
        return informationPanel;
    }

    /**
     * Gets is shuffled.
     *
     * @return the is shuffled
     */
    public boolean getIsShuffled() {
        return isShuffled;
    }
}
