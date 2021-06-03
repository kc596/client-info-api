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
5. Cancel-and-clean-up behavior represented by the Disposable interface. subscribe() have a Disposable return type and subscription can be cancelled, by calling its dispose() method.


### Frameworks and Libraries


| Name              | Description   |
|---                |---            |
|[Spring Boot]      | framework to create stand-alone, production-grade Spring based Applications |
|[Spring WebFlux]   | reactive programming support for web applications |
|[Spring Reactor]   | fully non-blocking reactive programming foundation for the JVM |
|[Spring WebClient] | non-blocking http client with support for reactive streams |


### Schedulers

The Schedulers class has static methods that give access to the following execution contexts:

1. No execution context (**Schedulers.immediate()**)

2. A single, reusable thread (**Schedulers.single()**)

    This method reuses the same thread for all callers, until the Scheduler is disposed. If you want a per-call dedicated thread, use **Schedulers.newSingle()** for each call.

3. An unbounded elastic thread pool (**Schedulers.elastic()**)

4. A bounded elastic thread pool (**Schedulers.boundedElastic()**). 
 
5. A fixed pool of workers that is tuned for parallel work (**Schedulers.parallel()**). 

    It creates as many workers as you have CPU cores.
    
> While boundedElastic is made to help with legacy blocking code if it cannot be avoided, single and parallel are not. 
> As a consequence, the use of Reactor blocking APIs (**block()**, **blockFirst()**, **blockLast()** 
> (as well as iterating over **toIterable()** or **toStream()**) inside the default single and parallel schedulers) results in an **IllegalStateException** being thrown.

Reactor offers two means of switching the execution context (or Scheduler) in a reactive chain: **publishOn** and **subscribeO**.

### Errors

1. Any error in a reactive sequence is a terminal event. 
    Even if an error-handling operator is used, it does not let the original sequence continue. 
    Rather, it converts the *onError* signal into the start of a new sequence.

2. When subscribing, the **onError** callback at the end of the chain is akin to a catch block. 
    There, execution skips to the catch in case an Exception is thrown,

3. **onErrorReturn** - equivalent of “Catch and return a static default value”

4. **onErrorResume** - “Catch and execute an alternative path with a fallback method”
    ```java
    Flux.just("timeout1", "unknown", "key2")
        .flatMap(k -> callExternalService(k)
            .onErrorResume(error -> { 
                if (error instanceof TimeoutException) 
                    return getFromCache(k);
                else if (error instanceof UnknownKeyException)  
                    return registerNewEntry(k, "DEFAULT");
                else
                    return Flux.error(error); 
            })
        );
    ```
5. **onErrorMap** - Catch and rethrow

6. **doOnError** - equivalent of “Catch, log an error-specific message, and re-throw”.
    It still terminates with an error, unless we use an error-recovery operator here.

7. **doFinally** is about side-effects that you want to be executed whenever the sequence 
    terminates (with onComplete or onError) or is cancelled. It gives you a hint as to what kind of termination triggered the side-effect.
    
8. **using** handles the case where a Flux is derived from a resource and that resource must be acted upon whenever processing is done.

9. **retry** - works by re-subscribing to the upstream Flux in case of error. 
    Doesn't starts from point of error but from start of stream. **retryWhen**
   
Reactor, defines a set of exceptions (such as *OutOfMemoryError*) that are always deemed to be fatal. 
These errors mean that Reactor cannot keep operating and are thrown rather than propagated.


There are also cases where an unchecked exception still cannot be propagated (most notably during the subscribe and request phases),
due to concurrency races that could lead to double onError or onComplete conditions. 
When these races happen, the error that cannot be propagated is “dropped”.
The corresponding hooks, **onNextDropped** and **onErrorDropped**, let you provide a global Consumer for these drops.


#### Checked Exceptions

Reactor has an Exceptions utility class that you can use to ensure that exceptions are wrapped only if they are checked exceptions:

- Use the **Exceptions.propagate** method to wrap exceptions, if necessary. It also calls throwIfFatal first and does not wrap RuntimeException.

- Use the **Exceptions.unwrap** method to get the original unwrapped exception (going back to the root cause of a hierarchy of reactor-specific exceptions).





[Spring Boot]: <https://spring.io/projects/spring-boot>
[Spring WebFlux]: <https://www.baeldung.com/spring-webflux>
[Spring Reactor]: <https://projectreactor.io/docs/core/release/reference/#getting-started-introducing-reactor>
[Spring WebClient]: <https://www.baeldung.com/spring-5-webclient>
[Java Streams API]: <https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html>
[Introduction to Reactive Programming]: <https://projectreactor.io/docs/core/release/reference/#intro-reactive>
