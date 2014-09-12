package ca.fragag.text;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Test class for {@link GenericCharSequenceReader}.
 *
 * @author Francis Gagné
 */
public class GenericCharSequenceReaderTest extends CharSequenceReaderContract {

    private static final CharSequence FOO = "foo";

    /**
     * Asserts that {@link GenericCharSequenceReader#GenericCharSequenceReader(CharSequence)} correctly initializes a
     * {@link GenericCharSequenceReader}.
     */
    @Test
    public void charSequenceReaderCharSequence() {
        final GenericCharSequenceReader reader = new GenericCharSequenceReader(FOO);
        assertThat(reader.getCharSequence(), is(sameInstance(FOO)));
        assertThat(reader.getCurrentPosition(), is(0));
    }

    /**
     * Asserts that {@link GenericCharSequenceReader#GenericCharSequenceReader(CharSequence, int)} correctly initializes a
     * {@link GenericCharSequenceReader}.
     */
    @Test
    public void charSequenceReaderCharSequenceInt() {
        final GenericCharSequenceReader reader = new GenericCharSequenceReader(FOO, 2);
        assertThat(reader.getCharSequence(), is(sameInstance(FOO)));
        assertThat(reader.getCurrentPosition(), is(2));
    }

    /**
     * Asserts that {@link GenericCharSequenceReader#GenericCharSequenceReader(CharSequence, int)} throws a
     * {@link NullPointerException} when the <code>charSequence</code> argument is <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void genericCharSequenceReaderCharSequenceIntNull() {
        new GenericCharSequenceReader(null, 0);
    }

    /**
     * Asserts that {@link GenericCharSequenceReader#GenericCharSequenceReader(CharSequence)} throws a {@link NullPointerException}
     * when the <code>charSequence</code> argument is <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void genericCharSequenceReaderCharSequenceNull() {
        new GenericCharSequenceReader(null);
    }

    @Override
    protected GenericCharSequenceReader createCharSequenceReader(CharSequence charSequence) {
        return new GenericCharSequenceReader(charSequence);
    }

    @Override
    protected GenericCharSequenceReader createCharSequenceReader(CharSequence charSequence, int position) {
        return new GenericCharSequenceReader(charSequence, position);
    }

}
