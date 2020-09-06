package com.example.service1.command;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.EntityId;

import com.example.commonapi.events.AppVersionBinaryUpdatedEvent;
import com.example.commonapi.events.AppVersionGalleryImageUpdatedEvent;
import com.example.commonapi.events.AppVersionLongDescriptionUpdatedEvent;
import com.example.commonapi.events.AppVersionNumberUpdatedEvent;
import com.example.commonapi.events.AppVersionPriceUpdatedEvent;
import com.example.commonapi.events.AppVersionShortDescriptionUpdatedEvent;
import com.example.commonapi.events.AppVersionUpdateInformationUpdatedEvent;
import com.example.commonapi.valueobjects.AppBinaryName;
import com.example.commonapi.valueobjects.AppId;
import com.example.commonapi.valueobjects.AppVersionGalleryImage;
import com.example.commonapi.valueobjects.AppVersionId;
import com.example.commonapi.valueobjects.AppVersionLifeCycle;
import com.example.commonapi.valueobjects.AppVersionLongDescription;
import com.example.commonapi.valueobjects.AppVersionNumber;
import com.example.commonapi.valueobjects.AppVersionPrice;
import com.example.commonapi.valueobjects.AppVersionShortDescription;
import com.example.commonapi.valueobjects.AppVersionUpdateInfo;
import com.example.commonapi.valueobjects.AppVersionVisibility;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class AppVersion {

    @EntityId
    private AppVersionId versionId;
    private AppVersionPrice price;
    private AppVersionShortDescription shortDescription;
    private AppVersionLongDescription longDescription;
    private List<AppVersionGalleryImage> galleryImages;
    private AppVersionUpdateInfo updateInformation;
    private AppVersionNumber versionNumber;
    private AppBinaryName binaryName;
    private AppVersionLifeCycle versionLifeCycle;
    private AppVersionVisibility visibility;
    private LocalDate createdOn;

    @CommandHandler
    private void handle(UpdateAppVersionCommand updateAppVersionCommand) {
        AppId appId = updateAppVersionCommand.getId();
        AppVersionId versionIdToUpdate = updateAppVersionCommand.getVersionId();

        if (!Objects.equals(this.shortDescription, updateAppVersionCommand.getShortDescription())) {
            apply(new AppVersionShortDescriptionUpdatedEvent(appId, versionIdToUpdate,
                    updateAppVersionCommand.getShortDescription()));
        }
        if (!Objects.equals(this.longDescription, updateAppVersionCommand.getLongDescription())) {
            apply(new AppVersionLongDescriptionUpdatedEvent(appId, versionIdToUpdate,
                    updateAppVersionCommand.getLongDescription()));
        }
        if (!Objects.equals(this.price.getValue(), updateAppVersionCommand.getPrice().getValue())) {
            apply(new AppVersionPriceUpdatedEvent(appId, versionIdToUpdate, updateAppVersionCommand.getPrice()));
        }

        if (!Objects.equals(this.updateInformation, updateAppVersionCommand.getUpdateInformation())) {
            apply(new AppVersionUpdateInformationUpdatedEvent(appId, versionIdToUpdate,
                    updateAppVersionCommand.getUpdateInformation()));
        }
        if (!Objects.equals(this.versionNumber, updateAppVersionCommand.getVersionNumber())) {
            apply(new AppVersionNumberUpdatedEvent(appId, versionIdToUpdate, updateAppVersionCommand.getVersionNumber()));
        }
        List<AppVersionGalleryImage> galleryImagesToUpdate = updateAppVersionCommand.getGalleryImages();
        if (Objects.nonNull(galleryImagesToUpdate)) {
            apply(new AppVersionGalleryImageUpdatedEvent(appId, versionIdToUpdate, galleryImagesToUpdate));
        }
        if (Objects.nonNull(updateAppVersionCommand.getBinaryName().getValue())) {
            apply(new AppVersionBinaryUpdatedEvent(appId, versionIdToUpdate, updateAppVersionCommand.getBinaryName()));
        }
    }

    @EventSourcingHandler
    private void on(AppVersionGalleryImageUpdatedEvent appVersionGalleryImageUpdatedEvent) {
        this.galleryImages = appVersionGalleryImageUpdatedEvent.getGalleryImages();
    }

    @EventSourcingHandler
    private void on(AppVersionNumberUpdatedEvent appVersionNumberUpdatedEvent) {
        this.versionNumber = appVersionNumberUpdatedEvent.getVersionNumber();
    }

    @EventSourcingHandler
    private void on(AppVersionBinaryUpdatedEvent appVersionBinaryUpdatedEvent) {
        this.binaryName = appVersionBinaryUpdatedEvent.getBinaryName();
    }

    @EventSourcingHandler
    private void on(AppVersionUpdateInformationUpdatedEvent appVersionUpdateInformationUpdatedEvent) {
        this.updateInformation = appVersionUpdateInformationUpdatedEvent.getUpdateInformation();
    }

    @EventSourcingHandler
    private void on(AppVersionPriceUpdatedEvent appVersionPriceUpdatedEvent) {
        this.price = appVersionPriceUpdatedEvent.getPrice();
    }

    @EventSourcingHandler
    private void on(AppVersionShortDescriptionUpdatedEvent appVersionShortDescriptionUpdatedEvent) {
        this.shortDescription = appVersionShortDescriptionUpdatedEvent.getShortDescription();
    }

    @EventSourcingHandler
    private void on(AppVersionLongDescriptionUpdatedEvent appVersionLongDescriptionUpdatedEvent) {
        this.longDescription = appVersionLongDescriptionUpdatedEvent.getLongDescription();
    }
}
