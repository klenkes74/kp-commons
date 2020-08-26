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


import de.kaiserpfalzedv.commons.version.VersionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SpellCheckingInspection")
public class SemverTest {
    private static void assertIsSemver(Semver version, String value, @SuppressWarnings("SameParameterValue") Integer major, Integer minor, Integer patch, String[] suffixTokens, String build) {
        assertEquals(value, version.getValue());
        assertEquals(major, version.getMajor());
        assertEquals(minor, version.getMinor());
        assertEquals(patch, version.getPatch());
        assertEquals(suffixTokens.length, version.getSuffixTokens().length);
        for (int i = 0; i < suffixTokens.length; i++) {
            assertEquals(suffixTokens[i], version.getSuffixTokens()[i]);
        }
        assertEquals(build, version.getBuild());
    }

    @Test
    public void constructor_with_empty_build_fails() {
        Assertions.assertThrows(VersionException.class, () -> new Semver("1.0.0+"));
    }

    @Test
    public void default_constructor_test_full_version() {
        String version = "1.2.3-beta.11+sha.0nsfgkjkjsdf";
        Semver semver = new Semver(version);
        assertIsSemver(semver, version, 1, 2, 3, new String[]{"beta", "11"}, "sha.0nsfgkjkjsdf");
    }

    @Test
    public void default_constructor_test_only_major_and_minor() {
        Assertions.assertThrows(VersionException.class, () -> {
            String version = "1.2-beta.11+sha.0nsfgkjkjsdf";
            new Semver(version);
        });
    }

    @Test
    public void default_constructor_test_only_major() {
        Assertions.assertThrows(VersionException.class, () -> {
            String version = "1-beta.11+sha.0nsfgkjkjsdf";
            new Semver(version);
        });
    }

    @Test
    public void npm_constructor_test_full_version() {
        String version = "1.2.3-beta.11+sha.0nsfgkjkjsdf";
        Semver semver = new Semver(version, Semver.SemverType.NPM);
        assertIsSemver(semver, version, 1, 2, 3, new String[]{"beta", "11"}, "sha.0nsfgkjkjsdf");
    }

    @Test
    public void npm_constructor_test_only_major_and_minor() {
        String version = "1.2-beta.11+sha.0nsfgkjkjsdf";
        Semver semver = new Semver(version, Semver.SemverType.NPM);
        assertIsSemver(semver, version, 1, 2, null, new String[]{"beta", "11"}, "sha.0nsfgkjkjsdf");
    }

    @Test
    public void npm_constructor_test_only_major() {
        String version = "1-beta.11+sha.0nsfgkjkjsdf";
        Semver semver = new Semver(version, Semver.SemverType.NPM);
        assertIsSemver(semver, version, 1, null, null, new String[]{"beta", "11"}, "sha.0nsfgkjkjsdf");
    }

    @Test
    public void npm_constructor_with_leading_v() {
        String version = "v1.2.3-beta.11+sha.0nsfgkjkjsdf";
        Semver semver = new Semver(version, Semver.SemverType.NPM);
        assertIsSemver(semver, "1.2.3-beta.11+sha.0nsfgkjkjsdf", 1, 2, 3, new String[]{"beta", "11"}, "sha.0nsfgkjkjsdf");

        String versionWithSpace = "v 1.2.3-beta.11+sha.0nsfgkjkjsdf";
        Semver semverWithSpace = new Semver(versionWithSpace, Semver.SemverType.NPM);
        assertIsSemver(semverWithSpace, "1.2.3-beta.11+sha.0nsfgkjkjsdf", 1, 2, 3, new String[]{"beta", "11"}, "sha.0nsfgkjkjsdf");
    }

    @Test
    public void cocoapods_constructor_test_full_version() {
        String version = "1.2.3-beta.11+sha.0nsfgkjkjsdf";
        Semver semver = new Semver(version, Semver.SemverType.COCOAPODS);
        assertIsSemver(semver, version, 1, 2, 3, new String[]{"beta", "11"}, "sha.0nsfgkjkjsdf");
    }

    @Test
    public void cocoapods_constructor_test_only_major_and_minor() {
        String version = "1.2-beta.11+sha.0nsfgkjkjsdf";
        Semver semver = new Semver(version, Semver.SemverType.COCOAPODS);
        assertIsSemver(semver, version, 1, 2, null, new String[]{"beta", "11"}, "sha.0nsfgkjkjsdf");
    }

    @Test
    public void cocoapods_constructor_test_only_major() {
        String version = "1-beta.11+sha.0nsfgkjkjsdf";
        Semver semver = new Semver(version, Semver.SemverType.COCOAPODS);
        assertIsSemver(semver, version, 1, null, null, new String[]{"beta", "11"}, "sha.0nsfgkjkjsdf");
    }

    @Test
    public void loose_constructor_test_only_major_and_minor() {
        String version = "1.2-beta.11+sha.0nsfgkjkjsdf";
        Semver semver = new Semver(version, Semver.SemverType.LOOSE);
        assertIsSemver(semver, version, 1, 2, null, new String[]{"beta", "11"}, "sha.0nsfgkjkjsdf");
    }

    @Test
    public void loose_constructor_test_only_major() {
        String version = "1-beta.11+sha.0nsfgkjkjsdf";
        Semver semver = new Semver(version, Semver.SemverType.LOOSE);
        assertIsSemver(semver, version, 1, null, null, new String[]{"beta", "11"}, "sha.0nsfgkjkjsdf");
    }

    @Test
    public void default_constructor_test_myltiple_hyphen_signs() {
        String version = "1.2.3-beta.1-1.ab-c+sha.0nsfgkjkjs-df";
        Semver semver = new Semver(version);
        assertIsSemver(semver, version, 1, 2, 3, new String[]{"beta", "1-1", "ab-c"}, "sha.0nsfgkjkjs-df");
    }

    @Test
    public void statisfies_works_will_all_the_types() {
        // Used to prevent bugs when we add a new type
        for (Semver.SemverType type : Semver.SemverType.values()) {
            String version = "1.2.3";
            Semver semver = new Semver(version, type);
            assertTrue(semver.satisfies("1.2.3"));
            assertFalse(semver.satisfies("4.5.6"));
        }
    }

    @Test
    public void isGreaterThan_test() {
        // 1.0.0-alpha < 1.0.0-alpha.1 < 1.0.0-alpha.beta < 1.0.0-beta < 1.0.0-beta.2 < 1.0.0-beta.11 < 1.0.0-rc.1 < 1.0.0

        assertTrue(new Semver("1.0.0-alpha.1").isGreaterThan("1.0.0-alpha"));
        assertTrue(new Semver("1.0.0-alpha.beta").isGreaterThan("1.0.0-alpha.1"));
        assertTrue(new Semver("1.0.0-beta").isGreaterThan("1.0.0-alpha.beta"));
        assertTrue(new Semver("1.0.0-beta.2").isGreaterThan("1.0.0-beta"));
        assertTrue(new Semver("1.0.0-beta.11").isGreaterThan("1.0.0-beta.2"));
        assertTrue(new Semver("1.0.0-rc.1").isGreaterThan("1.0.0-beta.11"));
        assertTrue(new Semver("1.0.0").isGreaterThan("1.0.0-rc.1"));


        assertFalse(new Semver("1.0.0-alpha").isGreaterThan("1.0.0-alpha.1"));
        assertFalse(new Semver("1.0.0-alpha.1").isGreaterThan("1.0.0-alpha.beta"));
        assertFalse(new Semver("1.0.0-alpha.beta").isGreaterThan("1.0.0-beta"));
        assertFalse(new Semver("1.0.0-beta").isGreaterThan("1.0.0-beta.2"));
        assertFalse(new Semver("1.0.0-beta.2").isGreaterThan("1.0.0-beta.11"));
        assertFalse(new Semver("1.0.0-beta.11").isGreaterThan("1.0.0-rc.1"));
        assertFalse(new Semver("1.0.0-rc.1").isGreaterThan("1.0.0"));

        assertFalse(new Semver("1.0.0").isGreaterThan("1.0.0"));
        assertFalse(new Semver("1.0.0-alpha.12").isGreaterThan("1.0.0-alpha.12"));

        assertFalse(new Semver("0.0.1").isGreaterThan("5.0.0"));
        assertFalse(new Semver("1.0.0-alpha.12.ab-c").isGreaterThan("1.0.0-alpha.12.ab-c"));
    }

    @Test
    public void isLowerThan_test() {
        // 1.0.0-alpha < 1.0.0-alpha.1 < 1.0.0-alpha.beta < 1.0.0-beta < 1.0.0-beta.2 < 1.0.0-beta.11 < 1.0.0-rc.1 < 1.0.0

        assertFalse(new Semver("1.0.0-alpha.1").isLowerThan("1.0.0-alpha"));
        assertFalse(new Semver("1.0.0-alpha.beta").isLowerThan("1.0.0-alpha.1"));
        assertFalse(new Semver("1.0.0-beta").isLowerThan("1.0.0-alpha.beta"));
        assertFalse(new Semver("1.0.0-beta.2").isLowerThan("1.0.0-beta"));
        assertFalse(new Semver("1.0.0-beta.11").isLowerThan("1.0.0-beta.2"));
        assertFalse(new Semver("1.0.0-rc.1").isLowerThan("1.0.0-beta.11"));
        assertFalse(new Semver("1.0.0").isLowerThan("1.0.0-rc.1"));

        assertTrue(new Semver("1.0.0-alpha").isLowerThan("1.0.0-alpha.1"));
        assertTrue(new Semver("1.0.0-alpha.1").isLowerThan("1.0.0-alpha.beta"));
        assertTrue(new Semver("1.0.0-alpha.beta").isLowerThan("1.0.0-beta"));
        assertTrue(new Semver("1.0.0-beta").isLowerThan("1.0.0-beta.2"));
        assertTrue(new Semver("1.0.0-beta.2").isLowerThan("1.0.0-beta.11"));
        assertTrue(new Semver("1.0.0-beta.11").isLowerThan("1.0.0-rc.1"));
        assertTrue(new Semver("1.0.0-rc.1").isLowerThan("1.0.0"));

        assertFalse(new Semver("1.0.0").isLowerThan("1.0.0"));
        assertFalse(new Semver("1.0.0-alpha.12").isLowerThan("1.0.0-alpha.12"));
        assertFalse(new Semver("1.0.0-alpha.12.x-yz").isLowerThan("1.0.0-alpha.12.x-yz"));
    }

    @Test
    public void isEquivalentTo_isEqualTo_and_build() {
        Semver version = new Semver("1.0.0+ksadhjgksdhgksdhgfj");
        String version2 = "1.0.0+sdgfsdgsdhsdfgdsfgf";
        assertFalse(version.isEqualTo(version2));
        assertTrue(version.isEquivalentTo(version2));
    }

    @Test
    public void statisfies_calls_the_requirement() {
        Requirement req = Mockito.mock(Requirement.class);
        Semver version = new Semver("1.2.2");
        version.satisfies(req);
        Mockito.verify(req).isSatisfiedBy(version);
    }

    @Test
    public void withIncMajor_test() {
        Semver version = new Semver("1.2.3-Beta.4+SHA123456789");
        version.withIncMajor(2).isEqualTo("3.2.3-Beta.4+SHA123456789");
    }

    @Test
    public void withIncMinor_test() {
        Semver version = new Semver("1.2.3-Beta.4+SHA123456789");
        version.withIncMinor(2).isEqualTo("1.4.3-Beta.4+SHA123456789");
    }

    @Test
    public void withIncPatch_test() {
        Semver version = new Semver("1.2.3-Beta.4+SHA123456789");
        version.withIncPatch(2).isEqualTo("1.2.5-Beta.4+SHA123456789");
    }

    @Test
    public void withClearedSuffix_test() {
        Semver version = new Semver("1.2.3-Beta.4+SHA123456789");
        version.withClearedSuffix().isEqualTo("1.2.3+SHA123456789");
    }

    @Test
    public void withClearedBuild_test() {
        Semver version = new Semver("1.2.3-Beta.4+sha123456789");
        version.withClearedBuild().isEqualTo("1.2.3-Beta.4");
    }

    @Test
    public void withClearedBuild_test_multiple_hyphen_signs() {
        Semver version = new Semver("1.2.3-Beta.4-test+sha12345-6789");
        version.withClearedBuild().isEqualTo("1.2.3-Beta.4-test");
    }

    @Test
    public void withClearedSuffixAndBuild_test() {
        Semver version = new Semver("1.2.3-Beta.4+SHA123456789");
        version.withClearedSuffixAndBuild().isEqualTo("1.2.3");
    }

    @Test
    public void withSuffix_test_change_suffix() {
        Semver version = new Semver("1.2.3-Alpha.4+SHA123456789");
        Semver result = version.withSuffix("Beta.1");

        assertEquals("1.2.3-Beta.1+SHA123456789", result.toString());
        assertArrayEquals(new String[]{"Beta", "1"}, result.getSuffixTokens());
    }

    @Test
    public void withSuffix_test_add_suffix() {
        Semver version = new Semver("1.2.3+SHA123456789");
        Semver result = version.withSuffix("Beta.1");

        assertEquals("1.2.3-Beta.1+SHA123456789", result.toString());
        assertArrayEquals(new String[]{"Beta", "1"}, result.getSuffixTokens());
    }

    @Test
    public void withBuild_test_change_build() {
        Semver version = new Semver("1.2.3-Alpha.4+SHA123456789");
        Semver result = version.withBuild("SHA987654321");

        assertEquals("1.2.3-Alpha.4+SHA987654321", result.toString());
        assertEquals("SHA987654321", result.getBuild());
    }

    @Test
    public void withBuild_test_add_build() {
        Semver version = new Semver("1.2.3-Alpha.4");
        Semver result = version.withBuild("SHA987654321");

        assertEquals("1.2.3-Alpha.4+SHA987654321", result.toString());
        assertEquals("SHA987654321", result.getBuild());
    }

    @Test
    public void nextMajor_test() {
        Semver version = new Semver("1.2.3-beta.4+sha123456789");
        version.nextMajor().isEqualTo("2.0.0");
    }

    @Test
    public void nextMinor_test() {
        Semver version = new Semver("1.2.3-beta.4+sha123456789");
        version.nextMinor().isEqualTo("1.3.0");
    }

    @Test
    public void nextPatch_test() {
        Semver version = new Semver("1.2.3-beta.4+sha123456789");
        version.nextPatch().isEqualTo("1.2.4");
    }

    @Test
    public void toStrict_test() {
        String[][] versionGroups = new String[][]{
                new String[]{"3.0.0-beta.4+sha123456789", "3.0-beta.4+sha123456789", "3-beta.4+sha123456789"},
                new String[]{"3.0.0+sha123456789", "3.0+sha123456789", "3+sha123456789"},
                new String[]{"3.0.0-beta.4", "3.0-beta.4", "3-beta.4"},
                new String[]{"3.0.0", "3.0", "3"},
        };

        Semver.SemverType[] types = new Semver.SemverType[]{
                Semver.SemverType.NPM,
                Semver.SemverType.IVY,
                Semver.SemverType.LOOSE,
                Semver.SemverType.COCOAPODS,
        };

        for (String[] versions : versionGroups) {
            Semver strict = new Semver(versions[0]);
            assertEquals(strict, strict.toStrict());
            for (Semver.SemverType type : types) {
                for (String version : versions) {
                    Semver sem = new Semver(version, type);
                    assertEquals(strict, sem.toStrict());
                }
            }
        }
    }

    @Test
    public void diff() {
        Semver sem = new Semver("1.2.3-beta.4+sha899d8g79f87");
        assertEquals(Semver.VersionDiff.NONE, sem.diff("1.2.3-beta.4+sha899d8g79f87"));
        assertEquals(Semver.VersionDiff.MAJOR, sem.diff("2.3.4-alpha.5+sha32iddfu987"));
        assertEquals(Semver.VersionDiff.MINOR, sem.diff("1.3.4-alpha.5+sha32iddfu987"));
        assertEquals(Semver.VersionDiff.PATCH, sem.diff("1.2.4-alpha.5+sha32iddfu987"));
        assertEquals(Semver.VersionDiff.SUFFIX, sem.diff("1.2.3-alpha.4+sha32iddfu987"));
        assertEquals(Semver.VersionDiff.SUFFIX, sem.diff("1.2.3-beta.5+sha32iddfu987"));
        assertEquals(Semver.VersionDiff.BUILD, sem.diff("1.2.3-beta.4+sha32iddfu987"));
        assertEquals(Semver.VersionDiff.BUILD, sem.diff("1.2.3-beta.4+sha899-d8g79f87"));
    }

    @Test
    public void compareTo_test() {
        // GIVEN
        Semver[] array = new Semver[]{
                new Semver("1.2.3"),
                new Semver("1.2.3-rc3"),
                new Semver("1.2.3-rc2"),
                new Semver("1.2.3-rc1"),
                new Semver("1.2.2"),
                new Semver("1.2.2-rc2"),
                new Semver("1.2.2-rc1"),
                new Semver("1.2.0")
        };
        int len = array.length;
        List<Semver> list = new ArrayList<>(len);
        Collections.addAll(list, array);

        // WHEN
        Collections.sort(list);

        // THEN
        for (int i = 0; i < list.size(); i++) {
            assertEquals(array[len - 1 - i], list.get(i));
        }
    }

    @Test
    public void compareTo_without_path_or_minor() {
        assertTrue(new Semver("1.2.3", Semver.SemverType.LOOSE).isGreaterThan("1.2"));
        assertTrue(new Semver("1.3", Semver.SemverType.LOOSE).isGreaterThan("1.2.3"));
        assertTrue(new Semver("1.2.3", Semver.SemverType.LOOSE).isGreaterThan("1"));
        assertTrue(new Semver("2", Semver.SemverType.LOOSE).isGreaterThan("1.2.3"));
    }

    @Test
    public void getValue_returns_the_original_value_trimmed_and_with_the_same_case() {
        String version = "  1.2.3-BETA.11+sHa.0nSFGKjkjsdf  ";
        Semver semver = new Semver(version);
        assertEquals("1.2.3-BETA.11+sHa.0nSFGKjkjsdf", semver.getValue());
    }

    @Test
    public void compareTo_with_buildNumber() {
        Semver v3 = new Semver("1.24.1-rc3+903423.234");
        Semver v4 = new Semver("1.24.1-rc3+903423.235");
        assertEquals(0, v3.compareTo(v4));
    }

    @Test
    public void isStable_test() {
        assertTrue(new Semver("1.2.3+sHa.0nSFGKjkjsdf").isStable());
        assertTrue(new Semver("1.2.3").isStable());
        assertFalse(new Semver("1.2.3-BETA.11+sHa.0nSFGKjkjsdf").isStable());
        assertFalse(new Semver("0.1.2+sHa.0nSFGKjkjsdf").isStable());
        assertFalse(new Semver("0.1.2").isStable());
    }
}
