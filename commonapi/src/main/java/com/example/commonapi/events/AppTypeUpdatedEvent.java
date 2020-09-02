package com.example.commonapi.events;

import org.axonframework.serialization.Revision;

import com.example.commonapi.valueobjects.AppId;
import com.example.commonapi.valueobjects.AppType;

import lombok.ToString;
import lombok.Value;

/**
 * An Axon event which will be published over the event bus when an existing
 * app/library is updated.
 *
 */
@Value
@ToString(includeFieldNames = true)
@Revision("1.0")
public final class AppTypeUpdatedEvent {

    private AppId id;
    private AppType type;
}
