package main;

import model.BoundingBox;
import model.ContactRef;
import model.Library;
import model.sceneObjects.EagleBoard;
import model.sceneObjects.coordinateTransformables.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.PrimMinimumSpanningTree;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.WeightedMultigraph;

import javafx.geometry.Point2D;
import java.util.*;

/**
 * The type Random board changer.
 */
public class RandomBoardChanger {

    private EagleBoard workingCopy;
    private Random generator;

    /**
     * Instantiates a new Random board changer.
     *
     * @param workingCopy the copy of the board we work on
     */
    public RandomBoardChanger(EagleBoard workingCopy) {
        this.workingCopy = workingCopy;
        this.generator = new Random();
    }

    /**
     * Shuffle board eagle board.
     *
     * @return the eagle board
     */
    public EagleBoard shuffleBoard() {
        // TODO: clone board
        EagleBoard shuffledBoard = getWorkingCopy();

        // get board bounds to limit the random positions
        BoundingBox platineBbox = shuffledBoard.getPlatineBbox();

        // generate board until there is no intersection between elements
        boolean boardIsValid = false;

        do {
            // for each element set new random position
            Map<String, Element> elements = repositionElements(shuffledBoard.getElements(), platineBbox);

            // check if elements dont intersect
            boardIsValid = elementPositionsValid(elements, platineBbox);

        } while (!boardIsValid);

        // here we have generated a valid board so we have to calculate the MST for each signal
        // TODO: MST here; vlt board übergeben?
        updateSignals();

        shuffledBoard.setIsShuffled(true);

        return shuffledBoard;
    }

    private void updateSignals() {
        for (Signal s : getWorkingCopy().getSignals()) {
            // TODO
            int layerNumber = s.getWires().get(0).getLayerNumber();
            double width = s.getWires().get(0).getWidth();
            String extent = s.getWires().get(0).getExtent();

            Graph<Point2D, DefaultWeightedEdge> graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);

            Point2D[] pads = new Point2D[s.getContactRefs().size()];

            for (int i = 0; i < s.getContactRefs().size(); i++) {
                // add cr's pad to pads
                Element element = getWorkingCopy().getElements().get(s.getContactRefs().get(i).getElement());
                Library lib = getWorkingCopy().getLibraries().get(element.getLibraryName());
                EaglePackage p = lib.getPackages().get(element.getPackageName());
                Pad temp = p.getPad(s.getContactRefs().get(i).getPad());
                Point2D toRotate = new Point2D(temp.getX1(), temp.getY1());
                pads[i] = new Point2D(rotatePoint(toRotate, element.getRot()).getX() + element.getX1(), rotatePoint(toRotate, element.getRot()).getY() + element.getY1());


                // add pad to graph as vertex
                graph.addVertex(pads[i]);
            }
            // delete current wires
            s.getWires().clear();

            // calculate graph weights
            double[][] weights = new double[pads.length][pads.length];
            // TODO for schleife anpassen, int j = i; Array anpassen
            for (int i = 0; i < weights[0].length; i++) {
                for (int j = 0; j < weights[0].length; j++) {
                    if (i == j) {
                        weights[i][j] = 0.0;
                    } else {
                        weights[i][j] = java.awt.geom.Point2D.distance(pads[i].getX(), pads[i].getY(), pads[j].getX(), pads[j].getY());

                        graph.addEdge(pads[i], pads[j]);
                        graph.setEdgeWeight(pads[i], pads[j], weights[i][j]);
                    }
                }
            }

            // calculate MST
            PrimMinimumSpanningTree<Point2D, DefaultWeightedEdge> mstAlg = new PrimMinimumSpanningTree<>(graph);
            SpanningTreeAlgorithm.SpanningTree<DefaultWeightedEdge> mst = mstAlg.getSpanningTree();

            // generate new wires
            for (DefaultWeightedEdge e : mst.getEdges()) {
                Wire wire = new Wire(getWorkingCopy(), graph.getEdgeSource(e).getX(), graph.getEdgeSource(e).getY(),
                        graph.getEdgeTarget(e).getX(), graph.getEdgeTarget(e).getY(), layerNumber, width, extent);
                s.getWires().add(wire);
            }
            System.out.println(s.getName());
            System.out.println(s.getWires().size());
            for (Wire w : s.getWires()) {
                System.out.println("X1=" + w.getX1() + " X2=" + w.getX2() + " ;Y1=" + w.getY1() + " Y2=" + w.getY2() + " ");
            }
        }
    }

    public Point2D rotatePoint(Point2D p, int angle) {
        double rad = -Math.PI * (angle / 180.0);

        double x = p.getX() * Math.cos(rad) - p.getY() * Math.sin(rad);
        double y = p.getX() * Math.sin(rad) + p.getY() * Math.cos(rad);

        return new Point2D(x, y);
    }

    private Map<String, Element> repositionElements(Map<String, Element> elements, BoundingBox bbox) {
        Map<String, Element> repositioned = new HashMap<>();

        for (Element elem : elements.values()) {
            double randomX = bbox.getMinX() + (bbox.getMaxX() - bbox.getMinX()) * generator.nextDouble();
            double randomY = bbox.getMinY() + (bbox.getMaxY() - bbox.getMinY()) * generator.nextDouble();
            elem.setX1(randomX);
            elem.setY1(randomY);

            int rotation = generator.nextInt(4) * 90;
            elem.setRot(rotation);
        }

        return elements;
    }

    private boolean elementPositionsValid(Map<String, Element> elements, BoundingBox platineBbox) {
        // get bounding box of each element and check if it overlaps with any other bbox
        // or if the box exceeds the platine
        List<BoundingBox> boundingBoxes = new ArrayList<>();

        for (Element elem : elements.values()) {
            BoundingBox elementBbox = elem.getBoundingBox();

            // check if the new box overlaps with any other
            for (BoundingBox bbox : boundingBoxes) {
                if(bbox.isOverlapping(elementBbox)
                || bbox.getLowerLeftEdge().getX() < platineBbox.getLowerLeftEdge().getX()
                || bbox.getUpperRightEdge().getX() > platineBbox.getUpperRightEdge().getX()
                || bbox.getLowerLeftEdge().getY() < platineBbox.getLowerLeftEdge().getY()
                || bbox.getUpperRightEdge().getY() > platineBbox.getUpperRightEdge().getY()) {
                    return false;
                }
            }

            // if not than add it to the others
            boundingBoxes.add(elementBbox);
        }

        return true;
    }

    /**
     * Gets copy.
     *
     * @return the copy
     */
    public EagleBoard getWorkingCopy() {
        return workingCopy;
    }
}
