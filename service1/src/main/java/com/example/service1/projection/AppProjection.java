package com.example.service1.projection;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.example.commonapi.events.AppCreatedEvent;
import com.example.commonapi.events.AppVersionAddedEvent;
import com.example.commonapi.events.AppVersionBinaryUpdatedEvent;
import com.example.commonapi.events.AppVersionGalleryImageUpdatedEvent;
import com.example.commonapi.events.AppVersionLongDescriptionUpdatedEvent;
import com.example.commonapi.events.AppVersionNumberUpdatedEvent;
import com.example.commonapi.events.AppVersionPriceUpdatedEvent;
import com.example.commonapi.events.AppVersionShortDescriptionUpdatedEvent;
import com.example.commonapi.events.AppVersionUpdateInformationUpdatedEvent;
import com.example.commonapi.queries.FindAppShortDescriptionByIdQuery;
import com.example.commonapi.queries.FindAppTypeByIdQuery;
import com.example.commonapi.valueobjects.AppVersionGalleryImage;
import com.google.common.base.Objects;

@Component
class AppProjection {

    private final AppDaoService appDaoService;

    AppProjection(AppDaoService appDaoService) {
        this.appDaoService = appDaoService;
    }

    @EventHandler
    void on(AppCreatedEvent appCreatedEvent) throws IOException {
        appDaoService.createApp(new AppEntity(
                appCreatedEvent.getId().getValue(),
                appCreatedEvent.getName().getValue(),
                appCreatedEvent.getType().getValue(),
                Arrays.asList(
                        convertToAppVersionEntity(appCreatedEvent)),
                appCreatedEvent.getDeveloperId().getValue(),
                appCreatedEvent.getLifeCycle().getValue(),
                appCreatedEvent.getCreatedOn()));
    }

    @EventHandler
    void on(AppVersionAddedEvent appVersionAddedEvent) throws IOException {
        appDaoService.addVersion(appVersionAddedEvent.getId().getValue(),
                convertToAppVersionEntity(appVersionAddedEvent));
    }

    @EventHandler
    void on(AppVersionShortDescriptionUpdatedEvent appVersionShortDescriptionUpdatedEvent) throws IOException {
        appDaoService.updateVersionShortDescription(appVersionShortDescriptionUpdatedEvent.getId().getValue(),
                appVersionShortDescriptionUpdatedEvent.getVersionId().getValue(),
                appVersionShortDescriptionUpdatedEvent.getShortDescription().getValue());
    }

    @EventHandler
    void on(AppVersionLongDescriptionUpdatedEvent appVersionLongDescriptionUpdatedEvent) throws IOException {
        appDaoService.updateVersionLongDescription(appVersionLongDescriptionUpdatedEvent.getId().getValue(),
                appVersionLongDescriptionUpdatedEvent.getVersionId().getValue(),
                appVersionLongDescriptionUpdatedEvent.getLongDescription().getValue());
    }

    @EventHandler
    void on(AppVersionPriceUpdatedEvent appVersionPriceUpdatedEvent) throws IOException {
        appDaoService.updateVersionPrice(appVersionPriceUpdatedEvent.getId().getValue(),
                appVersionPriceUpdatedEvent.getVersionId().getValue(),
                appVersionPriceUpdatedEvent.getPrice().getValue());
    }

    @EventHandler
    void on(AppVersionUpdateInformationUpdatedEvent appVersionUpdateInformationUpdatedEvent) throws IOException {
        appDaoService.updateVersionUpdateInformation(appVersionUpdateInformationUpdatedEvent.getId().getValue(),
                appVersionUpdateInformationUpdatedEvent.getVersionId().getValue(),
                appVersionUpdateInformationUpdatedEvent.getUpdateInformation().getValue());
    }

    @EventHandler
    void on(AppVersionNumberUpdatedEvent appVersionNumberUpdatedEvent) throws IOException {
        appDaoService.updateVersionNumber(appVersionNumberUpdatedEvent.getId().getValue(),
                appVersionNumberUpdatedEvent.getVersionId().getValue(),
                appVersionNumberUpdatedEvent.getVersionNumber().getValue());
    }

    @EventHandler
    void on(AppVersionBinaryUpdatedEvent appVersionBinaryUpdatedEvent) throws IOException {
        appDaoService.updateBinaryName(appVersionBinaryUpdatedEvent.getId().getValue(),
                appVersionBinaryUpdatedEvent.getVersionId().getValue(),
                appVersionBinaryUpdatedEvent.getBinaryName().getValue());
    }

    @EventHandler
    void on(AppVersionGalleryImageUpdatedEvent appVersionGalleryImageUpdatedEvent) throws IOException {
        appDaoService.updateVersionGalleryImages(appVersionGalleryImageUpdatedEvent.getId().getValue(),
                appVersionGalleryImageUpdatedEvent.getVersionId().getValue(),
                mapToAppVersionGalleryImageEntity(appVersionGalleryImageUpdatedEvent.getGalleryImages()));
    }

    private AppVersionEntity convertToAppVersionEntity(AppCreatedEvent appCreatedEvent) {
        return new AppVersionEntity(appCreatedEvent.getVersionId().getValue(),
                appCreatedEvent.getPrice().getValue(),
                appCreatedEvent.getShortDescription().getValue(),
                appCreatedEvent.getLongDescription().getValue(),
                mapToAppVersionGalleryImageEntity(appCreatedEvent.getGalleryImages()),
                null,
                appCreatedEvent.getVersionNumber().getValue(),
                appCreatedEvent.getBinaryName().getValue(),
                appCreatedEvent.getVersionLifeCycle().getValue(),
                appCreatedEvent.getVisibility().getValue(),
                appCreatedEvent.getCreatedOn());
    }

    private AppVersionEntity convertToAppVersionEntity(AppVersionAddedEvent appVersionAddedEvent) {
        return new AppVersionEntity(appVersionAddedEvent.getVersionId().getValue(),
                appVersionAddedEvent.getPrice().getValue(),
                appVersionAddedEvent.getShortDescription().getValue(),
                appVersionAddedEvent.getLongDescription().getValue(),
                mapToAppVersionGalleryImageEntity(appVersionAddedEvent.getGalleryImages()),
                appVersionAddedEvent.getUpdateInformation().getValue(),
                appVersionAddedEvent.getVersionNumber().getValue(),
                appVersionAddedEvent.getBinaryName().getValue(),
                appVersionAddedEvent.getVersionLifeCycle().getValue(),
                appVersionAddedEvent.getVisibility().getValue(),
                appVersionAddedEvent.getCreatedOn());
    }

    @QueryHandler
    String getAppTypeById(FindAppTypeByIdQuery getAppByIdQuery) throws IOException {
        return appDaoService.getAppTypeById(getAppByIdQuery.getAppId());
    }

    @QueryHandler
    String getAppShortDescriptionById(FindAppShortDescriptionByIdQuery getAppByIdQuery) throws IOException {
        Optional<AppVersionEntity> findAny = appDaoService.findById(getAppByIdQuery.getAppId()).getAppVersions().stream()
                .filter(av -> Objects.equal(getAppByIdQuery.getVersionId(), av.getVersionId())).findAny();
        return findAny.get().getShortDescription();
    }

    private List<AppVersionGalleryImageEntity> mapToAppVersionGalleryImageEntity(List<AppVersionGalleryImage> galleryImages) {
        return galleryImages.stream().map(galleryImage -> new AppVersionGalleryImageEntity(galleryImage.getThumbnailImageName(),
                galleryImage.getProfileImageName(), galleryImage.getOriginalImageName())).collect(Collectors.toList());
    }
}
