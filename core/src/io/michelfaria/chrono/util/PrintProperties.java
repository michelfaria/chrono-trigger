package io.michelfaria.chrono.util;

import com.badlogic.gdx.maps.MapProperties;

import java.util.Iterator;

public final class PrintProperties {
    private PrintProperties() {
    }

    public static void print(MapProperties map) {
        Iterator<String> it = map.getKeys();
        while (it.hasNext()) {
            String key = it.next();
            Object value = map.get(key);
            System.out.println("key=" + key + ",value=" + value);
        }
    }
}
