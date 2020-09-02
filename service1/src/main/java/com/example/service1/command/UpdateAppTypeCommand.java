package com.example.service1.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.example.commonapi.valueobjects.AppId;
import com.example.commonapi.valueobjects.AppType;

import lombok.ToString;
import lombok.Value;

/**
 * An axon command which will be published over a axon command bus when an
 * existing app/libray is updated.
 */
@Value
@ToString(includeFieldNames = true)
public final class UpdateAppTypeCommand {

    @TargetAggregateIdentifier
    private AppId id;
    private AppType type;

}
