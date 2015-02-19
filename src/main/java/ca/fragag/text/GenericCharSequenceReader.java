package ca.fragag.text;

import javax.annotation.Nonnull;

/**
 * Reads code points out of a {@link CharSequence}.
 *
 * @author Francis Gagné
 */
public final class GenericCharSequenceReader extends CharSequenceReader<CharSequence> {

    @Nonnull
    private final CharSequence charSequence;
    private int current;

    /**
     * Initializes a new GenericCharSequenceReader.
     *
     * @param charSequence
     *            the {@link CharSequence} to read from
     */
    public GenericCharSequenceReader(@Nonnull CharSequence charSequence) {
        if (charSequence == null) {
            throw new NullPointerException("charSequence");
        }

        this.charSequence = charSequence;
    }

    /**
     * Initializes a new GenericCharSequenceReader.
     *
     * @param charSequence
     *            the {@link CharSequence} to read from
     * @param position
     *            the reader's initial position
     */
    public GenericCharSequenceReader(@Nonnull CharSequence charSequence, int position) {
        if (charSequence == null) {
            throw new NullPointerException("charSequence");
        }

        this.charSequence = charSequence;
        this.current = position;
    }

    @Override
    public final void advance() {
        if (this.current < 0) {
            this.current = 0;
        } else if (this.current < this.charSequence.length()) {
            this.current = Character.offsetByCodePoints(this.charSequence, this.current, 1);
        } else {
            this.current = this.charSequence.length();
        }
    }

    @Nonnull
    @Override
    public final CharSequence getCharSequence() {
        return this.charSequence;
    }

    @Override
    public final int getCurrentCodePoint() {
        if (this.current >= 0 && this.current < this.charSequence.length()) {
            return Character.codePointAt(this.charSequence, this.current);
        }

        return -1;
    }

    @Override
    public final int getCurrentPosition() {
        return this.current;
    }

    @Override
    public final void rewind() {
        if (this.current > 0) {
            this.current = Character.offsetByCodePoints(this.charSequence, this.current, -1);
        } else {
            this.current = -1;
        }
    }

    @Override
    protected final void setCurrentPositionCore(int position) {
        this.current = position;
    }

}
