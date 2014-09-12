package ca.fragag.collections;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * A factory of {@link AbstractImmutableTreeList} objects.
 *
 * @param <E>
 *            the type of the elements in the list
 *
 * @author Francis Gagné
 */
@Immutable
public class ImmutableTreeListFactory<E> extends AbstractImmutableTreeListFactory<E, ImmutableTreeNode<E>, ImmutableTreeList<E>> {

    /**
     * The unique instance of the {@link ImmutableTreeListFactory} class.
     */
    private static final ImmutableTreeListFactory<?> INSTANCE = new ImmutableTreeListFactory<>();

    /**
     * Gets the unique instance of the {@link ImmutableTreeListFactory} class.
     *
     * @return the unique instance of the {@link ImmutableTreeListFactory} class
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <E> ImmutableTreeListFactory<E> getInstance() {
        return (ImmutableTreeListFactory<E>) INSTANCE;
    }

    /**
     * Initializes a new ImmutableTreeListFactory.
     */
    private ImmutableTreeListFactory() {
        super(ImmutableTreeNode.Factory.<E> getInstance());
    }

    @Nonnull
    @Override
    protected ImmutableTreeList<E> createList(@CheckForNull ImmutableTreeNode<E> root) {
        if (root == null) {
            return ImmutableTreeList.<E> getEmpty();
        }

        return new ImmutableTreeList<>(root);
    }

}
