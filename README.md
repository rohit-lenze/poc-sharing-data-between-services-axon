# poc-sharing-data-between-services-axon

## POC to explore below options of data sharing between multiple services:
1. using axon query handler
2. Using axon event store.

## There are two services in this project:
1. Service1: It behaves as the upstream service and uses spring's CommandLineRunner to create multiple apps/libraries in the elastic 
             search and corresponding events in the axon event store.
2. Service2: It behaves as the downstream service and reads data from service1 by sending queries and also accessing axon event store.

Apart from above there is commonapi which is included as dependency in above services to share all events, queries and value objects.

## To see the outcome of the poc perform below steps
  - Pull repo and checkout.
  - Build poc-sharing-data-between-services-axon project and create JAR using `mvn package`.
  - Run the docker compose file to start axon and elastic-search services.
  - Start Service1 and Service2.

Currently Service2 fetches two types of data:
  - Immutable( In this case it is appType, as it cannot be changed after the app is created)
  - Mutable( In this case it is appShortFDescription, as it can be changed after the app is created)
  
 To get apptype of an app, hit below url in browser, in console it shows the total time taken to fetch the apptype using query-handler
 as well as using event-store respectively:
  - http://localhost:8081/appType/{appId}.
 
 To get short-description of an app hit below url in browser, in console it shows the total time taken to fetch the short-description
 using query-handler as well as using event-store respectively:
  - http://localhost:8081/shortDesc/{appId}/{versionId}
  
## After multiple runs below is the outcome:
### With Immutable data, which is pretty straigh forward for event store as we need to process only one event in event store for a specific aggregateIdentifier
| AppId | aggregateSequenceNumber | AppType | Time Taken with Query-Handler | Time Taken with Event-Store |
|-------|-------------------------|---------|-------------------------------|-----------------------------|
|d6658316-4d4d-465b-af65-bfe68c0999d8|0|App|186ms|186ms
|d6658316-4d4d-465b-af65-bfe68c0999d8|0|App|186ms|186ms
|d6658316-4d4d-465b-af65-bfe68c0999d8|0|App|186ms|186ms

### With Mutable data, which is not that straight forward in event store as we need to process multiple events(20000 in this case) in event store for a specific aggregateIdentifier.
| AppId | aggregateSequenceNumber | AppType | Time Taken with Query-Handler | Time Taken with Event-Store |
|-------|-------------------------|---------|-------------------------------|-----------------------------|
|d6658316-4d4d-465b-af65-bfe68c0999d8|0|App|186ms|186ms
|d6658316-4d4d-465b-af65-bfe68c0999d8|0|App|186ms|186ms
|d6658316-4d4d-465b-af65-bfe68c0999d8|0|App|186ms|186ms
