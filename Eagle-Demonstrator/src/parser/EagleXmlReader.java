package parser;

import model.*;
import model.enums.*;
import model.sceneObjects.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.sceneObjects.coordinateTransformables.*;
import org.w3c.dom.*;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * The type Eagle xml reader.
 */
public class EagleXmlReader {

    private EagleBoard board;

    /**
     * Gets board.
     *
     * @return the board
     */
    public EagleBoard getBoard() {
        return board;
    }

    /**
     * Read file.
     *
     * @param file the file
     * @throws ParserConfigurationException the parser configuration exception
     * @throws IOException                  the io exception
     * @throws SAXException                 the sax exception
     * @throws NumberFormatException        the number format exception
     */
    public void readFile(File file) throws ParserConfigurationException, IOException, SAXException, NumberFormatException {
        // this should be asserted by the file chooser dialog in the gui
        assert file.exists() && file.isFile();

        // building a document with w3c.dom
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // ignoring the validation of the eagle.dtd file
        dbf.setValidating(false);
        dbf.setNamespaceAware(true);
        dbf.setFeature("http://xml.org/sax/features/namespaces", false);
        dbf.setFeature("http://xml.org/sax/features/validation", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();

        // extracting the root element (<eagle>)
        Element eagle = doc.getDocumentElement();
        String version = eagle.getAttribute("version");

        // initializing the board
        this.board = new EagleBoard(version, file.getName());

        // start of <drawing>: implicit tag; won't be extracted explicitly
        // extracting the settings (if available)
        // TODO Wenn hasTag geändert wurde, muss drawing extrahiert werden!!!
        if (hasTag(eagle, "settings")) {
            NodeList settingsList = eagle.getElementsByTagName("settings");
            Node settingsNode = settingsList.item(0);
            if (settingsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element settings = (Element) settingsNode;
                extractSettings(settings);
            }
        }

        // extracting the grid (if available)
        NodeList gridList = eagle.getElementsByTagName("grid");
        Node gridNode = gridList.item(0);
        if (gridNode.getNodeType() == Node.ELEMENT_NODE) {
            Element gridE = (Element) gridNode;
            extractGrid(gridE);
        }

        // extracting the layers
        NodeList layersList = eagle.getElementsByTagName("layers");
        Node layersNode = layersList.item(0);
        if (layersNode.getNodeType() == Node.ELEMENT_NODE) {
            Element layers = (Element) layersNode;
            extractLayers(layers);
        }

        // extracting the board
        if (hasTag(eagle, "board")) {
            NodeList boardList = eagle.getElementsByTagName("board");
            Node boardNode = boardList.item(0);
            if (boardNode.getNodeType() == Node.ELEMENT_NODE) {
                Element board = (Element) boardNode;
                extractBoard(board);
            }
        }

        getBoard().init();
    }

    /**
     * Checks if a given String represents a boolean and if that boolean is true.
     *
     * @param s the String to check
     * @return  true, if the String represents a true boolean; false otherwise
     */
    private boolean isTrue(String s) {
        return (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("true"));
    }

    // TODO muss überarbeitet werden

    /**
     * Checks if the given tag is a child of the element.
     *
     * @param root  the element
     * @param tag   the tag to check
     * @return      true, if the tag is child of the element
     */
    private boolean hasTag(Element root, String tag) {
        return root.getElementsByTagName(tag).getLength() > 0;
    }

    /**
     * Extracts the digits from a mixed String
     *
     * @param mixed the mixed String
     * @return      the String only containing digits
     */
    private String getDigitsFromMixedString(String mixed) {
        return mixed.replaceAll("[^0-9]", "");
    }

    /**
     * Extracts the settings.
     *
     * @param settings the settings element
     */
    private void extractSettings(Element settings) {
        Setting setting = new Setting();

        NodeList settingList = settings.getElementsByTagName("setting");
        for (int i = 0; i < settingList.getLength(); i++) {
            Node settingNode = settingList.item(i);
            if (settingNode.getNodeType() == Node.ELEMENT_NODE) {
                Element settingE = (Element) settingNode;
                if (settingE.hasAttribute("verticaltext")) {
                    if (settingE.getAttribute("verticaltext").equalsIgnoreCase("down")) {
                        setting.setVerticalText(VerticalText.Down);
                    }
                }
                if (settingE.hasAttribute("alwaysvectorfont")) {
                    if (isTrue(settingE.getAttribute("alwaystextfont"))) {
                        setting.setAlwaysVectorFont(true);
                    }
                }
            }
        }

        getBoard().setSetting(setting);
    }

    /**
     * Extracts the grid
     *
     * @param gridE                     the grid element
     * @throws NumberFormatException    when String parse goes wrong
     */
    private void extractGrid(Element gridE) throws NumberFormatException {
        // extract requireds
        double distance = Double.parseDouble(gridE.getAttribute("distance"));
        GridUnit unitDist = getGridUnit(gridE.getAttribute("unitdist"));
        GridUnit unit = getGridUnit(gridE.getAttribute("unit"));
        double altDistance = Double.parseDouble(gridE.getAttribute("altdistance"));
        GridUnit altUnitDist = getGridUnit(gridE.getAttribute("altunitdist"));
        GridUnit altUnit = getGridUnit(gridE.getAttribute("altunit"));

        Grid grid = new Grid(distance, unitDist, unit, altDistance, altUnitDist, altUnit);

        // extract optionals
        if (gridE.hasAttribute("style")) {
            if (gridE.getAttribute("style").equalsIgnoreCase("dots")) {
                grid.setStyle(GridStyle.Dots);
            }
        }
        if (gridE.hasAttribute("multiple")) {
            grid.setMultiple(Integer.parseInt(gridE.getAttribute("multiple")));
        }
        if (gridE.hasAttribute("display")) {
            grid.setDisplay(isTrue(gridE.getAttribute("display")));
        }
        board.setGrid(grid);
    }

    /**
     * Parses the GridUnit from a String
     *
     * @param s the String
     * @return  the GridUnit
     */
    private GridUnit getGridUnit(String s) {
        switch (s.toLowerCase()) {
            case "mic":
                return GridUnit.Mic;
            case "mil":
                return GridUnit.Mil;
            case "inch":
                return GridUnit.Inch;
        }
        return GridUnit.Mm;
    }

    /**
     * Extracts the layers
     *
     * @param layers                    the layers
     * @throws NumberFormatException    when String parse goes wrong
     */
    private void extractLayers(Element layers) throws NumberFormatException {
        NodeList layerList = layers.getElementsByTagName("layer");
        for (int i = 0; i < layerList.getLength(); i++) {
            Node layerNode = layerList.item(i);
            if (layerNode.getNodeType() == Node.ELEMENT_NODE) {
                // parse required attributes
                Element layerE = (Element) layerNode;
                int number = Integer.parseInt(layerE.getAttribute("number"));
                String name = layerE.getAttribute("name");
                int color = Integer.parseInt(layerE.getAttribute("color"));
                int fill = Integer.parseInt(layerE.getAttribute("fill"));
                Layer layer = new Layer(number, name, color, fill);

                // parse optional attributes
                if (layerE.hasAttribute("visible")) {
                    if (!isTrue(layerE.getAttribute("visible"))) {
                        layer.setVisible(false);
                    }
                }
                if (layerE.hasAttribute("active")) {
                    if (!isTrue(layerE.getAttribute("active"))) {
                        layer.setActive(false);
                    }
                }

                getBoard().addLayer(layer);
            }
        }
    }

    /**
     * Extracts the board
     *
     * @param board the board
     */
    private void extractBoard(Element board) {
        // TODO
        // extracting description (if available)
        if (hasTag(board, "description")) {
            Node descNode = board.getElementsByTagName("description").item(0);
            if (descNode.getNodeType() == Node.ELEMENT_NODE) {
                Element description = (Element) descNode;
                getBoard().setDescription(description.getTextContent());
            }
        }
        // extracting plain (if available)
        if (hasTag(board, "plain")) {
            Node plainNode = board.getElementsByTagName("plain").item(0);
            if (plainNode.getNodeType() == Node.ELEMENT_NODE) {
                Element plain = (Element) plainNode;
                extractPlain(plain);
            }
        }
        // extracting libraries + packages (if available)
        if (hasTag(board, "libraries")) {
            Node librariesNode = board.getElementsByTagName("libraries").item(0);
            if (librariesNode.getNodeType() == Node.ELEMENT_NODE) {
                Element libraries = (Element) librariesNode;
                extractLibraries(libraries);
            }
        }
        // extracting attributes (if available)
        if (hasTag(board, "attributes")) {
            Node attributesNode = board.getElementsByTagName("attributes").item(0);
            if (attributesNode.getNodeType() == Node.ELEMENT_NODE) {
                Element attributes = (Element) attributesNode;
                extractAttributes(attributes);
            }
        }
        // extracting variantdefs (if available)
        if (hasTag(board, "variantdefs")) {
            Node variantdefsNode = board.getElementsByTagName("variantdefs").item(0);
            if (variantdefsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element variantdefs = (Element) variantdefsNode;
                extractVariantdefs(variantdefs);
            }
        }
        // extracting classes (if available)
        if (hasTag(board, "classes")) {
            Node classesNode = board.getElementsByTagName("classes").item(0);
            if (classesNode.getNodeType() == Node.ELEMENT_NODE) {
                Element classes = (Element) classesNode;
                extractClasses(classes);
            }
        }
        // extracting designrules (if available)
        if (hasTag(board, "designrules")) {
            Node designrulesNode = board.getElementsByTagName("designrules").item(0);
            if (designrulesNode.getNodeType() == Node.ELEMENT_NODE) {
                Element designrules = (Element) designrulesNode;
                extractDesignrules(designrules);
            }
        }
        // extracting autorouter + passes (if available)
        if (hasTag(board, "autorouter")) {
            Node autorouterNode = board.getElementsByTagName("autorouter").item(0);
            if (autorouterNode.getNodeType() == Node.ELEMENT_NODE) {
                Element autorouter = (Element) autorouterNode;
                extractAutorouter(autorouter);
            }
        }
        // extracting elements (if available)
        if (hasTag(board, "elements")) {
            Node elementsNode = board.getElementsByTagName("elements").item(0);
            if (elementsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elements = (Element) elementsNode;
                extractElements(elements);
            }
        }
        // extracting signals (if available)
        if (hasTag(board, "signals")) {
            Node signalsNode = board.getElementsByTagName("signals").item(0);
            if (signalsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element signals = (Element) signalsNode;
                extractSignals(signals);
            }
        }
        // extracting errors (if available)
        if (hasTag(board, "errors")) {
            Node errorsNode = board.getElementsByTagName("errors").item(0);
            if (errorsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element errors = (Element) errorsNode;
                extractErrors(errors);
            }
        }
    }

    /**
     * Extracts the description from an element
     *
     * @param element   the element
     * @return          the description
     */
    private String getDescription(Element element) {
        return element.getElementsByTagName("description").item(0).getTextContent();
    }

    /**
     * Extracts the plain.
     *
     * @param plainE                    the plain element
     * @throws NumberFormatException    when String parse goes wrong
     */
    private void extractPlain(Element plainE) throws NumberFormatException {
        Plain plain = new Plain();

        // create a list of all building blocks in the plain
        List<ILayerable> buildingBlocks = new ArrayList<>();
        buildingBlocks.addAll(getPolygons(plainE));
        buildingBlocks.addAll(getWires(plainE));
        buildingBlocks.addAll(getTexts(plainE));
        buildingBlocks.addAll(getDimensions(plainE));
        buildingBlocks.addAll(getCircles(plainE));
        buildingBlocks.addAll(getRectangles(plainE));
        buildingBlocks.addAll(getFrames(plainE));
        buildingBlocks.addAll(getHoles(plainE));

        // add all building blocks to the plain
        for (ILayerable so : buildingBlocks) {
            plain.addBuildingBlock(so);
        }

        getBoard().setPlain(plain);
    }

    /**
     * Lists all polygons of an element.
     *
     * @param element                   the element
     * @return                          list of polygons
     * @throws NumberFormatException    when String parse goes wrong
     */
    private List<EaglePolygon> getPolygons(Element element) throws NumberFormatException {
        List<EaglePolygon> polygons = new ArrayList<>();
        // add Polygons
        NodeList polygonList = element.getElementsByTagName("polygon");
        for (int i = 0; i < polygonList.getLength(); i++) {
            Node polygonNode = polygonList.item(i);
            if (polygonNode.getNodeType() == Node.ELEMENT_NODE) {
                Element polygonE = (Element) polygonNode;

                // add requireds
                double width = Double.parseDouble(polygonE.getAttribute("width"));
                int layerNumber = Integer.parseInt(polygonE.getAttribute("layer"));

                EaglePolygon polygon = new EaglePolygon(getBoard(), width, layerNumber);

                extractVertices(polygon, polygonE);

                // add optionals
                if (polygonE.hasAttribute("spacing")) {
                    polygon.setSpacing(Double.parseDouble(polygonE.getAttribute("spacing")));
                }
                if (polygonE.hasAttribute("isolate")) {
                    polygon.setIsolate(Double.parseDouble(polygonE.getAttribute("isolate")));
                }
                if (polygonE.hasAttribute("pour")) {
                    switch (polygonE.getAttribute("pour").toLowerCase()) {
                        case "hatch":
                            polygon.setPour(PolygonPour.Hatch);
                            break;
                        case "cutout":
                            polygon.setPour(PolygonPour.CutOut);
                            break;
                        default:
                            polygon.setPour(PolygonPour.Solid);
                    }
                }
                if (polygonE.hasAttribute("orphans")) {
                    if (polygonE.getAttribute("orphans").equalsIgnoreCase("yes")) {
                        polygon.setOrphans(true);
                    }
                    else {
                        polygon.setOrphans(false);
                    }
                }
                if (polygonE.hasAttribute("thermals")) {
                    if (polygonE.getAttribute("thermals").equalsIgnoreCase("no")) {
                        polygon.setThermals(false);
                    }
                    else {
                        polygon.setThermals(true);
                    }
                }
                if (polygonE.hasAttribute("rank")) {
                    polygon.setRank(Integer.parseInt(polygonE.getAttribute("rank")));
                }

                polygons.add(polygon);
            }
        }
        return polygons;
    }

    /**
     * Extracts the vertices of a polygon
     *
     * @param polygon                   the current polygon
     * @param polygonE                  the polygon element
     * @throws NumberFormatException    when String parse goes wrong
     */
    private void extractVertices(EaglePolygon polygon, Element polygonE) throws NumberFormatException {
        NodeList verticesList = polygonE.getElementsByTagName("vertex");
        for (int i = 0; i < verticesList.getLength(); i++) {
            Node vertexNode = verticesList.item(i);
            if (vertexNode.getNodeType() == Node.ELEMENT_NODE) {
                Element vertexE = (Element) vertexNode;

                // add requireds
                double x = Double.parseDouble(vertexE.getAttribute("x"));
                double y = Double.parseDouble(vertexE.getAttribute("y"));

                Vertex vertex = new Vertex(x, y);

                // add optionals
                if (polygonE.hasAttribute("curve")) {
                    vertex.setCurve(Double.parseDouble(polygonE.getAttribute("curve")));
                }

                polygon.addVertex(vertex);
            }
        }
    }

    /**
     * Lists the wires of an element.
     *
     * @param element                   the element
     * @return                          list of wires
     * @throws NumberFormatException    when String parse goes wrong
     */
    private List<Wire> getWires(Element element) throws NumberFormatException {
        List<Wire> wires = new ArrayList<>();
        // add Wires
        NodeList wireList = element.getElementsByTagName("wire");
        for (int i = 0; i < wireList.getLength(); i++) {
            Node wireNode = wireList.item(i);
            if (wireNode.getNodeType() == Node.ELEMENT_NODE) {
                Element wireE = (Element) wireNode;

                // add requireds
                double x1 = Double.parseDouble(wireE.getAttribute("x1"));
                double y1 = Double.parseDouble(wireE.getAttribute("y1"));
                double x2 = Double.parseDouble(wireE.getAttribute("x2"));
                double y2 = Double.parseDouble(wireE.getAttribute("y2"));
                double width = Double.parseDouble(wireE.getAttribute("width"));
                int layer = Integer.parseInt(wireE.getAttribute("layer"));
                String extent = wireE.getAttribute("extent");

                Wire wire = new Wire(getBoard(), x1, y1, x2, y2, layer, width, extent);

                // add optionals
                if (wireE.hasAttribute("style")) {
                    switch (wireE.getAttribute("style").toLowerCase()) {
                        case "longdash":
                            wire.setStyle(WireStyle.LongDash);
                            break;
                        case "shortdash":
                            wire.setStyle(WireStyle.ShortDash);
                            break;
                        case "dashdot":
                            wire.setStyle(WireStyle.DashDot);
                            break;
                        default:
                            wire.setStyle(WireStyle.Continous);
                    }
                }
                if (wireE.hasAttribute("curve")) {
                    wire.setCurve(Double.parseDouble(wireE.getAttribute("curve")));
                }
                if (wireE.hasAttribute("cap")) {
                    if (wireE.getAttribute("cap").equalsIgnoreCase("flat")) {
                        wire.setCap(WireCap.Flat);
                    }
                    else {
                        wire.setCap(WireCap.Round);
                    }
                }
                wires.add(wire);
            }
        }
        return wires;
    }

    /**
     * Lists the texts of an element
     *
     * @param element                   the element
     * @return                          list of texts
     * @throws NumberFormatException    when String parse goes wrong
     */
    private List<EagleText> getTexts(Element element) throws NumberFormatException {
        List<EagleText> texts = new ArrayList<>();
        // add texts
        NodeList textList = element.getElementsByTagName("text");
        for (int i = 0; i < textList.getLength(); i++) {
            Node textNode = textList.item(i);
            if (textNode.getNodeType() == Node.ELEMENT_NODE) {
                Element textE = (Element) textNode;

                // add requireds
                double x = Double.parseDouble(textE.getAttribute("x"));
                double y = Double.parseDouble(textE.getAttribute("y"));
                double size = Double.parseDouble(textE.getAttribute("size"));
                int layer = Integer.parseInt(textE.getAttribute("layer"));

                EagleText text = new EagleText(getBoard(), textE.getTextContent(), x, y, layer, size);

                // add optionals
                if (textE.hasAttribute("font")) {
                    switch (textE.getAttribute("font").toLowerCase()) {
                        case "vector":
                            text.setFont(TextFont.Vector);
                            break;
                        case "fixed":
                            text.setFont(TextFont.Fixed);
                            break;
                        default:
                            text.setFont(TextFont.Proportional);
                    }
                }
                if (textE.hasAttribute("ratio")) {
                    text.setRatio(Integer.parseInt(textE.getAttribute("ratio")));
                }
                if (textE.hasAttribute("rot")) {
                    text.setRot(Integer.parseInt(getDigitsFromMixedString(textE.getAttribute("rot"))));
                }
                if (textE.hasAttribute("align")) {
                    switch (textE.getAttribute("align").toLowerCase()) {
                        case "bottom-center":
                            text.setAlign(Align.BottomLeft);
                            break;
                        case "bottom-right":
                            text.setAlign(Align.BottomRight);
                            break;
                        case "center-left":
                            text.setAlign(Align.CenterLeft);
                            break;
                        case "center":
                            text.setAlign(Align.Center);
                            break;
                        case "center-right":
                            text.setAlign(Align.CenterRight);
                            break;
                        case "top-left":
                            text.setAlign(Align.TopLeft);
                            break;
                        case "top-center":
                            text.setAlign(Align.TopCenter);
                            break;
                        case "top-right":
                            text.setAlign(Align.TopRight);
                            break;
                        default:
                            text.setAlign(Align.BottomLeft);
                    }
                }
                if (textE.hasAttribute("distance")) {
                    text.setDistance(Integer.parseInt(textE.getAttribute("distance")));
                }
                texts.add(text);
            }
        }
        return texts;
    }

    /**
     * Lists the dimensions of an element
     *
     * @param element                   the element
     * @return                          list of dimensions
     * @throws NumberFormatException    when String parse goes wrong
     */
    private List<Dimension> getDimensions(Element element) throws NumberFormatException {
        List<Dimension> dimensions = new ArrayList<>();
        // add dimensions
        NodeList dimensionList = element.getElementsByTagName("dimension");
        for (int i = 0; i < dimensionList.getLength(); i++) {
            Node dimensionNode = dimensionList.item(i);
            if (dimensionNode.getNodeType() == Node.ELEMENT_NODE) {
                Element dimensionE = (Element) dimensionNode;

                // add requireds
                double x1 = Double.parseDouble(dimensionE.getAttribute("x1"));
                double y1 = Double.parseDouble(dimensionE.getAttribute("y1"));
                double x2 = Double.parseDouble(dimensionE.getAttribute("x2"));
                double y2 = Double.parseDouble(dimensionE.getAttribute("y2"));
                double x3 = Double.parseDouble(dimensionE.getAttribute("x3"));
                double y3 = Double.parseDouble(dimensionE.getAttribute("y3"));
                int layer = Integer.parseInt(dimensionE.getAttribute("layer"));
                double textSize = Double.parseDouble(dimensionE.getAttribute("textsize"));

                Dimension dimension = new Dimension(getBoard(), x1, y1, layer, x2, y2, x3, y3, textSize);

                // add optionals
                if (dimensionE.hasAttribute("width")) {
                    dimension.setWidth(Double.parseDouble(dimensionE.getAttribute("width")));
                }
                if (dimensionE.hasAttribute("dtype")) {
                    switch (dimensionE.getAttribute("dtype")) {
                        case "horizontal":
                            dimension.setDtype(DimensionType.Horizontal);
                            break;
                        case "vertical":
                            dimension.setDtype(DimensionType.Vertical);
                            break;
                        case "radius":
                            dimension.setDtype(DimensionType.Radius);
                            break;
                        case "diameter":
                            dimension.setDtype(DimensionType.Diameter);
                            break;
                        case "leader":
                            dimension.setDtype(DimensionType.Leader);
                            break;
                        default:
                            dimension.setDtype(DimensionType.Parallel);
                    }
                }
                if (dimensionE.hasAttribute("extwidth")) {
                    dimension.setExtWidth(Double.parseDouble(dimensionE.getAttribute("extwidth")));
                }
                if (dimensionE.hasAttribute("extlength")) {
                    dimension.setExtLength(Double.parseDouble(dimensionE.getAttribute("extlength")));
                }
                if (dimensionE.hasAttribute("extoffset")) {
                    dimension.setExtOffset(Double.parseDouble(dimensionE.getAttribute("extoffset")));
                }
                if (dimensionE.hasAttribute("textratio")) {
                    dimension.setTextRatio(Double.parseDouble(dimensionE.getAttribute("textratio")));
                }
                if (dimensionE.hasAttribute("unit")) {
                    switch (dimensionE.getAttribute("unit")) {
                        case "mic":
                            dimension.setUnit(GridUnit.Mic);
                            break;
                        case "mil":
                            dimension.setUnit(GridUnit.Mil);
                            break;
                        case "inch":
                            dimension.setUnit(GridUnit.Inch);
                            break;
                        default:
                            dimension.setUnit(GridUnit.Mm);
                    }
                }
                if (dimensionE.hasAttribute("precision")) {
                    dimension.setPrecision(Integer.parseInt(dimensionE.getAttribute("precision")));
                }
                if (dimensionE.hasAttribute("visible")) {
                    dimension.setVisible(isTrue(dimensionE.getAttribute("visible")));
                }
                dimensions.add(dimension);
            }
        }
        return dimensions;
    }

    /**
     * Lists the circles of an element
     *
     * @param element                   the element
     * @return                          list of circles
     * @throws NumberFormatException    when String parse goes wrong
     */
    private List<EagleCircle> getCircles(Element element) throws NumberFormatException {
        List<EagleCircle> circles = new ArrayList<>();
        // add circles
        NodeList circleList = element.getElementsByTagName("circle");
        for (int i = 0; i < circleList.getLength(); i++) {
            Node circleNode = circleList.item(i);
            if (circleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element circleE = (Element) circleNode;

                // add requireds
                double x = Double.parseDouble(circleE.getAttribute("x"));
                double y = Double.parseDouble(circleE.getAttribute("y"));
                double r = Double.parseDouble(circleE.getAttribute("radius"));
                double width = Double.parseDouble(circleE.getAttribute("width"));
                int layer = Integer.parseInt(circleE.getAttribute("layer"));

                EagleCircle circle = new EagleCircle(getBoard(), x, y, layer, r, width);

                // no optionals for circles

                circles.add(circle);
            }
        }
        return circles;
    }

    /**
     * Lists the rectangles of an element
     *
     * @param element                   the element
     * @return                          list of rectangles
     * @throws NumberFormatException    when String parse goes wrong
     */
    private List<EagleRectangle> getRectangles(Element element) throws NumberFormatException {
        List<EagleRectangle> rectangles = new ArrayList<>();
        // add rectangles
        NodeList rectangleList = element.getElementsByTagName("rectangle");
        for (int i = 0; i < rectangleList.getLength(); i++) {
            Node rectangleNode = rectangleList.item(i);
            if (rectangleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element rectangleE = (Element) rectangleNode;

                // add requireds
                double x1 = Double.parseDouble(rectangleE.getAttribute("x1"));
                double y1 = Double.parseDouble(rectangleE.getAttribute("y1"));
                double x2 = Double.parseDouble(rectangleE.getAttribute("x2"));
                double y2 = Double.parseDouble(rectangleE.getAttribute("y2"));
                int layer = Integer.parseInt(rectangleE.getAttribute("layer"));

                EagleRectangle rectangle = new EagleRectangle(getBoard(), x1, y1, layer, x2, y2);

                // add optionals
                if (rectangleE.hasAttribute("rot")) {
                    rectangle.setRot(Integer.parseInt(getDigitsFromMixedString(rectangleE.getAttribute("rot"))));
                }
                rectangles.add(rectangle);
            }
        }
        return rectangles;
    }

    /**
     * Lists the frames of an element
     *
     * @param element   the element
     * @return          list of frames
     */
    private List<Frame> getFrames(Element element) {
        List<Frame> frames = new ArrayList<>();
        // add frames
        NodeList frameList = element.getElementsByTagName("frame");
        for (int i = 0; i < frameList.getLength(); i++) {
            Node frameNode = frameList.item(i);
            if (frameNode.getNodeType() == Node.ELEMENT_NODE) {
                Element frameE = (Element) frameNode;

                // add requireds
                double x1 = Double.parseDouble(frameE.getAttribute("x1"));
                double y1 = Double.parseDouble(frameE.getAttribute("y1"));
                double x2 = Double.parseDouble(frameE.getAttribute("x2"));
                double y2 = Double.parseDouble(frameE.getAttribute("y2"));
                int columns = Integer.parseInt(frameE.getAttribute("columns"));
                int rows = Integer.parseInt(frameE.getAttribute("rows"));
                int layer = Integer.parseInt(frameE.getAttribute("layer"));

                Frame frame = new Frame(getBoard(), x1, y1, layer, x2, y2, columns, rows);

                // add optionals
                if (frameE.hasAttribute("border-left")) {
                    frame.setBorderLeft(isTrue(frameE.getAttribute("border-left")));
                }
                if (frameE.hasAttribute("border-top")) {
                    frame.setBorderTop(isTrue(frameE.getAttribute("border-top")));
                }
                if (frameE.hasAttribute("border-right")) {
                    frame.setBorderRight(isTrue(frameE.getAttribute("border-right")));
                }
                if (frameE.hasAttribute("border-bottom")) {
                    frame.setBorderBottom(isTrue(frameE.getAttribute("border-bottom")));
                }
                frames.add(frame);
            }
        }
        return frames;
    }

    /**
     * Lists the holes of an element
     *
     * @param element   the element
     * @return          list of holes
     */
    private List<Hole> getHoles(Element element) {
        List<Hole> holes = new ArrayList<>();
        // add holes
        NodeList holeList = element.getElementsByTagName("hole");
        for (int i = 0; i < holeList.getLength(); i++) {
            Node holeNode = holeList.item(i);
            if (holeNode.getNodeType() == Node.ELEMENT_NODE) {
                Element holeE = (Element) holeNode;

                // add requireds
                double x = Double.parseDouble(holeE.getAttribute("x"));
                double y = Double.parseDouble(holeE.getAttribute("y"));
                double drill = Double.parseDouble(holeE.getAttribute("drill"));

                Hole hole = new Hole(x, y, drill);

                // no optionals for holes

                holes.add(hole);
            }
        }
        return holes;
    }

    // TODO

    /**
     * Lists the vias of an element
     *
     * @param element                   the element
     * @return                          list of vias
     * @throws NumberFormatException    when String parse goes wrong
     */
    private List<Via> getVias(Element element) throws NumberFormatException {
        List<Via> vias = new ArrayList<>();

        // add vias
        NodeList viaList = element.getElementsByTagName("via");
        for (int i = 0; i < viaList.getLength(); i++) {
            Node viaNode = viaList.item(i);
            if (viaNode.getNodeType() == Node.ELEMENT_NODE) {
                Element viaE = (Element) viaNode;

                // add requireds
                double x = Double.parseDouble(viaE.getAttribute("x"));
                double y = Double.parseDouble(viaE.getAttribute("y"));
                String extent = viaE.getAttribute("extent");
                double drill = Double.parseDouble(viaE.getAttribute("drill"));

                Via via = new Via(x, y, extent, drill);

                // add optionals
                if (viaE.hasAttribute("diameter")) {
                    via.setDiameter(Double.parseDouble(viaE.getAttribute("diameter")));
                }
                if (viaE.hasAttribute("shape")) {
                    switch (viaE.getAttribute("shape").toLowerCase()) {
                        case "octagon":
                            via.setShape(ViaShape.Octagon);
                            break;
                        case "square":
                            via.setShape(ViaShape.Square);
                            break;
                        default:
                            via.setShape(ViaShape.Round);
                    }
                }
                if (viaE.hasAttribute("alwaysstop")) {
                    via.setAlwaysStop(isTrue(viaE.getAttribute("alwaysstop")));
                }

                vias.add(via);
            }
        }
        return vias;
    }

    // TODO

    /**
     * Lists the contactrefs of an element
     *
     * @param element   the element
     * @return          list of contactrefs
     */
    private List<ContactRef> getContactRefs(Element element) {
        List<ContactRef> contactRefs = new ArrayList<>();

        // add vias
        NodeList contactRefList = element.getElementsByTagName("contactref");
        for (int i = 0; i < contactRefList.getLength(); i++) {
            Node contactRefNode = contactRefList.item(i);
            if (contactRefNode.getNodeType() == Node.ELEMENT_NODE) {
                Element contactRefE = (Element) contactRefNode;

                // add requireds
                String elem = contactRefE.getAttribute("element");
                String pad = contactRefE.getAttribute("pad");

                ContactRef contactRef = new ContactRef(getBoard(), elem, pad);

                // add optionals
                if (contactRefE.hasAttribute("route")) {
                    if (contactRefE.getAttribute("route").equalsIgnoreCase("any")) {
                        contactRef.setRoute(ContactRoute.Any);
                    }
                    else {
                        contactRef.setRoute(ContactRoute.All);
                    }
                }
                if (contactRefE.hasAttribute("routetag")) {
                    contactRef.setRouteTag(contactRefE.getAttribute("routetag"));
                }

                contactRefs.add(contactRef);
            }
        }
        return contactRefs;
    }

    // TODO

    /**
     * Extracts the libraries
     *
     * @param libraries the libraries element
     */
    private void extractLibraries(Element libraries) {
        NodeList libraryList = libraries.getElementsByTagName("library");
        for (int i = 0; i < libraryList.getLength(); i++) {
            Node libraryNode = libraryList.item(i);
            if (libraryNode.getNodeType() == Node.ELEMENT_NODE) {
                Element libraryE = (Element) libraryNode;
                // TODO Description
                String description = "";
                if (hasTag(libraryE, "description")) {

                }
                Library lib = new Library(libraryE.getAttribute("name"), "");
                // TODO Parse packages
                extractPackages(libraryE, lib);

                getBoard().addLibrary(lib);
            }
        }
    }

    /**
     * Extracts the packages of a library
     *
     * @param library                   the library element
     * @param lib                       the current library
     * @throws NumberFormatException    when String parse goes wrong
     */
    private void extractPackages(Element library, Library lib) throws NumberFormatException {
        NodeList packagesList = library.getElementsByTagName("package");
        for (int i = 0; i < packagesList.getLength(); i++) {
            Node packageNode = packagesList.item(i);
            if (packageNode.getNodeType() == Node.ELEMENT_NODE) {
                Element packageE = (Element) packageNode;
                // TODO Description optional
                EaglePackage pack = new EaglePackage(packageE.getAttribute("name"), "");
                List<ILayerable> temp = getPackageBlocks(packageE);
                // save all pads to the package for mst calculation
                for (ILayerable layerable : temp) {
                    if (layerable instanceof Pad) {
                        pack.addPad((Pad) layerable);
                    }
                }
                pack.addBuildingBlocks(temp);

                // parse value and text
                Iterator<ILayerable> iter = pack.getBuildingBlocks().iterator();
                while (iter.hasNext()) {
                    ICoordinateTransformable elem = iter.next();
                    if (elem instanceof EagleText) {
                        EagleText text = (EagleText) elem;
                        if (text.getValue().equalsIgnoreCase(">name")) {
                            pack.setNameText(text);
                            iter.remove();
                        }
                        if (text.getValue().equalsIgnoreCase(">value")) {
                            pack.setValueText(text);
                            iter.remove();
                        }
                    }
                }

                lib.addPackage(pack);
            }
        }
    }

    /**
     * Lists the building blocks included in a package
     *
     * @param pack                      the package element
     * @return                          list of layerable building blocks
     * @throws NumberFormatException    when String parse goes wrong
     */
    private List<ILayerable> getPackageBlocks(Element pack) throws NumberFormatException {
        List<ILayerable> packageBlocks = new ArrayList<>();

        packageBlocks.addAll(getPolygons(pack));
        packageBlocks.addAll(getWires(pack));
        packageBlocks.addAll(getTexts(pack));
        packageBlocks.addAll(getDimensions(pack));
        packageBlocks.addAll(getCircles(pack));
        packageBlocks.addAll(getRectangles(pack));
        packageBlocks.addAll(getFrames(pack));
        packageBlocks.addAll(getHoles(pack));
        packageBlocks.addAll(getThtPads(pack));
        packageBlocks.addAll(getSmdPads(pack));

        return packageBlocks;
    }

    /**
     * Lists the tht pads of an element
     *
     * @param packageE                  the package element
     * @return                          list of tht pads
     * @throws NumberFormatException    when String parse goes wrong
     */
    private List<ThtPad> getThtPads(Element packageE) throws NumberFormatException {
        List<ThtPad> pads = new ArrayList<>();

        // add pads
        NodeList thtList = packageE.getElementsByTagName("pad");
        for (int i = 0; i < thtList.getLength(); i++) {
            Node thtNode = thtList.item(i);
            if (thtNode.getNodeType() == Node.ELEMENT_NODE) {
                Element thtE = (Element) thtNode;

                // add requireds
                String name = thtE.getAttribute("name");
                double x = Double.parseDouble(thtE.getAttribute("x"));
                double y = Double.parseDouble(thtE.getAttribute("y"));
                double drill = Double.parseDouble(thtE.getAttribute("drill"));

                ThtPad pad = new ThtPad(getBoard(), name, x, y, drill);

                // add optionals
                if (thtE.hasAttribute("diameter")) {
                    pad.setDiameter(Double.parseDouble(thtE.getAttribute("diameter")));
                }
                if (thtE.hasAttribute("shape")) {
                    switch (thtE.getAttribute("shape").toLowerCase()) {
                        case "long":
                            pad.setShape(PadShape.Long);
                            break;
                        case "octagon":
                            pad.setShape(PadShape.Octagon);
                            break;
                        case "offset":
                            pad.setShape(PadShape.Offset);
                            break;
                        case "square":
                            pad.setShape(PadShape.Square);
                            break;
                        default:
                            pad.setShape(PadShape.Round);
                    }
                }
                if (thtE.hasAttribute("rot")) {
                    pad.setRot(Integer.parseInt(getDigitsFromMixedString(thtE.getAttribute("rot"))));
                }
                if (thtE.hasAttribute("stop")) {
                    pad.setStop(isTrue(thtE.getAttribute("stop")));
                }
                if (thtE.hasAttribute("thermals")) {
                    pad.setThermals(isTrue(thtE.getAttribute("thermals")));
                }
                if (thtE.hasAttribute("first")) {
                    pad.setFirst(isTrue(thtE.getAttribute("first")));
                }

                pads.add(pad);
            }
        }
        return pads;
    }

    /**
     * Lists the smd pads of an element
     *
     * @param packageE                  the package element
     * @return                          list of smd pads
     * @throws NumberFormatException    when String parse goes wrong
     */
    private List<SmdPad> getSmdPads(Element packageE) throws NumberFormatException {
        List<SmdPad> smds = new ArrayList<>();

        // add pads
        NodeList smdList = packageE.getElementsByTagName("smd");
        for (int i = 0; i < smdList.getLength(); i++) {
            Node smdNode = smdList.item(i);
            if (smdNode.getNodeType() == Node.ELEMENT_NODE) {
                Element smdE = (Element) smdNode;

                // add requireds
                String name = smdE.getAttribute("name");
                double x = Double.parseDouble(smdE.getAttribute("x"));
                double y = Double.parseDouble(smdE.getAttribute("y"));
                double dx = Double.parseDouble(smdE.getAttribute("dx"));
                double dy = Double.parseDouble(smdE.getAttribute("dy"));
                int layer = Integer.parseInt(smdE.getAttribute("layer"));

                SmdPad smd = new SmdPad(getBoard(), name, x, y, dx, dy, layer);

                // add optionals
                if (smdE.hasAttribute("roundness")) {
                    smd.setRoundness(Integer.parseInt(smdE.getAttribute("roundness")));
                }
                if (smdE.hasAttribute("rot")) {
                    smd.setRot(Integer.parseInt(getDigitsFromMixedString(smdE.getAttribute("rot"))));
                }
                if (smdE.hasAttribute("stop")) {
                    smd.setStop(isTrue(smdE.getAttribute("stop")));
                }
                if (smdE.hasAttribute("thermals")) {
                    smd.setThermals(isTrue(smdE.getAttribute("thermals")));
                }
                if (smdE.hasAttribute("cream")) {
                    smd.setCream(isTrue(smdE.getAttribute("cream")));
                }

                smds.add(smd);
            }
        }
        return smds;
    }

    /**
     * Extracts the attributes
     *
     * @param attributes                the attributes element
     * @throws NumberFormatException    when String parse goes wrong
     */
    private void extractAttributes(Element attributes) throws  NumberFormatException {
        NodeList attributesList = attributes.getElementsByTagName("attribute");
        for (int i = 0; i < attributesList.getLength(); i++) {
            Node attributeNode = attributesList.item(i);
            if (attributeNode.getNodeType() == Node.ELEMENT_NODE) {
                Element attributeE = (Element) attributeNode;

                Attribute attribute = new Attribute(getBoard(), attributeE.getAttribute("name"));

                // add optionals
                if (attributeE.hasAttribute("value")) {
                    attribute.setValue(attributeE.getAttribute("value"));
                }
                if (attributeE.hasAttribute("x")) {
                    attribute.setX1(Double.parseDouble(attributeE.getAttribute("x")));
                }
                if (attributeE.hasAttribute("y")) {
                    attribute.setY1(Double.parseDouble(attributeE.getAttribute("y")));
                }
                if (attributeE.hasAttribute("size")) {
                    attribute.setSize(Double.parseDouble(attributeE.getAttribute("size")));
                }
                if (attributeE.hasAttribute("layer")) {
                    attribute.setLayerNumber(Integer.parseInt(attributeE.getAttribute("layer")));
                }
                if (attributeE.hasAttribute("font")) {
                    switch (attributeE.getAttribute("font")) {
                        case "vector":
                            attribute.setFont(TextFont.Vector);
                            break;
                        case "proportional":
                            attribute.setFont(TextFont.Proportional);
                            break;
                        case "fixed":
                            attribute.setFont(TextFont.Fixed);
                            break;
                    }
                }
                if (attributeE.hasAttribute("ratio")) {
                    attribute.setRatio(Integer.parseInt(attributeE.getAttribute("ratio")));
                }
                if (attributeE.hasAttribute("rot")) {
                    attribute.setRot(Integer.parseInt(getDigitsFromMixedString(attributeE.getAttribute("rot"))));
                }
                if (attributeE.hasAttribute("display")) {
                    switch (attributeE.getAttribute("display")) {
                        case "off":
                            attribute.setDisplay(AttributeDisplay.Off);
                            break;
                        case "name":
                            attribute.setDisplay(AttributeDisplay.Name);
                            break;
                        case "both":
                            attribute.setDisplay(AttributeDisplay.Both);
                            break;
                        default:
                            attribute.setDisplay(AttributeDisplay.Value);
                    }
                }
                if (attributeE.hasAttribute("constant")) {
                    attribute.setConstant(isTrue(attributeE.getAttribute("constant")));
                }
            }
        }
    }

    /**
     * Extracts the variantdefs
     *
     * @param variantdefs   the variantdefs element
     */
    private void extractVariantdefs(Element variantdefs) {
        NodeList variantdefsList = variantdefs.getElementsByTagName("variantdef");
        for (int i = 0; i < variantdefsList.getLength(); i++) {
            Node variantdefNode = variantdefsList.item(i);
            if (variantdefNode.getNodeType() == Node.ELEMENT_NODE) {
                Element variantdefE = (Element) variantdefNode;

                VariantDef variantDef = new VariantDef(variantdefE.getAttribute("name"));

                // add optionals
                if (variantdefE.hasAttribute("current")) {
                    variantDef.setCurrent(isTrue(variantdefE.getAttribute("current")));
                }
            }
        }
    }

    // TODO add clearances to class; add class to board

    /**
     * Extracts the classes
     *
     * @param classes                   the classes element
     * @throws NumberFormatException    when String parse goes wrong
     */
    private void extractClasses(Element classes) throws NumberFormatException {
        NodeList classesList = classes.getElementsByTagName("class");
        for (int i = 0; i < classesList.getLength(); i++) {
            Node classNode = classesList.item(i);
            if (classNode.getNodeType() == Node.ELEMENT_NODE) {
                Element classE = (Element) classNode;

                // add requireds
                int number = Integer.parseInt(classE.getAttribute("number"));
                String name = classE.getAttribute("name");

                EagleClass eagleClass = new EagleClass(number, name);

                // add optionals
                if (classE.hasAttribute("width")) {
                    eagleClass.setWidth(Double.parseDouble(classE.getAttribute("width")));
                }
                if (classE.hasAttribute("drill")) {
                    eagleClass.setDrill(Double.parseDouble(classE.getAttribute("drill")));
                }

                // add clearances
                extractClearances(classE);
            }
        }
    }

    /**
     * Extracts the clearances of a class
     *
     * @param classE                    the class element
     * @throws NumberFormatException    when String parse goes wrong
     */
    private void extractClearances(Element classE) throws NumberFormatException {
        NodeList clearancesList = classE.getElementsByTagName("clearance");
        for (int i = 0; i < clearancesList.getLength(); i++) {
            Node clearanceNode = clearancesList.item(i);
            if (clearanceNode.getNodeType() == Node.ELEMENT_NODE) {
                Element clearanceE = (Element) clearanceNode;

                Clearance clearance = new Clearance(clearanceE.getAttribute("class"));

                // add optionals
                if (clearanceE.hasAttribute("value")) {
                    clearance.setValue(Double.parseDouble(clearanceE.getAttribute("value")));
                }
            }
        }
    }


    // TODO BIG !!!

    /**
     * Extracts the designrules. Simple extraction.
     *
     * @param designrules the designrules
     */
    private void extractDesignrules(Element designrules) {
        // designrules (description*, param*)
        // we don't need design rules for this project, therefore we just extract the name attribute and the whole body of the <designrules> tag
        getBoard().setDesignRules(new DesignRules(designrules.getAttribute("name"), designrules.getTextContent()));
    }

    /**
     * Extracts the autorouter. Simple extraction.
     *
     * @param autorouter the autorouter
     */
    private void extractAutorouter(Element autorouter) {
        // autorouter (pass)*
        // pass (param)*
        // we don't need autorouters for this project, therefore we just extract the whole body of the <autorouters> tag
        getBoard().setAutorouter(autorouter.getTextContent());
    }

    /**
     * Extracts the elements
     *
     * @param elements                  the elements element
     * @throws NumberFormatException    when String parse goes wrong
     */
    private void extractElements(Element elements) throws NumberFormatException {
        // TODO element (attribute*, variant*)
        // TODO variant
        NodeList elementsList = elements.getElementsByTagName("element");
        for (int i = 0; i < elementsList.getLength(); i++) {
            Node elementNode = elementsList.item(i);
            if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elementE = (Element) elementNode;

                // add requireds
                String name = elementE.getAttribute("name");
                String library = elementE.getAttribute("library");
                String packagE = elementE.getAttribute("package");
                String value = elementE.getAttribute("value");
                double x = Double.parseDouble(elementE.getAttribute("x"));
                double y = Double.parseDouble(elementE.getAttribute("y"));

                model.sceneObjects.coordinateTransformables.Element element = new model.sceneObjects.coordinateTransformables.Element(getBoard(), name, library, packagE, value, x, y);

                // add optionals
                if (elementE.hasAttribute("locked")) {
                    element.setLocked(isTrue(elementE.getAttribute("locked")));
                }
                if (elementE.hasAttribute("smashed")) {
                    element.setSmashed(isTrue(elementE.getAttribute("smashed")));
                }
                if (elementE.hasAttribute("rot")) {
                    element.setRot(Integer.parseInt(getDigitsFromMixedString(elementE.getAttribute("rot"))));
                }

                getBoard().addElement(element);
            }
        }
    }

    /**
     * Extracts the signals
     *
     * @param signals                   the signals element
     * @throws NumberFormatException    when String parse goes wrong
     */
    private void extractSignals(Element signals) throws NumberFormatException {
        // signal (contactref | polygon | wire | via)*
        NodeList signalList = signals.getElementsByTagName("signal");
        for (int i = 0; i < signalList.getLength(); i++) {
            Node signalNode = signalList.item(i);
            if (signalNode.getNodeType() == Node.ELEMENT_NODE) {
                Element signalE = (Element) signalNode;

                Signal signal = new Signal(getBoard(), signalE.getAttribute("name"));

                // add optionals
                if (signalE.hasAttribute("class")) {
                    signal.setClassNumber(Integer.parseInt(signalE.getAttribute("class")));
                }
                if (signalE.hasAttribute("airwireshidden")) {
                    signal.setAirWiresHidden(isTrue(signalE.getAttribute("airwireshidden")));
                }

                signal.addPolygons(getPolygons(signalE));
                signal.addWires(getWires(signalE));
                signal.addVias(getVias(signalE));
                signal.addContactRefs(getContactRefs(signalE));

                getBoard().addSignal(signal);
            }
        }
    }


    /**
     * Extracts the errors. Simple extraction.
     *
     * @param errors the errors element
     */
    private void extractErrors(Element errors) {
        // errors (approved)*
        // we don't need errors for this project, therefore we just extract the whole body of the <errors> tag
        getBoard().setErrors(errors.getTextContent());
    }
}
