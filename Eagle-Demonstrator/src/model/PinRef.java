package model;

/**
 * The type Pin ref.
 */
public class PinRef {

    // required
    private String part;
    private String gate;
    private String pin;

    /**
     * Instantiates a new Pin ref.
     *
     * @param part the part
     * @param gate the gate
     * @param pin  the pin
     */
    public PinRef(String part, String gate, String pin) {
        this.part = part;
        this.gate = gate;
        this.pin = pin;
    }

    /**
     * Gets part.
     *
     * @return the part
     */
    public String getPart() {
        return part;
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
}
