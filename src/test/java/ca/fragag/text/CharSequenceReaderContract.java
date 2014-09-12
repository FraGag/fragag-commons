package ca.fragag.text;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

/**
 * A helper class that implements test cases that test the contract of the virtual and abstract methods in
 * {@link CharSequenceReader}.
 *
 * @author Francis Gagné
 */
public abstract class CharSequenceReaderContract {

    private static final String FOO = "foo";
    private static final String BAR = "bar";

    /**
     * Calls {@link CharSequenceReader#advance()} then {@link CharSequenceReader#rewind()} on a reader that is positioned on the
     * last code point of a {@link CharSequence} and asserts that {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values
     * after each call.
     */
    @Test
    public void advanceAtEndRewind() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(BAR, 2);
        assertThat(reader.getCurrentPosition(), is(2));
        assertThat(reader.getCurrentChar(), is('r'));
        assertThat(reader.getCurrentCodePoint(), is(0x72));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(3));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(2));
        assertThat(reader.getCurrentChar(), is('r'));
        assertThat(reader.getCurrentCodePoint(), is(0x72));
    }

    /**
     * Asserts that, following a call to {@link CharSequenceReader#advance()}, {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values.
     * The test is performed on a {@link CharSequence} containing only BMP code points and calls
     * {@link CharSequenceReader#advance()} multiple times.
     */
    @Test
    public void advanceBmp() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(BAR);
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentChar(), is('b'));
        assertThat(reader.getCurrentCodePoint(), is(0x62));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(1));
        assertThat(reader.getCurrentChar(), is('a'));
        assertThat(reader.getCurrentCodePoint(), is(0x61));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(2));
        assertThat(reader.getCurrentChar(), is('r'));
        assertThat(reader.getCurrentCodePoint(), is(0x72));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(3));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(3));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Asserts that, following a call to {@link CharSequenceReader#advance()}, {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values.
     * The test is performed on a {@link CharSequence} containing a high surrogate that is not followed by a low surrogate and calls
     * {@link CharSequenceReader#advance()} multiple times.
     */
    @Test
    public void advanceBrokenSurrogate() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader("x\uD83Cy");
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentChar(), is('x'));
        assertThat(reader.getCurrentCodePoint(), is(0x78));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(1));
        assertThat(reader.getCurrentChar(), is('\uD83C'));
        assertThat(reader.getCurrentCodePoint(), is(0xD83C));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(2));
        assertThat(reader.getCurrentChar(), is('y'));
        assertThat(reader.getCurrentCodePoint(), is(0x79));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(3));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(3));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Asserts that, following a call to {@link CharSequenceReader#advance()}, {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values.
     * The test is performed on a {@link CharSequence} containing a high surrogate at the end and calls
     * {@link CharSequenceReader#advance()} multiple times.
     */
    @Test
    public void advanceBrokenSurrogateAtEnd() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader("x\uD83C");
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentChar(), is('x'));
        assertThat(reader.getCurrentCodePoint(), is(0x78));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(1));
        assertThat(reader.getCurrentChar(), is('\uD83C'));
        assertThat(reader.getCurrentCodePoint(), is(0xD83C));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(2));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(2));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Asserts that {@link CharSequenceReader#advance()} works when the {@link CharSequence} is empty.
     */
    @Test
    public void advanceEmpty() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader("");
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Asserts that, following a call to {@link CharSequenceReader#advance()}, {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values.
     * The test is performed on a {@link CharSequence} containing a mix of BMP code points and non-BMP code points and calls
     * {@link CharSequenceReader#advance()} multiple times.
     */
    @Test
    public void advanceNonBmp() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader("x\uD83C\uDF41y");
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentChar(), is('x'));
        assertThat(reader.getCurrentCodePoint(), is(0x78));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(1));
        assertThat(reader.getCurrentChar(), is('\uD83C'));
        assertThat(reader.getCurrentCodePoint(), is(0x1F341));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(3));
        assertThat(reader.getCurrentChar(), is('y'));
        assertThat(reader.getCurrentCodePoint(), is(0x79));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(4));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(4));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Calls {@link CharSequenceReader#advance()} then {@link CharSequenceReader#rewind()} on a reader that is positioned at the
     * start of a {@link CharSequence} and asserts that {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values
     * after each call.
     */
    @Test
    public void advanceRewind() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(BAR);
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentChar(), is('b'));
        assertThat(reader.getCurrentCodePoint(), is(0x62));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(1));
        assertThat(reader.getCurrentChar(), is('a'));
        assertThat(reader.getCurrentCodePoint(), is(0x61));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentChar(), is('b'));
        assertThat(reader.getCurrentCodePoint(), is(0x62));
    }

    /**
     * Calls {@link CharSequenceReader#rewind()} then {@link CharSequenceReader#advance()} on a reader that is positioned at the end
     * of a {@link CharSequence} and asserts that {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values
     * after each call.
     */
    @Test
    public void atEndRewindAdvance() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(BAR, 3);
        assertThat(reader.getCurrentPosition(), is(3));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(2));
        assertThat(reader.getCurrentChar(), is('r'));
        assertThat(reader.getCurrentCodePoint(), is(0x72));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(3));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Calls {@link CharSequenceReader#advance()} then {@link CharSequenceReader#rewind()} on a reader that is positioned before the
     * start of a {@link CharSequence} and asserts that {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values
     * after each call.
     */
    @Test
    public void beforeStartAdvanceRewind() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(BAR, -1);
        assertThat(reader.getCurrentPosition(), is(-1));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentChar(), is('b'));
        assertThat(reader.getCurrentCodePoint(), is(0x62));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(-1));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Tests that {@link CharSequenceReader#getCurrentChar()} throws a {@link NoSuchElementException} when the reader is positioned
     * at the end of the sequence.
     */
    @Test(expected = NoSuchElementException.class)
    public void getCurrentCharAtEnd() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO, 3);
        reader.getCurrentChar();
    }

    /**
     * Tests that {@link CharSequenceReader#getCurrentChar()} throws a {@link NoSuchElementException} when the reader is positioned
     * before the start of the sequence.
     */
    @Test(expected = NoSuchElementException.class)
    public void getCurrentCharBeforeStart() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO, -1);
        reader.getCurrentChar();
    }

    /**
     * Asserts that {@link CharSequenceReader#getCurrentChar()} returns the correct value when the reader is positioned on a BMP
     * code point.
     */
    @Test
    public void getCurrentCharBmp() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO);
        assertThat(reader.getCurrentChar(), is('f'));
    }

    /**
     * Asserts that {@link CharSequenceReader#getCurrentChar()} returns the correct value when the reader is positioned on a code
     * point that is outside the BMP.
     */
    @Test
    public void getCurrentCharNonBmp() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader("\uD83C\uDF41");
        assertThat(reader.getCurrentChar(), is('\uD83C'));
    }

    /**
     * Asserts that {@link CharSequenceReader#getCurrentCodePoint()} returns the correct value when the reader is positioned on a
     * BMP code point.
     */
    @Test
    public void getCurrentCodePointBmp() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO);
        assertThat(reader.getCurrentCodePoint(), is(0x66));
    }

    /**
     * Asserts that {@link CharSequenceReader#getCurrentCodePoint()} returns the correct value when the reader is positioned on a
     * code point that is outside the BMP.
     */
    @Test
    public void getCurrentCodePointNonBmp() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader("\uD83C\uDF41");
        assertThat(reader.getCurrentCodePoint(), is(0x1F341));
    }

    /**
     * Asserts that {@link CharSequenceReader#readSubstring(int)} returns the correct string when the specified length is equal to
     * the length of the {@link CharSequence}.
     */
    @Test
    public void readSubstringAll() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO);
        assertThat(reader.readSubstring(3), is(FOO));
        assertThat(reader.getCurrentPosition(), is(3));
    }

    /**
     * Asserts that {@link CharSequenceReader#readSubstring(int)} throws an {@link IllegalArgumentException} when the reader is
     * positioned at the end of the {@link CharSequence} and the specified length is positive.
     */
    @Test
    public void readSubstringAtEndPositiveLength() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO, 3);

        try {
            reader.readSubstring(1);
            Assert.fail("expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        }

        assertThat(reader.getCurrentPosition(), is(3));
    }

    /**
     * Asserts that {@link CharSequenceReader#readSubstring(int)} returns an empty string when the reader is positioned at the end
     * of the {@link CharSequence} and the specified length is 0.
     */
    @Test
    public void readSubstringAtEndZeroLength() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO, 3);
        assertThat(reader.readSubstring(0), is(""));
        assertThat(reader.getCurrentPosition(), is(3));
    }

    /**
     * Asserts that {@link CharSequenceReader#readSubstring(int)} throws an {@link IllegalStateException} when the reader is
     * positioned before the start of the {@link CharSequence} and the specified length is positive.
     */
    @Test
    public void readSubstringBeforeStartPositiveLength() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO, -1);

        try {
            reader.readSubstring(1);
            Assert.fail("expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
        }

        assertThat(reader.getCurrentPosition(), is(-1));
    }

    /**
     * Asserts that {@link CharSequenceReader#readSubstring(int)} throws an {@link IllegalStateException} when the reader is
     * positioned before the start of the {@link CharSequence} and the specified length is 0.
     */
    @Test
    public void readSubstringBeforeStartZeroLength() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO, -1);

        try {
            reader.readSubstring(0);
            Assert.fail("expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
        }

        assertThat(reader.getCurrentPosition(), is(-1));
    }

    /**
     * Asserts that {@link CharSequenceReader#readSubstring(int)} returns the correct string when the reader is not positioned at
     * the start of the {@link CharSequence} and the specified length is positive and less than the length of the rest of the
     * {@link CharSequence}.
     */
    @Test
    public void readSubstringFromMiddlePartial() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO, 1);
        assertThat(reader.readSubstring(1), is("o"));
        assertThat(reader.getCurrentPosition(), is(2));
    }

    /**
     * Asserts that {@link CharSequenceReader#readSubstring(int)} returns the correct string when the reader is not positioned at
     * the start of the {@link CharSequence} and the specified length is equal to the length of the rest of the {@link CharSequence}
     * .
     */
    @Test
    public void readSubstringFromMiddleRest() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(BAR, 1);
        assertThat(reader.readSubstring(2), is("ar"));
        assertThat(reader.getCurrentPosition(), is(3));
    }

    /**
     * Asserts that {@link CharSequenceReader#readSubstring(int)} throws an {@link IllegalArgumentException} when the reader is not
     * positioned at the start of the {@link CharSequence} and the specified length is too high.
     */
    @Test
    public void readSubstringFromMiddleTooLong() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO, 1);

        try {
            reader.readSubstring(3);
            Assert.fail("expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        }

        assertThat(reader.getCurrentPosition(), is(1));
    }

    /**
     * Asserts that {@link CharSequenceReader#readSubstring(int)} returns an empty string when the reader is not positioned at the
     * start of the {@link CharSequence} and the specified length is 0.
     */
    @Test
    public void readSubstringFromMiddleZeroLength() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO, 1);
        assertThat(reader.readSubstring(0), is(""));
        assertThat(reader.getCurrentPosition(), is(1));
    }

    /**
     * Asserts that {@link CharSequenceReader#readSubstring(int)} throws an {@link IllegalArgumentException} when the specified
     * length is negative.
     */
    @Test
    public void readSubstringNegativeLength() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO);

        try {
            reader.readSubstring(-1);
            Assert.fail("expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        }

        assertThat(reader.getCurrentPosition(), is(0));
    }

    /**
     * Asserts that {@link CharSequenceReader#readSubstring(int)} returns the correct string when the specified length is positive
     * and less than the length of the {@link CharSequence}.
     */
    @Test
    public void readSubstringPartial() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO);
        assertThat(reader.readSubstring(2), is("fo"));
        assertThat(reader.getCurrentPosition(), is(2));
    }

    /**
     * Asserts that {@link CharSequenceReader#readSubstring(int)} throws an {@link IllegalArgumentException} when the specified
     * length is too high.
     */
    @Test
    public void readSubstringTooLong() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO);

        try {
            reader.readSubstring(4);
            Assert.fail("expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        }

        assertThat(reader.getCurrentPosition(), is(0));
    }

    /**
     * Asserts that {@link CharSequenceReader#readSubstring(int)} returns an empty string when the specified length is 0.
     */
    @Test
    public void readSubstringZeroLength() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO);
        assertThat(reader.readSubstring(0), is(""));
        assertThat(reader.getCurrentPosition(), is(0));
    }

    /**
     * Calls {@link CharSequenceReader#rewind()} then {@link CharSequenceReader#advance()} on a reader that is positioned on the
     * last code point of a {@link CharSequence} and asserts that {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values
     * after each call.
     */
    @Test
    public void rewindAdvance() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(BAR, 2);
        assertThat(reader.getCurrentPosition(), is(2));
        assertThat(reader.getCurrentChar(), is('r'));
        assertThat(reader.getCurrentCodePoint(), is(0x72));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(1));
        assertThat(reader.getCurrentChar(), is('a'));
        assertThat(reader.getCurrentCodePoint(), is(0x61));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(2));
        assertThat(reader.getCurrentChar(), is('r'));
        assertThat(reader.getCurrentCodePoint(), is(0x72));
    }

    /**
     * Calls {@link CharSequenceReader#rewind()} then {@link CharSequenceReader#advance()} on a reader that is positioned at the
     * start of a {@link CharSequence} and asserts that {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values
     * after each call.
     */
    @Test
    public void rewindBeforeStartAdvance() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(BAR);
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentChar(), is('b'));
        assertThat(reader.getCurrentCodePoint(), is(0x62));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(-1));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.advance();
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentChar(), is('b'));
        assertThat(reader.getCurrentCodePoint(), is(0x62));
    }

    /**
     * Asserts that, following a call to {@link CharSequenceReader#rewind()}, {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values.
     * The test is performed on a {@link CharSequence} containing only BMP code points and calls {@link CharSequenceReader#rewind()}
     * multiple times.
     */
    @Test
    public void rewindBmp() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(BAR, 2);
        assertThat(reader.getCurrentPosition(), is(2));
        assertThat(reader.getCurrentChar(), is('r'));
        assertThat(reader.getCurrentCodePoint(), is(0x72));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(1));
        assertThat(reader.getCurrentChar(), is('a'));
        assertThat(reader.getCurrentCodePoint(), is(0x61));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentChar(), is('b'));
        assertThat(reader.getCurrentCodePoint(), is(0x62));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(-1));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(-1));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Asserts that, following a call to {@link CharSequenceReader#rewind()}, {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values.
     * The test is performed on a {@link CharSequence} containing a low surrogate that is not preceded by a high surrogate and calls
     * {@link CharSequenceReader#rewind()} multiple times.
     */
    @Test
    public void rewindBrokenSurrogate() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader("x\uDF41y", 2);
        assertThat(reader.getCurrentPosition(), is(2));
        assertThat(reader.getCurrentChar(), is('y'));
        assertThat(reader.getCurrentCodePoint(), is(0x79));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(1));
        assertThat(reader.getCurrentChar(), is('\uDF41'));
        assertThat(reader.getCurrentCodePoint(), is(0xDF41));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentChar(), is('x'));
        assertThat(reader.getCurrentCodePoint(), is(0x78));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(-1));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(-1));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Asserts that, following a call to {@link CharSequenceReader#rewind()}, {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values.
     * The test is performed on a {@link CharSequence} containing a low surrogate that is not preceded by a high surrogate and calls
     * {@link CharSequenceReader#rewind()} multiple times.
     */
    @Test
    public void rewindBrokenSurrogateAtStart() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader("\uDF41y", 1);
        assertThat(reader.getCurrentPosition(), is(1));
        assertThat(reader.getCurrentChar(), is('y'));
        assertThat(reader.getCurrentCodePoint(), is(0x79));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentChar(), is('\uDF41'));
        assertThat(reader.getCurrentCodePoint(), is(0xDF41));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(-1));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(-1));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Asserts that {@link CharSequenceReader#rewind()} works when the {@link CharSequence} is empty.
     */
    @Test
    public void rewindEmpty() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader("");
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(-1));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Asserts that, following a call to {@link CharSequenceReader#rewind()}, {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values.
     * The test is performed on a {@link CharSequence} containing a mix of BMP code points and non-BMP code points and calls
     * {@link CharSequenceReader#rewind()} multiple times.
     */
    @Test
    public void rewindNonBmp() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader("x\uD83C\uDF41y", 3);
        assertThat(reader.getCurrentPosition(), is(3));
        assertThat(reader.getCurrentChar(), is('y'));
        assertThat(reader.getCurrentCodePoint(), is(0x79));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(1));
        assertThat(reader.getCurrentChar(), is('\uD83C'));
        assertThat(reader.getCurrentCodePoint(), is(0x1F341));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(0));
        assertThat(reader.getCurrentChar(), is('x'));
        assertThat(reader.getCurrentCodePoint(), is(0x78));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(-1));
        assertThat(reader.getCurrentCodePoint(), is(-1));
        reader.rewind();
        assertThat(reader.getCurrentPosition(), is(-1));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Asserts that, following a call to {@link CharSequenceReader#setCurrentPosition(int)} with a position within the
     * {@link CharSequence}, {@link CharSequenceReader#getCurrentPosition()}, {@link CharSequenceReader#getCurrentChar()} and
     * {@link CharSequenceReader#getCurrentCodePoint()} return the correct values.
     */
    @Test
    public void setCurrentPosition() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO);
        reader.setCurrentPosition(2);
        assertThat(reader.getCurrentPosition(), is(2));
        assertThat(reader.getCurrentChar(), is('o'));
        assertThat(reader.getCurrentCodePoint(), is(0x6F));
    }

    /**
     * Asserts that, following a call to {@link CharSequenceReader#setCurrentPosition(int)} with a position at the end of the
     * {@link CharSequence}, {@link CharSequenceReader#getCurrentPosition()} and {@link CharSequenceReader#getCurrentCodePoint()}
     * return the correct values.
     */
    @Test
    public void setCurrentPositionAtEnd() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO);
        reader.setCurrentPosition(3);
        assertThat(reader.getCurrentPosition(), is(3));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Asserts that, following a call to {@link CharSequenceReader#setCurrentPosition(int)} with a position before the start of the
     * {@link CharSequence}, {@link CharSequenceReader#getCurrentPosition()} and {@link CharSequenceReader#getCurrentCodePoint()}
     * return the correct values.
     */
    @Test
    public void setCurrentPositionBeforeStart() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO);
        reader.setCurrentPosition(-1);
        assertThat(reader.getCurrentPosition(), is(-1));
        assertThat(reader.getCurrentCodePoint(), is(-1));
    }

    /**
     * Asserts that, following a call to {@link CharSequenceReader#setCurrentPosition(int)} with a position in the middle of a
     * surrogate pair in the {@link CharSequence}, {@link CharSequenceReader#getCurrentPosition()},
     * {@link CharSequenceReader#getCurrentChar()} and {@link CharSequenceReader#getCurrentCodePoint()} return the correct values.
     */
    @Test
    public void setCurrentPositionInMiddleOfSurrogatePair() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader("\uD83C\uDF41");
        reader.setCurrentPosition(1);
        assertThat(reader.getCurrentPosition(), is(1));
        assertThat(reader.getCurrentChar(), is('\uDF41'));
        assertThat(reader.getCurrentCodePoint(), is(0xDF41));
    }

    /**
     * Asserts that a call to {@link CharSequenceReader#setCurrentPosition(int)} with a position greater than the
     * {@link CharSequence}'s length throws an {@link IndexOutOfBoundsException}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void setCurrentPositionToHigh() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO);
        reader.setCurrentPosition(4);
    }

    /**
     * Asserts that a call to {@link CharSequenceReader#setCurrentPosition(int)} with a position less than -1 throws an
     * {@link IndexOutOfBoundsException}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void setCurrentPositionToLow() {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(FOO);
        reader.setCurrentPosition(-2);
    }

    /**
     * Creates a {@link CharSequenceReader} for the specified {@link CharSequence}.
     *
     * @param charSequence
     *            the {@link CharSequence} to read from
     * @return a new {@link CharSequenceReader}
     */
    protected abstract CharSequenceReader<?> createCharSequenceReader(CharSequence charSequence);

    /**
     * Creates a {@link CharSequenceReader} for the specified {@link CharSequence} initially positioned at the specified position.
     *
     * @param charSequence
     *            the {@link CharSequence} to read from
     * @param position
     *            the reader's initial position
     * @return a new {@link CharSequenceReader}
     */
    protected CharSequenceReader<?> createCharSequenceReader(CharSequence charSequence, int position) {
        final CharSequenceReader<?> reader = this.createCharSequenceReader(charSequence);
        reader.setCurrentPosition(position);
        return reader;
    }

}
