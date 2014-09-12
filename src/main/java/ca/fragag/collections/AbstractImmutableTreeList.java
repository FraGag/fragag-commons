package ca.fragag.collections;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * An immutable list that is structured as a binary tree.
 *
 * @param <E>
 *            the type of the elements in the list
 * @param <N>
 *            the type of node in the list
 *
 * @author Francis Gagné
 */
@Immutable
public abstract class AbstractImmutableTreeList<E, N extends AbstractImmutableTreeNode<E, N>> extends UnmodifiableList<E> {

    private class TreeListIterator implements ListIterator<E> {

        private long path;
        private int index;
        private final int fromIndex;
        private final int toIndex;
        private final ArrayList<N> nodes;

        /**
         * Initializes a new TreeListIterator.
         *
         * @param index
         *            the index at which iteration starts
         * @param fromIndex
         *            the index of the first element to iterate through
         * @param toIndex
         *            the index following the last element to iterate through
         */
        public TreeListIterator(int index, int fromIndex, int toIndex) {
            this.path = 0;
            this.index = index;
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
            this.nodes = new ArrayList<>(32);

            // Find the path to the element at the specified starting index.
            N node = AbstractImmutableTreeList.this.getRoot();
            if (node != null) {
                for (;;) {
                    this.nodes.add(node);

                    final N left = node.getLeft();
                    final int leftSize = left == null ? 0 : left.getSize();

                    // If index == leftSize, then we have reached our target.
                    if (index == leftSize) {
                        break;
                    }

                    this.path <<= 1;
                    if (index < leftSize) {
                        // Move to the left node and keep searching.
                        assert left != null;
                        node = left;
                    } else {
                        // Move to the right node and keep searching.
                        this.path |= 1;
                        index -= leftSize + 1;
                        node = node.getRight();

                        // If there is no right node, then the index is beyond the end of the list.
                        if (node == null) {
                            this.nodes.clear();
                            this.path = 0;
                            return;
                        }
                    }
                }
            }
        }

        @Override
        public void add(E e) {
            throw this.modificationUnsupported();
        }

        @Override
        public boolean hasNext() {
            return this.index < this.toIndex;
        }

        @Override
        public boolean hasPrevious() {
            return this.index > this.fromIndex;
        }

        @Override
        public E next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException("No element at index " + this.nextIndex() + ".");
            }

            this.index += 1;

            N node = this.nodes.get(this.nodes.size() - 1);
            E value = node.getValue();

            // Find the path to the next element.
            // If there is a node to the right, move to that node and find the leftmost child node.
            final N right = node.getRight();
            if (right != null) {
                this.path <<= 1;
                this.path |= 1;
                this.nodes.add(right);

                node = right;
                N left;
                while ((left = node.getLeft()) != null) {
                    this.path <<= 1;
                    this.nodes.add(left);
                    node = left;
                }
            } else {
                boolean wasOnLeft;
                do {
                    // Otherwise, go back one level.
                    this.nodes.remove(this.nodes.size() - 1);

                    // If this.nodes is empty, we have reached the end of the list.
                    if (this.nodes.isEmpty()) {
                        break;
                    }

                    node = this.nodes.get(this.nodes.size() - 1);

                    // If the previous node was on the left of its parent, stop on the parent.
                    wasOnLeft = (this.path & 1) == 0;
                    this.path >>>= 1;
                } while (!wasOnLeft);
            }

            return value;
        }

        @Override
        public int nextIndex() {
            return this.index - this.fromIndex;
        }

        @Override
        public E previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException("No element at index " + this.previousIndex() + ".");
            }

            this.index -= 1;

            // If this.nodes is empty (which is the case when the iterator is positioned at the end of the list), find the rightmost child node.
            N node;
            if (this.nodes.isEmpty()) {
                node = AbstractImmutableTreeList.this.getRoot();
                assert node != null;
                this.nodes.add(node);

                N right;
                while ((right = node.getRight()) != null) {
                    this.path <<= 1;
                    this.path |= 1;
                    this.nodes.add(right);
                    node = right;
                }
            } else {
                node = this.nodes.get(this.nodes.size() - 1);

                // Find the path to the previous element.
                // If there is a node to the left, move to that node and find the rightmost child node.
                final N left = node.getLeft();
                if (left != null) {
                    this.path <<= 1;
                    this.nodes.add(left);

                    node = left;
                    N right;
                    while ((right = node.getRight()) != null) {
                        this.path <<= 1;
                        this.path |= 1;
                        this.nodes.add(right);
                        node = right;
                    }
                } else {
                    boolean wasOnRight;
                    do {
                        this.nodes.remove(this.nodes.size() - 1);
                        node = this.nodes.get(this.nodes.size() - 1);

                        // If the previous node was on the right of its parent, stop on the parent.
                        wasOnRight = (this.path & 1) != 0;
                        this.path >>>= 1;
                    } while (!wasOnRight);
                }
            }

            return node.getValue();
        }

        @Override
        public int previousIndex() {
            return this.index - this.fromIndex - 1;
        }

        @Override
        public void remove() {
            throw this.modificationUnsupported();
        }

        @Override
        public void set(E e) {
            throw this.modificationUnsupported();
        }

        private UnsupportedOperationException modificationUnsupported() {
            return new UnsupportedOperationException("this iterator does not support modification operations");
        }

    }

    @Immutable
    private class TreeSubList extends UnmodifiableList<E> {

        private final int fromIndex;
        private final int toIndex;

        TreeSubList(int fromIndex, int toIndex) {
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
        }

        @Override
        public boolean contains(Object o) {
            return this.indexOf(o) >= 0;
        }

        @Override
        public boolean containsAll(@Nonnull Collection<?> c) {
            for (Object e : c) {
                if (!this.contains(e)) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public E get(int index) {
            this.checkInterval(index, 0, this.size() - 1);
            return AbstractImmutableTreeList.this.get(index + this.fromIndex);
        }

        @Override
        public int indexOf(Object object) {
            return this.getRoot().indexOf(object, 0, this.fromIndex, this.toIndex);
        }

        @Override
        public boolean isEmpty() {
            return this.fromIndex == this.toIndex;
        }

        @Nonnull
        @Override
        public Iterator<E> iterator() {
            return AbstractImmutableTreeList.this.new TreeListIterator(this.fromIndex, this.fromIndex, this.toIndex);
        }

        @Override
        public int lastIndexOf(Object object) {
            return this.getRoot().lastIndexOf(object, this.getRoot().getSize(), this.fromIndex, this.toIndex);
        }

        @Nonnull
        @Override
        public ListIterator<E> listIterator() {
            return AbstractImmutableTreeList.this.new TreeListIterator(this.fromIndex, this.fromIndex, this.toIndex);
        }

        @Nonnull
        @Override
        public ListIterator<E> listIterator(int index) {
            return AbstractImmutableTreeList.this.new TreeListIterator(this.fromIndex + index, this.fromIndex, this.toIndex);
        }

        @Override
        public int size() {
            return this.toIndex - this.fromIndex;
        }

        @Nonnull
        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            this.checkInterval(fromIndex, 0, this.size());
            this.checkInterval(toIndex, 0, this.size());

            if (fromIndex == 0 && toIndex == this.size()) {
                return this;
            }

            return AbstractImmutableTreeList.this.new TreeSubList(this.fromIndex + fromIndex, this.fromIndex + toIndex);
        }

        @Nonnull
        @Override
        public Object[] toArray() {
            final Object[] array = new Object[this.size()];
            this.getRoot().toArray(array, 0, this.fromIndex, this.toIndex);
            return array;
        }

        @Nonnull
        @Override
        public <T> T[] toArray(@Nonnull T[] array) {
            final int size = this.size();
            if (array.length < size) {
                @SuppressWarnings("unchecked")
                final T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), size);
                array = newArray;
            }

            this.getRoot().toArray(array, 0, this.fromIndex, this.toIndex);

            if (array.length > size) {
                array[size] = null;
            }

            return array;
        }

        private final void checkInterval(int index, int startIndex, int endIndex) {
            AbstractImmutableTreeList.checkInterval(index, startIndex, endIndex, this.size());
        }

        @Nonnull
        private final N getRoot() {
            // AbstractImmutableTreeList.subList() will never create a TreeSubList for an empty list.
            // Instead, it will return the list itself.
            final N root = AbstractImmutableTreeList.this.getRoot();
            assert root != null;
            return root;
        }

    }

    static void checkInterval(int index, int startIndex, int endIndex, int size) {
        if (index < startIndex || index > endIndex) {
            throw new IndexOutOfBoundsException("Invalid index: " + index + ", size=" + size);
        }
    }

    @CheckForNull
    private final N root;

    /**
     * Initializes a new AbstractImmutableTreeList.
     *
     * @param root
     *            the list's root node
     */
    protected AbstractImmutableTreeList(@CheckForNull N root) {
        this.root = root;
    }

    @Override
    public final boolean contains(Object o) {
        return this.indexOf(o) >= 0;
    }

    @Override
    public final boolean containsAll(@Nonnull Collection<?> c) {
        for (Object e : c) {
            if (!this.contains(e)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public final E get(int index) {
        this.checkInterval(index, 0, this.size() - 1);
        assert this.root != null;
        return this.root.get(index);
    }

    @Override
    public final int indexOf(Object object) {
        if (this.root == null) {
            return -1;
        }

        return this.root.indexOf(object, 0);
    }

    @Override
    public final boolean isEmpty() {
        return this.root == null;
    }

    @Nonnull
    @Override
    public final Iterator<E> iterator() {
        return this.new TreeListIterator(0, 0, this.size());
    }

    @Override
    public final int lastIndexOf(Object object) {
        if (this.root == null) {
            return -1;
        }

        return this.root.lastIndexOf(object, this.root.getSize());
    }

    @Nonnull
    @Override
    public final ListIterator<E> listIterator() {
        return this.new TreeListIterator(0, 0, this.size());
    }

    @Nonnull
    @Override
    public final ListIterator<E> listIterator(int index) {
        this.checkInterval(index, 0, this.size());
        return this.new TreeListIterator(index, 0, this.size());
    }

    @Override
    public final int size() {
        return this.root == null ? 0 : this.root.getSize();
    }

    @Nonnull
    @Override
    public final List<E> subList(int fromIndex, int toIndex) {
        this.checkInterval(fromIndex, 0, this.size());
        this.checkInterval(toIndex, 0, this.size());

        if (fromIndex == 0 && toIndex == this.size()) {
            return this;
        }

        return new TreeSubList(fromIndex, toIndex);
    }

    @Nonnull
    @Override
    public final Object[] toArray() {
        final Object[] array = new Object[this.size()];
        if (this.root != null) {
            this.root.toArray(array, 0);
        }

        return array;
    }

    @Nonnull
    @Override
    public final <T> T[] toArray(@Nonnull T[] array) {
        final int size = this.size();
        if (array.length < size) {
            @SuppressWarnings("unchecked")
            final T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), size);
            array = newArray;
        }

        if (this.root != null) {
            this.root.toArray(array, 0);
        }

        if (array.length > size) {
            array[size] = null;
        }

        return array;
    }

    /**
     * Gets the root node of this list.
     *
     * @return the root node.
     */
    @CheckForNull
    protected N getRoot() {
        return this.root;
    }

    private final void checkInterval(int index, int startIndex, int endIndex) {
        checkInterval(index, startIndex, endIndex, this.size());
    }

}
