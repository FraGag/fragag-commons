package ca.fragag.collections;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
class ImmutableTreeNode<E> extends AbstractImmutableTreeNode<E, ImmutableTreeNode<E>> {

    @Immutable
    static final class Factory<E> extends AbstractImmutableTreeNodeFactory<E, ImmutableTreeNode<E>> {

        private static final Factory<?> INSTANCE = new Factory<>();

        @Nonnull
        @SuppressWarnings("unchecked")
        static <E> Factory<E> getInstance() {
            return (Factory<E>) INSTANCE;
        }

        private Factory() {
        }

        @Nonnull
        @Override
        protected ImmutableTreeNode<E> createNode(@CheckForNull ImmutableTreeNode<E> left, E value,
                @CheckForNull ImmutableTreeNode<E> right) {
            return new ImmutableTreeNode<>(left, value, right);
        }

    }

    /**
     * Initializes a new Node.
     *
     * @param left
     *            the left subtree
     * @param value
     *            the element value
     * @param right
     *            the right subtree
     */
    ImmutableTreeNode(@CheckForNull ImmutableTreeNode<E> left, E value, @CheckForNull ImmutableTreeNode<E> right) {
        super(left, value, right);
    }

}
