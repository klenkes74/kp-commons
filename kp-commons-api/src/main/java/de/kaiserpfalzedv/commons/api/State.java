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
import org.immutables.value.Value;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * This is a generic state object for additional metadata accompaning a data
 * set.
 *
 * @author rlichti
 * @version 1.0.0 2020-08-14
 * @since 1.0.0 2020-08-14
 */
@Immutable
@Value.Immutable
@JsonSerialize(as = StateImmutable.class)
@JsonDeserialize(builder = StateImmutable.Builder.class)
public interface State extends Serializable {
    /**
     * The object got created.
     */
    String CREATED = "created";

    /**
     * The object got updated.
     */
    String UPDATED = "updated";

    /**
     * The object got deleted.
     */
    String DELETED = "deleted";


    /**
     * The data set is active.
     */
    String ACTIVE = "active";

    /**
     * The data set is inactive.
     */
    String INACTIVE = "inactive";

    /**
     * The object is currently in a state of transition.
     */
    String PENDING = "pending";


    /**
     * The type depends on the state that may be useful for the data object.
     *
     * @return the type of state.
     */
    String kind();

    /**
     * Additional data for this state. It depends on the state needed for the
     * data object type.
     *
     * @return the state data.
     */
    Serializable data();

    /**
     * @return a UUID of this dataset.
     */
    UUID uuid();

    /**
     * @return The creation timestamp of this object.
     */
    Optional<OffsetDateTime> created();
}