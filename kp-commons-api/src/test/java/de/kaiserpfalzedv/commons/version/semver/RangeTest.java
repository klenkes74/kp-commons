/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-present Vincent DURMONT <vdurmont@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.kaiserpfalzedv.commons.version.semver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RangeTest {
    @Test
    public void isSatisfiedBy_EQ() {
        Range range = new Range("1.2.3", Range.RangeOperator.EQ);

        // SAME VERSION
        assertTrue(range.isSatisfiedBy("1.2.3"));

        // GREATER
        assertFalse(range.isSatisfiedBy("2.2.3")); // major
        assertFalse(range.isSatisfiedBy("1.3.3")); // minor
        assertFalse(range.isSatisfiedBy("1.2.4")); // patch

        // LOWER
        assertFalse(range.isSatisfiedBy("0.2.3")); // major
        assertFalse(range.isSatisfiedBy("1.1.3")); // minor
        assertFalse(range.isSatisfiedBy("1.2.2")); // patch
        Range rangeWithSuffix = new Range("1.2.3-alpha", Range.RangeOperator.EQ);
        assertFalse(rangeWithSuffix.isSatisfiedBy("1.2.3")); // null suffix
        assertFalse(rangeWithSuffix.isSatisfiedBy("1.2.3-beta")); // non null suffix
    }

    @Test
    public void isSatisfiedBy_LT() {
        Range range = new Range("1.2.3", Range.RangeOperator.LT);

        assertFalse(range.isSatisfiedBy("1.2.3"));
        assertFalse(range.isSatisfiedBy("1.2.4"));
        assertTrue(range.isSatisfiedBy("1.2.2"));
    }

    @Test
    public void isSatisfiedBy_LTE() {
        Range range = new Range("1.2.3", Range.RangeOperator.LTE);

        assertTrue(range.isSatisfiedBy("1.2.3"));
        assertFalse(range.isSatisfiedBy("1.2.4"));
        assertTrue(range.isSatisfiedBy("1.2.2"));
    }

    @Test
    public void isSatisfiedBy_GT() {
        Range range = new Range("1.2.3", Range.RangeOperator.GT);

        assertFalse(range.isSatisfiedBy("1.2.3"));
        assertFalse(range.isSatisfiedBy("1.2.2"));
        assertTrue(range.isSatisfiedBy("1.2.4"));
    }

    @Test
    public void isSatisfiedBy_GTE() {
        Range range = new Range("1.2.3", Range.RangeOperator.GTE);

        assertTrue(range.isSatisfiedBy("1.2.3"));
        assertFalse(range.isSatisfiedBy("1.2.2"));
        assertTrue(range.isSatisfiedBy("1.2.4"));
    }

    @Test
    public void prettyString() {
        assertEquals("=1.2.3", new Range("1.2.3", Range.RangeOperator.EQ).toString());
        assertEquals("<1.2.3", new Range("1.2.3", Range.RangeOperator.LT).toString());
        assertEquals("<=1.2.3", new Range("1.2.3", Range.RangeOperator.LTE).toString());
        assertEquals(">1.2.3", new Range("1.2.3", Range.RangeOperator.GT).toString());
        assertEquals(">=1.2.3", new Range("1.2.3", Range.RangeOperator.GTE).toString());
    }

    @Test
    public void testEquals() {
        Range range = new Range("1.2.3", Range.RangeOperator.EQ);

        assertEquals(range, range);
        assertNotEquals(range, null);
        //noinspection AssertBetweenInconvertibleTypes
        assertNotEquals(range, "string");
        assertNotEquals(range, new Range("1.2.3", Range.RangeOperator.GTE));
        assertNotEquals(range, new Range("1.2.4", Range.RangeOperator.EQ));
    }

    @Test
    public void testHashCode() {
        Range range = new Range("1.2.3", Range.RangeOperator.EQ);

        assertEquals(range.hashCode(), range.hashCode());
        assertNotEquals(range.hashCode(), new Range("1.2.3", Range.RangeOperator.GTE).hashCode());
        assertNotEquals(range.hashCode(), new Range("1.2.4", Range.RangeOperator.EQ).hashCode());
    }
}
