/*
 * Copyright (c) 2020  Kaiserpfalz EDV-Service, Roland T. Lichti.
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.commons.version;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author rlichti
 * @version 1.0.0 2020-08-26
 * @since 1.0.0 2020-08-26
 */
public class VersionTest {
    public static final String FULL_VERSION = "2.3.4-rc-1.beta-2";
    private static final Logger LOG = LoggerFactory.getLogger(VersionTest.class);
    private static final int MAJOR = 2;
    private static final int MINOR = 3;
    private static final int PATCH = 4;
    private static final Version.VersionType TYPE = Version.VersionType.NPM;
    private static final String[] SUFFIX = {"rc-1", "beta-2"};
    private Version sut;

    @BeforeAll
    public static void addLog() {
        MDC.put("test-class", "version");
    }

    @AfterAll
    public static void removeLog() {
        MDC.remove("test-class");
        MDC.remove("test");
    }

    @Test
    public void shouldMatchWithAllPartsWhenFullySet() {
        MDC.put("test", "full-definition");

        String result = sut.value();
        LOG.trace("Checking full version handling. expected={}, value={}", FULL_VERSION, result);

        assertEquals(FULL_VERSION, result);
    }

    @Test
    public void shouldBeValidatedStrictWhenMajorMinorPatchIsSet() {
        sut.validate(Version.VersionType.STRICT);

        // no exception thrown, so it's ok.
    }

    @Test
    public void shouldBeInvalidStrictWhenMajorMinorIsSet() {
        Assertions.assertThrows(VersionException.class, () -> VersionImmutable.builder().from(sut).patch(Optional.empty()).build().validate(Version.VersionType.STRICT));
    }

    @Test
    public void shouldBeInvalidStrictWhenMajorPatchIsSet() {
        Assertions.assertThrows(VersionException.class, () -> VersionImmutable.builder().from(sut).minor(Optional.empty()).build().validate(Version.VersionType.STRICT));
    }

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "2.0, true",
            "2.3.4-rc-1.beta-2, false",
            "3.0, false"
    })
    public void shouldBeGreaterThanString(final String version, final boolean expectation) {
        MDC.put("test", "greater-than-string");
        LOG.trace("Checking: expectation={}, value={}", expectation, version);
        boolean result = sut.isGreaterThan(version);

        assertEquals(expectation, result);
    }

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "2.0, true",
            "2.3.4-rc-1.beta-2, false",
            "3.0, false"
    })
    public void shouldBeGreaterThanVersion(final String version, final boolean expectation) {
        MDC.put("test", "greater-than-version");
        LOG.trace("Checking: expectation={}, value={}", expectation, version);

        boolean result = sut.isGreaterThan(Version.from(version, Version.VersionType.LOOSE));

        assertEquals(expectation, result);
    }

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "2.0, true",
            "2.3.4-rc-1.beta-2, true",
            "3.0, false"
    })
    public void shouldBeGreaterThanOrEqualToString(final String version, final boolean expectation) {
        MDC.put("test", "greater-than-or-equal-to-string");
        LOG.trace("Checking: expectation={}, value={}", expectation, version);

        boolean result = sut.isGreaterThanOrEqualTo(version);

        assertEquals(expectation, result);
    }

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "2.0, true",
            "2.3.4-rc-1.beta-2, true",
            "3.0, false"
    })

    public void shouldBeGreaterThanOrEqualToVersion(final String version, final boolean expectation) {
        MDC.put("test", "greater-than-or-equal-to-version");
        LOG.trace("Checking: expectation={}, value={}", expectation, version);

        boolean result = sut.isGreaterThanOrEqualTo(Version.from(version, Version.VersionType.LOOSE));

        assertEquals(expectation, result);
    }

    @ParameterizedTest
    @CsvSource({
            "0, false",
            "2.0, false",
            "2.3.4-rc-1.beta-2, false",
            "3.0, true"
    })
    public void shouldBeLowerThanString(final String version, final boolean expectation) {
        MDC.put("test", "lower-than-string");
        LOG.trace("Checking: expectation={}, value={}", expectation, version);
        boolean result = sut.isLowerThan(version);

        assertEquals(expectation, result);
    }

    @ParameterizedTest
    @CsvSource({
            "0, false",
            "2.0, false",
            "2.3.4-rc-1.beta-2, false",
            "3.0, true"
    })
    public void shouldBeLowerThanVersion(final String version, final boolean expectation) {
        MDC.put("test", "lower-than-version");
        LOG.trace("Checking: expectation={}, value={}", expectation, version);

        boolean result = sut.isLowerThan(Version.from(version, Version.VersionType.LOOSE));

        assertEquals(expectation, result);
    }

    @ParameterizedTest
    @CsvSource({
            "0, false",
            "2.0, false",
            "2.3.4-rc-1.beta-2, true",
            "3.0, true"
    })
    public void shouldBeLowerThanOrEqualToString(final String version, final boolean expectation) {
        MDC.put("test", "lower-than-or-equal-to-string");
        LOG.trace("Checking: expectation={}, value={}", expectation, version);

        boolean result = sut.isLowerThanOrEqualTo(version);

        assertEquals(expectation, result);
    }

    @ParameterizedTest
    @CsvSource({
            "0, false",
            "2.0, false",
            "2.3.4-rc-1.beta-2, true",
            "3.0, true"
    })

    public void shouldBeLowerThanOrEqualToVersion(final String version, final boolean expectation) {
        MDC.put("test", "lower-than-or-equal-to-version");
        LOG.trace("Checking: expectation={}, value={}", expectation, version);

        boolean result = sut.isLowerThanOrEqualTo(Version.from(version, Version.VersionType.LOOSE));

        assertEquals(expectation, result);
    }

    @BeforeEach
    public void setUpServiceUnderTest() {
        sut = VersionImmutable.builder()
                .major(MAJOR)
                .minor(MINOR)
                .patch(PATCH)
                .type(TYPE)
                .suffixes(SUFFIX)
                .build();
    }
}
