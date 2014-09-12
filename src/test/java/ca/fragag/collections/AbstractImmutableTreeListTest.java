package ca.fragag.collections;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.hamcrest.Matcher;
import org.junit.Test;

/**
 * Test class for {@link AbstractImmutableTreeList} (via {@link ImmutableTreeList}).
 *
 * @author Francis Gagné
 */
public class AbstractImmutableTreeListTest {

    private static final Object A_SAMPLE_OBJECT = new Object();
    private static final Object ANOTHER_SAMPLE_OBJECT = new Object();
    private static final Object[] EMPTY_ARRAY_OF_OBJECTS = new Object[0];
    private static final ImmutableTreeNode<Object> LEAF_NODE_OF_NULL = new ImmutableTreeNode<>(null, null, null);
    private static final ImmutableTreeNode<Object> LEAF_NODE_OF_OBJECT = new ImmutableTreeNode<>(null, A_SAMPLE_OBJECT, null);
    private static final ImmutableTreeList<Object> LIST_OF_A_SINGLE_NULL = new ImmutableTreeList<>(LEAF_NODE_OF_NULL);
    private static final ImmutableTreeList<Object> LIST_OF_A_SINGLE_OBJECT = new ImmutableTreeList<>(LEAF_NODE_OF_OBJECT);
    private static final ImmutableTreeList<Object> LIST_WITH_DUPLICATES = new ImmutableTreeList<>(new ImmutableTreeNode<>(
            LEAF_NODE_OF_NULL, null, LEAF_NODE_OF_OBJECT));

    /**
     * Asserts that {@link AbstractImmutableTreeList#AbstractImmutableTreeList(AbstractImmutableTreeNode)} correctly initializes an
     * {@link AbstractImmutableTreeList}.
     */
    @Test
    public void abstractImmutableTreeList() {
        assertThat(LIST_OF_A_SINGLE_NULL.getRoot(), is(sameInstance(LEAF_NODE_OF_NULL)));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#containsAll(Collection)} returns <code>false</code> when some of the elements
     * of the specified collection are not elements of the list.
     */
    @Test
    public void containsAllFalse() {
        ImmutableTreeList<Object> list = new ImmutableTreeList<>(new ImmutableTreeNode<>(null, A_SAMPLE_OBJECT, null));
        assertThat(list.containsAll(Arrays.asList(A_SAMPLE_OBJECT, ANOTHER_SAMPLE_OBJECT)), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#containsAll(Collection)} returns <code>true</code> when all of the elements of
     * the specified collection are elements of the list.
     */
    @Test
    public void containsAllTrue() {
        ImmutableTreeList<Object> list = new ImmutableTreeList<>(new ImmutableTreeNode<>(null, A_SAMPLE_OBJECT,
                new ImmutableTreeNode<>(null, ANOTHER_SAMPLE_OBJECT, null)));
        assertThat(list.containsAll(Arrays.asList(A_SAMPLE_OBJECT, ANOTHER_SAMPLE_OBJECT)), is(true));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#contains(Object)} returns <code>true</code> when the specified object is not an
     * element of the list.
     */
    @Test
    public void containsFalse() {
        assertThat(ImmutableTreeList.getEmpty().contains(null), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#contains(Object)} returns <code>true</code> when the specified object is an
     * element of the list.
     */
    @Test
    public void containsTrue() {
        assertThat(LIST_OF_A_SINGLE_NULL.contains(null), is(true));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#get(int)} throws an {@link IndexOutOfBoundsException} when the
     * <code>index</code> argument is greater than or equal to the sublist's size.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getIndexTooHigh() {
        ImmutableTreeList.getEmpty().get(0);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#get(int)} throws an {@link IndexOutOfBoundsException} when the
     * <code>index</code> argument is negative.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getIndexTooLow() {
        ImmutableTreeList.getEmpty().get(-1);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#get(int)} returns the element at the specified position.
     */
    @Test
    public void getIndexValid() {
        assertThat(LIST_OF_A_SINGLE_NULL.get(0), is(nullValue()));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#indexOf(Object)} returns -1 when the specified object is not found in the list.
     */
    @Test
    public void indexOfEmpty() {
        assertThat(ImmutableTreeList.getEmpty().indexOf(null), is(-1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#indexOf(Object)} returns the index of the first occurrence of the specified
     * object within the list when it is found in the list.
     */
    @Test
    public void indexOfNonEmpty() {
        assertThat(LIST_WITH_DUPLICATES.indexOf(null), is(0));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#isEmpty()} returns <code>false</code> when the list is not empty.
     */
    @Test
    public void isEmptyFalse() {
        assertThat(LIST_OF_A_SINGLE_NULL.isEmpty(), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#isEmpty()} returns <code>true</code> when the list is empty.
     */
    @Test
    public void isEmptyTrue() {
        assertThat(ImmutableTreeList.getEmpty().isEmpty(), is(true));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#iterator()} returns an {@link Iterator} that iterates over the list's elements.
     */
    @Test
    public void iterator() {
        final Iterator<Object> iterator = LIST_OF_A_SINGLE_OBJECT.iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(sameInstance(A_SAMPLE_OBJECT)));
        assertThat(iterator.hasNext(), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#lastIndexOf(Object)} returns -1 when the specified object is not found in the
     * list.
     */
    @Test
    public void lastIndexOfEmpty() {
        assertThat(ImmutableTreeList.getEmpty().lastIndexOf(null), is(-1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#lastIndexOf(Object)} returns the index of the last occurrence of the specified
     * object within the list when it is found in the list.
     */
    @Test
    public void lastIndexOfNonEmpty() {
        assertThat(LIST_WITH_DUPLICATES.lastIndexOf(null), is(1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#listIterator()} returns a {@link ListIterator} that iterates over the list's
     * elements.
     */
    @Test
    public void listIterator() {
        final Iterator<Object> iterator = LIST_OF_A_SINGLE_OBJECT.listIterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(sameInstance(A_SAMPLE_OBJECT)));
        assertThat(iterator.hasNext(), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#listIterator(int)} returns a {@link ListIterator} that iterates over no
     * elements when the <code>index</code> argument is equal to the sublist's size.
     */
    @Test
    public void listIteratorIntEnd() {
        final Iterator<Object> iterator = LIST_OF_A_SINGLE_OBJECT.listIterator(1);
        assertThat(iterator.hasNext(), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#listIterator(int)} returns a {@link ListIterator} that iterates over all of the
     * list's elements when the <code>index</code> argument is 0.
     */
    @Test
    public void listIteratorIntStart() {
        final Iterator<Object> iterator = LIST_OF_A_SINGLE_OBJECT.listIterator(0);
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(sameInstance(A_SAMPLE_OBJECT)));
        assertThat(iterator.hasNext(), is(false));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#size()} returns 0 for an empty list.
     */
    @Test
    public void sizeEmpty() {
        assertThat(ImmutableTreeList.getEmpty().size(), is(0));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#size()} returns the list's size for a non-empty list.
     */
    @Test
    public void sizeNonEmpty() {
        assertThat(LIST_OF_A_SINGLE_NULL.size(), is(1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#subList(int, int)} returns a sublist.
     */
    @Test
    public void subListNotWholeList() {
        assertThat(LIST_OF_A_SINGLE_NULL.subList(0, 0), hasSize(0));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#subList(int, int)} returns the original instance when the
     * <code>fromIndex</code> and <code>toIndex</code> arguments designate the whole sublist.
     */
    @Test
    public void subListWholeList() {
        assertThat(LIST_OF_A_SINGLE_NULL.subList(0, 1), is(sameInstance((List<Object>) LIST_OF_A_SINGLE_NULL)));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#toArray()} returns an array that contains the list's elements.
     */
    @Test
    public void toArray() {
        Object[] array = LIST_OF_A_SINGLE_NULL.toArray();
        assertThat(array, is(arrayContaining(Arrays.<Matcher<? super Object>> asList(nullValue()))));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#toArray()} returns an empty array for an empty list.
     */
    @Test
    public void toArrayEmpty() {
        Object[] array = ImmutableTreeList.getEmpty().toArray();
        assertThat(array, is(arrayWithSize(0)));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#toArray(Object[])} returns the specified array filled with the list's elements
     * when the specified array has the same size as the list.
     */
    @Test
    public void toArrayObjectArrayArgumentLargeEnough() {
        Object[] arrayArgument = new Object[1];
        Object[] array = LIST_OF_A_SINGLE_OBJECT.toArray(arrayArgument);
        assertThat(array, is(sameInstance(arrayArgument)));
        assertThat(array, is(arrayContaining(A_SAMPLE_OBJECT)));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#toArray(Object[])} returns the specified array filled with the list's elements,
     * followed by a <code>null</code> element, when the specified array's size is larger than the list's size.
     */
    @Test
    public void toArrayObjectArrayArgumentLarger() {
        Object[] arrayArgument = new Object[2];
        arrayArgument[1] = arrayArgument; // set this array item to test that null is written to it
        Object[] array = LIST_OF_A_SINGLE_OBJECT.toArray(arrayArgument);
        assertThat(array, is(sameInstance(arrayArgument)));
        assertThat(array, is(arrayContaining(A_SAMPLE_OBJECT, null)));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#toArray(Object[])} returns a new array that contains the list's elements when
     * the specified array's size is smaller than the list's size.
     */
    @Test
    public void toArrayObjectArrayArgumentTooSmall() {
        Object[] array = LIST_OF_A_SINGLE_OBJECT.toArray(EMPTY_ARRAY_OF_OBJECTS);
        assertThat(array, is(arrayContaining(A_SAMPLE_OBJECT)));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#toArray(Object[])} returns the specified array when both the specified array
     * and the list are empty.
     */
    @Test
    public void toArrayObjectArrayEmptyArgument() {
        Object[] array = ImmutableTreeList.getEmpty().toArray(EMPTY_ARRAY_OF_OBJECTS);
        assertThat(array, is(sameInstance(EMPTY_ARRAY_OF_OBJECTS)));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeList#toArray(Object[])} returns the specified array with the first element set to
     * <code>null</code> when the list is empty.
     */
    @Test
    public void toArrayObjectArrayEmptyArgumentLarger() {
        Object[] arrayArgument = new Object[1];
        arrayArgument[0] = arrayArgument; // set this array item to test that null is written to it
        Object[] array = ImmutableTreeList.getEmpty().toArray(arrayArgument);
        assertThat(array, is(sameInstance(arrayArgument)));
        assertThat(array, is(arrayContaining(new Object[] { null })));
    }

}
