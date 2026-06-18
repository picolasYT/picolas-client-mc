package dev.picolas.client.config;

import java.util.function.Consumer;

/**
 * A typed, named setting that belongs to a module.
 *
 * @param <T> Value type: Boolean, Integer, Double, Float, String, or an enum.
 */
public class Setting<T> {

    private final String name;
    private final String description;
    private T value;
    private final T defaultValue;
    private final T minValue;   // null for non-numeric types
    private final T maxValue;

    private Consumer<T> onChange;

    // ── Constructors ─────────────────────────────────────────────────────────

    /** For Boolean / String / enum settings. */
    public Setting(String name, String description, T defaultValue) {
        this(name, description, defaultValue, null, null);
    }

    /** For numeric settings with a range. */
    public Setting(String name, String description, T defaultValue, T min, T max) {
        this.name         = name;
        this.description  = description;
        this.value        = defaultValue;
        this.defaultValue = defaultValue;
        this.minValue     = min;
        this.maxValue     = max;
    }

    // ── Value access ─────────────────────────────────────────────────────────

    public T getValue() { return value; }

    public void setValue(T value) {
        this.value = clamp(value);
        if (onChange != null) onChange.accept(this.value);
    }

    public T getDefaultValue() { return defaultValue; }
    public T getMinValue()     { return minValue;     }
    public T getMaxValue()     { return maxValue;     }

    @SuppressWarnings("unchecked")
    private T clamp(T val) {
        if (minValue == null || maxValue == null) return val;
        if (val instanceof Comparable) {
            Comparable<T> c = (Comparable<T>) val;
            if (c.compareTo(minValue) < 0) return minValue;
            if (c.compareTo(maxValue) > 0) return maxValue;
        }
        return val;
    }

    // ── Metadata ──────────────────────────────────────────────────────────────

    public String getName()        { return name;        }
    public String getDescription() { return description; }

    public Setting<T> onChange(Consumer<T> handler) {
        this.onChange = handler;
        return this;
    }

    // ── Type helpers ──────────────────────────────────────────────────────────

    public boolean isBoolean()  { return value instanceof Boolean;  }
    public boolean isNumeric()  { return value instanceof Number;   }
    public boolean isString()   { return value instanceof String;   }
    public boolean isEnum()     { return value instanceof Enum;     }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
