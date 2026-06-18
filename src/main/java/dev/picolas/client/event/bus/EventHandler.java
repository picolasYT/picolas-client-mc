package dev.picolas.client.event.bus;

import java.lang.annotation.*;

/** Marks a method as an event listener for the {@link EventBus}. */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {}
