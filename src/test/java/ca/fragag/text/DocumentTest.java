package ca.fragag.text;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for {@link Document}.
 *
 * @author Francis Gagné
 */
public class DocumentTest {

    private static final String EMPTY_DOCUMENT_CONTENTS = "";
    private static final String SMALL_DOCUMENT_CONTENTS = "foobar";
    private static final String LARGE_DOCUMENT_CONTENTS = stringOfChar('a', Document.CHUNK_SIZE + 16);
    private static final String HUGE_DOCUMENT_CONTENTS = stringOfChar('a', Document.CHUNK_SIZE * 2 + 16);
    private static final String QUUX = "quux";

    private static final Document EMPTY_DOCUMENT = new Document(EMPTY_DOCUMENT_CONTENTS);
    private static final Document SMALL_DOCUMENT = new Document(SMALL_DOCUMENT_CONTENTS);
    private static final Document LARGE_DOCUMENT = new Document(LARGE_DOCUMENT_CONTENTS);
    private static final Document HUGE_DOCUMENT = new Document(HUGE_DOCUMENT_CONTENTS);

    private static String stringOfChar(char ch, int length) {
        char[] chars = new char[length];
        Arrays.fill(chars, ch);
        return new String(chars);
    }

    /**
     * Asserts that {@link Document#charAt(int)} throws an {@link IndexOutOfBoundsException} when the <code>index</code> argument is
     * negative for an empty document.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void charAtEmptyDocumentMinusOne() {
        EMPTY_DOCUMENT.charAt(-1);
    }

    /**
     * Asserts that {@link Document#charAt(int)} throws an {@link IndexOutOfBoundsException} when the <code>index</code> argument is
     * 0 for an empty document.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void charAtEmptyDocumentZero() {
        EMPTY_DOCUMENT.charAt(0);
    }

    /**
     * Asserts that {@link Document#charAt(int)} throws an {@link IndexOutOfBoundsException} when the <code>index</code> argument is
     * greater than the document's length for a document that is composed of one chunk.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void charAtSmallDocumentLength() {
        final int index = SMALL_DOCUMENT_CONTENTS.length();
        SMALL_DOCUMENT.charAt(index);
    }

    /**
     * Asserts that {@link Document#charAt(int)} returns the character at the specified index for a document that is composed of one
     * chunk.
     */
    @Test
    public void charAtSmallDocumentLengthMinusOne() {
        final int index = SMALL_DOCUMENT_CONTENTS.length() - 1;
        assertThat(SMALL_DOCUMENT.charAt(index), is(SMALL_DOCUMENT_CONTENTS.charAt(index)));
    }

    /**
     * Asserts that {@link Document#charAt(int)} throws an {@link IndexOutOfBoundsException} when the <code>index</code> argument is
     * negative for a document that is composed of one chunk.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void charAtSmallDocumentMinusOne() {
        final int index = -1;
        SMALL_DOCUMENT.charAt(index);
    }

    /**
     * Asserts that {@link Document#charAt(int)} returns the character at the specified index for a document that is composed of one
     * chunk.
     */
    @Test
    public void charAtSmallDocumentZero() {
        final int index = 0;
        assertThat(SMALL_DOCUMENT.charAt(index), is(SMALL_DOCUMENT_CONTENTS.charAt(index)));
    }

    /**
     * Asserts that {@link Document#Document(CharSequence)} throws a {@link NullPointerException} when the <code>text</code>
     * argument is <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void documentCharSequenceNull() {
        new Document((CharSequence) null);
    }

    /**
     * Asserts that {@link Document#Document(CharSequence)} correctly initializes a {@link Document} when the <code>text</code>
     * argument is a {@link String}.
     */
    @Test
    public void documentCharSequenceString() {
        final Document document = new Document((CharSequence) SMALL_DOCUMENT_CONTENTS);
        assertThat(document.toString(), is(SMALL_DOCUMENT_CONTENTS));
    }

    /**
     * Asserts that {@link Document#Document(CharSequence)} correctly initializes a {@link Document} when the <code>text</code>
     * argument is a subdocument produced by {@link Document#subSequence(int, int)}.
     */
    @Test
    public void documentCharSequenceSubDocument() {
        final Document document = new Document(SMALL_DOCUMENT.subSequence(2, 4));
        assertThat(document.toString(), is("ob"));
    }

    /**
     * Asserts that {@link Document#Document(String)} throws a {@link NullPointerException} when the <code>text</code> argument is
     * <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void documentStringNull() {
        new Document((String) null);
    }

    /**
     * Asserts that {@link Document#length()} returns 0 for an empty document.
     */
    @Test
    public void lengthEmptyDocument() {
        assertThat(EMPTY_DOCUMENT.length(), is(EMPTY_DOCUMENT_CONTENTS.length()));
    }

    /**
     * Asserts that {@link Document#length()} returns the document's length for a document that is composed of two chunks.
     */
    @Test
    public void lengthLargeDocument() {
        assertThat(LARGE_DOCUMENT.length(), is(LARGE_DOCUMENT_CONTENTS.length()));
    }

    /**
     * Asserts that {@link Document#length()} returns the document's length for a document that is composed of one chunk.
     */
    @Test
    public void lengthSmallDocument() {
        assertThat(SMALL_DOCUMENT.length(), is(SMALL_DOCUMENT_CONTENTS.length()));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns a new document with the correct contents when replacing text
     * across two chunks in the document.
     */
    @Test
    public void replaceAcrossChunks() {
        final String bs = "bbbbbbbbbbbbbbbb";
        final String expected = new String(new char[Document.CHUNK_SIZE - 8]).replace('\0', 'a') + bs + "aaaaaaaa";
        final Document newDocument = LARGE_DOCUMENT.replace(LARGE_DOCUMENT_CONTENTS.length() - 16 - 8, 16, bs);
        assertThat(newDocument.toString(), is(expected));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns the original document when there is nothing to remove and
     * nothing to insert.
     */
    @Test
    public void replaceEmpty() {
        assertThat(SMALL_DOCUMENT.replace(0, 0, ""), is(sameInstance(SMALL_DOCUMENT)));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns the original document when the text to insert is identical to
     * the text to remove.
     */
    @Test
    public void replaceIdempotent() {
        assertThat(SMALL_DOCUMENT.replace(2, 2, SMALL_DOCUMENT_CONTENTS.substring(2, 4)), is(sameInstance(SMALL_DOCUMENT)));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns a new document with the correct contents when removing
     * nothing and inserting text at the beginning of the document.
     */
    @Test
    public void replaceInsertHead() {
        final Document newDocument = SMALL_DOCUMENT.replace(0, 0, QUUX);
        assertThat(newDocument.toString(), is("quuxfoobar"));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns a new document with the correct contents when removing
     * nothing and inserting text in the middle of the document.
     */
    @Test
    public void replaceInsertMiddle() {
        final Document newDocument = SMALL_DOCUMENT.replace(3, 0, QUUX);
        assertThat(newDocument.toString(), is("fooquuxbar"));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns a new document with the correct contents when removing
     * nothing and inserting text at the end of the document.
     */
    @Test
    public void replaceInsertTail() {
        final Document newDocument = SMALL_DOCUMENT.replace(SMALL_DOCUMENT_CONTENTS.length(), 0, QUUX);
        assertThat(newDocument.toString(), is("foobarquux"));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns a new document with the correct contents when replacing text
     * across many chunks in the document.
     */
    @Test
    public void replaceManyChunks() {
        final Document newDocument = HUGE_DOCUMENT.replace(8, HUGE_DOCUMENT_CONTENTS.length() - 8, "bbbb");
        assertThat(newDocument.toString(), is("aaaaaaaabbbb"));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} throws an {@link IllegalArgumentException} when the
     * <code>length</code> argument is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void replaceNegativeLength() {
        SMALL_DOCUMENT.replace(0, -1, QUUX);
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} throws an {@link IndexOutOfBoundsException} when the
     * <code>offset</code> argument is negative.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void replaceNegativeOffset() {
        SMALL_DOCUMENT.replace(-1, 0, QUUX);
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} throws an {@link IndexOutOfBoundsException} when the sum of the
     * <code>offset</code> and the <code>length</code> arguments is greater than the document's length.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void replaceOffsetPlusLengthTooLarge() {
        SMALL_DOCUMENT.replace(1, SMALL_DOCUMENT_CONTENTS.length(), QUUX);
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns a new document with the correct contents when removing text
     * at the beginning of the document and inserting nothing.
     */
    @Test
    public void replaceRemoveHead() {
        final Document newDocument = SMALL_DOCUMENT.replace(0, 2, null);
        assertThat(newDocument.toString(), is("obar"));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns a new document with the correct contents when removing text
     * in the middle of the document and inserting nothing.
     */
    @Test
    public void replaceRemoveMiddle() {
        final Document newDocument = SMALL_DOCUMENT.replace(2, 2, null);
        assertThat(newDocument.toString(), is("foar"));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns a new document with the correct contents when removing text
     * at the end of the document and inserting nothing.
     */
    @Test
    public void replaceRemoveTail() {
        final Document newDocument = SMALL_DOCUMENT.replace(SMALL_DOCUMENT_CONTENTS.length() - 2, 2, null);
        assertThat(newDocument.toString(), is("foob"));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns a new document with the correct contents when replacing text
     * at the beginning of the document.
     */
    @Test
    public void replaceReplaceHead() {
        final Document newDocument = SMALL_DOCUMENT.replace(0, 2, QUUX);
        assertThat(newDocument.toString(), is("quuxobar"));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns a new document with the correct contents when replacing text
     * in the middle of the document.
     */
    @Test
    public void replaceReplaceMiddle() {
        final Document newDocument = SMALL_DOCUMENT.replace(2, 2, QUUX);
        assertThat(newDocument.toString(), is("foquuxar"));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns a new document with the correct contents when replacing text
     * at the end of the document.
     */
    @Test
    public void replaceReplaceTail() {
        final Document newDocument = SMALL_DOCUMENT.replace(SMALL_DOCUMENT_CONTENTS.length() - 2, 2, QUUX);
        assertThat(newDocument.toString(), is("foobquux"));
    }

    /**
     * Asserts that {@link Document#replace(int, int, String)} returns a new document with the correct contents when the removal
     * covers the whole document.
     */
    @Test
    public void replaceWhole() {
        final Document newDocument = SMALL_DOCUMENT.replace(0, SMALL_DOCUMENT_CONTENTS.length(), QUUX);
        assertThat(newDocument.toString(), is(QUUX));
    }

    /**
     * Asserts that {@link Document#subSequence(int, int)} returns an empty subdocument when the <code>start</code> and the
     * <code>end</code> arguments are equal.
     */
    @Test
    public void subSequenceEmptyDocument() {
        assertThat(EMPTY_DOCUMENT.subSequence(0, 0).length(), is(0));
    }

    /**
     * Asserts that {@link Document#subSequence(int, int)} throws an {@link IndexOutOfBoundsException} when the <code>end</code>
     * argument is greater than the document's length.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void subSequenceEndTooLarge() {
        SMALL_DOCUMENT.subSequence(0, SMALL_DOCUMENT_CONTENTS.length() + 1);
    }

    /**
     * Asserts that {@link Document#subSequence(int, int)} throws an {@link IndexOutOfBoundsException} when the <code>start</code>
     * argument is greater than the <code>end</code> argument.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void subSequenceStartAfterEnd() {
        SMALL_DOCUMENT.subSequence(1, 0);
    }

    /**
     * Asserts that {@link Document#subSequence(int, int)} throws an {@link IndexOutOfBoundsException} when the <code>start</code>
     * argument is negative.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void subSequenceStartTooSmall() {
        SMALL_DOCUMENT.subSequence(-1, 0);
    }

    /**
     * Asserts that {@link Document#subSequence(int, int)} does not throw when the <code>start</code> and <code>end</code> arguments
     * are valid.
     */
    @Test
    public void subSequenceValid() {
        SMALL_DOCUMENT.subSequence(1, SMALL_DOCUMENT_CONTENTS.length() - 1);
    }

    /**
     * Asserts that {@link Document#substring(int, int)} throws an {@link IndexOutOfBoundsException} when the <code>end</code>
     * argument is greater than the document's length.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void substringEndTooLarge() {
        SMALL_DOCUMENT.substring(0, SMALL_DOCUMENT_CONTENTS.length() + 1);
    }

    /**
     * Asserts that {@link Document#substring(int, int)} throws an {@link IndexOutOfBoundsException} when the <code>end</code>
     * argument is greater than the <code>start</code> argument.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void substringStartAfterEnd() {
        SMALL_DOCUMENT.substring(1, 0);
    }

    /**
     * Asserts that {@link Document#substring(int, int)} throws an {@link IndexOutOfBoundsException} when the <code>start</code>
     * argument is negative.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void substringStartTooSmall() {
        SMALL_DOCUMENT.substring(-1, 0);
    }

    /**
     * Asserts that {@link Document#substring(int, int)} returns a {@link String} that contains the contents of the document in the
     * specified range.
     */
    @Test
    public void substringValid() {
        assertThat(SMALL_DOCUMENT.substring(1, SMALL_DOCUMENT_CONTENTS.length() - 1), is("ooba"));
    }

    /**
     * Asserts that {@link Document#toString()} returns an empty string for an empty document.
     */
    @Test
    public void toStringEmptyDocument() {
        assertThat(EMPTY_DOCUMENT.toString(), is(EMPTY_DOCUMENT_CONTENTS));
    }

    /**
     * Asserts that {@link Document#toString()} returns the document's contents as a {@link String} for a {@link Document}
     * constructed from a custom {@link CharSequence}.
     */
    @Test
    public void toStringFromCharSequence() {
        final Document document = new Document(new CharSequence() {
            @Override
            public char charAt(int index) {
                return 'a';
            }

            @Override
            public int length() {
                return Document.CHUNK_SIZE + 16;
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                throw new UnsupportedOperationException();
            }
        });
        assertThat(document.toString(), is(LARGE_DOCUMENT_CONTENTS));
    }

    /**
     * Asserts that {@link Document#toString()} returns the document's contents as a {@link String} for a document that is composed
     * of two chunks.
     */
    @Test
    public void toStringLargeDocument() {
        assertThat(LARGE_DOCUMENT.toString(), is(LARGE_DOCUMENT_CONTENTS));
    }

    /**
     * Asserts that {@link Document#toString()} returns the document's contents as a {@link String} for a document that is composed
     * of one chunk.
     */
    @Test
    public void toStringSmallDocument() {
        assertThat(SMALL_DOCUMENT.toString(), is(SMALL_DOCUMENT_CONTENTS));
    }

}
