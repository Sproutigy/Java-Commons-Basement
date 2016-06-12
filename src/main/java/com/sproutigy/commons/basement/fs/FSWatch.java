package com.sproutigy.commons.basement.fs;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public class FSWatch implements AutoCloseable, Runnable {

    public interface Listener {
        void onFileSystemEvent(Path path, EventType eventType);
    }

    public enum EventType {
        CREATED, MODIFIED, DELETED
    }

    private WatchService watchService;
    private Map<String, Path> watchPaths = new ConcurrentHashMap<>();
    private Collection<Listener> listeners = new CopyOnWriteArrayList<>();
    private ExecutorService executor;
    private ScheduledExecutorService scheduler;
    private AtomicBoolean running = new AtomicBoolean(false);
    private final Map<Path, EventType> events = new LinkedHashMap<>();
    private volatile long delayMillis = 0;


    public FSWatch() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FSWatch addListener(Listener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        listeners.add(listener);

        return this;
    }

    public FSWatch removeListener(Listener listener) {
        listeners.remove(listener);

        return this;
    }

    public long getDelayMillis() {
        return delayMillis;
    }

    public FSWatch setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
        if (delayMillis > 0 && scheduler == null) {
            scheduler = Executors.newScheduledThreadPool(1);
        } else {
            scheduler = null;
        }
        return this;
    }

    public FSWatch setDelay(long duration, TimeUnit unit) {
        setDelayMillis(unit.toMillis(duration));
        return this;
    }

    public FSWatch watch(String path) throws IOException {
        Path p = Paths.get(path);
        String s = p.toAbsolutePath().toString();
        if (!isFolder(p)) {
            p = p.getParent();
        }
        watchPaths.put(s, p);

        p.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

        return this;
    }

    public FSWatch watch(String... paths) throws IOException {
        for(String path : paths) {
            watch(path);
        }
        return this;
    }

    public void unwatch(String path) throws IOException {
        watchPaths.get(path).register(watchService);
    }

    public FSWatch unwatch(String... paths) throws IOException {
        for(String path : paths) {
            unwatch(path);
        }
        return this;
    }

    public FSWatch start() {
        if (running.get()) {
            throw new IllegalStateException("Already running");
        }

        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }

        executor.execute(this);

        return this;
    }

    public void run() {
        if (running.get()) {
            throw new IllegalStateException("Already running");
        }
        running.set(true);

        WatchKey key = null;
        while(true) {
            try {
                key = watchService.poll(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ClosedWatchServiceException e) {
                running.set(false);
                return;
            }

            if (key != null) {
                Path watchPath = (Path) key.watchable();

                WatchEvent.Kind<?> kind = null;
                for (WatchEvent<?> watchEvent : key.pollEvents()) {
                    kind = watchEvent.kind();
                    if (OVERFLOW == kind) {
                        continue;
                    }

                    Path relPath = ((WatchEvent<Path>) watchEvent).context();
                    Path path = watchPath.resolve(relPath).toAbsolutePath();
                    if (!isWatchingPath(path)) {
                        return;
                    }

                    EventType eventType = null;
                    if (kind == ENTRY_CREATE) eventType = EventType.CREATED;
                    if (kind == ENTRY_MODIFY) eventType = EventType.MODIFIED;
                    if (kind == ENTRY_DELETE) eventType = EventType.DELETED;

                    if (eventType != null) {
                        if (delayMillis > 0) {
                            synchronized (events) {
                                boolean schedule = events.isEmpty();
                                events.put(path, eventType);
                                
                                if (schedule) {
                                    ScheduledExecutorService scheduler = this.scheduler;
                                    if (scheduler != null) {
                                        scheduler.schedule(new Runnable() {
                                            @Override
                                            public void run() {
                                                synchronized (events) {
                                                    for (Map.Entry<Path, EventType> event : events.entrySet()) {
                                                        emitEvent(event.getKey(), event.getValue());
                                                    }
                                                    events.clear();
                                                }
                                            }
                                        }, delayMillis, TimeUnit.MILLISECONDS);
                                    }
                                }
                            }
                        } else {
                            emitEvent(path, eventType);
                        }
                    }
                }

                if (!key.reset()) {
                    break; //loop
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    running.set(false);
                    return;
                }

                if (!running.get()) {
                    return; //get out
                }
            }
        }
    }

    public FSWatch stop() {
        running.set(false);

        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }

        events.clear();
        return this;
    }

    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void close() throws IOException {
        stop();

        watchService.close();
    }

    private void emitEvent(Path path, EventType eventType) {
        for (Listener listener : listeners) {
            try {
                listener.onFileSystemEvent(path, eventType);
            } catch (Exception ignore) {
            }
        }
    }

    private boolean isWatchingPath(Path path) {
        String s = path.toAbsolutePath().toString();
        if (watchPaths.containsKey(s)) {
            return true;
        }
        Path parentPath = path.getParent();
        if (parentPath == null) {
            return false;
        }
        return isWatchingPath(parentPath);
    }

    private static boolean isFolder(Path path) throws IOException {
        return (boolean) Files.getAttribute(path, "basic:isDirectory", NOFOLLOW_LINKS);
    }
}
