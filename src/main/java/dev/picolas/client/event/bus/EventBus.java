package dev.picolas.client.event.bus;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple annotation-based event bus.
 *
 * Usage:
 *   EventBus bus = ...;
 *   bus.subscribe(this);           // register all @EventHandler methods
 *   bus.post(new RenderEvent(...)); // dispatch to listeners
 */
public class EventBus {

    private final Map<Class<?>, List<ListenerMethod>> listeners = new ConcurrentHashMap<>();

    /** Subscribe all methods annotated with {@link EventHandler} in {@code listener}. */
    public void subscribe(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventHandler.class)) continue;
            if (method.getParameterCount() != 1) continue;

            Class<?> eventType = method.getParameterTypes()[0];
            method.setAccessible(true);

            listeners.computeIfAbsent(eventType, k -> Collections.synchronizedList(new ArrayList<>()))
                     .add(new ListenerMethod(listener, method));
        }
    }

    /** Remove all listeners registered by {@code listener}. */
    public void unsubscribe(Object listener) {
        for (List<ListenerMethod> list : listeners.values()) {
            list.removeIf(lm -> lm.owner() == listener);
        }
    }

    /** Dispatch {@code event} to every matching listener. */
    public void post(Object event) {
        List<ListenerMethod> list = listeners.get(event.getClass());
        if (list == null) return;
        for (ListenerMethod lm : list) {
            try {
                lm.method().invoke(lm.owner(), event);
            } catch (Exception e) {
                // Never let a bad listener crash the client
                dev.picolas.client.PicolasClient.LOGGER
                        .error("Error dispatching event {} to {}", event.getClass().getSimpleName(),
                               lm.owner().getClass().getSimpleName(), e);
            }
        }
    }

    /** Internal holder for a registered listener. */
    private record ListenerMethod(Object owner, Method method) {}
}
