/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-present Vincent DURMONT <vdurmont@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the"Software"), to deal
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

import de.kaiserpfalzedv.commons.version.semver.Semver.SemverType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NpmSemverTest {
    private static final Logger LOG = LoggerFactory.getLogger(NpmSemverTest.class);

    @BeforeAll
    public static void addLogInfo() {
        MDC.put("test-class", "npm-semver");
    }

    @AfterAll
    public static void removeLogInfo() {
        MDC.remove("test-class");
        MDC.remove("test");
    }

    @DisplayName("Npm Version test")
    @ParameterizedTest(name = "\"{0}\" should match \"{1}\": {2}")
    @CsvSource({
            "1.2.3, 1.2.3, true",
            "1.2.4, 1.2.3, false",
            "1.2.3, 1.2, true",
            "1.2.4, 1.3, false",
            "1.2.3, 1, true",
            "1.2.4, 2, false",
            "1.2.4-beta+exp.sha.5114f85, 1.2.3 - 2.3.4, false",
            "1.2.4, 1.2.3 - 2.3.4, true",
            "1.2.3, 1.2.3 - 2.3.4, true",
            "2.3.4, 1.2.3 - 2.3.4, true",
            "2.3.0-alpha, 1.2.3 - 2.3.0-beta, true",
            "2.3.4, 1.2.3 - 2.3, true",
            "2.3.4, 1.2.3 - 2, true",
            "4.4, 3.X - 4.X, true",
            "1.0.0, 1.2.3 - 2.3.4, false",
            "3.0.0, 1.2.3 - 2.3.4, false",
            "2.4.3, 1.2.3 - 2.3, false",
            "2.3.0-rc1, 1.2.3 - 2.3.0-beta, false",
            "3.0.0, 1.2.3 - 2, false",
            "3.1.5, '', true",
            "3.1.5, *, true",
            "0.0.0, *, true",
            "1.0.0-beta, *, true",
            "3.1.5-beta, 3.1.x, false",
            "3.1.5-beta+exp.sha.5114f85, 3.1.x, false",
            "3.1.5+exp.sha.5114f85, 3.1.x, true",
            "3.1.5, 3.1.x, true",
            "3.1.5, 3.1.X, true",
            "3.1.5, 3.x, true",
            "3.1.5, 3.*, true",
            "3.1.5, 3.1, true",
            "3.1.5, 3, true",
            "3.2.5, 3.1.x, false",
            "3.0.5, 3.1.x, false",
            "4.0.0, 3.x, false",
            "2.0.0, 3.x, false",
            "3.2.5, 3.1, false",
            "3.0.5, 3.1, false",
            "4.0.0, 3, false",
            "2.0.0, 3, false",
            "1.2.4-beta, ~1.2.3, false",
            "1.2.4-beta+exp.sha.5114f85, ~1.2.3, false",
            "1.2.3, ~1.2.3, true",
            "1.2.7, ~1.2.3, true",
            "1.2.2, ~1.2, true",
            "1.2.0, ~1.2, true",
            "1.3.0, ~1, true",
            "1.0.0, ~1, true",
            "1.2.3, ~1.2.3-beta.2, true",
            "1.2.3-beta.4, ~1.2.3-beta.2, true",
            "1.2.4, ~1.2.3-beta.2, true",
            "1.3.0, ~1.2.3, false",
            "1.2.2, ~1.2.3, false",
            "1.1.0, ~1.2, false",
            "1.3.0, ~1.2, false",
            "2.0.0, ~1, false",
            "0.0.0, ~1, false",
            "1.2.3-beta.1, ~1.2.3-beta.2, false",
            "1.2.3, ^1.2.3, true",
            "1.2.4, ^1.2.3, true",
            "1.3.0, ^1.2.3, true",
            "0.2.3, ^0.2.3, true",
            "0.2.4, ^0.2.3, true",
            "0.0.3, ^0.0.3, true",
            "0.0.3+exp.sha.5114f85, ^0.0.3, true",
            "0.0.3, ^0.0.3-beta, true",
            "0.0.3-pr.2, ^0.0.3-beta, true",
            "1.2.2, ^1.2.3, false",
            "2.0.0, ^1.2.3, false",
            "0.2.2, ^0.2.3, false",
            "0.3.0, ^0.2.3, false",
            "0.0.4, ^0.0.3, false",
            "0.0.3-alpha, ^0.0.3-beta, false",
            "0.0.4, ^0.0.3-beta, false",
            "2.0.0, =2.0.0, true",
            "2.0.0, =2.0, true",
            "2.0.1, =2.0, true",
            "2.0.0, =2, true",
            "2.0.1, =2, true",
            "2.0.1, =2.0.0, false",
            "1.9.9, =2.0.0, false",
            "1.9.9, =2.0, false",
            "1.9.9, =2, false",
            "2.0.1, >2.0.0, true",
            "3.0.0, >2.0.0, true",
            "3.0.0, >2.0, true",
            "3.0.0, >2, true",
            "2.0.0, >2.0.0, false",
            "1.9.9, >2.0.0, false",
            "2.0.0, >2.0, false",
            "1.9.9, >2.0, false",
            "2.0.1, >2, false",
            "2.0.0, >2, false",
            "1.9.9, >2, false",
            "1.9.9, <2.0.0, true",
            "1.9.9, <2.0, true",
            "1.9.9, <2, true",
            "2.0.0, <2.0.0, false",
            "2.0.1, <2.0.0, false",
            "3.0.0, <2.0.0, false",
            "2.0.0, <2.0, false",
            "2.0.1, <2.0, false",
            "3.0.0, <2.0, false",
            "2.0.0, <2, false",
            "2.0.1, <2, false",
            "3.0.0, <2, false",
            "2.0.0, >=2.0.0, true",
            "2.0.1, >=2.0.0, true",
            "3.0.0, >=2.0.0, true",
            "2.0.0, >=2.0, true",
            "3.0.0, >=2.0, true",
            "2.0.0, >=2, true",
            "2.0.1, >=2, true",
            "3.0.0, >=2, true",
            "1.9.9, >=2.0.0, false",
            "1.9.9, >=2.0, false",
            "1.9.9, >=2, false",
            "1.9.9, <=2.0.0, true",
            "2.0.0, <=2.0.0, true",
            "1.9.9, <=2.0, true",
            "2.0.0, <=2.0, true",
            "2.0.1, <=2.0, true",
            "1.9.9, <=2, true",
            "2.0.0, <=2, true",
            "2.0.1, <=2, true",
            "2.0.1, <=2.0.0, false",
            "3.0.0, <=2.0.0, false",
            "3.0.0, <=2.0, false",
            "3.0.0, <=2, false",
            "2.0.1, >2.0.0 <3.0.0, true",
            "2.0.1, >2.0 <3.0, false",
            "1.2.0, 1.2 <1.2.8, true",
            "1.2.7, 1.2 <1.2.8, true",
            "1.1.9, 1.2 <1.2.8, false",
            "1.2.9, 1.2 <1.2.8, false",
            "1.2.3, 1.2.3 || 1.2.4, true",
            "1.2.4, 1.2.3 || 1.2.4, true",
            "1.2.5, 1.2.3 || 1.2.4, false",
            "1.2.2, >1.2.1 <1.2.8 || >2.0.0, true",
            "1.2.7, >1.2.1 <1.2.8 || >2.0.0, true",
            "2.0.1, >1.2.1 <1.2.8 || >2.0.0, true",
            "1.2.1, >1.2.1 <1.2.8 || >2.0.0, false",
            "2.0.0, >1.2.1 <1.2.8 || >2.0.0, false",
            "1.2.2, >1.2.1 <1.2.8 || >2.0.0 <3.0.0, true",
            "1.2.7, >1.2.1 <1.2.8 || >2.0.0 <3.0.0, true",
            "2.0.1, >1.2.1 <1.2.8 || >2.0.0 <3.0.0, true",
            "2.5.0, >1.2.1 <1.2.8 || >2.0.0 <3.0.0, true",
            "1.2.1, >1.2.1 <1.2.8 || >2.0.0 <3.0.0, false",
            "1.2.8, >1.2.1 <1.2.8 || >2.0.0 <3.0.0, false",
            "2.0.0, >1.2.1 <1.2.8 || >2.0.0 <3.0.0, false",
            "3.0.0, >1.2.1 <1.2.8 || >2.0.0 <3.0.0, false",
            "1.2.2, 1.2.2 - 1.2.7 || 2.0.1 - 2.9.9, true",
            "1.2.7, 1.2.2 - 1.2.7 || 2.0.1 - 2.9.9, true",
            "2.0.1, 1.2.2 - 1.2.7 || 2.0.1 - 2.9.9, true",
            "2.5.0, 1.2.2 - 1.2.7 || 2.0.1 - 2.9.9, true",
            "1.2.1, 1.2.2 - 1.2.7 || 2.0.1 - 2.9.9, false",
            "1.2.8, 1.2.2 - 1.2.7 || 2.0.1 - 2.9.9, false",
            "2.0.0, 1.2.2 - 1.2.7 || 2.0.1 - 2.9.9, false",
            "3.0.0, 1.2.2 - 1.2.7 || 2.0.1 - 2.9.9, false",
            "1.2.0, 1.2 <1.2.8 || >2.0.0, true",
            "1.2.7, 1.2 <1.2.8 || >2.0.0, true",
            "1.2.7, 1.2 <1.2.8 || >2.0.0, true",
            "2.0.1, 1.2 <1.2.8 || >2.0.0, true",
            "1.1.0, 1.2 <1.2.8 || >2.0.0, false",
            "1.2.9, 1.2 <1.2.8 || >2.0.0, false",
            "2.0.0, 1.2 <1.2.8 || >2.0.0, false",
    })
    public void test(String v, String r, boolean e) {
        MDC.put("test", "checking-requirement");
        LOG.trace("Checking: version='{}', requirement='{}', expectation='{}'", v, r, e);

        assertEquals(e, new Semver(v, SemverType.NPM).satisfies(r));
    }
}
