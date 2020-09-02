package com.example.service1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
import com.example.commonapi.valueobjects.AppVersionUpdateInfo;
import com.example.commonapi.valueobjects.DeveloperId;
import com.example.service1.command.AddAppVersionCommand;
import com.example.service1.command.CreateAppCommand;
import com.example.service1.command.UpdateAppTypeCommand;
import com.example.service1.command.UpdateAppVersionCommand;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private CommandGateway commandGateway;

    private Map<String, List<String>> appIdsWithVersionMap = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        AppType apptype = AppType.LIBRARY;
        for (int i = 0; i < 1000; i++) {
            if (i % 2 == 0) {
                apptype = AppType.APP;
            }
            String appId = UUID.randomUUID().toString();
            String appVersionId = UUID.randomUUID().toString();
            List<String> versionIds = new ArrayList<>();
            versionIds.add(appVersionId);
            appIdsWithVersionMap.put(appId, versionIds);
            commandGateway.sendAndWait(CreateAppCommand
                    .builder(new AppId(appId), new AppName("app" + i), AppType.get(apptype.getValue()),
                            new AppVersionId(appVersionId), new AppVersionNumber(Integer.toString(1)),
                            new AppBinaryName("binary_1"), new DeveloperId("deverloper"),
                            LocalDate.now())
                    .price(new AppVersionPrice(Currency.getInstance("EUR"), 10.0))
                    .shortDescription(new AppVersionShortDescription("shortDescription"))
                    .longDescription(new AppVersionLongDescription("shortDescription"))
                    .galleryImages(Arrays.asList(new AppVersionGalleryImage("image_" + "thumbnail",
                            "image_" + "profile", "image_" + "original")))
                    .build());
        }
        Set<String> appIds = appIdsWithVersionMap.keySet();
        updateAppType(appIds);
        addAppVersion(appIds);
        updateAppVersion(appIdsWithVersionMap);
    }

    private void updateAppVersion(Map<String, List<String>> appIdsWithVersionMap) {
        appIdsWithVersionMap.forEach((appId,
                versionIds) -> versionIds.forEach(versionId -> commandGateway.sendAndWait(UpdateAppVersionCommand
                        .builder(new AppId(appId), new AppVersionId(versionId),
                                new AppVersionNumber("11"),
                                new AppBinaryName("binaryName_1"))
                        .price(new AppVersionPrice(Currency.getInstance("EUR"), 12.0))
                        .shortDescription(new AppVersionShortDescription("appShortDescription_1"))
                        .longDescription(new AppVersionLongDescription("appLongDescription_1"))
                        .galleryImages(Arrays.asList(new AppVersionGalleryImage("image_1" + "thumbnail",
                                "image_1" + "profile", "image_1" + "original")))
                        .updateInformation(new AppVersionUpdateInfo("whatsNewInVersion_1"))
                        .build())));

        appIdsWithVersionMap.forEach((appId,
                versionIds) -> versionIds.forEach(versionId -> commandGateway.sendAndWait(UpdateAppVersionCommand
                        .builder(new AppId(appId), new AppVersionId(versionId),
                                new AppVersionNumber("12_updated"),
                                new AppBinaryName("binaryName_updated"))
                        .price(new AppVersionPrice(Currency.getInstance("EUR"), 13.0))
                        .shortDescription(new AppVersionShortDescription("appShortDescription_updated"))
                        .longDescription(new AppVersionLongDescription("appLongDescription_updated"))
                        .galleryImages(Arrays.asList(new AppVersionGalleryImage("image_updated" + "thumbnail",
                                "image_updated" + "profile", "image_updated" + "original")))
                        .updateInformation(new AppVersionUpdateInfo("whatsNewInVersion_updated"))
                        .build())));

        appIdsWithVersionMap.forEach((appId,
                versionIds) -> versionIds.forEach(versionId -> commandGateway.sendAndWait(UpdateAppVersionCommand
                        .builder(new AppId(appId), new AppVersionId(versionId),
                                new AppVersionNumber("123"),
                                new AppBinaryName("123"))
                        .price(new AppVersionPrice(Currency.getInstance("EUR"), 131.0))
                        .shortDescription(new AppVersionShortDescription("123"))
                        .longDescription(new AppVersionLongDescription("123"))
                        .galleryImages(Arrays.asList(new AppVersionGalleryImage("123" + "thumbnail",
                                "123" + "profile", "123" + "original")))
                        .updateInformation(new AppVersionUpdateInfo("123"))
                        .build())));

        appIdsWithVersionMap.forEach((appId,
                versionIds) -> versionIds.forEach(versionId -> commandGateway.sendAndWait(UpdateAppVersionCommand
                        .builder(new AppId(appId), new AppVersionId(versionId),
                                new AppVersionNumber("1234"),
                                new AppBinaryName("1234"))
                        .price(new AppVersionPrice(Currency.getInstance("EUR"), 1314.0))
                        .shortDescription(new AppVersionShortDescription("1234"))
                        .longDescription(new AppVersionLongDescription("1234"))
                        .galleryImages(Arrays.asList(new AppVersionGalleryImage("1234" + "thumbnail",
                                "123" + "profile", "1234" + "original")))
                        .updateInformation(new AppVersionUpdateInfo("1234"))
                        .build())));
        appIdsWithVersionMap.forEach((appId,
                versionIds) -> versionIds.forEach(versionId -> commandGateway.sendAndWait(UpdateAppVersionCommand
                        .builder(new AppId(appId), new AppVersionId(versionId),
                                new AppVersionNumber("12345"),
                                new AppBinaryName("12345"))
                        .price(new AppVersionPrice(Currency.getInstance("EUR"), 13145.0))
                        .shortDescription(new AppVersionShortDescription("12345"))
                        .longDescription(new AppVersionLongDescription("12345"))
                        .galleryImages(Arrays.asList(new AppVersionGalleryImage("12345" + "thumbnail",
                                "1235" + "profile", "12345" + "original")))
                        .updateInformation(new AppVersionUpdateInfo("12345"))
                        .build())));
        appIdsWithVersionMap.forEach((appId,
                versionIds) -> versionIds.forEach(versionId -> commandGateway.sendAndWait(UpdateAppVersionCommand
                        .builder(new AppId(appId), new AppVersionId(versionId),
                                new AppVersionNumber("123456"),
                                new AppBinaryName("123456"))
                        .price(new AppVersionPrice(Currency.getInstance("EUR"), 123456.0))
                        .shortDescription(new AppVersionShortDescription("123456"))
                        .longDescription(new AppVersionLongDescription("123456"))
                        .galleryImages(Arrays.asList(new AppVersionGalleryImage("123456" + "thumbnail",
                                "123456" + "profile", "123456" + "original")))
                        .updateInformation(new AppVersionUpdateInfo("123456"))
                        .build())));
        appIdsWithVersionMap.forEach((appId,
                versionIds) -> versionIds.forEach(versionId -> commandGateway.sendAndWait(UpdateAppVersionCommand
                        .builder(new AppId(appId), new AppVersionId(versionId),
                                new AppVersionNumber("1234567"),
                                new AppBinaryName("1234567"))
                        .price(new AppVersionPrice(Currency.getInstance("EUR"), 1234567.0))
                        .shortDescription(new AppVersionShortDescription("1234567"))
                        .longDescription(new AppVersionLongDescription("1234567"))
                        .galleryImages(Arrays.asList(new AppVersionGalleryImage("1234567" + "thumbnail",
                                "1234567" + "profile", "1234567" + "original")))
                        .updateInformation(new AppVersionUpdateInfo("1234567"))
                        .build())));
        appIdsWithVersionMap.forEach((appId,
                versionIds) -> versionIds.forEach(versionId -> commandGateway.sendAndWait(UpdateAppVersionCommand
                        .builder(new AppId(appId), new AppVersionId(versionId),
                                new AppVersionNumber("12345678"),
                                new AppBinaryName("12345678"))
                        .price(new AppVersionPrice(Currency.getInstance("EUR"), 12345678.0))
                        .shortDescription(new AppVersionShortDescription("12345678"))
                        .longDescription(new AppVersionLongDescription("12345678"))
                        .galleryImages(Arrays.asList(new AppVersionGalleryImage("12345678" + "thumbnail",
                                "12345678" + "profile", "12345678" + "original")))
                        .updateInformation(new AppVersionUpdateInfo("12345678"))
                        .build())));
        appIdsWithVersionMap.forEach((appId,
                versionIds) -> versionIds.forEach(versionId -> commandGateway.sendAndWait(UpdateAppVersionCommand
                        .builder(new AppId(appId), new AppVersionId(versionId),
                                new AppVersionNumber("version_latest"),
                                new AppBinaryName("binary_latest"))
                        .price(new AppVersionPrice(Currency.getInstance("EUR"), 1.0))
                        .shortDescription(new AppVersionShortDescription("appShortDescription_latest"))
                        .longDescription(new AppVersionLongDescription("appLongDescription_latest"))
                        .galleryImages(Arrays.asList(new AppVersionGalleryImage("image_latest" + "thumbnail",
                                "image_latest" + "profile", "image_latest" + "original")))
                        .updateInformation(new AppVersionUpdateInfo("whatsNewInVersion_latest"))
                        .build())));
    }

    private void addAppVersion(Set<String> appIds) {
        appIds.forEach(appId -> {
            String appVersionId = UUID.randomUUID().toString();
            appIdsWithVersionMap.get(appId).add(appVersionId);
            commandGateway.sendAndWait(AddAppVersionCommand.builder(new AppId(appId),
                    new AppVersionId(appVersionId), new AppVersionNumber(Integer.toString(2)),
                    new AppBinaryName("binary_2"),
                    LocalDate.now(),
                    new DeveloperId("developerId"))
                    .price(new AppVersionPrice(Currency.getInstance("EUR"), 11.0))
                    .shortDescription(new AppVersionShortDescription("appShortDescription"))
                    .longDescription(new AppVersionLongDescription("appLongDescription"))
                    .galleryImages(Arrays.asList(new AppVersionGalleryImage("image_" + "thumbnail",
                            "image_" + "profile", "image_" + "original")))
                    .updateInformation(new AppVersionUpdateInfo("whatsNewInVersion")).build());
        });

        appIds.forEach(appId -> {
            String appVersionId = UUID.randomUUID().toString();
            appIdsWithVersionMap.get(appId).add(appVersionId);
            commandGateway.sendAndWait(AddAppVersionCommand.builder(new AppId(appId),
                    new AppVersionId(appVersionId), new AppVersionNumber(Integer.toString(3)),
                    new AppBinaryName("binary_2"),
                    LocalDate.now(),
                    new DeveloperId("developerId"))
                    .price(new AppVersionPrice(Currency.getInstance("EUR"), 11.0))
                    .shortDescription(new AppVersionShortDescription("appShortDescription"))
                    .longDescription(new AppVersionLongDescription("appLongDescription"))
                    .galleryImages(Arrays.asList(new AppVersionGalleryImage("image_" + "thumbnail",
                            "image_" + "profile", "image_" + "original")))
                    .updateInformation(new AppVersionUpdateInfo("whatsNewInVersion")).build());
        });
    }

    private void updateAppType(Set<String> appIds) {
        appIds.forEach(appId -> commandGateway.sendAndWait(new UpdateAppTypeCommand(new AppId(appId), AppType.LIBRARY)));
    }
}
