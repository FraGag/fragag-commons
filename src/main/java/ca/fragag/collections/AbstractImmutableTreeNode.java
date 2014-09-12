package ca.fragag.collections;

import java.util.Objects;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * An immutable node in a binary tree.
 *
 * @param <E>
 *            the type of the value in the node and its child nodes
 * @param <N>
 *            the type of the child nodes (usually the type itself)
 * @author Francis Gagné
 */
@Immutable
public abstract class AbstractImmutableTreeNode<E, N extends AbstractImmutableTreeNode<E, N>> {

    /** The left subtree. */
    @CheckForNull
    private final N left;
    /** The stored element. */
    private final E value;
    /** The right subtree. */
    @CheckForNull
    private final N right;

    /** How many nodes are in this subtree. */
    private final transient int size;

    /**
     * Initializes a new AbstractImmutableTreeNode.
     *
     * @param left
     *            the left subtree
     * @param value
     *            the element value
     * @param right
     *            the right subtree
     */
    protected AbstractImmutableTreeNode(@CheckForNull N left, E value, @CheckForNull N right) {
        this.left = left;
        this.value = value;
        this.right = right;

        this.size = (left == null ? 0 : left.getSize()) + (right == null ? 0 : right.getSize()) + 1;
    }

    /**
     * Gets the root node of this node's left subtree.
     *
     * @return the root of the left subtree, or <code>null</code> if there is no left subtree
     */
    @CheckForNull
    public final N getLeft() {
        return this.left;
    }

    /**
     * Gets the root node of this node's right subtree.
     *
     * @return the root of the right subtree, or <code>null</code> if there is no right subtree
     */
    @CheckForNull
    public final N getRight() {
        return this.right;
    }

    /**
     * Gets the value stored in this node.
     *
     * @return the value
     */
    @CheckForNull
    public final E getValue() {
        return this.value;
    }

    /**
     * Gets the number of nodes in this subtree.
     *
     * @return the size of the subtree.
     */
    protected final int getSize() {
        return this.size;
    }

    /**
     * Gets the element at the specified index.
     *
     * @param index
     *            the index of the element to get
     * @return the element
     */
    @CheckForNull
    final E get(int index) {
        int leftSize = 0;
        if (this.left != null) {
            leftSize = this.left.getSize();
            if (index < leftSize) {
                return this.left.get(index);
            }

            index -= leftSize;
        }

        if (index == 0) {
            return this.value;
        }

        assert this.right != null;
        return this.right.get(index - 1);
    }

    /**
     * Finds the index of the first location where an object is stored in the list.
     *
     * @param object
     *            the object to find
     * @param index
     *            the index of the leftmost node in this subtree
     * @return the index of the first location where the object is located, or -1 if the object is not stored in the list.
     */
    final int indexOf(@CheckForNull Object object, int index) {
        if (this.left != null) {
            int result = this.left.indexOf(object, index);
            if (result != -1) {
                return result;
            }

            index += this.left.getSize();
        }

        if (Objects.equals(this.value, object)) {
            return index;
        }

        if (this.right != null) {
            return this.right.indexOf(object, index + 1);
        }

        return -1;
    }

    /**
     * Finds the index of the first location where an object is stored in a sublist.
     *
     * @param object
     *            the object to find
     * @param index
     *            the index of the leftmost node in this subtree
     * @param fromIndex
     *            the index of the first element in the sublist
     * @param toIndex
     *            the index following the last element in the sublist
     * @return the index of the first location where the object is located, or -1 if the object is not stored in the sublist.
     */
    final int indexOf(@CheckForNull Object object, int index, int fromIndex, int toIndex) {
        if (this.left != null) {
            if (fromIndex < index + this.left.getSize()) {
                int result = this.left.indexOf(object, index, fromIndex, toIndex);
                if (result != -1) {
                    return result;
                }
            }

            index += this.left.getSize();
        }

        if (index < toIndex) {
            if (fromIndex <= index) {
                if (Objects.equals(this.value, object)) {
                    return index - fromIndex;
                }
            }

            index += 1;
            if (index < toIndex) {
                if (this.right != null) {
                    return this.right.indexOf(object, index, fromIndex, toIndex);
                }
            }
        }

        return -1;
    }

    /**
     * Finds the index of the last location where an object is stored in the list.
     *
     * @param object
     *            the object to find
     * @param index
     *            the index following the rightmost node in this subtree
     * @return the index of the last location where the object is located, or -1 if the object is not stored in the list.
     */
    final int lastIndexOf(@CheckForNull Object object, int index) {
        if (this.right != null) {
            int result = this.right.lastIndexOf(object, index);
            if (result != -1) {
                return result;
            }

            index -= this.right.getSize();
        }

        index -= 1;
        if (Objects.equals(this.value, object)) {
            return index;
        }

        if (this.left != null) {
            return this.left.lastIndexOf(object, index);
        }

        return -1;
    }

    /**
     * Finds the index of the last location where an object is stored in a sublist.
     *
     * @param object
     *            the object to find
     * @param index
     *            the index following the rightmost node in this subtree
     * @param fromIndex
     *            the index of the first element in the sublist
     * @param toIndex
     *            the index following the last element in the sublist
     * @return the index of the last location where the object is located, or -1 if the object is not stored in the sublist.
     */
    final int lastIndexOf(@CheckForNull Object object, int index, int fromIndex, int toIndex) {
        if (this.right != null) {
            if (toIndex > index - this.right.getSize()) {
                int result = this.right.lastIndexOf(object, index, fromIndex, toIndex);
                if (result != -1) {
                    return result;
                }
            }

            index -= this.right.getSize();
        }

        index -= 1;
        if (index < toIndex) {
            if (fromIndex <= index) {
                if (Objects.equals(this.value, object)) {
                    return index - fromIndex;
                }
            }

            if (fromIndex < index) {
                if (this.left != null) {
                    return this.left.lastIndexOf(object, index, fromIndex, toIndex);
                }
            }
        }

        return -1;
    }

    /**
     * Copies the contents of this node and its children to an array.
     *
     * @param array
     *            the array to fill
     * @param index
     *            the index of the leftmost node in this subtree
     */
    final void toArray(@Nonnull Object[] array, int index) {
        if (this.left != null) {
            this.left.toArray(array, index);
            index += this.left.getSize();
        }

        array[index] = this.value;

        if (this.right != null) {
            this.right.toArray(array, index + 1);
        }
    }

    /**
     * Copies the contents of a range of the list to an array.
     *
     * @param array
     *            the array to fill
     * @param index
     *            the index of the leftmost node in this subtree
     * @param fromIndex
     *            the index of the first element to write to the array
     * @param toIndex
     *            the index following the last element to write to the array
     */
    final void toArray(@Nonnull Object[] array, int index, int fromIndex, int toIndex) {
        if (this.left != null) {
            if (fromIndex < index + this.left.getSize()) {
                this.left.toArray(array, index, fromIndex, toIndex);
            }

            index += this.left.getSize();
        }

        if (index < toIndex) {
            if (fromIndex <= index) {
                array[index - fromIndex] = this.value;
            }

            index += 1;
            if (index < toIndex) {
                assert this.right != null;
                this.right.toArray(array, index, fromIndex, toIndex);
            }
        }
    }

}
