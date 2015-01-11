package ca.fragag.collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;

import org.junit.Test;

/**
 * Test class for {@link PeekableIterator}.
 *
 * @author Francis Gagné
 */
public class PeekableIteratorTest {

    @Nonnull
    private static final Object OBJECT_1 = new Object();
    @Nonnull
    private static final Object OBJECT_2 = new Object();
    @Nonnull
    private static final Object OBJECT_3 = new Object();
    @Nonnull
    private static final Object OBJECT_4 = new Object();
    @Nonnull
    private static final List<Object> LIST = Collections.unmodifiableList(Arrays.asList(OBJECT_1, OBJECT_2, OBJECT_3, OBJECT_4));

    /**
     * Asserts that {@link PeekableIterator#hasNext()} returns the correct result when there have been no calls to
     * {@link PeekableIterator#peek()}.
     */
    @Test
    public void hasNextNoPeek() {
        final PeekableIterator<Object> iterator = new PeekableIterator<>(LIST.iterator());
        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        assertThat(iterator.hasNext(), is(false));
    }

    /**
     * Asserts that {@link PeekableIterator#hasNext()} returns the correct result when there have been calls to
     * {@link PeekableIterator#peek()}.
     */
    @Test
    public void hasNextPeek() {
        final PeekableIterator<Object> iterator = new PeekableIterator<>(LIST.iterator());
        iterator.peek();
        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        iterator.peek();
        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        iterator.peek();
        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        iterator.peek();
        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        assertThat(iterator.hasNext(), is(false));
    }

    /**
     * Asserts that {@link PeekableIterator#next()} returns the correct result when there have been no calls to
     * {@link PeekableIterator#peek()}.
     */
    @Test
    public void next() {
        final PeekableIterator<Object> iterator = new PeekableIterator<>(LIST.iterator());
        assertThat(iterator.next(), is(OBJECT_1));
        assertThat(iterator.next(), is(OBJECT_2));
        assertThat(iterator.next(), is(OBJECT_3));
        assertThat(iterator.next(), is(OBJECT_4));
        try {
            iterator.next();
            fail("PeekableIterator.next() should have thrown a NoSuchElementException");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Asserts that {@link PeekableIterator#peek()} and {@link PeekableIterator#next()} return the correct result when mixed.
     */
    @Test
    public void peek() {
        final PeekableIterator<Object> iterator = new PeekableIterator<>(LIST.iterator());
        assertThat(iterator.peek(), is(OBJECT_1));
        assertThat(iterator.next(), is(OBJECT_1));
        assertThat(iterator.peek(), is(OBJECT_2));
        assertThat(iterator.peek(), is(OBJECT_2));
        assertThat(iterator.peek(), is(OBJECT_2));
        assertThat(iterator.next(), is(OBJECT_2));
        assertThat(iterator.next(), is(OBJECT_3));
        assertThat(iterator.peek(), is(OBJECT_4));
        assertThat(iterator.next(), is(OBJECT_4));
        try {
            iterator.peek();
            fail("PeekableIterator.peek() should have thrown a NoSuchElementException");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Asserts that {@link PeekableIterator#PeekableIterator(Iterator)} throws a {@link NullPointerException} when the
     * <code>iterator</code> argument is <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void peekableIteratorNullIterator() {
        new PeekableIterator<>(null);
    }

    /**
     * Asserts that {@link PeekableIterator#remove()} throws an {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void remove() {
        new PeekableIterator<>(LIST.iterator()).remove();
    }

}
