package ca.fragag.collections;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.hamcrest.Matcher;
import org.junit.Test;

import ca.fragag.NamedTestObject;

/**
 * Test class for {@link AbstractImmutableTreeList}.
 *
 * @author Francis Gagné
 */
public class AbstractImmutableTreeListSubListTest {

    private static final Object LEFT_OBJECT = new NamedTestObject("left object");
    private static final Object CENTER_OBJECT = new NamedTestObject("center object");
    private static final Object RIGHT_OBJECT = new NamedTestObject("right object");
    private static final Object FOREIGN_OBJECT = new NamedTestObject("foreign object");
    private static final ImmutableTreeNode<Object> LEFT_NODE = new ImmutableTreeNode<>(null, LEFT_OBJECT, null);
    private static final ImmutableTreeNode<Object> RIGHT_NODE = new ImmutableTreeNode<>(null, RIGHT_OBJECT, null);
    private static final ImmutableTreeNode<Object> ROOT_NODE = new ImmutableTreeNode<>(LEFT_NODE, CENTER_OBJECT, RIGHT_NODE);

    private static final ImmutableTreeList<Object> LIST = new ImmutableTreeList<>(ROOT_NODE);
    private static final List<Object> SUB_LIST_1_1 = LIST.subList(1, 1);
    private static final List<Object> SUB_LIST_1_2 = LIST.subList(1, 2);
    private static final List<Object> SUB_LIST_1_3 = LIST.subList(1, 3);

    private static final List<Object> SUB_LIST_WITH_DUPLICATES = ImmutableTreeListFactory.getInstance()
            .create(Arrays.asList(LEFT_OBJECT, CENTER_OBJECT, RIGHT_OBJECT, LEFT_OBJECT, CENTER_OBJECT, RIGHT_OBJECT))
            .subList(1, 5);

    private static final Object[] EMPTY_ARRAY_OF_OBJECTS = new Object[0];

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#containsAll(Collection)} returns <code>false</code> when some of
     * the elements of the specified collection are not elements of the sublist.
     */
    @Test
    public void containsAllFalse() {
        assertThat(SUB_LIST_1_2.containsAll(Arrays.asList(CENTER_OBJECT, RIGHT_OBJECT)), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#containsAll(Collection)} returns <code>true</code> when all of the
     * elements of the specified collection are elements of the sublist.
     */
    @Test
    public void containsAllTrue() {
        assertThat(SUB_LIST_1_3.containsAll(Arrays.asList(CENTER_OBJECT, RIGHT_OBJECT)), is(true));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#contains(Object)} returns <code>false</code> when the specified
     * object is not an element of the sublist.
     */
    @Test
    public void containsFalse() {
        assertThat(SUB_LIST_1_2.contains(FOREIGN_OBJECT), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#contains(Object)} returns <code>true</code> when the specified
     * object is an element of the sublist.
     */
    @Test
    public void containsTrue() {
        assertThat(SUB_LIST_1_2.contains(CENTER_OBJECT), is(true));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#get(int)} throws an {@link IndexOutOfBoundsException} when the
     * <code>index</code> argument is greater than or equal to the sublist's size.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getIndexTooHigh() {
        SUB_LIST_1_1.get(0);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#get(int)} throws an {@link IndexOutOfBoundsException} when the
     * <code>index</code> argument is negative.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getIndexTooLow() {
        SUB_LIST_1_1.get(-1);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#get(int)} returns the element at the specified position.
     */
    @Test
    public void getIndexValid() {
        assertThat(SUB_LIST_1_2.get(0), is(CENTER_OBJECT));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#indexOf(Object)} returns -1 when the specified object is not an
     * element of the sublist.
     */
    @Test
    public void indexOfAbsent() {
        assertThat(SUB_LIST_1_1.indexOf(CENTER_OBJECT), is(-1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#indexOf(Object)} returns the index of the first occurrence of the
     * specified object within the sublist.
     */
    @Test
    public void indexOfPresent() {
        assertThat(SUB_LIST_WITH_DUPLICATES.indexOf(CENTER_OBJECT), is(0));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#isEmpty()} returns <code>false</code> when the sublist is not
     * empty.
     */
    @Test
    public void isEmptyFalse() {
        assertThat(SUB_LIST_1_2.isEmpty(), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#isEmpty()} returns <code>true</code> when the sublist is empty.
     */
    @Test
    public void isEmptyTrue() {
        assertThat(SUB_LIST_1_1.isEmpty(), is(true));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#iterator()} returns an {@link Iterator} that iterates over the
     * sublist's elements.
     */
    @Test
    public void iterator() {
        final Iterator<Object> iterator = SUB_LIST_1_2.iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(CENTER_OBJECT));
        assertThat(iterator.hasNext(), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#lastIndexOf(Object)} returns -1 when the specified object is not an
     * element of the sublist.
     */
    @Test
    public void lastIndexOfAbsent() {
        assertThat(SUB_LIST_1_1.lastIndexOf(CENTER_OBJECT), is(-1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#lastIndexOf(Object)} returns the index of the last occurrence of
     * the specified object within the sublist.
     */
    @Test
    public void lastIndexOfPresent() {
        assertThat(SUB_LIST_WITH_DUPLICATES.lastIndexOf(CENTER_OBJECT), is(3));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#listIterator()} returns a {@link ListIterator} that iterates over
     * the sublist's elements.
     */
    @Test
    public void listIterator() {
        final Iterator<Object> iterator = SUB_LIST_1_2.listIterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(CENTER_OBJECT));
        assertThat(iterator.hasNext(), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#listIterator(int)} returns a {@link ListIterator} that iterates
     * over no elements when the <code>index</code> argument is equal to the sublist's size.
     */
    @Test
    public void listIteratorIntEnd() {
        final Iterator<Object> iterator = SUB_LIST_1_2.listIterator(1);
        assertThat(iterator.hasNext(), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#listIterator(int)} returns a {@link ListIterator} that iterates
     * over all of the sublist's elements when the <code>index</code> argument is 0.
     */
    @Test
    public void listIteratorIntStart() {
        final Iterator<Object> iterator = SUB_LIST_1_2.listIterator(0);
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(CENTER_OBJECT));
        assertThat(iterator.hasNext(), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#size()} returns the size of the sublist.
     */
    @Test
    public void size() {
        assertThat(SUB_LIST_1_2.size(), is(1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#subList(int, int)} returns a sublist that contains the elements
     * contained in the range designated by the <code>fromIndex</code> and <code>toIndex</code> arguments.
     */
    @Test
    public void subListPartialSubListFrom0() {
        final List<Object> subList = SUB_LIST_1_3.subList(0, 1);
        assertThat(subList.size(), is(1));
        assertThat(subList.get(0), is(CENTER_OBJECT));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#subList(int, int)} returns a sublist that contains the elements
     * contained in the range designated by the <code>fromIndex</code> and <code>toIndex</code> arguments.
     */
    @Test
    public void subListPartialSubListFrom1() {
        final List<Object> subList = SUB_LIST_1_3.subList(1, 2);
        assertThat(subList.size(), is(1));
        assertThat(subList.get(0), is(RIGHT_OBJECT));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#subList(int, int)} returns the original instance when the
     * <code>fromIndex</code> and <code>toIndex</code> arguments designate the whole sublist.
     */
    @Test
    public void subListWholeSubList() {
        assertThat(SUB_LIST_1_2.subList(0, 1), is(sameInstance(SUB_LIST_1_2)));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#toArray()} returns an array that contains the sublist's elements.
     */
    @Test
    public void toArray() {
        Object[] array = SUB_LIST_1_2.toArray();
        assertThat(array, is(arrayContaining(Arrays.<Matcher<? super Object>> asList(sameInstance(CENTER_OBJECT)))));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#toArray(Object[])} returns the specified array filled with the
     * sublist's elements when the specified array has the same size as the sublist.
     */
    @Test
    public void toArrayObjectArrayArgumentLargeEnough() {
        Object[] arrayArgument = new Object[1];
        Object[] array = SUB_LIST_1_2.toArray(arrayArgument);
        assertThat(array, is(sameInstance(arrayArgument)));
        assertThat(array, is(arrayContaining(Arrays.<Matcher<? super Object>> asList(sameInstance(CENTER_OBJECT)))));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#toArray(Object[])} returns the specified array filled with the
     * sublist's elements, followed by a <code>null</code> element, when the specified array's size is larger than the sublist's
     * size.
     */
    @Test
    public void toArrayObjectArrayArgumentLarger() {
        Object[] arrayArgument = new Object[2];
        arrayArgument[1] = arrayArgument; // set this array item to test that null is written to it
        Object[] array = SUB_LIST_1_2.toArray(arrayArgument);
        assertThat(array, is(sameInstance(arrayArgument)));
        assertThat(array, is(arrayContaining(Arrays.<Matcher<? super Object>> asList(sameInstance(CENTER_OBJECT), nullValue()))));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList.TreeSubList#toArray(Object[])} returns a new array that contains the sublist's
     * elements when the specified array's size is smaller than the sublist's size.
     */
    @Test
    public void toArrayObjectArrayArgumentTooSmall() {
        Object[] array = SUB_LIST_1_2.toArray(EMPTY_ARRAY_OF_OBJECTS);
        assertThat(array, is(arrayContaining(Arrays.<Matcher<? super Object>> asList(sameInstance(CENTER_OBJECT)))));
    }

}
