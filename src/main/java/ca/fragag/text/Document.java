package ca.fragag.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Represents an immutable text document from which derived documents sharing memory can be created.
 *
 * @author Francis Gagné
 */
@Immutable
public final class Document implements CharSequence {

    @Immutable
    private static final class SubDocument implements CharSequence {

        @Nonnull
        final Document document;
        final int start;
        final int end;

        SubDocument(@Nonnull Document document, int start, int end) {
            this.document = document;
            this.start = start;
            this.end = end;
        }

        @Override
        public final char charAt(int index) {
            if (index < 0 || index >= this.end - this.start) {
                throw new IndexOutOfBoundsException();
            }

            return this.document.charAt(this.start + index);
        }

        @Override
        public final int length() {
            return this.end - this.start;
        }

        @Nonnull
        @Override
        public final CharSequence subSequence(int start, int end) {
            if (start < 0 || end > this.end - this.start) {
                throw new IndexOutOfBoundsException();
            }

            if (start > end) {
                throw new IllegalArgumentException("start is greater than end");
            }

            return new SubDocument(this.document, this.start + start, this.start + end);
        }

        @Nonnull
        @Override
        public final String toString() {
            return this.document.substring(this.start, this.end);
        }

    }

    static final int CHUNK_SIZE = 32000;

    /** The empty document. */
    public static final Document EMPTY = new Document();

    private static void addChunks(@Nonnull ArrayList<char[]> textChunks, @Nonnull CharSequence text) {
        // Split the text in chunks of CHUNK_SIZE characters.
        int textLength = text.length();
        for (int i = 0; i < textLength;) {
            int end = Math.min(i + CHUNK_SIZE, textLength);
            char[] chunk = new char[end - i];

            for (int j = 0; i < end; i++, j++) {
                chunk[j] = text.charAt(i);
            }

            textChunks.add(chunk);
        }
    }

    private static void addChunks(@Nonnull ArrayList<char[]> textChunks, @Nonnull String text) {
        // Split the text in chunks of CHUNK_SIZE characters.
        int textLength = text.length();
        for (int i = 0; i < textLength; i += CHUNK_SIZE) {
            int end = Math.min(i + CHUNK_SIZE, textLength);
            char[] chunk = new char[end - i];
            text.getChars(i, end, chunk, 0);
            textChunks.add(chunk);
        }
    }

    @Nonnull
    private static DocumentImmutableTreeList initTextChunks(@Nonnull CharSequence text) {
        final ArrayList<char[]> textChunks = new ArrayList<>();
        addChunks(textChunks, text);
        return DocumentImmutableTreeList.Factory.INSTANCE.create(textChunks);
    }

    @Nonnull
    private static DocumentImmutableTreeList initTextChunks(@Nonnull String text) {
        final ArrayList<char[]> textChunks = new ArrayList<>();
        addChunks(textChunks, text);
        return DocumentImmutableTreeList.Factory.INSTANCE.create(textChunks);
    }

    @Nonnull
    private static DocumentImmutableTreeList initTextChunks(@Nonnull SubDocument text) {
        return text.document.replace(text.end, text.document.length() - text.end, "").replace(0, text.start, "").textChunks;
    }

    private static <T> T next(@Nonnull Iterator<T> iterator) {
        if (iterator.hasNext()) {
            return iterator.next();
        }

        return null;
    }

    @Nonnull
    private static String toString(@Nonnull List<char[]> textChunks, int start, int end) {
        if (start == end) {
            return "";
        }

        int capacity;
        if (end == Integer.MAX_VALUE) {
            if (textChunks instanceof DocumentImmutableTreeList) {
                capacity = ((DocumentImmutableTreeList) textChunks).textLength();
            } else {
                capacity = 0;
                for (int i = 0; i < textChunks.size(); i++) {
                    capacity += textChunks.get(i).length;
                }
            }
        } else {
            capacity = end - start;
        }

        final StringBuilder sb = new StringBuilder(capacity);
        for (int i = 0; i < textChunks.size(); i++) {
            final char[] chunk = textChunks.get(i);

            if (start < chunk.length) {
                final int chunkStart = start >= 0 ? start : 0;
                final int chunkEnd = end <= chunk.length ? end : chunk.length;
                sb.append(chunk, chunkStart, chunkEnd - chunkStart);
            }

            start -= chunk.length;
            end -= chunk.length;

            if (end <= 0) {
                break;
            }
        }

        return sb.toString();
    }

    // While the Document class is immutable, textChunks WILL change after calling Document.replace(int, int, String).
    // However, the chunks before and after are equivalent: they represent the same sequence of characters.
    // The new list of chunks shares some chunks with the document created by Document.replace(int, int, String),
    // and the old list of chunks will eventually be garbage-collected.
    @Nonnull
    private DocumentImmutableTreeList textChunks;

    /**
     * Initializes a new Document with the given contents.
     *
     * @param text
     *            the contents of the new document
     */
    public Document(@Nonnull CharSequence text) {
        if (text == null) {
            throw new NullPointerException("text");
        }

        if (text instanceof String) {
            this.textChunks = initTextChunks((String) text);
        } else if (text instanceof SubDocument) {
            this.textChunks = initTextChunks((SubDocument) text);
        } else {
            this.textChunks = initTextChunks(text);
        }
    }

    /**
     * Initializes a new Document with the given contents.
     *
     * @param text
     *            the contents of the new document
     */
    public Document(@Nonnull String text) {
        if (text == null) {
            throw new NullPointerException("text");
        }

        this.textChunks = initTextChunks(text);
    }

    private Document() {
        this.textChunks = DocumentImmutableTreeList.Factory.INSTANCE.create();
    }

    private Document(@Nonnull DocumentImmutableTreeList textChunks) {
        this.textChunks = textChunks;
    }

    @Override
    public final char charAt(int index) {
        return this.textChunks.charAt(index);
    }

    @Override
    public final int length() {
        return this.textChunks.textLength();
    }

    /**
     * Creates a new document with the contents of this document and the replacement specified by the arguments.
     *
     * @param offset
     *            the offset at which the replacement occurs
     * @param lengthToRemove
     *            the length of text to remove at the specified offset
     * @param textToInsert
     *            the new text to insert at the specified offset
     * @return the new Document
     */
    @Nonnull
    public final Document replace(int offset, int lengthToRemove, @CheckForNull String textToInsert) {
        if (offset < 0) {
            throw new IndexOutOfBoundsException("offset: " + offset);
        }

        if (lengthToRemove < 0) {
            throw new IllegalArgumentException("length: " + lengthToRemove);
        }

        if (offset + lengthToRemove > this.length()) {
            throw new IndexOutOfBoundsException("offset + length: " + (offset + lengthToRemove));
        }

        if (textToInsert == null) {
            textToInsert = "";
        }

        if (lengthToRemove == 0 && textToInsert.length() == 0) {
            // Nothing to do.
            return this;
        }

        if (this.subSequence(offset, offset + lengthToRemove).toString().equals(textToInsert)) {
            // Trying to replace some text with the same text: nothing to do.
            return this;
        }

        if (offset == 0 && lengthToRemove == this.length()) {
            // Trying to replace the whole document with new text.
            return new Document(textToInsert);
        }

        // The idea here is to reuse existing character arrays and create new ones only when necessary.

        ArrayList<char[]> currentTextChunks = new ArrayList<>();
        ArrayList<char[]> newTextChunks = new ArrayList<>();
        Iterator<char[]> chunkIterator = this.textChunks.iterator();
        char[] currentChunk = next(chunkIterator);
        int textOffset = 0;

        // Reuse the initial chunks that are common to the current and to the new document.
        while (textOffset < offset) {
            if (textOffset + currentChunk.length > offset) {
                break;
            }

            currentTextChunks.add(currentChunk);
            newTextChunks.add(currentChunk);
            textOffset += currentChunk.length;
            currentChunk = next(chunkIterator);
        }

        int offsetIntoCurrentChunk = 0;
        if (textOffset < offset) {
            // Create a new chunk from the start of the current chunk that is common to the current and to the new document.
            offsetIntoCurrentChunk = offset - textOffset;
            char[] newChunk = Arrays.copyOf(currentChunk, offsetIntoCurrentChunk);
            currentTextChunks.add(newChunk);
            newTextChunks.add(newChunk);
        }

        if (lengthToRemove > 0) {
            if (offsetIntoCurrentChunk + lengthToRemove < currentChunk.length) {
                // Create a new chunk from the text that is removed in the new document.
                currentTextChunks.add(Arrays.copyOfRange(currentChunk, offsetIntoCurrentChunk, offsetIntoCurrentChunk
                        + lengthToRemove));
                offsetIntoCurrentChunk += lengthToRemove;
            } else {
                // Create or reuse chunks from the text that is removed in the new document.

                // Create a new chunk from the rest of the current chunk, which is removed in the new document.
                currentTextChunks.add(Arrays.copyOfRange(currentChunk, offsetIntoCurrentChunk, currentChunk.length));
                int remainingLength = lengthToRemove + offsetIntoCurrentChunk - currentChunk.length;
                currentChunk = next(chunkIterator);

                // Reuse chunks from the text that is removed in the new document.
                while (remainingLength > 0 && remainingLength >= currentChunk.length) {
                    currentTextChunks.add(currentChunk);
                    remainingLength -= currentChunk.length;
                    currentChunk = next(chunkIterator);
                }

                offsetIntoCurrentChunk = remainingLength;

                // Create a new chunk from the start of the current chunk that is removed in the new document.
                if (remainingLength > 0) {
                    currentTextChunks.add(Arrays.copyOf(currentChunk, remainingLength));
                }
            }
        }

        // Create new chunks from the new text that is added to the new document.
        addChunks(newTextChunks, textToInsert);

        if (offsetIntoCurrentChunk > 0) {
            // Create a new chunk from the rest of the current chunk, which is common to the current and to the new document.
            char[] newChunk = Arrays.copyOfRange(currentChunk, offsetIntoCurrentChunk, currentChunk.length);
            currentTextChunks.add(newChunk);
            newTextChunks.add(newChunk);
            currentChunk = next(chunkIterator);
        }

        // Reuse the final chunks that are common to the current and to the new document.
        while (currentChunk != null) {
            currentTextChunks.add(currentChunk);
            newTextChunks.add(currentChunk);
            currentChunk = next(chunkIterator);
        }

        assert toString(this.textChunks, 0, Integer.MAX_VALUE).equals(toString(currentTextChunks, 0, Integer.MAX_VALUE)) : "The new list of text chunks for the current document has a different content from the current list!";
        this.textChunks = DocumentImmutableTreeList.Factory.INSTANCE.create(currentTextChunks);
        return new Document(DocumentImmutableTreeList.Factory.INSTANCE.create(newTextChunks));
    }

    @Nonnull
    @Override
    public final CharSequence subSequence(int start, int end) {
        if (start < 0) {
            throw new IndexOutOfBoundsException("start: " + start);
        }

        if (end > this.length()) {
            throw new IndexOutOfBoundsException("end: " + end);
        }

        if (start > end) {
            throw new IndexOutOfBoundsException("end - start: " + (end - start));
        }

        return new SubDocument(this, start, end);
    }

    /**
     * Returns a substring from this document.
     *
     * @param start
     *            the starting index (inclusive) of the substring
     * @param end
     *            the ending index (exclusive) of the substring
     * @return the substring
     * @throws IndexOutOfBoundsException
     *             start is negative, end is greater than the size of this document or start is greater than end
     */
    @Nonnull
    public final String substring(int start, int end) {
        if (start < 0) {
            throw new IndexOutOfBoundsException("start is negative: " + start);
        }

        final int length = this.length();
        if (end > length) {
            throw new IndexOutOfBoundsException("end is greater than document size: end=" + end + ", size=" + length);
        }

        if (start > end) {
            throw new IndexOutOfBoundsException("start is greater than end: start=" + start + ", end=" + end);
        }

        return toString(this.textChunks, start, end);
    }

    @Nonnull
    @Override
    public final String toString() {
        return toString(this.textChunks, 0, Integer.MAX_VALUE);
    }

    @Nonnull
    final DocumentImmutableTreeList getTextChunks() {
        return this.textChunks;
    }

}
