package ca.fragag.collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import ca.fragag.NamedTestObject;

/**
 * Test class for {@link AbstractImmutableTreeNode} (via {@link ImmutableTreeNode}).
 *
 * @author Francis Gagné
 */
public class AbstractImmutableTreeNodeTest {

    private static final Object LEFT_OBJECT = new NamedTestObject("left object");
    private static final Object CENTER_OBJECT = new NamedTestObject("center object");
    private static final Object RIGHT_OBJECT = new NamedTestObject("right object");
    private static final Object FOREIGN_OBJECT = new NamedTestObject("foreign object");
    private static final ImmutableTreeNode<Object> LEFT_NODE = new ImmutableTreeNode<>(null, LEFT_OBJECT, null);
    private static final ImmutableTreeNode<Object> RIGHT_NODE = new ImmutableTreeNode<>(null, RIGHT_OBJECT, null);
    private static final ImmutableTreeNode<Object> ROOT_NODE = new ImmutableTreeNode<>(LEFT_NODE, CENTER_OBJECT, RIGHT_NODE);
    private static final ImmutableTreeNode<Object> NODE_WITH_DUPLICATES = new ImmutableTreeNode<>(LEFT_NODE, CENTER_OBJECT,
            LEFT_NODE);

    /**
     * Asserts that
     * {@link AbstractImmutableTreeNode#AbstractImmutableTreeNode(AbstractImmutableTreeNode, Object, AbstractImmutableTreeNode)}
     * correctly initializes an {@link AbstractImmutableTreeNode}.
     */
    @Test
    public void abstractImmutableTreeNode() {
        assertThat(LEFT_NODE.getLeft(), is(nullValue()));
        assertThat(LEFT_NODE.getValue(), is(LEFT_OBJECT));
        assertThat(LEFT_NODE.getRight(), is(nullValue()));
        assertThat(LEFT_NODE.getSize(), is(1));

        assertThat(RIGHT_NODE.getLeft(), is(nullValue()));
        assertThat(RIGHT_NODE.getValue(), is(RIGHT_OBJECT));
        assertThat(RIGHT_NODE.getRight(), is(nullValue()));
        assertThat(RIGHT_NODE.getSize(), is(1));

        assertThat(ROOT_NODE.getLeft(), is(LEFT_NODE));
        assertThat(ROOT_NODE.getValue(), is(CENTER_OBJECT));
        assertThat(ROOT_NODE.getRight(), is(RIGHT_NODE));
        assertThat(ROOT_NODE.getSize(), is(3));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#get(int)} returns the correct element by selecting the element on the root
     * node.
     */
    @Test
    public void getCenter() {
        assertThat(ROOT_NODE.get(1), is(CENTER_OBJECT));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#get(int)} returns the correct element by navigating to the left node from the
     * root node.
     */
    @Test
    public void getLeft() {
        assertThat(ROOT_NODE.get(0), is(LEFT_OBJECT));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#get(int)} returns the correct element by navigating to the right node from the
     * root node.
     */
    @Test
    public void getRight() {
        assertThat(ROOT_NODE.get(2), is(RIGHT_OBJECT));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#indexOf(Object, int)} returns the index of an element found at the root node.
     */
    @Test
    public void indexOfObjectIntCenter() {
        assertThat(ROOT_NODE.indexOf(CENTER_OBJECT, 100), is(101));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#indexOf(Object, int)} returns the index of the first occurrence of an element
     * in the tree.
     */
    @Test
    public void indexOfObjectIntDuplicates() {
        assertThat(NODE_WITH_DUPLICATES.indexOf(LEFT_OBJECT, 100), is(100));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#indexOf(Object, int)} returns -1 for an element not found in the tree.
     */
    @Test
    public void indexOfObjectIntForeign() {
        assertThat(ROOT_NODE.indexOf(FOREIGN_OBJECT, 100), is(-1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#indexOf(Object, int, int, int)} returns the index of an element found at the
     * root node.
     */
    @Test
    public void indexOfObjectIntIntIntCenter() {
        assertThat(ROOT_NODE.indexOf(CENTER_OBJECT, 100, 100, 103), is(1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#indexOf(Object, int, int, int)} returns -1 for a subset of the tree that
     * excludes the searched element.
     */
    @Test
    public void indexOfObjectIntIntIntCenterWithCenterRightExcluded() {
        assertThat(ROOT_NODE.indexOf(CENTER_OBJECT, 100, 100, 101), is(-1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#indexOf(Object, int, int, int)} returns -1 for a subset of the tree that
     * excludes the searched element.
     */
    @Test
    public void indexOfObjectIntIntIntCenterWithLeftCenterExcluded() {
        assertThat(ROOT_NODE.indexOf(CENTER_OBJECT, 100, 102, 103), is(-1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#indexOf(Object, int, int, int)} returns the index of an element found at the
     * root node for a subset of the tree.
     */
    @Test
    public void indexOfObjectIntIntIntCenterWithLeftExcluded() {
        assertThat(ROOT_NODE.indexOf(CENTER_OBJECT, 100, 101, 103), is(0));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#indexOf(Object, int, int, int)} returns the index of an element found at the
     * root node for a subset of the tree.
     */
    @Test
    public void indexOfObjectIntIntIntCenterWithRightExcluded() {
        assertThat(ROOT_NODE.indexOf(CENTER_OBJECT, 100, 100, 102), is(1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#indexOf(Object, int, int, int)} returns the index of the first occurrence of an
     * element in the tree.
     */
    @Test
    public void indexOfObjectIntIntIntDuplicates() {
        assertThat(NODE_WITH_DUPLICATES.indexOf(LEFT_OBJECT, 100, 100, 103), is(0));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#indexOf(Object, int, int, int)} returns the index of an element found by
     * navigating the left subtree.
     */
    @Test
    public void indexOfObjectIntIntIntLeft() {
        assertThat(ROOT_NODE.indexOf(LEFT_OBJECT, 100, 100, 103), is(0));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#indexOf(Object, int, int, int)} returns the index of an element found by
     * navigating the right subtree.
     */
    @Test
    public void indexOfObjectIntIntIntRight() {
        assertThat(ROOT_NODE.indexOf(RIGHT_OBJECT, 100, 100, 103), is(2));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#indexOf(Object, int)} returns the index of an element found by navigating the
     * left subtree.
     */
    @Test
    public void indexOfObjectIntLeft() {
        assertThat(ROOT_NODE.indexOf(LEFT_OBJECT, 100), is(100));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#indexOf(Object, int)} returns the index of an element found by navigating the
     * right subtree.
     */
    @Test
    public void indexOfObjectIntRight() {
        assertThat(ROOT_NODE.indexOf(RIGHT_OBJECT, 100), is(102));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#lastIndexOf(Object, int)} returns the index of an element found at the root
     * node.
     */
    @Test
    public void lastIndexOfObjectIntCenter() {
        assertThat(ROOT_NODE.lastIndexOf(CENTER_OBJECT, 103), is(101));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#lastIndexOf(Object, int)} returns the index of the last occurrence of an
     * element in the tree.
     */
    @Test
    public void lastIndexOfObjectIntDuplicates() {
        assertThat(NODE_WITH_DUPLICATES.lastIndexOf(LEFT_OBJECT, 103), is(102));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#lastIndexOf(Object, int)} returns -1 for an element not found in the tree.
     */
    @Test
    public void lastIndexOfObjectIntForeign() {
        assertThat(ROOT_NODE.lastIndexOf(FOREIGN_OBJECT, 103), is(-1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#lastIndexOf(Object, int, int, int)} returns the index of an element found at
     * the root node.
     */
    @Test
    public void lastIndexOfObjectIntIntIntCenter() {
        assertThat(ROOT_NODE.lastIndexOf(CENTER_OBJECT, 103, 100, 103), is(1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#lastIndexOf(Object, int, int, int)} returns -1 for a subset of the tree that
     * excludes the searched element.
     */
    @Test
    public void lastIndexOfObjectIntIntIntCenterWithCenterRightExcluded() {
        assertThat(ROOT_NODE.lastIndexOf(CENTER_OBJECT, 103, 100, 101), is(-1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#lastIndexOf(Object, int, int, int)} returns -1 for a subset of the tree that
     * excludes the searched element.
     */
    @Test
    public void lastIndexOfObjectIntIntIntCenterWithLeftCenterExcluded() {
        assertThat(ROOT_NODE.lastIndexOf(CENTER_OBJECT, 103, 102, 103), is(-1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#lastIndexOf(Object, int, int, int)} returns the index of an element found at
     * the root node for a subset of the tree.
     */
    @Test
    public void lastIndexOfObjectIntIntIntCenterWithLeftExcluded() {
        assertThat(ROOT_NODE.lastIndexOf(CENTER_OBJECT, 103, 101, 103), is(0));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#lastIndexOf(Object, int, int, int)} returns the index of an element found at
     * the root node for a subset of the tree.
     */
    @Test
    public void lastIndexOfObjectIntIntIntCenterWithRightExcluded() {
        assertThat(ROOT_NODE.lastIndexOf(CENTER_OBJECT, 103, 100, 102), is(1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#lastIndexOf(Object, int, int, int)} returns the index of the last occurrence of
     * an element in the tree.
     */
    @Test
    public void lastIndexOfObjectIntIntIntDuplicates() {
        assertThat(NODE_WITH_DUPLICATES.lastIndexOf(LEFT_OBJECT, 103, 100, 103), is(2));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#lastIndexOf(Object, int, int, int)} returns the index of an element found by
     * navigating the left subtree.
     */
    @Test
    public void lastIndexOfObjectIntIntIntLeft() {
        assertThat(ROOT_NODE.lastIndexOf(LEFT_OBJECT, 103, 100, 103), is(0));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#lastIndexOf(Object, int, int, int)} returns the index of an element found by
     * navigating the right subtree.
     */
    @Test
    public void lastIndexOfObjectIntIntIntRight() {
        assertThat(ROOT_NODE.lastIndexOf(RIGHT_OBJECT, 103, 100, 103), is(2));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#lastIndexOf(Object, int)} returns the index of an element found by navigating
     * the left subtree.
     */
    @Test
    public void lastIndexOfObjectIntLeft() {
        assertThat(ROOT_NODE.lastIndexOf(LEFT_OBJECT, 103), is(100));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#lastIndexOf(Object, int)} returns the index of an element found by navigating
     * the right subtree.
     */
    @Test
    public void lastIndexOfObjectIntRight() {
        assertThat(ROOT_NODE.lastIndexOf(RIGHT_OBJECT, 103), is(102));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#toArray(Object[], int)} fills the specified array, starting at the specified
     * index, with the elements of the node and of its children.
     */
    @Test
    public void toArrayObjectArrayInt() {
        Object[] array = new Object[] { FOREIGN_OBJECT, FOREIGN_OBJECT, FOREIGN_OBJECT, FOREIGN_OBJECT, FOREIGN_OBJECT,
                FOREIGN_OBJECT, FOREIGN_OBJECT };

        ROOT_NODE.toArray(array, 2);

        assertThat(array[0], is(FOREIGN_OBJECT));
        assertThat(array[1], is(FOREIGN_OBJECT));
        assertThat(array[2], is(LEFT_OBJECT));
        assertThat(array[3], is(CENTER_OBJECT));
        assertThat(array[4], is(RIGHT_OBJECT));
        assertThat(array[5], is(FOREIGN_OBJECT));
        assertThat(array[6], is(FOREIGN_OBJECT));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#toArray(Object[], int, int, int)} fills the specified array with the element at
     * the root node.
     */
    @Test
    public void toArrayObjectArrayIntIntIntCenter() {
        Object[] array = new Object[] { FOREIGN_OBJECT };

        ROOT_NODE.toArray(array, 100, 101, 102);

        assertThat(array[0], is(CENTER_OBJECT));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#toArray(Object[], int, int, int)} fills the specified array with the element at
     * the left subnode of the root node.
     */
    @Test
    public void toArrayObjectArrayIntIntIntLeft() {
        Object[] array = new Object[] { FOREIGN_OBJECT };

        ROOT_NODE.toArray(array, 100, 100, 101);

        assertThat(array[0], is(LEFT_OBJECT));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeNode#toArray(Object[], int, int, int)} fills the specified array with the element at
     * the right subnode of the root node.
     */
    @Test
    public void toArrayObjectArrayIntIntIntRight() {
        Object[] array = new Object[] { FOREIGN_OBJECT };

        ROOT_NODE.toArray(array, 100, 102, 103);

        assertThat(array[0], is(RIGHT_OBJECT));
    }

}
