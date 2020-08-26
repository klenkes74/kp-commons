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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.commons.api.Immutable;
import de.kaiserpfalzedv.commons.version.semver.Requirement;
import de.kaiserpfalzedv.commons.version.semver.Semver;
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.Optional;

/**
 * A semantic version. Based on the interface of
 * <a href=https://github.com/vdurmont/semver4j>semver4j</a>.
 *
 * @author rlichti
 * @version 1.0.0 2020-08-26
 * @since 1.0.0 2020-08-26
 */
@Immutable
@Value.Immutable
@JsonSerialize(as = VersionImmutable.class)
@JsonDeserialize(builder = VersionImmutable.Builder.class)
public interface Version extends Serializable {
    static Version from(final Version orig) {
        return VersionImmutable.copyOf(orig);
    }

    static Version from(final String version, final VersionType type) {
        Semver semver = new Semver(version, Semver.SemverType.valueOf(type.name()));

        VersionImmutable.Builder result = VersionImmutable.builder()
                .major(semver.getMajor());

        if (semver.getMinor() != null)
            result.minor(semver.getMinor());

        if (semver.getPatch() != null)
            result.patch(semver.getPatch());

        if (semver.getSuffixTokens().length > 0)
            result.suffixes(semver.getSuffixTokens());

        if (semver.getBuild() != null)
            result.build(semver.getBuild());

        return result.build();
    }

    /**
     * @return the major version part.
     */
    int major();

    /**
     * @return the minor version part (if set).
     */
    Optional<Integer> minor();

    /**
     * @return the patch level (if set).
     */
    Optional<Integer> patch();

    /**
     * @return array of suffixes (if set).
     */
    Optional<String[]> suffixes();

    /**
     * @return build number (if set).
     */
    Optional<String> build();

    /**
     * @return type of this version.
     */
    Optional<VersionType> type();

    @Value.Default
    default String value() {
        return semver().getValue();
    }

    /**
     * Converts the Version object into a Semver object. Used internally to access the backend functions of Semver until
     * they are replaced by original code. Never use it! It will be removed without further notice!
     *
     * @return the version as backend object.
     * @deprecated this method is used internally - it will be removed as soon as the semver backend has been removed.
     */
    @Deprecated
    @Value.Default
    default Semver semver() {
        return Semver.create(Semver.SemverType.valueOf(type().orElse(VersionType.LOOSE).name()),
                             major(), minor().orElse(null), patch().orElse(null),
                             suffixes().orElse(new String[0]),
                             build().orElse(null)
        );
    }


    @Value.Default
    default void validate(VersionType type) {
        if (VersionType.STRICT.equals(type)) {
            if (minor().isEmpty()) {
                throw new VersionException("Invalid version (no minor version): " + value());
            }

            if (patch().isEmpty()) {
                throw new VersionException("Invalid version (no patch version): " + value());
            }
        }
    }


    @Value.Default
    default boolean isGreaterThan(final String version) {
        return semver().isGreaterThan(version);
    }

    @Value.Default
    default boolean isGreaterThan(final Version version) {
        return semver().isGreaterThan(version.semver());
    }


    @Value.Default
    default boolean isGreaterThanOrEqualTo(final String version) {
        return semver().isGreaterThanOrEqualTo(version);
    }

    @Value.Default
    default boolean isGreaterThanOrEqualTo(final Version version) {
        return semver().isGreaterThanOrEqualTo(version.semver());
    }


    @Value.Default
    default boolean isLowerThan(final String version) {
        return semver().isLowerThan(version);
    }

    @Value.Default
    default boolean isLowerThan(final Version version) {
        return semver().isLowerThan(version.semver());
    }


    @Value.Default
    default boolean isLowerThanOrEqualTo(final String version) {
        return semver().isLowerThanOrEqualTo(version);
    }

    @Value.Default
    default boolean isLowerThanOrEqualTo(final Version version) {
        return semver().isLowerThanOrEqualTo(version.semver());
    }


    @Value.Default
    default boolean isEquivalentTo(final String version) {
        return semver().isEquivalentTo(version);
    }

    @Value.Default
    default boolean isEquivalentTo(final Version version) {
        return semver().isEquivalentTo(version.semver());
    }


    @Value.Default
    default boolean satisfies(final String requirement) {
        return semver().satisfies(requirement);
    }

    @Value.Default
    default boolean satisfies(final Requirement requirement) {
        return semver().satisfies(requirement);
    }


    @Value.Default
    default boolean isStable() {
        return major() > 0 && suffixes().isEmpty();
    }

    /**
     * @param version the version to compare
     * @return the greatest difference
     * @see #diff(Version)
     */
    @Value.Default
    default VersionDiff diff(final String version) {
        return VersionDiff.valueOf(semver().diff(version).name());
    }

    /**
     * Returns the greatest difference between 2 versions.
     * For example, if the current version is "1.2.3" and compared version is "1.3.0", the biggest difference
     * is the 'MINOR' number.
     *
     * @param version the version to compare
     * @return the greatest difference
     */
    @Value.Default
    default VersionDiff diff(final Version version) {
        return VersionDiff.valueOf(semver().diff(version.semver()).name());
    }


    /**
     * The different types of supported version systems.
     */
    enum VersionType {
        /**
         * The default type of version.
         * Major, minor and patch parts are required.
         * Suffixes and build are optional.
         */
        STRICT,

        /**
         * Major part is required.
         * Minor, patch, suffixes and build are optional.
         */
        LOOSE,

        /**
         * Follows the rules of NPM.
         * Supports ^, x, *, ~, and more.
         * See https://github.com/npm/node-semver
         */
        NPM,

        /**
         * Follows the rules of Cocoapods.
         * Supports optimistic and comparison operators
         * See https://guides.cocoapods.org/using/the-podfile.html
         */
        COCOAPODS,

        /**
         * Follows the rules of ivy.
         * Supports dynamic parts (eg: 4.2.+) and ranges
         * See http://ant.apache.org/ivy/history/latest-milestone/ivyfile/dependency.html
         */
        IVY
    }

    /**
     * The types of diffs between two versions.
     */
    enum VersionDiff {
        NONE, MAJOR, MINOR, PATCH, SUFFIX, BUILD
    }
}
