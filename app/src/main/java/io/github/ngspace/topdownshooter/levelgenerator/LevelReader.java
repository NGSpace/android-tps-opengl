package io.github.ngspace.topdownshooter.levelgenerator;

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

public class LevelReader {
    Map<String, Room[]> room_types = new HashMap<>();

    public LevelReader(GameScene scene) throws IOException, JSONException {
        JSONObject types = new JSONObject(readResource(scene, "room_types.json"));
        for (Iterator<String> it = types.keys(); it.hasNext(); ) {
            String key = it.next();
            JSONArray jsonArray = types.getJSONArray(key);
            String[] rooms = new String[jsonArray.length()];
            for (int i=0;i<jsonArray.length();i++)
                rooms[i] = jsonArray.getString(i);
            room_types.put(key, processRooms(scene, rooms));
        }
    }

    private Room[] processRooms(GameScene scene, String[] roomnames) throws IOException, JSONException {
        Room[] rooms = new Room[roomnames.length];
        for (int i = 0;i<roomnames.length;i++) {
            rooms[i] = processRoom(scene, roomnames[i]);
        }
        return rooms;
    }

    private Room processRoom(GameScene scene, String roomname) throws IOException, JSONException {
        JSONObject types = new JSONObject(readResource(scene, roomname));
        JSONArray jsonArray = types.getJSONArray("elements");
        Element[] elements = new Element[jsonArray.length()];
        for (int i=0;i<jsonArray.length();i++)
            elements[i] = processElement(jsonArray.getJSONObject(i));
        return new Room(elements);
    }

    private Element processElement(JSONObject element) throws JSONException {
        String type = element.has("type") ? element.getString("type") : "sprite";
        boolean collision = !element.has("collision") || element.getBoolean("collision");
        int x = element.getInt("x");
        int y = element.getInt("y");
        int width = element.getInt("width");
        int height = element.getInt("height");
        return new Element(new Bounds(x, y, width, height), type, collision);
    }

    public String readResource(GameScene scene, String name) throws IOException {
        return new BufferedReader(new InputStreamReader(scene.getAssets().open(name)))
                .lines().parallel().collect(Collectors.joining("\n"));
    }
}
