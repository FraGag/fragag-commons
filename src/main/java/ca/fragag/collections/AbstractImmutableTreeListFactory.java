package ca.fragag.collections;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * A factory of {@link AbstractImmutableTreeList} objects.
 *
 * @param <E>
 *            the type of the elements in the list
 * @param <N>
 *            the type of node used in the list
 * @param <L>
 *            the type of list that the factory returns
 *
 * @author Francis Gagné
 */
@Immutable
public abstract class AbstractImmutableTreeListFactory<E, N extends AbstractImmutableTreeNode<E, N>, L extends AbstractImmutableTreeList<E, N>> {

    @Nonnull
    private final AbstractImmutableTreeNodeFactory<E, N> nodeFactory;

    /**
     * Initializes a new AbstractImmutableTreeListFactory.
     *
     * @param nodeFactory
     *            a factory of nodes of type {@code N} to use to create nodes
     */
    protected AbstractImmutableTreeListFactory(@Nonnull AbstractImmutableTreeNodeFactory<E, N> nodeFactory) {
        if (nodeFactory == null) {
            throw new NullPointerException("nodeFactory");
        }

        this.nodeFactory = nodeFactory;
    }

    /**
     * Creates a new list with the contents of the specified list and the specified element appended to it.
     *
     * @param list
     *            the list
     * @param element
     *            the element to add
     * @return the new list
     */
    @Nonnull
    public L add(@Nonnull L list, E element) {
        return this.add(list, list.size(), element);
    }

    /**
     * Creates a new list with the contents of the specified list and the specified element added to it at the specified position.
     *
     * @param list
     *            the list
     * @param index
     *            the position at which the element is to be inserted
     * @param element
     *            the element to add
     * @return the new list
     */
    @Nonnull
    public L add(@Nonnull L list, int index, E element) {
        if (index < 0 || index > list.size()) {
            throw new IndexOutOfBoundsException("index = " + index + ", size = " + list.size());
        }

        // If the input list is empty, create a new list with a single element at the root.
        final N root = list.getRoot();
        if (root == null) {
            return this.createList(this.nodeFactory.createNode(null, element, null));
        }

        return this.createList(this.add(root, index, element));
    }

    /**
     * Creates an empty list.
     *
     * @return an empty list.
     */
    @Nonnull
    public L create() {
        return this.createList(null);
    }

    /**
     * Creates a list with the elements of a {@link Collection}.
     * <p>
     * This method creates a well-balanced tree by taking advantage of the {@link Collection#size()} method.
     *
     * @param collection
     *            the collection
     * @return the new list
     */
    @Nonnull
    public L create(@Nonnull Collection<? extends E> collection) {
        int size = collection.size();
        Iterator<? extends E> iterator = collection.iterator();
        return this.createList(this.createBalanced(iterator, size));
    }

    /**
     * Creates a list with the elements of an {@link Iterable}.
     * <p>
     * This method calls {@link AbstractImmutableTreeListFactory#create(Collection)} if <code>iterable</code> is a
     * {@link Collection}.
     *
     * @param iterable
     *            the iterable
     * @return the new list
     */
    @Nonnull
    public L create(@Nonnull Iterable<? extends E> iterable) {
        if (iterable instanceof Collection<?>) {
            return this.create((Collection<? extends E>) iterable);
        }

        L result = this.create();
        for (E e : iterable) {
            result = this.add(result, e);
        }

        return result;
    }

    /**
     * Creates a new list with the contents of the specified list and the element at the specified position removed from it.
     *
     * @param list
     *            the list
     * @param index
     *            the position of the element to remove
     * @return the new list
     */
    @Nonnull
    public L remove(@Nonnull L list, int index) {
        if (index < 0 || index >= list.size()) {
            throw new IndexOutOfBoundsException("index = " + index + ", size = " + list.size());
        }

        final N root = list.getRoot();
        assert root != null;
        return this.createList(this.remove(root, index));
    }

    /**
     * Creates a new list with the contents of the specified list and the element at the specified position replaced with the
     * specified element.
     *
     * @param list
     *            the list
     * @param index
     *            the position of the element to change
     * @param element
     *            the new element
     * @return the new list
     */
    @Nonnull
    public L set(@Nonnull L list, int index, E element) {
        if (index < 0 || index >= list.size()) {
            throw new IndexOutOfBoundsException("index = " + index + ", size = " + list.size());
        }

        final N root = list.getRoot();
        assert root != null;
        return this.createList(this.set(root, index, element));
    }

    /**
     * Creates a list (of type {@code L}) with the specified root node.
     *
     * @param root
     *            the root node
     * @return a list with the specified root node.
     */
    @Nonnull
    protected abstract L createList(@CheckForNull N root);

    @Nonnull
    private final N add(@Nonnull N node, int index, E element) {
        if (node == null) {
            return this.createLeafNode(element);
        }

        final N left = node.getLeft();
        final N right = node.getRight();
        final E value = node.getValue();

        final int leftSize = left == null ? 0 : left.getSize();

        if (index <= leftSize) {
            return this.nodeFactory.balanceLeft(this.add(left, index, element), value, right);
        }

        return this.nodeFactory.balanceRight(left, value, this.add(right, index - leftSize - 1, element));
    }

    @CheckForNull
    private final N createBalanced(@Nonnull Iterator<? extends E> iterator, int size) {
        if (size == 0) {
            return null;
        }

        if (size == 1) {
            return this.createLeafNode(iterator.next());
        }

        int sizeOnRight = size / 2;
        int sizeOnLeft = size - sizeOnRight - 1;
        N nodeOnLeft = this.createBalanced(iterator, sizeOnLeft);
        E centerValue = iterator.next();
        N nodeOnRight = this.createBalanced(iterator, sizeOnRight);
        return this.nodeFactory.createNode(nodeOnLeft, centerValue, nodeOnRight);
    }

    @Nonnull
    private final N createLeafNode(E value) {
        return this.nodeFactory.createNode(null, value, null);
    }

    @Nonnull
    private final N remove(@Nonnull N node, int index) {
        final N left = node.getLeft();
        final N right = node.getRight();
        final E value = node.getValue();

        final int leftSize = left == null ? 0 : left.getSize();

        if (index == leftSize) {
            return this.nodeFactory.glue(left, right);
        }

        if (index < leftSize) {
            return this.nodeFactory.balanceRight(this.remove(left, index), value, right);
        }

        return this.nodeFactory.balanceLeft(left, value, this.remove(right, index - leftSize - 1));
    }

    @Nonnull
    private final N set(@Nonnull N node, int index, E element) {
        final N left = node.getLeft();
        final E value = node.getValue();
        final N right = node.getRight();

        final int leftSize = left == null ? 0 : left.getSize();

        if (index == leftSize) {
            return this.nodeFactory.createNode(left, element, right);
        }

        if (index < leftSize) {
            return this.nodeFactory.createNode(this.set(left, index, element), value, right);
        }

        return this.nodeFactory.createNode(left, value, this.set(right, index - leftSize - 1, element));
    }

}
