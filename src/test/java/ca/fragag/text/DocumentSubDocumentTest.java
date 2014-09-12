package ca.fragag.text;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Test class for {@link Document.SubDocument}.
 *
 * @author Francis Gagné
 */
public class DocumentSubDocumentTest {

    private static final String SMALL_DOCUMENT_CONTENTS = "foobar";

    private static final Document SMALL_DOCUMENT = new Document(SMALL_DOCUMENT_CONTENTS);

    private static final CharSequence SMALL_DOCUMENT_SUBDOCUMENT = SMALL_DOCUMENT.subSequence(3, 5);

    /**
     * Asserts that {@link Document.SubDocument#charAt(int)} throws an {@link IndexOutOfBoundsException} when the <code>index</code>
     * argument is greater than the subdocument's length.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void charAtIndexTooLarge() {
        SMALL_DOCUMENT_SUBDOCUMENT.charAt(2);
    }

    /**
     * Asserts that {@link Document.SubDocument#charAt(int)} throws an {@link IndexOutOfBoundsException} when the <code>index</code>
     * argument is negative.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void charAtIndexTooSmall() {
        SMALL_DOCUMENT_SUBDOCUMENT.charAt(-1);
    }

    /**
     * Asserts that {@link Document.SubDocument#charAt(int)} returns the character at the specified index in the subdocument.
     */
    @Test
    public void charAtValid() {
        assertThat(SMALL_DOCUMENT_SUBDOCUMENT.charAt(0), is('b'));
    }

    /**
     * Asserts that {@link Document.SubDocument#length()} returns the subdocument's length.
     */
    @Test
    public void length() {
        assertThat(SMALL_DOCUMENT_SUBDOCUMENT.length(), is(2));
    }

    /**
     * Asserts that {@link Document.SubDocument#subSequence(int, int)} throws an {@link IndexOutOfBoundsException} when the
     * <code>end</code> argument is greater than the subdocument's length.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void subSequenceEndTooLarge() {
        SMALL_DOCUMENT_SUBDOCUMENT.subSequence(0, 3);
    }

    /**
     * Asserts that {@link Document.SubDocument#subSequence(int, int)} throws an {@link IndexOutOfBoundsException} when the
     * <code>start</code> argument is greater than the <code>end</code> argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void subSequenceStartAfterEnd() {
        SMALL_DOCUMENT_SUBDOCUMENT.subSequence(2, 1);
    }

    /**
     * Asserts that {@link Document.SubDocument#subSequence(int, int)} throws an {@link IndexOutOfBoundsException} when the
     * <code>start</code> argument is negative.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void subSequenceStartTooSmall() {
        SMALL_DOCUMENT_SUBDOCUMENT.subSequence(-1, 2);
    }

    /**
     * Asserts that {@link Document.SubDocument#subSequence(int, int)} returns a {@link CharSequence} that contains a subsequence of
     * the subdocument.
     */
    @Test
    public void subSequenceValid() {
        assertThat(SMALL_DOCUMENT_SUBDOCUMENT.subSequence(1, 2).toString(), is("a"));
    }

    /**
     * Asserts that {@link Document.SubDocument#toString()} returns the subdocument's contents as a string.
     */
    @Test
    public void testToString() {
        assertThat(SMALL_DOCUMENT_SUBDOCUMENT.toString(), is("ba"));
    }

}
