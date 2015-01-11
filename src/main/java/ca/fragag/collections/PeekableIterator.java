package ca.fragag.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Wraps an {@link Iterator} and provides a {@link #peek()} method that reads and stores the next value so that it can be obtained
 * several times.
 *
 * @param <E>
 *            the type of elements returned by this iterator
 *
 * @author Francis Gagné
 */
public final class PeekableIterator<E> implements Iterator<E> {

    @Nonnull
    private final Iterator<E> iterator;
    private boolean peeked;
    @CheckForNull
    private E peekedElement;

    /**
     * Initializes a new PeekableIterator.
     *
     * @param iterator
     *            the iterator to wrap
     */
    public PeekableIterator(@Nonnull Iterator<E> iterator) {
        if (iterator == null) {
            throw new NullPointerException("iterator");
        }

        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return this.peeked || this.iterator.hasNext();
    }

    @Override
    public E next() {
        if (this.peeked) {
            this.peeked = false;

            final E peekedElement = this.peekedElement;
            this.peekedElement = null; // don't retain a reference to the element unnecessarily
            return peekedElement;
        }

        return this.iterator.next();
    }

    /**
     * Returns the next element of this iterator without advancing it.
     *
     * @return the next element
     * @throws NoSuchElementException
     *             if the iteration has no more elements
     */
    public E peek() {
        if (!this.peeked) {
            this.peeked = true;
            this.peekedElement = this.iterator.next();
        }

        return this.peekedElement;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("PeekableIterator doesn't support remove()");
    }

}
