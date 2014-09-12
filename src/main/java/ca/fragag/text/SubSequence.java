package ca.fragag.text;

import javax.annotation.Nonnull;

/**
 * A subsequence of a {@link CharSequence}.
 *
 * @author Francis Gagné
 */
public final class SubSequence implements CharSequence {

    @Nonnull
    private final CharSequence charSequence;
    private final int start, end;

    /**
     * Initializes a new SubSequence.
     *
     * @param charSequence
     *            the containing {@link CharSequence}
     * @param start
     *            the starting offset of the subsequence
     * @param end
     *            the ending offset of the subsequence
     */
    public SubSequence(@Nonnull CharSequence charSequence, int start, int end) {
        if (charSequence == null) {
            throw new NullPointerException("charSequence");
        }

        if (start < 0 || start > charSequence.length()) {
            throw new IllegalArgumentException("start");
        }

        if (end < start || end > charSequence.length()) {
            throw new IllegalArgumentException("end");
        }

        this.charSequence = charSequence;
        this.start = start;
        this.end = end;
    }

    @Override
    public final char charAt(int index) {
        if (index < 0 || index >= this.length()) {
            throw new IndexOutOfBoundsException();
        }

        return this.charSequence.charAt(this.start + index);
    }

    @Override
    public final int length() {
        return this.end - this.start;
    }

    @Nonnull
    @Override
    public final CharSequence subSequence(int start, int end) {
        return new SubSequence(this.charSequence, this.start + start, this.start + end);
    }

    @Nonnull
    @Override
    public final String toString() {
        return this.charSequence.toString().substring(this.start, this.end);
    }

}
