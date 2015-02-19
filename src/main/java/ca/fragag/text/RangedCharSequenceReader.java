package ca.fragag.text;

import javax.annotation.Nonnull;

/**
 * Wraps an existing {@link CharSequenceReader} and restricts the range of code points that can be read from the underlying
 * {@link CharSequence}.
 *
 * @author Francis Gagné
 */
public final class RangedCharSequenceReader extends CharSequenceReader<SubSequence> {

    @Nonnull
    private final CharSequenceReader<?> reader;
    private final int start;
    private final int end;

    /**
     * Initializes a new RangedCharSequenceReader.
     *
     * @param reader
     *            the {@link CharSequenceReader} to wrap
     * @param start
     *            the starting position (in chars) of the ranged reader
     * @param end
     *            the ending position (in chars) of the ranged reader
     */
    public <CS extends CharSequence> RangedCharSequenceReader(@Nonnull CharSequenceReader<CS> reader, int start, int end) {
        if (reader == null) {
            throw new NullPointerException("reader");
        }

        if (start < 0 || start > reader.getCharSequence().length()) {
            throw new IllegalArgumentException("start");
        }

        if (end < start || end > reader.getCharSequence().length()) {
            throw new IllegalArgumentException("end");
        }

        this.reader = reader;
        this.start = start;
        this.end = end;
        this.reader.setCurrentPosition(start);
    }

    @Override
    public final void advance() {
        if (this.reader.getCurrentPosition() < this.end) {
            this.reader.advance();
        }
    }

    @Override
    public final SubSequence getCharSequence() {
        return new SubSequence(this.reader.getCharSequence(), this.start, this.end);
    }

    @Override
    public final int getCurrentCodePoint() {
        final int readerPosition = this.reader.getCurrentPosition();
        if (readerPosition < this.start || readerPosition >= this.end) {
            return -1;
        }

        return this.reader.getCurrentCodePoint();
    }

    @Override
    public final int getCurrentPosition() {
        final int position = this.reader.getCurrentPosition();
        if (position < this.start) {
            return -1;
        }

        return position - this.start;
    }

    /**
     * Gets the ending position of the range this reader reads from.
     *
     * @return the ending position
     */
    public final int getEnd() {
        return this.end;
    }

    /**
     * Gets the starting position of the range this reader reads from.
     *
     * @return the starting position
     */
    public final int getStart() {
        return this.start;
    }

    @Override
    public final void rewind() {
        if (this.reader.getCurrentPosition() >= this.start) {
            this.reader.rewind();
        }
    }

    @Override
    protected final void setCurrentPositionCore(int position) {
        this.reader.setCurrentPosition(position + this.start);
    }

}
