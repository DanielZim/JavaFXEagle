package model;

import model.enums.ContactRoute;
import model.sceneObjects.EagleBoard;

/**
 * The type Contact ref.
 */
public class ContactRef {

    private EagleBoard board;
    private String element;
    private String pad;

    private ContactRoute route;
    private String routeTag;

    /**
     * Instantiates a new Contact ref.
     *
     * @param board   the board
     * @param element the element
     * @param pad     the pad
     */
    public ContactRef(EagleBoard board, String element, String pad) {
        this.board = board;
        this.element = element;
        this.pad = pad;

        this.route = ContactRoute.All;
        this.routeTag = "";
    }

    /**
     * Sets route.
     *
     * @param route the route
     */
    public void setRoute(ContactRoute route) {
        this.route = route;
    }

    /**
     * Sets route tag.
     *
     * @param routeTag the route tag
     */
    public void setRouteTag(String routeTag) {
        this.routeTag = routeTag;
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
     * Gets element.
     *
     * @return the element
     */
    public String getElement() {
        return element;
    }

    /**
     * Gets pad.
     *
     * @return the pad
     */
    public String getPad() {
        return pad;
    }

    /**
     * Gets route.
     *
     * @return the route
     */
    public ContactRoute getRoute() {
        return route;
    }

    /**
     * Gets route tag.
     *
     * @return the route tag
     */
    public String getRouteTag() {
        return routeTag;
    }
}
