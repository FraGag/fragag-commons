package ca.fragag.collections;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * An immutable list that is structured as a binary tree.
 *
 * @param <E>
 *            the type of the elements in the list
 * @author Francis Gagné
 */
@Immutable
public class ImmutableTreeList<E> extends AbstractImmutableTreeList<E, ImmutableTreeNode<E>> {

    private static final ImmutableTreeList<Object> EMPTY = new ImmutableTreeList<>(null);

    @Nonnull
    @SuppressWarnings("unchecked")
    static <E> ImmutableTreeList<E> getEmpty() {
        return (ImmutableTreeList<E>) EMPTY;
    }

    /**
     * Initializes a new ImmutableTreeList.
     */
    ImmutableTreeList(@CheckForNull ImmutableTreeNode<E> root) {
        super(root);
    }

}
