# Sproutigy Java Commons Basement

The Sproutigy Java Commons Basement provides a set of utilities, helper classes and reusable solutions that simplifies some technical aspects of coding in JVM (especially in Java language).
Can be used standalone or in companion with [Google Guava](https://github.com/google/guava) and/or [Apache Commons](https://commons.apache.org/) (what we strongly advise).


## Requirements
Requires Java 1.7+.
No additional required dependencies.
[SLF4J API](http://www.slf4j.org/) is an optional dependency.


## Add as dependency to your project

### Maven
```xml
<dependency>
    <groupId>com.sproutigy.commons</groupId>
    <artifactId>basement</artifactId>
    <version>RELEASE</version>
</dependency>
```

### Gradle
```
dependencies {
  compile 'com.sproutigy.commons:basement:RELEASE'
}
```


## Functionality

### Standard functional interfaces

```
Builder builder = ...
builder.build();

Provider provider = ...
provider.provide();

MyEvent event = ...
Handler<MyEvent> handler = ...
handler.handle(event);
```


### Collections

#### Collect

Easy conversion between different standard collection types. Supports array, ```Enumeration```, ```Iterator```, ```Iterable```, ```Collection```, ```Set```, ```List``` and others:

```
Collect.asArray(elements);
Collect.asArray(typeClass, elements);
Collect.asList(elements);
Collect.asArrayList(elements);
Collect.asLinkedList(elements);
Collect.asCopyOnWriteArrayList(elements);
Collect.asSet(elements);
Collect.asHashSet(elements);
Collect.asLinkedHashSet(elements);
Collect.asTreeSet(elements);
Collect.asEnumeration(elements);
Collect.asIterator(elements);
Collect.asIterable(elements);

Collect.to(collection, elements);

Collect.remove(collection, elements);

Collect.containsAll(collection, elements);
Collect.containsAny(collection, elements);

Collect.unmodifiable(collection_or_iterator);
```


#### MapBuilder
Build maps fluently:

```
MapBuilder.<String, Integer>ofHashMap().put("a", 1).put("b", 2).build();
MapBuilder.<String, Integer>ofLinkedHashMap().put("a", 1).put("b", 2).build();
MapBuilder.<String, Integer>ofTreeMap().put("a", 1).put("b", 2).build();
```


### FileSystem

#### FSWatch
To watch for directory or file changes, use FSWatch instance:
```
FSWatch watch = new FSWatch();
watch.watch("C:\\test"); //directory
watch.watch("C:\\test2\\note.txt"); //file
watch.addListener((path, eventType) -> System.out.println(eventType + " " + path)); //possible events: CREATED, MODIFIED, DELETED
watch.setDelayMillis(500); //waits for a half of a second to aggregate events and results in last event for a specific path (e.g. to compensate multiple modification events over short amount of time)
watch.start();
Thread.sleep(60000);
watch.close();
```


### Utilities

#### Obj

Generic-purpose Object utility that works with strings, collections and ```Optional```s (both from Java and Guava).
```
boolean empty = Obj.isEmpty(str);
Object valid = Obj.nonnull(input); //may throw an exception
Object value = Obj.nonempty(input); //may throw an exception

boolean isOpt = Obj.isOptional(opt);
Object val = Obj.nullable(opt);

String blabla = null;
blabla = Obj.stringify(blabla); //empty string "" instead of "null"
```

#### Close
To close ```Closeable``` and ```AutoClosable``` instances, helpful one-line ```Close``` utility can be used (depending on needs):
```java
Close.unchecked(stream); //may throw unchecked exception
Close.silently(stream); //catches and ignores exception
Close.loggable(stream); //catches and logs exception
Close.loggableAndThrowable(stream); //catches, logs exception and rethrows it
```

#### Sleep
To delay current thread's processing, one-line ```Sleep``` utility may be used (depending on needs):
```java
Sleep.interruptable(10, TimeUnit.SECONDS); //may throw InterruptedException
Sleep.unchecked(10, TimeUnit.SECONDS); //may throw unchecked exception (RuntimeException)
Sleep.untilInterrupted(10, TimeUnit.SECONDS); //never throws exception
```



### Exceptions
When some functionality is not ready, ```NotImplementedException``` may be thrown:
```
public void futureMethod() {
    throw new NotImplementedException();
}
```

To check an intermediate or root cause of an exception:
```
try {
    execute();
} catch (Exception e) {
    if (Throwables.isCausedBy(NullPointerException.class, e)) {
        System.out.println("Start using Optional!");
        throw e;
    } else {
        throw e;
    }
}
```


### Platform

#### Operating System and Architecture
```
Platform platform = Platform.current();
if (Platform.current() instanceof WindowsPlatform) {
    //WINDOWS
} else if (Platform.current() instanceof MacPlatform) {
    //MACOS
} else {
    //other
}

File userDir = platform.getUserHome();
File appDataDir = platform.getAppData("MyApp");

platform.browse("http://www.sproutigy.com/"); //open URL in default web browser
platform.open(file); //run default application

boolean isOS64bit = PlatformArchitecture.isOS64bit();
boolean isJVM64bit = PlatformArchitecture.isJVM64bit();
```


#### LocalHost utility
```
InetAddress inetAddr = LocalHost.get();
String hostname = LocalHost.getName();
String ip = LocalHost.getIP();
```


#### ResourceFile
When playing with resource files and there's a need to have it accessible as a (temporary) file in local FS,
`ResourceFile` can be used as it manages temporary file creation and deletion as also preparing the file on demand:
```
ResourceFile myResource = new ResourceFile(MyClass.class, "myResource.txt");
File tempResFile = myResource.toFile();
//do something with it
myResource.close();
```


#### RuntimeUtil
To run garbage collection:
```
RuntimeUtil.forceGarbageCollect();
```


## More
For more information and commercial support visit [Sproutigy](http://www.sproutigy.com/opensource)
