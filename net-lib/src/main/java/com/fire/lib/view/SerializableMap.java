package com.fire.lib.view;

import java.io.Serializable;
import java.util.Map;

/**
 * Map 序列化传值
 */
public class SerializableMap implements Serializable {

    private Map<String,Object> map;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
