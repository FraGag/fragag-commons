package ca.fragag.text;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Test class for {@link RangedCharSequenceReader}.
 *
 * @author Francis Gagné
 */
public class RangedCharSequenceReaderTest extends CharSequenceReaderContract {

    /**
     * Asserts that {@link RangedCharSequenceReader#RangedCharSequenceReader(CharSequenceReader, int, int)} does not throw when the
     * specified ending position is equal to the underlying reader's {@link CharSequence}'s length.
     */
    @Test
    public void rangedCharSequenceReaderEndMaximum() {
        final RangedCharSequenceReader reader = new RangedCharSequenceReader(new GenericCharSequenceReader("foo"), 1, 3);
        assertThat(reader.getStart(), is(1));
        assertThat(reader.getEnd(), is(3));
    }

    /**
     * Asserts that {@link RangedCharSequenceReader#RangedCharSequenceReader(CharSequenceReader, int, int)} does not throw when the
     * specified ending position is equal to the specified starting position.
     */
    @Test
    public void rangedCharSequenceReaderEndMinimum() {
        final RangedCharSequenceReader reader = new RangedCharSequenceReader(new GenericCharSequenceReader("foo"), 1, 1);
        assertThat(reader.getStart(), is(1));
        assertThat(reader.getEnd(), is(1));
    }

    /**
     * Asserts that {@link RangedCharSequenceReader#RangedCharSequenceReader(CharSequenceReader, int, int)} throws an
     * {@link IllegalArgumentException} when the specified ending position is greater than the underlying reader's
     * {@link CharSequence}'s length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void rangedCharSequenceReaderEndTooHigh() {
        new RangedCharSequenceReader(new GenericCharSequenceReader("foo"), 1, 4);
    }

    /**
     * Asserts that {@link RangedCharSequenceReader#RangedCharSequenceReader(CharSequenceReader, int, int)} throws an
     * {@link IllegalArgumentException} when the specified ending position is less than the specified starting position.
     */
    @Test(expected = IllegalArgumentException.class)
    public void rangedCharSequenceReaderEndTooLow() {
        new RangedCharSequenceReader(new GenericCharSequenceReader("foo"), 1, 0);
    }

    /**
     * Asserts that {@link RangedCharSequenceReader#RangedCharSequenceReader(CharSequenceReader, int, int)} throws a
     * {@link NullPointerException} when the specified underlying reader is <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void rangedCharSequenceReaderNullReader() {
        new RangedCharSequenceReader(null, 0, 0);
    }

    /**
     * Asserts that {@link RangedCharSequenceReader#RangedCharSequenceReader(CharSequenceReader, int, int)} does not throw when the
     * specified starting position is equal to the underlying reader's {@link CharSequence}'s length.
     */
    @Test
    public void rangedCharSequenceReaderStartMaximum() {
        final RangedCharSequenceReader reader = new RangedCharSequenceReader(new GenericCharSequenceReader("foo"), 3, 3);
        assertThat(reader.getStart(), is(3));
        assertThat(reader.getEnd(), is(3));
    }

    /**
     * Asserts that {@link RangedCharSequenceReader#RangedCharSequenceReader(CharSequenceReader, int, int)} does not throw when the
     * specified starting position is equal to 0.
     */
    @Test
    public void rangedCharSequenceReaderStartMinimum() {
        final RangedCharSequenceReader reader = new RangedCharSequenceReader(new GenericCharSequenceReader("foo"), 0, 0);
        assertThat(reader.getStart(), is(0));
        assertThat(reader.getEnd(), is(0));
    }

    /**
     * Asserts that {@link RangedCharSequenceReader#RangedCharSequenceReader(CharSequenceReader, int, int)} throws an
     * {@link IllegalArgumentException} when the specified starting position is greater than the underlying reader's
     * {@link CharSequence}'s length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void rangedCharSequenceReaderStartTooHigh() {
        new RangedCharSequenceReader(new GenericCharSequenceReader("foo"), 4, 4);
    }

    /**
     * Asserts that {@link RangedCharSequenceReader#RangedCharSequenceReader(CharSequenceReader, int, int)} throws an
     * {@link IllegalArgumentException} when the specified starting position is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void rangedCharSequenceReaderStartTooLow() {
        new RangedCharSequenceReader(new GenericCharSequenceReader("foo"), -1, 0);
    }

    @Override
    protected CharSequenceReader<?> createCharSequenceReader(CharSequence charSequence) {
        return new RangedCharSequenceReader(new GenericCharSequenceReader("<<" + charSequence + ">>"), 2, 2 + charSequence.length());
    }

}
