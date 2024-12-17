<div align="center">
  <img src=".assets/broadcast.png" alt="broadcast" width="250"/>
  <br>
  <img src="https://img.shields.io/badge/language-Java-magenta?style=flat" />
  <img src="https://img.shields.io/badge/release-v1.0.3-magenta?style=flat" />
  <img src="https://img.shields.io/badge/repository-jitpack.io-magenta?style=flat" />
  <img src="https://img.shields.io/badge/license-MIT-magenta?style=flat" />
</div>

# OVERVIEW

The library provides a set of tools for convenient realization of 
the system of sending messages between users of bots for social networks 
and not only.

---

The library provides a set of technologies to handle the 
dissemination of information across various integration services, 
with the ability to retrieve source records relative to which 
information is sent.

Records can be local imutable collections and other caches, 
as well as values from SQL database, the process of receiving 
can be asynchronous, split into parallel chunks, or just 
synchronously with the main thread.

---

# API

Examples of interaction with the API can be viewed in full 
in the `examples` module in the source code under the `src/main/java` 
directory.

Here I will try to tell a little more specifically what happens 
and how to use it on the examples that are implemented in the 
`examples` module

---

To begin with, it is worth understanding that the whole process 
happens with the main component of the library - `BroadcastEngine` - 
let's consider its initialization on the following example:

```java
private static final String[] NAMES = {"Flora", "John", "Mark", "Andrey"};

private static final RecordsMap<Integer, String> IMMUTABLE_RECORDS =
        RecordsMap.<Integer, String>builderHashMap()
                .putStream(Arrays.asList(10, 20, 30, 40), idNumber -> NAMES[(idNumber / 10) - 1])
                .build();

public BroadcastEngine createEngine() {
    AnnouncementExtractor<StringAnnouncement> announcementExtractor =
            AnnouncementExtractor.fromID(Integer.class, (id) -> new StringAnnouncement(String.format("[ID: %s] -> \"Hello world!\"", id)));

    BroadcastPipeline<Integer, StringAnnouncement> broadcastPipeline = BroadcastPipeline.createPipeline(Integer.class, StringAnnouncement.class)
            .setDispatcher(new STDOUTBroadcastDispatcher<>()) // optional
            .setRecordExtractor(Extractors.constant(IMMUTABLE_RECORDS.toRecordsSet()))
            .setAnnouncementExtractor(announcementExtractor)
            .setScheduler(Scheduler.threadScheduler(2)); // optional
    
    return new BroadcastEngine(broadcastPipeline);
}
```

So, let's try to understand this code in detail:

The first thing we do is to create `Records` - they are needed to 
identify fields/people/entities, regarding which some information will 
be distributed. For example - it can be a Telegram user who 
has `ChatID (Long)` and `Username (String)`, which in total gives us 
a record of the following type: Record<Long, String>, which further 
goes to all other distribution services of the library for processing, 
sending and creating notifications.

Next, we create and populate the `BroadcastPipeline`. It serves to 
initialize and identify distribution systems such as `Dispatcher`, 
`Record-Extractor`, `Message-Preparing`, `Scheduler` and `event listeners`, 
which have different implementations in the abstraction.

---

To send an alert at the current moment, we can use:

```java
BroadcastEngine broadcastEngine = createEngine();
broadcastEngine.broadcastNow();
```

To schedule a cyclic sending of an alert once at some 
time generating it, we can use:

```java
BroadcastEngine broadcastEngine = createEngine();
broadcastEngine.scheduleBroadcastEverytime(Duration.ofDays(1));
```

---

# WRAPPERS & FEATURES

The library also provides a set of its own pre-packaged tools 
to facilitate the realization of common business tasks

| Name          | Artifact ID                    | Version | API Usage                                                                                          |
|---------------|--------------------------------|---------|----------------------------------------------------------------------------------------------------|
| Hibernate     | `social-broadcast-hibernate`   | 1.0.3   | [View code examples](examples/src/main/java/io/broadcast/example/HibernateBroadcastExample.java)   |
| Spring Data   | `social-broadcast-spring-data` | 1.0.3   | [View code examples](examples/src/main/java/io/broadcast/example/SpringDataBroadcastExample.java)  |
| Telegram Bots | `social-broadcast-telegram`    | 1.0.3   | [View code examples](examples/src/main/java/io/broadcast/example/TelegramBotBroadcastExample.java) |
| Redis (jedis) | `social-broadcast-redis`       | 1.0.3   | [View code examples](examples/src/main/java/io/broadcast/example/JedisBroadcastExample.java)       |
| java.sql      | `social-broadcast-jdbc`        | 1.0.3   | [View code examples](examples/src/main/java/io/broadcast/example/JdbcH2BroadcastExample.java)      |
| javax.mail    | `social-broadcast-smtp`        | 1.0.3   | [View code examples](examples/src/main/java/io/broadcast/example/SMTPBroadcastExample.java)        |

To use one of the components specified in the table in your project, 
simply implement the dependency as follows, where `[Artifact-ID]` 
is the dependency identifier from the `Artifact ID` table column:

**Maven:**

```xml
<dependency>
    <groupId>com.github.MikhailSterkhov</groupId>
    <artifactId>[Artifact-ID]</artifactId>
    <version>[Version]</version>
</dependency>
```

**Gradle / Groovy:**

```groovy
implementation 'con.github.mikhailterkhov:[Artifact-ID]:[Version]'
```

---

# IMPORT TO PROJECT

You can install the dependency in your project using any build automation system, 
since the library is uploaded to the public jitpack.io repository, 
you just need to provide a link to it and the correct attributes 
of the dependency itself, depending on the type of build automation system:

**Maven:**

Repository:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Dependency:

```xml
<dependency>
    <groupId>com.github.MikhailSterkhov</groupId>
    <artifactId>social-broadcast-engine</artifactId>
    <version>1.0.3</version>
</dependency>
```

**Gradle / Groovy:**

Repository:

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

Dependency:

```groovy
implementation 'con.github.mikhailterkhov:social-broadcast-engine:1.0.3'
```
