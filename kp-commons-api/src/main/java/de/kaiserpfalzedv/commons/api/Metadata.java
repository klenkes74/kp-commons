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

package de.kaiserpfalzedv.commons.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.commons.version.Version;
import org.immutables.value.Value;

import java.io.Serializable;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * This is a generic metadata set for all types of datasets.
 *
 * @author rlichti
 * @version 1.0.0 2020-08-14
 * @since 1.0.0 2020-08-14
 */
@Immutable
@Value.Immutable
@Value.Modifiable
@JsonSerialize(as = MetadataImmutable.class)
@JsonDeserialize(builder = MetadataImmutable.Builder.class)
public interface Metadata extends Serializable {
    String kind();
    Version apiVersion();

    /**
     * @return a UUID of this dataset.
     */
    UUID uuid();

    @Value.Default
    default String scope() {
        return "./.";
    }

    /**
     * @return the name of the dataset
     */
    String name();

    /**
     * @return a version of this object.
     */
    Optional<Version> version();

    /**
     * @return the authoritative URL for this dataset
     */
    Optional<URL> url();

    /**
     * The dataset may be part of another one or "owned" by another dataset.
     * @return the owning construct.
     */
    Optional<BaseDataSet> owner();

    /**
     * Technical annotations to this dataset.
     *
     * @return the annotations of this dataset in an immutable map
     */
    Map<String, String> annotations();

    /**
     * Every dataset can be labeled. These labels are free purpose for other
     * systems or users to use.
     *
     * @return the labels of the dataset in an immutable map
     */
    Map<String, String> labels();
}
