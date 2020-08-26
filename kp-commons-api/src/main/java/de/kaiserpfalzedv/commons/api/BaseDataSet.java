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

import de.kaiserpfalzedv.commons.version.Version;

import java.io.Serializable;

/**
 * A generic base definition for datasets. This base does not contain any data
 * but may be used as a pointer to some data.
 *
 * @author rlichti
 * @version 1.0.0 2020-08-15
 * @since 1.0.0 2020-08-15
 */
public interface BaseDataSet extends Serializable {
    /**
     * The type of dataset. Needs to be unique.
     * @return the type of this dataset.
     */
    default String kind() {
        return metadata().kind();
    }

    /**
     * The version of this dataset defintion. Some services may support multiple
     * versions of this kind.
     * @return the version of the dataset defintion.
     */
    default Version apiVersion() {
        return metadata().apiVersion();
    }

    /**
     * The metadata of the dataset.
     *
     * @return the metadata for this dataset.
     */
    Metadata metadata();
}
