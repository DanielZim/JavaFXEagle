package model;

import model.enums.ContactRoute;

/**
 * The type Connect.
 */
public class Connect {

    private String gate;
    private String pin;
    private String pad;

    private ContactRoute route;

    /**
     * Instantiates a new Connect.
     *
     * @param gate the gate
     * @param pin  the pin
     * @param pad  the pad
     */
    public Connect(String gate, String pin, String pad) {
        this.gate = gate;
        this.pin = pin;
        this.pad = pad;

        this.route = ContactRoute.All;
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
     * Gets gate.
     *
     * @return the gate
     */
    public String getGate() {
        return gate;
    }

    /**
     * Gets pin.
     *
     * @return the pin
     */
    public String getPin() {
        return pin;
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
}
