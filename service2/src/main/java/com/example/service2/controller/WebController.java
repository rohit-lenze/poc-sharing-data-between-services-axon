package com.example.service2.controller;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.commonapi.events.AppCreatedEvent;
import com.example.commonapi.events.AppVersionShortDescriptionUpdatedEvent;
import com.example.commonapi.queries.FindAppShortDescriptionByIdQuery;
import com.example.commonapi.queries.FindAppTypeByIdQuery;
import com.example.commonapi.valueobjects.AppId;

@Controller
class WebController {

    private final QueryGateway queryGateway;
    private final EventStore eventStore;

    WebController(QueryGateway queryGateway, EventStore eventStore) {
        this.queryGateway = queryGateway;
        this.eventStore = eventStore;
    }

    @GetMapping("/query/{id}/{versionId}")
    public void getAppTypeByQuery(@PathVariable String id, @PathVariable String versionId)
            throws InterruptedException, ExecutionException {
        long startTimeForAppType = System.currentTimeMillis();
        String appType = queryGateway
                .query(new FindAppTypeByIdQuery(id), ResponseTypes.instanceOf(String.class))
                .get();
        long endTimeForAppType = System.currentTimeMillis();
        System.out.println(
                "Using QueryHandler::Type of app-id: " + id + " is: " + appType + "TIME TAKEN:: "
                        + (endTimeForAppType - startTimeForAppType) + "ms");

        String appShortDescription = queryGateway
                .query(new FindAppShortDescriptionByIdQuery(id, versionId), ResponseTypes.instanceOf(String.class))
                .get();
        long endTimeForShortDescription = System.currentTimeMillis();
        System.out.println(
                "Using QueryHandler::short desc. of app-id: " + id + " is: " + appShortDescription + " TOTAL TIME TAKEN:: "
                        + (endTimeForShortDescription - endTimeForAppType) + "ms");
    }

    @GetMapping("/eventstore/{id}/{versionId}")
    public void getAppTypeByIdFromEventStore(@PathVariable String id, @PathVariable String versionId) {
        long startTimeForAppType = System.currentTimeMillis();
        String appId = new AppId(id).toString();
        Optional<? extends DomainEventMessage<?>> domainEventMessage = eventStore.readEvents(appId).asStream()
                .filter(event -> Objects.equals(AppCreatedEvent.class, event.getPayloadType())).findFirst();
        String appType = null;
        if (domainEventMessage.isPresent()) {
            appType = ((AppCreatedEvent) domainEventMessage.get().getPayload()).getType().getValue();
        }
        long endTimeForAppType = System.currentTimeMillis();
        System.out.println(
                "Using Event-Store::Type of app-id: " + id + " is: " + appType + " TOTAL TIME TAKEN:: "
                        + (endTimeForAppType - startTimeForAppType) + "ms");
//        Optional<Long> lastSequenceNumberFor = eventStore
//                .lastSequenceNumberFor(appAddedToMachineEvent.getAppReference().getAppId().toString());
        Stream<? extends DomainEventMessage<?>> asStream = eventStore.readEvents(appId).asStream();
        Optional<? extends DomainEventMessage<?>> eventMessage = Optional.empty();
        eventMessage = asStream
                .filter(event -> Objects.equals(AppVersionShortDescriptionUpdatedEvent.class, event.getPayloadType()))
                .filter(event -> Objects.equals(versionId,
                        ((AppVersionShortDescriptionUpdatedEvent) event.getPayload()).getVersionId().getValue()))
                .reduce((first, second) -> second);
        long endTimeForAppShortDesc = System.currentTimeMillis();
        if (eventMessage.isPresent()) {
            System.out.println("Using Event-Store::shortDesc of app-id: " + id + " is: "
                    + ((AppVersionShortDescriptionUpdatedEvent) eventMessage.get().getPayload()).getShortDescription().getValue()
                    + " current seq number:: " + eventMessage.get().getSequenceNumber() + " TOTAL TIME TAKEN:: "
                    + (endTimeForAppShortDesc - endTimeForAppType) + "ms");
        }
    }

}
