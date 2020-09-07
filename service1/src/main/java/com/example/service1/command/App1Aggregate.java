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

@Aggregate(snapshotTriggerDefinition = "app1SnapshotTrigger")
public class App1Aggregate {
    private static final Logger LOGGER = LoggerFactory.getLogger(App1Aggregate.class);
    @AggregateIdentifier
    private AppId id;
    private AppName name;
    private AppType type;
    private DeveloperId developerId;
    @AggregateMember
    private List<App1Version> appVersions = new ArrayList<>();
    private LocalDate createdOn;
    private LocalDate lastModifiedOn;
    private AppLifeCycle lifeCycle;

    private App1Aggregate() {

    }

    @CommandHandler
    private App1Aggregate(CreateApp1Command createAppCommand) {
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
        LOGGER.info("AppCreatedEvent occured with appId {}", appCreatedEvent.getId().getValue());
        this.id = appCreatedEvent.getId();
        this.name = appCreatedEvent.getName();
        this.type = appCreatedEvent.getType();
        this.appVersions.add(new App1Version(appCreatedEvent.getVersionId(), appCreatedEvent.getPrice(),
                appCreatedEvent.getShortDescription(), appCreatedEvent.getLongDescription(), appCreatedEvent.getGalleryImages(),
                null, appCreatedEvent.getVersionNumber(), appCreatedEvent.getBinaryName(), appCreatedEvent.getVersionLifeCycle(),
                appCreatedEvent.getVisibility(), appCreatedEvent.getCreatedOn()));
        this.lifeCycle = appCreatedEvent.getLifeCycle();
        this.createdOn = appCreatedEvent.getCreatedOn();
    }

    @CommandHandler
    private void handle(AddApp1VersionCommand addAppVersionCommand) {
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
        LOGGER.info("AppVersionAddedEvent occured with appId {}", appVersionAddedEvent.getId().getValue());
        this.appVersions.add(new App1Version(appVersionAddedEvent.getVersionId(), appVersionAddedEvent.getPrice(),
                appVersionAddedEvent.getShortDescription(), appVersionAddedEvent.getLongDescription(),
                appVersionAddedEvent.getGalleryImages(),
                appVersionAddedEvent.getUpdateInformation(), appVersionAddedEvent.getVersionNumber(),
                appVersionAddedEvent.getBinaryName(), appVersionAddedEvent.getVersionLifeCycle(),
                appVersionAddedEvent.getVisibility(), appVersionAddedEvent.getCreatedOn()));
        this.lastModifiedOn = appVersionAddedEvent.getCreatedOn();
    }
}
