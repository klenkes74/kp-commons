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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * A dataset containing data.
 *
 * @author rlichti
 * @version 1.0.0 2020-08-15
 * @since 1.0.0 2020-08-15
 */
@JsonPropertyOrder({"kind", "apiVersion", "metadata", "data", "state"})
public interface DataSet<T extends Serializable> extends DataObject {
    /**
     * The data within this data set.
     *
     * @return the data.
     */
    Optional<T> data();

    /**
     * The log of changes to this data set.
     *
     * @return a set of data change logs of a data object.
     */
    default Optional<List<State>> state() { return Optional.empty(); }
}
