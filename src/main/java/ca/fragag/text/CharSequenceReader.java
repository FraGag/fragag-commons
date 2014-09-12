package ca.fragag.text;

import java.util.NoSuchElementException;

import javax.annotation.Nonnull;

/**
 * Provides an interface for reading code points out of a {@link CharSequence}.
 *
 * @param <CS>
 *            the type of {@link CharSequence} to read from
 *
 * @author Francis Gagné
 */
public abstract class CharSequenceReader<CS extends CharSequence> {

    /**
     * Advances this reader's current position to the next code point.
     */
    public abstract void advance();

    /**
     * Determines whether this reader's current position is past the beginning or the end of the {@link CharSequence}.
     *
     * @return <code>true</code> if the current position is past the beginning or the {@link CharSequence}; otherwise,
     *         <code>false</code>
     */
    public final boolean atEnd() {
        return this.getCurrentCodePoint() == -1;
    }

    /**
     * Gets the {@link CharSequence} managed by this reader.
     *
     * @return the {@link CharSequence}
     */
    @Nonnull
    public abstract CS getCharSequence();

    /**
     * Gets the character at this reader's current position.
     *
     * @return the character at the current position
     * @throws NoSuchElementException
     *             the reader is positioned past the beginning or the end of the {@link CharSequence}
     */
    public abstract char getCurrentChar();

    /**
     * Gets the code point at this reader's current position.
     *
     * @return the code point at the current position, or -1 if the reader is positioned past the beginning or the end of the
     *         {@link CharSequence}
     */
    public abstract int getCurrentCodePoint();

    /**
     * Gets this reader's current position.
     *
     * @return the current position
     */
    public abstract int getCurrentPosition();

    /**
     * Reads a substring at this reader's current position and advances this reader to the end of the substring.
     *
     * @param length
     *            the length of the substring to read, in chars
     * @return the substring
     * @throws IllegalArgumentException
     *             <code>length</code> is negative
     * @throws IllegalStateException
     *             the reader's {@linkplain #getCurrentPosition() position} is before the start of the {@link CharSequence}
     */
    @Nonnull
    public final String readSubstring(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length");
        }

        if (this.getCurrentPosition() < 0) {
            throw new IllegalStateException("readSubstring: positioned before start of input");
        }

        if (length == 0) {
            return "";
        }

        return this.readSubstringCore(length);
    }

    /**
     * Rewinds this reader's current position to the previous code point.
     */
    public abstract void rewind();

    /**
     * Sets this reader's current position. If the position is in the middle of a surrogate pair, it is <strong>not</strong>
     * adjusted, so {@link #getCurrentChar()} and {@link #getCurrentCodePoint()} will return incorrect values. This method should be
     * used with the value returned from {@link #getCurrentPosition()} after series of calls to {@link #advance()} and
     * {@link #rewind()}, which should be a valid position.
     *
     * @param position
     *            the position to set as the current position
     * @throws IndexOutOfBoundsException
     *             <code>position</code> is less than -1 or greater than the length of the {@link CharSequence} returned by
     *             {@link #getCharSequence()}
     */
    public final void setCurrentPosition(int position) {
        if (position < -1) {
            throw new IndexOutOfBoundsException("position < -1: " + position);
        }

        if (position > this.getCharSequence().length()) {
            throw new IndexOutOfBoundsException("position > length: " + position);
        }

        this.setCurrentPositionCore(position);
    }

    /**
     * Reads a substring at this reader's current position and advances this reader to the end of the substring.
     * <p>
     * This method is called from {@link #readSubstring(int)}, which validates the <code>length</code> argument and this reader's
     * {@linkplain #getCurrentPosition() position}.
     *
     * @param length
     *            the length of the substring to read, in chars
     * @return the substring
     */
    @Nonnull
    protected String readSubstringCore(int length) {
        final int startPosition = this.getCurrentPosition();

        // Allocate 1 more char than the requested length in case the last char turns out to be part of a surrogate pair.
        final char[] chars = new char[length + 1];

        try {
            for (int i = 0; i < length; i += Character.toChars(this.getCurrentCodePoint(), chars, i), this.advance()) {
                if (this.atEnd()) {
                    throw new IllegalArgumentException("length");
                }
            }

            this.setCurrentPosition(startPosition + length);
        } catch (Throwable t) {
            this.setCurrentPosition(startPosition);
            throw t;
        }

        return new String(chars, 0, length);
    }

    /**
     * Sets this reader's current position.
     * <p>
     * This method is called from {@link #setCurrentPosition(int)}, which validates the <code>position</code> argument.
     *
     * @param position
     *            the position to set as the current position
     */
    protected abstract void setCurrentPositionCore(int position);

}
