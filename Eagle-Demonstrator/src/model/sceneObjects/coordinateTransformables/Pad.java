package model.sceneObjects.coordinateTransformables;

import model.sceneObjects.EagleBoard;
import utils.CoordinateTransformer;

/**
 * The type Pad.
 */
public abstract class Pad implements ICoordinateTransformable, ILayerable {

    /**
     * The Board.
     */
// required
    EagleBoard board;
    /**
     * The X 1.
     */
    double x1;
    /**
     * The Y 1.
     */
    double y1;
    /**
     * The Name.
     */
    String name;

    /**
     * The Rot.
     */
// optional
    int rot;
    /**
     * The Stop.
     */
    boolean stop;
    /**
     * The Thermals.
     */
    boolean thermals;

    private boolean isSetRot;
    private boolean isSetStop;
    private boolean isSetThermals;

    /**
     * Instantiates a new Pad.
     *
     * @param board the board
     * @param x1    the x 1
     * @param y1    the y 1
     * @param name  the name
     */
    public Pad(EagleBoard board, double x1, double y1, String name) {
        this.board = board;
        this.x1 = x1;
        this.y1 = y1;
        this.name = name;

        this.rot = 0;
        this.stop = true;
        this.thermals = true;

        this.isSetThermals = false;
        this.isSetStop = false;
        this.isSetRot = false;
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
     * Sets stop.
     *
     * @param stop the stop
     */
    public void setStop(boolean stop) {
        this.stop = stop;
        this.isSetStop = true;
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
     * Gets rot.
     *
     * @return the rot
     */
    public int getRot() {
        return rot;
    }

    /**
     * Gets stop.
     *
     * @return the stop
     */
    public boolean getStop() {
        return stop;
    }

    /**
     * Gets thermals.
     *
     * @return the thermals
     */
    public boolean getThermals() {
        return thermals;
    }

    @Override
    public void transformCoordinates(CoordinateTransformer ct, boolean signTransformation) {
        this.y1 = signTransformation ? ct.transformSign(getY1()) : ct.transformOrigin(getY1());
    }
}
