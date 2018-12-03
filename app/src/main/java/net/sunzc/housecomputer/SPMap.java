package net.sunzc.housecomputer;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class SPMap {
    public static final String ENGINE_SPEED = "engine_speed";
    public static final String SIGNAL_CONFIG = "signal_config";
    private SharedPreferences sharedPreferences;

    public static SPMap load(Context context, String spFile) {
        return new SPMap(context, spFile, Context.MODE_PRIVATE);
    }

    public static SPMap loadWithProcess(Context context, String spFile) {
        return new SPMap(context, spFile, Context.MODE_MULTI_PROCESS);
    }

    private SPMap(Context context, String spFile, int mode) {
        sharedPreferences = context.getSharedPreferences(spFile, mode);
    }

    public void put(String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof Integer) {
            editor.putInt(key, (int) value);
        } else if (value instanceof Short) {
            editor.putInt(key, ((Short) value).intValue());
        } else if (value instanceof Byte) {
            editor.putInt(key, ((Byte) value).intValue());
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof String) {
            editor.putString(key, String.valueOf(value));
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else {
        }
        editor.apply();
    }

    public int get(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public short get(String key, short defaultValue) {
        return (short) sharedPreferences.getInt(key, defaultValue);
    }

    public byte get(String key, byte defaultValue) {
        return (byte) sharedPreferences.getInt(key, defaultValue);
    }

    public float get(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public long get(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public boolean get(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public String get(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public Set<String> get(String key, Set<String> defaultValue) {
        return sharedPreferences.getStringSet(key, defaultValue);
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public void remove(String key) {
        if (contains(key))
            sharedPreferences.edit().remove(key).apply();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    public Map<String, ?> toMap() {
        return sharedPreferences.getAll();
    }
}
