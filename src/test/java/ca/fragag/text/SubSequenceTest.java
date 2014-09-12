package ca.fragag.text;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Test class for {@link SubSequence}.
 *
 * @author Francis Gagné
 */
public class SubSequenceTest {

    private static final SubSequence RTYUI = new SubSequence("qwertyuiop", 3, 8);

    /**
     * Asserts that {@link SubSequence#charAt(int)} throws an {@link IndexOutOfBoundsException} when the <code>index</code> argument
     * is greater than or equal to the subsequence's length.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void charAtIndexTooHigh() {
        RTYUI.charAt(5);
    }

    /**
     * Asserts that {@link SubSequence#charAt(int)} throws an {@link IndexOutOfBoundsException} when the <code>index</code> argument
     * is negative.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void charAtIndexTooLow() {
        RTYUI.charAt(-1);
    }

    /**
     * Asserts that {@link SubSequence#charAt(int)} returns the character at the specified position in the subsequence.
     */
    @Test
    public void charAtValid() {
        assertThat(RTYUI.charAt(2), is('y'));
    }

    /**
     * Asserts that {@link SubSequence#length()} returns the subsequence's length.
     */
    @Test
    public void length() {
        assertThat(RTYUI.length(), is(5));
    }

    /**
     * Asserts that {@link SubSequence#subSequence(int, int)} returns a {@link CharSequence} whose contents are a subsequence of the
     * original subsequence.
     */
    @Test
    public void subSequence() {
        assertThat(RTYUI.subSequence(1, 4).toString(), is("tyu"));
    }

    /**
     * Asserts that {@link SubSequence#SubSequence(CharSequence, int, int)} throws an {@link IllegalArgumentException} when the
     * <code>start</code> argument is greater than the <code>end</code> argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void subSequenceEndBeforeStart() {
        new SubSequence("foo", 2, 1);
    }

    /**
     * Asserts that {@link SubSequence#SubSequence(CharSequence, int, int)} throws an {@link IllegalArgumentException} when the
     * <code>end</code> argument is greater than the {@link CharSequence}'s length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void subSequenceEndTooHigh() {
        new SubSequence("foo", 2, 4);
    }

    /**
     * Asserts that {@link SubSequence#SubSequence(CharSequence, int, int)} throws a {@link NullPointerException} when the
     * <code>charSequence</code> argument is <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void subSequenceNullCharSequence() {
        new SubSequence(null, 0, 0);
    }

    /**
     * Asserts that {@link SubSequence#SubSequence(CharSequence, int, int)} throws an {@link IllegalArgumentException} when the
     * <code>start</code> argument is greater than the {@link CharSequence}'s length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void subSequenceStartTooHigh() {
        new SubSequence("foo", 4, 4);
    }

    /**
     * Asserts that {@link SubSequence#SubSequence(CharSequence, int, int)} throws an {@link IllegalArgumentException} when the
     * <code>start</code> argument is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void subSequenceStartTooLow() {
        new SubSequence("foo", -1, 0);
    }

    /**
     * Asserts that {@link SubSequence#toString()} returns the subsequence's contents as a {@link String}.
     */
    @Test
    public void testToString() {
        assertThat(RTYUI.toString(), is("rtyui"));
    }

}
