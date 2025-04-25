package io.github.ngspace.topdownshooter.levelgenerator;

import android.graphics.Point;
import android.graphics.PointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.ngspace.topdownshooter.engine.GameScene;
import io.github.ngspace.topdownshooter.utils.Bounds;
import io.github.ngspace.topdownshooter.utils.Logcat;

public class LevelReader {
    Map<String, Room[]> room_types = new HashMap<>();
    Map<String, Room> room_names = new HashMap<>();

    public LevelReader(GameScene scene) throws IOException, JSONException {
        JSONObject types = new JSONObject(readResource(scene, "room_types.json"));
        for (Iterator<String> it = types.keys(); it.hasNext(); ) {
            String key = it.next();
            JSONArray jsonArray = types.getJSONArray(key);
            Room[] rooms = new Room[jsonArray.length()];
            for (int i=0;i<jsonArray.length();i++) {
                String roomName = jsonArray.getString(i);
                rooms[i] = processRoom(scene, roomName);
                room_names.put(roomName, rooms[i]);
            }
            room_types.put(key, rooms);
        }
    }

    private Room processRoom(GameScene scene, String roomname) throws IOException, JSONException {
        JSONObject types = new JSONObject(readResource(scene, roomname));
        String parent = types.has("parent") ? types.getString("parent") : null;
        int offsetx = types.has("offsetX") ? types.getInt("offsetX") : 0;
        int offsety = types.has("offsetY") ? types.getInt("offsetY") : 0;
        JSONArray jsonArray = types.getJSONArray("elements");
        Element[] elements = new Element[jsonArray.length()];
        for (int i=0;i<jsonArray.length();i++)
            elements[i] = processElement(jsonArray.getJSONObject(i));
        return new Room(elements, parent, new Point(offsetx, offsety));
    }

    private Element processElement(JSONObject element) throws JSONException {
        String type = element.has("type") ? element.getString("type") : "sprite";
        boolean trigger = element.has("trigger") && element.getBoolean("trigger");
        boolean visible = !element.has("visible") || element.getBoolean("visible");
        int x = element.getInt("x");
        int y = element.getInt("y");
        int width = element.getInt("width");
        int height = element.getInt("height");
        Map<String, Object> customdata = new HashMap<>();
        if (element.has("custom")) {
            JSONObject custom = element.getJSONObject("custom");
            for (Iterator<String> it = custom.keys(); it.hasNext();) {
                String key = it.next();
                customdata.put(key, custom.get(key));
            }
        }
        return new Element(new Bounds(x, y, width, height), type, trigger, visible, customdata);
    }

    private String readResource(GameScene scene, String name) throws IOException {
        return new BufferedReader(new InputStreamReader(scene.getAssets().open(name)))
                .lines().parallel().collect(Collectors.joining("\n"));
    }

    public Map<String, Room[]> getRoomTypesMap() {return room_types;}
    public Room[] getRoomsOfType(String type) {return getRoomTypesMap().get(type);}

    public Room getRoomOfName(String s) {
        Logcat.log(s, room_names.toString());
        return room_names.get(s);
    }
}
