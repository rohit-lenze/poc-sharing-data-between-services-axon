package com.example.service2.controller;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

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

    @GetMapping("/appType/{id}")
    public void getAppType(@PathVariable String id) throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        String appType = queryGateway
                .query(new FindAppTypeByIdQuery(id), ResponseTypes.instanceOf(String.class))
                .get();
        long endTime = System.currentTimeMillis();
        System.out.println(
                "Using QueryHandler::Type of app-id: " + id + " is: " + appType + "TIME TAKEN:: "
                        + (endTime - startTime) + "ms");

        String appId = new AppId(id).toString();
        Optional<? extends DomainEventMessage<?>> domainEventMessage = eventStore.readEvents(appId).asStream()
                .filter(event -> Objects.equals(AppCreatedEvent.class, event.getPayloadType())).findFirst();
        String appType1 = null;
        if (domainEventMessage.isPresent()) {
            appType1 = ((AppCreatedEvent) domainEventMessage.get().getPayload()).getType().getValue();
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println(
                "Using Event-Store::Type of app-id: " + id + " is: " + appType1 + " TOTAL TIME TAKEN:: "
                        + (endTime2 - endTime) + "ms");
    }

    @GetMapping("/shortDesc/{id}/{versionId}")
    public void getAppTypeByIdFromEventStore(@PathVariable String id, @PathVariable String versionId)
            throws InterruptedException, ExecutionException {
//        Optional<Long> lastSequenceNumberFor = eventStore
//                .lastSequenceNumberFor(appAddedToMachineEvent.getAppReference().getAppId().toString());
        String appId = new AppId(id).toString();
        long startTime = System.currentTimeMillis();
        Optional<? extends DomainEventMessage<?>> eventMessage = eventStore.readEvents(appId).asStream()
                .filter(event -> Objects.equals(AppVersionShortDescriptionUpdatedEvent.class, event.getPayloadType()))
                .filter(event -> Objects.equals(versionId,
                        ((AppVersionShortDescriptionUpdatedEvent) event.getPayload()).getVersionId().getValue()))
                .reduce((first, second) -> second);
        long endTime = System.currentTimeMillis();
        if (eventMessage.isPresent()) {
            System.out.println("Using Event-Store::shortDesc of app-id: " + id + " is: "
                    + ((AppVersionShortDescriptionUpdatedEvent) eventMessage.get().getPayload()).getShortDescription().getValue()
                    + " current seq number:: " + eventMessage.get().getSequenceNumber() + " TOTAL TIME TAKEN:: "
                    + (endTime - startTime) + "ms");
        }

        String appShortDescription = queryGateway
                .query(new FindAppShortDescriptionByIdQuery(id, versionId), ResponseTypes.instanceOf(String.class))
                .get();
        long endTime2 = System.currentTimeMillis();
        System.out.println(
                "Using QueryHandler::short desc. of app-id: " + id + " is: " + appShortDescription + " TOTAL TIME TAKEN:: "
                        + (endTime2 - endTime) + "ms");
    }
}
