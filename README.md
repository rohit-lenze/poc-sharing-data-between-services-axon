# poc-sharing-data-between-services-axon

## POC to explore multiple options of data sharing between mutiple services:
1. using axon query handler
2. Using axon event store.

## There are two services:
1. Service1: It behaves as the upstream service and uses spring's CommandLineRunner to creates multiple apps/libraries in the elastic 
             search and corresponding events in the axon event store.
2. Service2: It behaves as the downstream service and reads data from service1 by sending queries and also accessing axon event store.

Apart from above there is commonapi which is included as dependency in above services to share all events, queries and value objects.

## To see the outcome of the poc perform below steps
  - Pull repo and checkout.
  - Build poc-sharing-data-between-services-axon project and create JAR using `mvn package`.
  - Run the docker compose file to start axon and elastic-search services.
  - Start Service1 and Service2.

 To get apptype of an app, hit below url in browser, in console it shows the total time taken to fetch the apptype using query-handler
 as well as using event-store respectively:
  - http://localhost:8081/appType/{appId}.
 
 To get short-description of an app hit below url in browser, in console it shows the total time taken to fetch the short-description
 using query-handler as well as using event-store respectively:
  - http://localhost:8081/shortDesc/{appId}/{versionId}