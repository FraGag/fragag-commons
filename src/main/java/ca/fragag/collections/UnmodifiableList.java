package ca.fragag.collections;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Base class for unmodifiable {@linkplain List lists}. All the modification operations throw an
 * {@link UnsupportedOperationException}.
 *
 * @param <E>
 *            the type of the elements in the list
 *
 * @author Francis Gagné
 */
public abstract class UnmodifiableList<E> implements List<E> {

    @Nonnull
    private static UnsupportedOperationException readOnlyException() {
        return new UnsupportedOperationException("this list is read-only");
    }

    /**
     * Initializes a new UnmodifiableList.
     */
    protected UnmodifiableList() {
    }

    @Override
    public final boolean add(E e) {
        throw readOnlyException();
    }

    @Override
    public final void add(int index, E element) {
        throw readOnlyException();
    }

    @Override
    public final boolean addAll(Collection<? extends E> c) {
        throw readOnlyException();
    }

    @Override
    public final boolean addAll(int index, Collection<? extends E> c) {
        throw readOnlyException();
    }

    @Override
    public final void clear() {
        throw readOnlyException();
    }

    @Override
    public final E remove(int index) {
        throw readOnlyException();
    }

    @Override
    public final boolean remove(Object o) {
        throw readOnlyException();
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        throw readOnlyException();
    }

    @Override
    public final boolean retainAll(Collection<?> c) {
        throw readOnlyException();
    }

    @Override
    public final E set(int index, E element) {
        throw readOnlyException();
    }

}
