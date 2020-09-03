package com.example.service1.command;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.commonapi.events.AppCreatedEvent;
import com.example.commonapi.events.AppVersionAddedEvent;
import com.example.commonapi.valueobjects.AppId;
import com.example.commonapi.valueobjects.AppLifeCycle;
import com.example.commonapi.valueobjects.AppName;
import com.example.commonapi.valueobjects.AppType;
import com.example.commonapi.valueobjects.AppVersionLifeCycle;
import com.example.commonapi.valueobjects.AppVersionVisibility;
import com.example.commonapi.valueobjects.DeveloperId;

@Aggregate
class AppAggregate {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppAggregate.class);

    @AggregateIdentifier
    private AppId id;
    private AppName name;
    private AppType type;
    private DeveloperId developerId;
    @AggregateMember
    private List<AppVersion> appVersions = new ArrayList<>();
    private LocalDate createdOn;
    private LocalDate lastModifiedOn;
    private AppLifeCycle lifeCycle;

    private AppAggregate() {

    }

    @CommandHandler
    private AppAggregate(CreateAppCommand createAppCommand) {
        LOGGER.info("Create App command occured with app name: {}", createAppCommand.getName().getValue());
        apply(AppCreatedEvent.builder(createAppCommand.getId(), createAppCommand.getName(), createAppCommand.getType(),
                createAppCommand.getVersionId(), createAppCommand.getVersionNumber(), createAppCommand.getBinaryName(),
                AppVersionLifeCycle.ACTIVE, AppVersionVisibility.PUBLIC, createAppCommand.getDeveloperId(), AppLifeCycle.ACTIVE,
                createAppCommand.getCreatedOn())
                .price(createAppCommand.getPrice())
                .shortDescription(createAppCommand.getShortDescription())
                .longDescription(createAppCommand.getLongDescription())
                .galleryImages(createAppCommand.getGalleryImages()).build());
    }

    @EventSourcingHandler
    private void on(AppCreatedEvent appCreatedEvent) {
        LOGGER.info("App created event occured with app name: {}", appCreatedEvent.getName().getValue());
        this.id = appCreatedEvent.getId();
        this.name = appCreatedEvent.getName();
        this.type = appCreatedEvent.getType();
        this.appVersions.add(new AppVersion(appCreatedEvent.getVersionId(), appCreatedEvent.getPrice(),
                appCreatedEvent.getShortDescription(), appCreatedEvent.getLongDescription(), appCreatedEvent.getGalleryImages(),
                null, appCreatedEvent.getVersionNumber(), appCreatedEvent.getBinaryName(), appCreatedEvent.getVersionLifeCycle(),
                appCreatedEvent.getVisibility(), appCreatedEvent.getCreatedOn()));
        this.lifeCycle = appCreatedEvent.getLifeCycle();
        this.createdOn = appCreatedEvent.getCreatedOn();
    }

    @CommandHandler
    private void handle(AddAppVersionCommand addAppVersionCommand) {
        LOGGER.info("add App version command occured for app id: {}", addAppVersionCommand.getId().getValue());
        apply(AppVersionAddedEvent.builder(addAppVersionCommand.getId(), addAppVersionCommand.getVersionId(),
                addAppVersionCommand.getVersionNumber(), addAppVersionCommand.getBinaryName(), AppVersionLifeCycle.ACTIVE,
                AppVersionVisibility.PUBLIC, addAppVersionCommand.getCreatedOn()).price(addAppVersionCommand.getPrice())
                .shortDescription(addAppVersionCommand.getShortDescription())
                .longDescription(addAppVersionCommand.getLongDescription())
                .galleryImages(addAppVersionCommand.getGalleryImages())
                .updateInformation(addAppVersionCommand.getUpdateInformation()).build());
    }

    @EventSourcingHandler
    private void on(AppVersionAddedEvent appVersionAddedEvent) {
        this.appVersions.add(new AppVersion(appVersionAddedEvent.getVersionId(), appVersionAddedEvent.getPrice(),
                appVersionAddedEvent.getShortDescription(), appVersionAddedEvent.getLongDescription(),
                appVersionAddedEvent.getGalleryImages(),
                appVersionAddedEvent.getUpdateInformation(), appVersionAddedEvent.getVersionNumber(),
                appVersionAddedEvent.getBinaryName(), appVersionAddedEvent.getVersionLifeCycle(),
                appVersionAddedEvent.getVisibility(), appVersionAddedEvent.getCreatedOn()));
        this.lastModifiedOn = appVersionAddedEvent.getCreatedOn();
    }
}
