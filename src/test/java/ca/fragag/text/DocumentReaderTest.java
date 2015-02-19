package ca.fragag.text;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Test class for {@link DocumentReader}.
 *
 * @author Francis Gagné
 */
public class DocumentReaderTest extends CharSequenceReaderContract {

    /**
     * Asserts that, following a call to {@link CharSequenceReader#advance()} that crosses a chunk boundary in the {@link Document},
     * {@link CharSequenceReader#getCurrentPosition()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct
     * values.
     */
    @Test
    public void advanceNextChunk() {
        final char[] chars = new char[Document.CHUNK_SIZE + 2];
        chars[Document.CHUNK_SIZE - 1] = 'a';
        chars[Document.CHUNK_SIZE] = 'b';
        chars[Document.CHUNK_SIZE + 1] = 'c';
        final String text = new String(chars);
        final Document document = new Document(text);
        final DocumentReader reader = new DocumentReader(document, Document.CHUNK_SIZE - 1);
        assertThat(reader.getCurrentPosition(), is(Document.CHUNK_SIZE - 1));
        assertThat(reader.getCurrentCodePoint(), is(0x61));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(Document.CHUNK_SIZE));
        assertThat(reader.getCurrentCodePoint(), is(0x62));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(Document.CHUNK_SIZE + 1));
        assertThat(reader.getCurrentCodePoint(), is(0x63));
    }

    /**
     * Positions a {@link Document} on a chunk, calls {@link CharSequenceReader#advance()} to read a code point from the next chunk,
     * then calls {@link CharSequenceReader#rewind()} to read back the code point from the previous chunk, and asserts that
     * {@link CharSequenceReader#getCurrentPosition()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct
     * values.
     */
    @Test
    public void advanceRewindAcrossChunks() {
        final char[] chars = new char[Document.CHUNK_SIZE + 1];
        chars[Document.CHUNK_SIZE - 1] = 'a';
        chars[Document.CHUNK_SIZE] = 'b';
        final String text = new String(chars);
        final Document document = new Document(text);
        final DocumentReader reader = new DocumentReader(document, Document.CHUNK_SIZE - 1);
        assertThat(reader.getCurrentPosition(), is(Document.CHUNK_SIZE - 1));
        assertThat(reader.getCurrentCodePoint(), is(0x61));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(Document.CHUNK_SIZE));
        assertThat(reader.getCurrentCodePoint(), is(0x62));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(Document.CHUNK_SIZE - 1));
        assertThat(reader.getCurrentCodePoint(), is(0x61));
    }

    /**
     * Asserts that {@link DocumentReader#DocumentReader(Document, int)} throws a {@link NullPointerException} when the
     * <code>document</code> argument is <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void documentReaderDocumentIntNull() {
        new DocumentReader(null, 0);
    }

    /**
     * Asserts that {@link DocumentReader#DocumentReader(Document)} throws a {@link NullPointerException} when the
     * <code>document</code> argument is <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void documentReaderDocumentNull() {
        new DocumentReader(null);
    }

    /**
     * Asserts that {@link DocumentReader#getDocument()} returns the document passed to the constructor.
     */
    @Test
    public void getDocument() {
        final Document document = new Document("foo");
        final DocumentReader reader = new DocumentReader(document);
        assertThat(reader.getDocument(), is(sameInstance(document)));
    }

    /**
     * Positions a {@link Document} on a chunk, calls {@link CharSequenceReader#rewind()} to read a code point from the previous
     * chunk, then calls {@link CharSequenceReader#advance()} to read back the code point from the next chunk, and asserts that
     * {@link CharSequenceReader#getCurrentPosition()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct
     * values.
     */
    @Test
    public void rewindAdvanceAcrossChunks() {
        final char[] chars = new char[Document.CHUNK_SIZE + 1];
        chars[Document.CHUNK_SIZE - 1] = 'a';
        chars[Document.CHUNK_SIZE] = 'b';
        final String text = new String(chars);
        final Document document = new Document(text);
        final DocumentReader reader = new DocumentReader(document, Document.CHUNK_SIZE);
        assertThat(reader.getCurrentPosition(), is(Document.CHUNK_SIZE));
        assertThat(reader.getCurrentCodePoint(), is(0x62));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(Document.CHUNK_SIZE - 1));
        assertThat(reader.getCurrentCodePoint(), is(0x61));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(Document.CHUNK_SIZE));
        assertThat(reader.getCurrentCodePoint(), is(0x62));
    }

    /**
     * Asserts that, following a call to {@link CharSequenceReader#rewind()} that crosses a chunk boundary in the {@link Document},
     * {@link CharSequenceReader#getCurrentPosition()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct
     * values.
     */
    @Test
    public void rewindPreviousChunk() {
        final char[] chars = new char[Document.CHUNK_SIZE + 2];
        chars[Document.CHUNK_SIZE - 1] = 'a';
        chars[Document.CHUNK_SIZE] = 'b';
        chars[Document.CHUNK_SIZE + 1] = 'c';
        final String text = new String(chars);
        final Document document = new Document(text);
        final DocumentReader reader = new DocumentReader(document, Document.CHUNK_SIZE + 1);
        assertThat(reader.getCurrentPosition(), is(Document.CHUNK_SIZE + 1));
        assertThat(reader.getCurrentCodePoint(), is(0x63));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(Document.CHUNK_SIZE));
        assertThat(reader.getCurrentCodePoint(), is(0x62));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(Document.CHUNK_SIZE - 1));
        assertThat(reader.getCurrentCodePoint(), is(0x61));
    }

    /**
     * Asserts that {@link DocumentReader#setCurrentPosition(int)} works on an empty {@link Document}.
     */
    @Test
    public void setCurrentPositionEmpty() {
        final DocumentReader reader = new DocumentReader(Document.EMPTY, 0);
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Asserts that {@link DocumentReader#setCurrentPosition(int)} works when the new position lies within a chunk different from
     * the reader's current chunk.
     */
    @Test
    public void setCurrentPositionInDifferentChunk() {
        final char[] chars = new char[Document.CHUNK_SIZE + 2];
        chars[Document.CHUNK_SIZE + 1] = 'a';
        final String text = new String(chars);
        final Document document = new Document(text);
        final DocumentReader reader = new DocumentReader(document);
        reader.setCurrentPosition(Document.CHUNK_SIZE + 1);
        assertThat(reader.getCurrentPosition(), is(Document.CHUNK_SIZE + 1));
        assertThat(reader.getCurrentCodePoint(), is(0x61));
    }

    /**
     * Asserts that {@link DocumentReader#setCurrentPosition(int)} works when there are 3 chunks.
     */
    @Test
    public void setCurrentPositionInLeftNode() {
        final char[] chars = new char[Document.CHUNK_SIZE * 2 + 2];
        chars[0] = 'a';
        final String text = new String(chars);
        final Document document = new Document(text);
        final DocumentReader reader = new DocumentReader(document, Document.CHUNK_SIZE);
        reader.setCurrentPosition(0);
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentCodePoint(), is(0x61));
    }

    @Override
    protected DocumentReader createCharSequenceReader(CharSequence charSequence) {
        return new DocumentReader(new Document(charSequence.toString()));
    }

    @Override
    protected DocumentReader createCharSequenceReader(CharSequence charSequence, int position) {
        return new DocumentReader(new Document(charSequence.toString()), position);
    }

}
