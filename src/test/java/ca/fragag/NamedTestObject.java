package ca.fragag;

/**
 * This class overrides {@link Object#toString()} to return a string specified in the constructor.
 *
 * @author Francis Gagné
 */
public final class NamedTestObject {

    private final String name;

    /**
     * Initializes a new NamedTestObject.
     *
     * @param name
     *            the name of the object, which will be returned by {@link #toString()}.
     */
    public NamedTestObject(String name) {
        this.name = name;
    }

    /**
     * Returns the name specified when the object was constructed.
     *
     * @see #NamedTestObject(String)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.name;
    }

}
