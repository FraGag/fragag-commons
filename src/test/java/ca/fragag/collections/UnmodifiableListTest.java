package ca.fragag.collections;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

/**
 * Test class for {@link UnmodifiableList}.
 *
 * @author Francis Gagné
 */
public class UnmodifiableListTest {

    private static final UnmodifiableList<Object> UNMODIFIABLE_LIST = new UnmodifiableList<Object>() {

        @Override
        public boolean contains(Object o) {
            fail();
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            fail();
            return false;
        }

        @Override
        public Object get(int index) {
            fail();
            return null;
        }

        @Override
        public int indexOf(Object o) {
            fail();
            return 0;
        }

        @Override
        public boolean isEmpty() {
            fail();
            return false;
        }

        @Override
        public Iterator<Object> iterator() {
            fail();
            return null;
        }

        @Override
        public int lastIndexOf(Object o) {
            fail();
            return 0;
        }

        @Override
        public ListIterator<Object> listIterator() {
            fail();
            return null;
        }

        @Override
        public ListIterator<Object> listIterator(int index) {
            fail();
            return null;
        }

        @Override
        public int size() {
            fail();
            return 0;
        }

        @Override
        public List<Object> subList(int fromIndex, int toIndex) {
            fail();
            return null;
        }

        @Override
        public Object[] toArray() {
            fail();
            return null;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            fail();
            return null;
        }

    };

    private static final List<Object> LIST_OF_A_SINGLE_NULL = Collections.singletonList(null);

    /**
     * Asserts that {@link UnmodifiableList#addAll(Collection)} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addAllCollection() {
        UNMODIFIABLE_LIST.addAll(LIST_OF_A_SINGLE_NULL);
    }

    /**
     * Asserts that {@link UnmodifiableList#addAll(int, Collection)} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addAllIntCollection() {
        UNMODIFIABLE_LIST.addAll(0, LIST_OF_A_SINGLE_NULL);
    }

    /**
     * Asserts that {@link UnmodifiableList#add(int, Object)} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addIntObject() {
        UNMODIFIABLE_LIST.add(0, null);
    }

    /**
     * Asserts that {@link UnmodifiableList#add(Object)} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addObject() {
        UNMODIFIABLE_LIST.add(null);
    }

    /**
     * Asserts that {@link UnmodifiableList#clear()} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void clear() {
        UNMODIFIABLE_LIST.clear();
    }

    /**
     * Asserts that {@link UnmodifiableList#removeAll(Collection)} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void removeAll() {
        UNMODIFIABLE_LIST.removeAll(LIST_OF_A_SINGLE_NULL);
    }

    /**
     * Asserts that {@link UnmodifiableList#remove(int)} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void removeInt() {
        UNMODIFIABLE_LIST.remove(0);
    }

    /**
     * Asserts that {@link UnmodifiableList#remove(Object)} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void removeObject() {
        UNMODIFIABLE_LIST.remove(null);
    }

    /**
     * Asserts that {@link UnmodifiableList#retainAll(Collection)} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void retainAll() {
        UNMODIFIABLE_LIST.retainAll(LIST_OF_A_SINGLE_NULL);
    }

    /**
     * Asserts that {@link UnmodifiableList#set(int, Object)} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void set() {
        UNMODIFIABLE_LIST.set(0, null);
    }

}
