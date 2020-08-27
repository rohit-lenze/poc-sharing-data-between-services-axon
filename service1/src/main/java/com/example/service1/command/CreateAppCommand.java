package com.example.service1.command;

import java.time.LocalDate;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.example.commonapi.valueobjects.AppBinaryName;
import com.example.commonapi.valueobjects.AppId;
import com.example.commonapi.valueobjects.AppName;
import com.example.commonapi.valueobjects.AppType;
import com.example.commonapi.valueobjects.AppVersionGalleryImage;
import com.example.commonapi.valueobjects.AppVersionId;
import com.example.commonapi.valueobjects.AppVersionLongDescription;
import com.example.commonapi.valueobjects.AppVersionNumber;
import com.example.commonapi.valueobjects.AppVersionPrice;
import com.example.commonapi.valueobjects.AppVersionShortDescription;
import com.example.commonapi.valueobjects.DeveloperId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * An axon command which will be published over a axon command bus when a new
 * app/lib is created.
 */
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class CreateAppCommand {

    @TargetAggregateIdentifier
    private AppId id;
    private AppName name;
    private AppType type;
    private AppVersionId versionId;
    private AppVersionPrice price;
    private AppVersionShortDescription shortDescription;
    private AppVersionLongDescription longDescription;
    private @Singular List<AppVersionGalleryImage> galleryImages;
    private AppVersionNumber versionNumber;
    private AppBinaryName binaryName;
    private DeveloperId developerId;
    private LocalDate createdOn;

    public static class CreateAppCommandBuilder {
        private CreateAppCommandBuilder() {
        }
    }

    public static CreateAppCommandBuilder builder(AppId id, AppName name, AppType type, AppVersionId versionId,
            AppVersionNumber versionNumber, AppBinaryName binaryName, DeveloperId developerId, LocalDate createdOn) {
        return new CreateAppCommandBuilder().id(id).name(name).type(type).versionId(versionId).versionNumber(versionNumber)
                .binaryName(binaryName).developerId(developerId).createdOn(createdOn);
    }
}
