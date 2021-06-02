# client-info-api

1. API to view UA, IP, PROXY and FORWARDED-FOR of client. <br />
**GET** ```/clientinfo/view```

2. API to view *whois* info (and ip address) of any url (or bulk). <br />
**GET** | **POST**                                              ```/whoisinfo/?domain=<url>``` <br />
**POST** &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ```/whoisinfo/bulk```

## Deploying on aws
1. Setup EC2 instance.
2. Setup Elastic IP.
3. Edit security rules to allow incoming connections.
4. Clone this project.
```git clone https://github.com/kc596/client-info-api.git```
5. Run aws deploy script for Ubuntu 16 else follow equivalent steps.
```sudo sh aws-deploy-and-run.sh```


## Reactive Programming

Everything in the world is in motion - Traffic, weather, people, bank transactions... and are constantly changing.
These different events are happening at the same time - sometimes they are independent, sometimes they can converge and interact.
Reactive programming tries to model the code same way as world - multiple concurrent streams of event or data.

In reactive streams, it is the Publisher that notifies the Subscriber of newly available values as they come, and this push aspect is the key to being reactive.
Operations applied to pushed values are expressed declaratively rather than imperatively - programmer expresses the logic of the computation rather than describing its exact control flow.

Example - `Observable.interval()` (in RxJava) will push a consecutive `Long` at each specified time interval. 
This `Long` emission is not only data but also an event!

[Java Streams API] is pull based even though the operators and syntax may look similar. 

Read [Introduction to Reactive Programming] for more.

### Why Reactive Programming?
1. Blocking wastes resources
2. Better than callbacks - no callback hell
3. Better than Future -

    i. It is easy to end up with another blocking situation with Future objects by calling the get() method.
    
    ii. They do not support lazy computation.
   
    iii. They lack support for multiple values and advanced error handling.

4. Composability and readability with high value abstraction that is concurrency-agnostic.

## Frameworks and Libraries


| Name              | Description   |
|---                |---            |
|[Spring Boot]      | framework to create stand-alone, production-grade Spring based Applications |
|[Spring WebFlux]   | reactive programming support for web applications |
|[Spring Reactor]   | fully non-blocking reactive programming foundation for the JVM |
|[Spring WebClient] | non-blocking http client with support for reactive streams |



[Spring Boot]: <https://spring.io/projects/spring-boot>
[Spring WebFlux]: <https://www.baeldung.com/spring-webflux>
[Spring Reactor]: <https://projectreactor.io/docs/core/release/reference/#getting-started-introducing-reactor>
[Spring WebClient]: <https://www.baeldung.com/spring-5-webclient>
[Java Streams API]: <https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html>
[Introduction to Reactive Programming]: <https://projectreactor.io/docs/core/release/reference/#intro-reactive>
