package io.github.ngspace.topdownshooter.levelgenerator;

import java.util.Map;

import io.github.ngspace.topdownshooter.gameobjects.AGameObject;
import io.github.ngspace.topdownshooter.utils.Bounds;

public record Element(Bounds bounds, String type, boolean trigger, boolean visible, Map<String, Object> customdata) {
    public Object get(String key) {return customdata.get(key);}
    public String getString(String key) {return (String) get(key);}
    public int getInt(String key) {return ((Number) get(key)).intValue();}
    public boolean getBoolean(String key) {return (Boolean) get(key);}
}
