package ca.fragag;

import javax.annotation.Nonnull;

/**
 * Represents an operation that accepts a single input argument and returns no result.
 *
 * @param <T>
 *            the type of object that this consumer expects
 *
 * @author Francis Gagné
 */
public interface Consumer<T> {

    /**
     * Performs the operation of the specified object.
     *
     * @param object
     *            the object to process
     */
    void accept(@Nonnull T object);

}
