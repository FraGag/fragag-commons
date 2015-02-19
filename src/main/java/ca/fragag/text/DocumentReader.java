package ca.fragag.text;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Reads code points out of a {@link Document}.
 *
 * @author Francis Gagné
 */
public final class DocumentReader extends CharSequenceReader<Document> {

    @Nonnull
    private final Document document;
    @Nonnull
    private final DocumentImmutableTreeList chunks;
    private int currentChunkIndex;
    @CheckForNull
    private char[] currentChunk;
    private int nextChunkIndex;
    @CheckForNull
    private char[] nextChunk;
    private int currentChunkStartPosition;
    private int currentPositionInChunk;
    private int nextPositionInChunk;
    private char currentChar;
    private int currentCodePoint;

    /**
     * Initializes a new DocumentReader.
     *
     * @param document
     *            the document to read from
     */
    public DocumentReader(@Nonnull Document document) {
        if (document == null) {
            throw new NullPointerException("document");
        }

        this.document = document;

        // We need to capture the document's list of chunks, because it may change.
        // Since we maintain indices in that list, our behavior would be incorrect if the list changed!
        this.chunks = document.getTextChunks();

        this.setCurrentPosition(0, 0, 0);
    }

    /**
     * Initializes a new DocumentReader.
     *
     * @param document
     *            the document to read from
     * @param position
     *            the reader's initial position
     */
    public DocumentReader(@Nonnull Document document, int position) {
        if (document == null) {
            throw new NullPointerException("document");
        }

        this.document = document;
        this.chunks = document.getTextChunks();
        this.setCurrentPosition(position);
    }

    @Override
    public final void advance() {
        this.currentPositionInChunk = this.nextPositionInChunk;
        if (this.currentChunk != this.nextChunk) {
            this.currentChunkStartPosition += this.currentChunk.length;
            this.currentChunkIndex = this.nextChunkIndex;
            this.currentChunk = this.nextChunk;
        }

        this.readNextCodePoint();
    }

    @Override
    public final Document getCharSequence() {
        return this.document;
    }

    @Override
    public final int getCurrentCodePoint() {
        return this.currentCodePoint;
    }

    @Override
    public final int getCurrentPosition() {
        return this.currentChunkStartPosition + this.currentPositionInChunk;
    }

    /**
     * Gets the {@link Document} managed by this reader.
     *
     * @return the document
     */
    @Nonnull
    public final Document getDocument() {
        return this.getCharSequence();
    }

    @Override
    public final void rewind() {
        this.nextPositionInChunk = this.currentPositionInChunk;
        this.nextChunkIndex = this.currentChunkIndex;
        this.nextChunk = this.currentChunk;

        this.readPreviousCodePoint();
    }

    @Override
    protected final String readSubstringCore(int length) {
        if (this.atEnd()) {
            throw new IllegalArgumentException("length");
        }

        final int startPosition = this.getCurrentPosition();

        if (length == 1) {
            final String result = String.valueOf(this.currentChar);
            this.setCurrentPosition(startPosition + length);
            return result;
        }

        final char[] chars = new char[length];

        try {
            for (int i = Character.toChars(this.currentCodePoint, chars, 0); i < length; i++) {
                final int ch = this.readNextChar();
                if (ch == -1) {
                    throw new IllegalArgumentException("length");
                }

                chars[i] = (char) ch;
            }

            this.setCurrentPosition(startPosition + length);
        } catch (Throwable t) {
            this.setCurrentPosition(startPosition);
            throw t;
        }

        return new String(chars);
    }

    @Override
    protected final void setCurrentPositionCore(int position) {
        // If the position lies within the current chunk, position the reader within the chunk and read the next code point.
        if (this.currentChunk != null && position >= this.currentChunkStartPosition
                && position <= this.currentChunkStartPosition + this.currentChunk.length) {
            this.nextChunkIndex = this.currentChunkIndex;
            this.nextChunk = this.currentChunk;
            this.nextPositionInChunk = position - this.currentChunkStartPosition;
            this.advance();
            return;
        }

        this.chunks.setCurrentPositionInDocumentReader(this, position);
    }

    final void setCurrentPosition(int chunkIndex, int chunkStartPosition, int positionInChunk) {
        this.currentChunkIndex = chunkIndex;

        if (this.chunks.size() > chunkIndex) {
            this.currentChunk = this.chunks.get(chunkIndex);
        } else {
            this.currentChunk = null;
        }

        this.currentChunkStartPosition = chunkStartPosition;
        this.nextChunkIndex = this.currentChunkIndex;
        this.nextChunk = this.currentChunk;
        this.currentPositionInChunk = positionInChunk;
        this.nextPositionInChunk = positionInChunk;
        this.readNextCodePoint();
    }

    private final int readNextChar() {
        if (this.nextPositionInChunk < 0) {
            this.nextPositionInChunk = 0;
            return -1;
        }

        if (this.nextChunk == null || this.nextPositionInChunk >= this.nextChunk.length) {
            if (this.nextChunkIndex + 1 >= this.chunks.size()) {
                return -1;
            }

            this.nextChunkIndex++;
            this.nextChunk = this.chunks.get(this.nextChunkIndex);
            this.nextPositionInChunk = 0;
        }

        return this.nextChunk[this.nextPositionInChunk++];
    }

    private final void readNextCodePoint() {
        final int c1 = this.readNextChar();
        this.currentChar = (char) c1;
        this.currentCodePoint = c1;
        if (c1 != -1 && Character.isHighSurrogate((char) c1)) {
            final int c2 = this.readNextChar();
            if (c2 != -1) {
                if (Character.isLowSurrogate((char) c2)) {
                    this.currentCodePoint = Character.toCodePoint((char) c1, (char) c2);
                } else {
                    this.nextPositionInChunk--;
                }
            }
        }
    }

    private final int readPreviousChar() {
        if (this.currentPositionInChunk < 0) {
            return -1;
        }

        if (this.currentChunk == null || this.currentPositionInChunk == 0) {
            if (this.currentChunkIndex <= 0) {
                this.currentPositionInChunk = -1;
                return -1;
            }

            this.currentChunkIndex--;
            this.currentChunk = this.chunks.get(this.currentChunkIndex);
            this.currentChunkStartPosition -= this.currentChunk.length;
            this.currentPositionInChunk = this.currentChunk.length;
        }

        return this.currentChunk[--this.currentPositionInChunk];
    }

    private final void readPreviousCodePoint() {
        final int c2 = this.readPreviousChar();
        this.currentChar = (char) c2;
        this.currentCodePoint = c2;
        if (c2 != -1 && Character.isLowSurrogate((char) c2)) {
            final int c1 = this.readPreviousChar();
            if (c1 != -1) {
                if (Character.isHighSurrogate((char) c1)) {
                    this.currentChar = (char) c1;
                    this.currentCodePoint = Character.toCodePoint((char) c1, (char) c2);
                } else {
                    this.currentPositionInChunk++;
                }
            } else {
                this.currentPositionInChunk = 0;
            }
        }
    }

}
