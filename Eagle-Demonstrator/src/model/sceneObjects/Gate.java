package model.sceneObjects;

import javafx.scene.Node;
import model.BoundingBox;
import model.enums.GateAddLevel;

/**
 * The type Gate.
 */
public class Gate implements ISceneObject {

    private EagleBoard board;
    private double x1;
    private double y1;
    private String name;
    private String symbol;

    private GateAddLevel addLevel;
    private int swapLevel;

    /**
     * Instantiates a new Gate.
     *
     * @param board  the board
     * @param x1     the x 1
     * @param y1     the y 1
     * @param name   the name
     * @param symbol the symbol
     */
    public Gate(EagleBoard board, double x1, double y1, String name, String symbol) {
        this.board = board;
        this.x1 = x1;
        this.y1 = y1;
        this.name = name;
        this.symbol = symbol;

        this.addLevel = GateAddLevel.Next;
        this.swapLevel = 0;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        return null;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return null;
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        return null;
    }

    /**
     * Sets add level.
     *
     * @param addLevel the add level
     */
    public void setAddLevel(GateAddLevel addLevel) {
        this.addLevel = addLevel;
    }

    /**
     * Sets swap level.
     *
     * @param swapLevel the swap level
     */
    public void setSwapLevel(int swapLevel) {
        this.swapLevel = swapLevel;
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

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets symbol.
     *
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets add level.
     *
     * @return the add level
     */
    public GateAddLevel getAddLevel() {
        return addLevel;
    }

    /**
     * Gets swap level.
     *
     * @return the swap level
     */
    public int getSwapLevel() {
        return swapLevel;
    }
}
