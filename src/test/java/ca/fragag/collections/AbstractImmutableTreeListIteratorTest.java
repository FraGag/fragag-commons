package ca.fragag.collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.Test;

import ca.fragag.NamedTestObject;

/**
 * Test class for {@link AbstractImmutableTreeList.TreeListIterator}.
 *
 * @author Francis Gagné
 */
public class AbstractImmutableTreeListIteratorTest {

    private static final Object OBJECT_0 = new NamedTestObject("object 0");
    private static final Object OBJECT_1 = new NamedTestObject("object 1");
    private static final Object OBJECT_2 = new NamedTestObject("object 2");
    private static final Object OBJECT_3 = new NamedTestObject("object 3");
    private static final Object OBJECT_4 = new NamedTestObject("object 4");
    private static final Object OBJECT_5 = new NamedTestObject("object 5");
    private static final Object OBJECT_6 = new NamedTestObject("object 6");

    /*
     * The following nodes construct a tree that looks like this:
     *
     *      0 <- NODE_LL
     *     /
     *    1 <--- NODE_L
     *   / \
     *  /   2 <- NODE_LR
     * 3 <------ NODE_ROOT
     *  \   4 <- NODE_RL
     *   \ /
     *    5 <--- NODE_R
     *     \
     *      6 <- NODE_RR
     */

    private static final ImmutableTreeNode<Object> NODE_LL = new ImmutableTreeNode<>(null, OBJECT_0, null);
    private static final ImmutableTreeNode<Object> NODE_LR = new ImmutableTreeNode<>(null, OBJECT_2, null);
    private static final ImmutableTreeNode<Object> NODE_RL = new ImmutableTreeNode<>(null, OBJECT_4, null);
    private static final ImmutableTreeNode<Object> NODE_RR = new ImmutableTreeNode<>(null, OBJECT_6, null);
    private static final ImmutableTreeNode<Object> NODE_L = new ImmutableTreeNode<>(NODE_LL, OBJECT_1, NODE_LR);
    private static final ImmutableTreeNode<Object> NODE_R = new ImmutableTreeNode<>(NODE_RL, OBJECT_5, NODE_RR);
    private static final ImmutableTreeNode<Object> NODE_ROOT = new ImmutableTreeNode<>(NODE_L, OBJECT_3, NODE_R);

    private static final ImmutableTreeList<Object> EMPTY_LIST = new ImmutableTreeList<>(null);
    private static final ImmutableTreeList<Object> LIST = new ImmutableTreeList<>(NODE_ROOT);

    private static int accessibleObjectsInitialized;
    private static Field pathField, nodesField;

    private static void initializeAccessibleObjects(Class<?> clazz) throws NoSuchFieldException, SecurityException {
        switch (accessibleObjectsInitialized) {
        case 0:
            accessibleObjectsInitialized = 1;

            assertThat(clazz.getName(), is("ca.fragag.collections.AbstractImmutableTreeList$TreeListIterator"));

            pathField = clazz.getDeclaredField("path");
            pathField.setAccessible(true);

            nodesField = clazz.getDeclaredField("nodes");
            nodesField.setAccessible(true);

            accessibleObjectsInitialized = 2;
            break;

        case 1:
            fail("Accessible objects not completely initialized in an earlier test; aborting");
            break; // unreachable

        default:
            break;
        }
    }

    private static void testConstructor(ListIterator<Object> iterator, long expectedPath, ImmutableTreeNode<?>... expectedNodes) {
        try {
            initializeAccessibleObjects(iterator.getClass());
        } catch (Exception e) {
            throw new AssertionError("Failed to initialize accessible objects", e);
        }

        long actualPath;
        try {
            actualPath = pathField.getLong(iterator);
        } catch (Exception e) {
            throw new AssertionError("Failed to get the value of field 'path'", e);
        }

        assertThat(actualPath, is(expectedPath));

        ArrayList<ImmutableTreeNode<Object>> actualNodes;
        try {
            @SuppressWarnings("unchecked")
            ArrayList<ImmutableTreeNode<Object>> actualNodes1 = (ArrayList<ImmutableTreeNode<Object>>) nodesField.get(iterator);
            actualNodes = actualNodes1;
        } catch (Exception e) {
            throw new AssertionError("Failed to get the value of field 'nodes'", e);
        }

        if (expectedNodes.length == 0) {
            assertThat(actualNodes, hasSize(0));
        } else {
            assertThat(actualNodes, contains(expectedNodes));
        }
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#add(Object)} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void add() {
        LIST.listIterator(0).add(null);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#hasNext()} returns <code>false</code> when the iterator is
     * positioned at the end of the list.
     */
    @Test
    public void hasNextFalse() {
        assertThat(LIST.listIterator(7).hasNext(), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#hasNext()} returns <code>true</code> when the iterator is not
     * positioned at the end of the list.
     */
    @Test
    public void hasNextTrue() {
        assertThat(LIST.listIterator(0).hasNext(), is(true));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#hasPrevious()} returns <code>false</code> when the iterator is
     * positioned at the beginning of the list.
     */
    @Test
    public void hasPreviousFalse() {
        assertThat(LIST.listIterator(0).hasPrevious(), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#hasPrevious()} returns <code>true</code> when the iterator is
     * not positioned at the beginning of the list.
     */
    @Test
    public void hasPreviousTrue() {
        assertThat(LIST.listIterator(7).hasPrevious(), is(true));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#next()} returns the element at the iterator's position and
     * increments the iterator's position by 1, and that it throws a {@link NoSuchElementException} when the iterator is positioned
     * at the end of the list.
     */
    @Test
    public void next() {
        final ListIterator<Object> listIterator = LIST.listIterator(0);
        assertThat(listIterator.next(), is(OBJECT_0));
        assertThat(listIterator.nextIndex(), is(1));
        assertThat(listIterator.next(), is(OBJECT_1));
        assertThat(listIterator.nextIndex(), is(2));
        assertThat(listIterator.next(), is(OBJECT_2));
        assertThat(listIterator.nextIndex(), is(3));
        assertThat(listIterator.next(), is(OBJECT_3));
        assertThat(listIterator.nextIndex(), is(4));
        assertThat(listIterator.next(), is(OBJECT_4));
        assertThat(listIterator.nextIndex(), is(5));
        assertThat(listIterator.next(), is(OBJECT_5));
        assertThat(listIterator.nextIndex(), is(6));
        assertThat(listIterator.next(), is(OBJECT_6));
        assertThat(listIterator.nextIndex(), is(7));
        try {
            listIterator.next();
            fail("listIterator.next() should have thrown a NoSuchElementException at the end of the list");
        } catch (NoSuchElementException e) {
            // Exception is expected
        }
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#nextIndex()} returns the index of the element that would be
     * returned by the next call to {@link AbstractImmutableTreeList.TreeListIterator#next()}.
     */
    @Test
    public void nextIndex() {
        assertThat(LIST.listIterator(3).nextIndex(), is(3));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#next()} decrements the iterator's position by 1 and returns
     * the element at the iterator's (decremented) position, and that it throws a {@link NoSuchElementException} when the iterator
     * is positioned at the beginning of the list.
     */
    @Test
    public void previous() {
        final ListIterator<Object> listIterator = LIST.listIterator(7);
        assertThat(listIterator.previous(), is(OBJECT_6));
        assertThat(listIterator.previousIndex(), is(5));
        assertThat(listIterator.previous(), is(OBJECT_5));
        assertThat(listIterator.previousIndex(), is(4));
        assertThat(listIterator.previous(), is(OBJECT_4));
        assertThat(listIterator.previousIndex(), is(3));
        assertThat(listIterator.previous(), is(OBJECT_3));
        assertThat(listIterator.previousIndex(), is(2));
        assertThat(listIterator.previous(), is(OBJECT_2));
        assertThat(listIterator.previousIndex(), is(1));
        assertThat(listIterator.previous(), is(OBJECT_1));
        assertThat(listIterator.previousIndex(), is(0));
        assertThat(listIterator.previous(), is(OBJECT_0));
        assertThat(listIterator.previousIndex(), is(-1));
        try {
            listIterator.previous();
            fail("listIterator.previous() should have thrown a NoSuchElementException at the end of the list");
        } catch (NoSuchElementException e) {
            // Exception is expected
        }
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#previousIndex()} returns the index of the element that would
     * be returned by the next call to {@link AbstractImmutableTreeList.TreeListIterator#previous()}.
     */
    @Test
    public void previousIndex() {
        assertThat(LIST.listIterator(3).previousIndex(), is(2));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#remove()} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void remove() {
        LIST.listIterator(0).remove();
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#set(Object)} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void set() {
        LIST.listIterator(0).set(null);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#TreeListIterator(int, int, int)} initializes a
     * {@link AbstractImmutableTreeList.TreeListIterator} correctly for a list of 7 elements initially positioned on the first
     * element.
     */
    @Test
    public void treeListIterator0() {
        testConstructor(LIST.listIterator(0), 0L, NODE_ROOT, NODE_L, NODE_LL);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#TreeListIterator(int, int, int)} initializes a
     * {@link AbstractImmutableTreeList.TreeListIterator} correctly for a list of 7 elements initially positioned on the second
     * element.
     */
    @Test
    public void treeListIterator1() {
        testConstructor(LIST.listIterator(1), 0L, NODE_ROOT, NODE_L);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#TreeListIterator(int, int, int)} initializes a
     * {@link AbstractImmutableTreeList.TreeListIterator} correctly for a list of 7 elements initially positioned on the third
     * element.
     */
    @Test
    public void treeListIterator2() {
        testConstructor(LIST.listIterator(2), 1L, NODE_ROOT, NODE_L, NODE_LR);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#TreeListIterator(int, int, int)} initializes a
     * {@link AbstractImmutableTreeList.TreeListIterator} correctly for a list of 7 elements initially positioned on the fourth
     * element.
     */
    @Test
    public void treeListIterator3() {
        testConstructor(LIST.listIterator(3), 0L, NODE_ROOT);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#TreeListIterator(int, int, int)} initializes a
     * {@link AbstractImmutableTreeList.TreeListIterator} correctly for a list of 7 elements initially positioned on the fifth
     * element.
     */
    @Test
    public void treeListIterator4() {
        testConstructor(LIST.listIterator(4), 2L, NODE_ROOT, NODE_R, NODE_RL);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#TreeListIterator(int, int, int)} initializes a
     * {@link AbstractImmutableTreeList.TreeListIterator} correctly for a list of 7 elements initially positioned on the sixth
     * element.
     */
    @Test
    public void treeListIterator5() {
        testConstructor(LIST.listIterator(5), 1L, NODE_ROOT, NODE_R);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#TreeListIterator(int, int, int)} initializes a
     * {@link AbstractImmutableTreeList.TreeListIterator} correctly for a list of 7 elements initially positioned on the last
     * element.
     */
    @Test
    public void treeListIterator6() {
        testConstructor(LIST.listIterator(6), 3L, NODE_ROOT, NODE_R, NODE_RR);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#TreeListIterator(int, int, int)} initializes a
     * {@link AbstractImmutableTreeList.TreeListIterator} correctly for a list of 7 elements initially positioned after the last
     * element.
     */
    @Test
    public void treeListIterator7() {
        testConstructor(LIST.listIterator(7), 0L);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeListIterator#TreeListIterator(int, int, int)} initializes a
     * {@link AbstractImmutableTreeList.TreeListIterator} correctly for an empty list.
     */
    @Test
    public void treeListIteratorNull() {
        testConstructor(EMPTY_LIST.listIterator(0), 0L);
    }

}
