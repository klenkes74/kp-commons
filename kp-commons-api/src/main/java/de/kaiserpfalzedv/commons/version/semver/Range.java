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


import java.util.Objects;

// TODO doc
public class Range {
    protected final Semver version;
    protected final RangeOperator op;

    public Range(final Semver version, @SuppressWarnings("CdiInjectionPointsInspection") final RangeOperator op) {
        this.version = version;
        this.op = op;
    }

    public Range(String version, RangeOperator op) {
        this(new Semver(version, Semver.SemverType.LOOSE), op);
    }

    public boolean isSatisfiedBy(String version) {
        return this.isSatisfiedBy(new Semver(version, this.version.getType()));
    }

    public boolean isSatisfiedBy(Semver version) {
        switch (this.op) {
            case EQ:
                return version.isEquivalentTo(this.version);
            case LT:
                return version.isLowerThan(this.version);
            case LTE:
                return version.isLowerThan(this.version) || version.isEquivalentTo(this.version);
            case GT:
                return version.isGreaterThan(this.version);
            case GTE:
                return version.isGreaterThan(this.version) || version.isEquivalentTo(this.version);
        }

        throw new RuntimeException("Code error. Unknown RangeOperator: " + this.op); // Should never happen
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Range)) return false;
        Range range = (Range) o;
        return Objects.equals(version, range.version) &&
                op == range.op;
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, op);
    }

    @Override
    public String toString() {
        return this.op.asString() + this.version;
    }

    public enum RangeOperator {
        /**
         * The version and the requirement are equivalent
         */
        EQ("="),

        /**
         * The version is lower than the requirent
         */
        LT("<"),

        /**
         * The version is lower than or equivalent to the requirement
         */
        LTE("<="),

        /**
         * The version is greater than the requirement
         */
        GT(">"),

        /**
         * The version is greater than or equivalent to the requirement
         */
        GTE(">=");

        private final String s;

        RangeOperator(@SuppressWarnings("CdiInjectionPointsInspection") final String s) {
            this.s = s;
        }

        public String asString() {
            return s;
        }
    }
}
