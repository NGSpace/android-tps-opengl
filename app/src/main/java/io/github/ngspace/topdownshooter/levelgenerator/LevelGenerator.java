package io.github.ngspace.topdownshooter.levelgenerator;

import org.json.JSONException;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiPredicate;

import io.github.ngspace.topdownshooter.GeneratedGameScene;
import io.github.ngspace.topdownshooter.gameobjects.AGameObject;
import io.github.ngspace.topdownshooter.gameobjects.Sprite;
import io.github.ngspace.topdownshooter.gameobjects.TriggerGameObject;
import io.github.ngspace.topdownshooter.renderer.renderer.Textures;
import io.github.ngspace.topdownshooter.utils.Bounds;
import io.github.ngspace.topdownshooter.utils.Logcat;

public class LevelGenerator {
    LevelReader reader;
    Random random = new Random();
    int keys;

    public LevelGenerator(GeneratedGameScene scene) throws JSONException, IOException {
        reader = new LevelReader(scene);
    }
    public void generateLevel(GeneratedGameScene scene, String type, int x, int y) {
        GeneratedLevel level = new GeneratedLevel();
        generateRoom(scene, getRandomRoomOfType(type), x, y, level);
//        for (AGameObject element : level.elements()) {
////            if (scene.)
//        }
        for (AGameObject element : level.elements) {
            scene.addPhysicsObject(element);
//            uielem.setVisible(element.visible());
        }
    }
    public void generateRoom(GeneratedGameScene scene, String type, int x, int y, GeneratedLevel level) {
        generateRoom(scene, getRandomRoomOfType(type), x, y, level);
    }
    public void generateRoom(GeneratedGameScene scene, Room room, int x, int y, GeneratedLevel level) {
        if (room.parentRoomName()!=null) generateRoom(scene, reader.getRoomOfName(room.parentRoomName()), x, y, level);
        for (Element element : room.elements()) {
            var uielem = addElement(scene, element, x + room.offset().x, y + room.offset().y, level);
            if (uielem==null) continue;
//            scene.addPhysicsObject(uielem);
            uielem.setVisible(element.visible());
        }
    }

    private AGameObject addElement(GeneratedGameScene scene, Element element, int x, int y, GeneratedLevel level) {
        Bounds bounds = element.bounds().add(x, y);
        return switch (element.type()) {
            case "sprite": {
                var wall = new Sprite(Textures.STARSET, bounds);
                level.add(wall);
                yield wall;
            }
            case "door": {
                AtomicBoolean b = new AtomicBoolean();
                String roomType = element.getString("genRoomType");
                int genOffsetX = element.getInt("genOffsetX");
                int genOffsetY = element.getInt("genOffsetY");
                Sprite door = null;
                if (element.getBoolean("canBeExitDoor") && random.nextBoolean() && !level.hasExitDoor) {
                    door = new TriggerGameObject(Textures.FUCKOPENGL, bounds, c -> {
                        if (b.get()) return;
                        b.set(true);
                        generateLevel(scene, roomType, (int) bounds.x() + genOffsetX, (int) bounds.y() + genOffsetY);
                    });
                    ((TriggerGameObject)door).setSelfDestroyTrigger((c,a)->true);
                    level.add(door);
                    level.hasExitDoor = true;

                    // Generate next room
                    generateLevel(scene, roomType, (int) bounds.x() + genOffsetX, (int) bounds.y() + genOffsetY);

                    yield door;
                }
                // Generate next room
                generateLevel(scene, roomType, (int) bounds.x() + genOffsetX, (int) bounds.y() + genOffsetY);

                if (!level.keys.isEmpty()&&random.nextInt(10)>0) {
                    KeyElement key = level.keys.remove();
                    level.add(key.element());
                }
                door = new Sprite(Textures.FUCKOPENGL, bounds);
                door.setTrigger(true);
                level.add(door);
                yield door;
            }
            case "key": {
                int keyID = keys++;
                TriggerGameObject key = new TriggerGameObject(Textures.STARSET, bounds, aGameObject -> {
                    if (aGameObject==scene.player)
                        scene.keys.add(keyID);
                });
                key.setSelfDestroyTrigger((c,a)->true);
                key.setTrigger(true);
                level.keys.add(new KeyElement(key, keyID));
                yield key;
            }
            case "startTrigger": {
                TriggerGameObject trigger = new TriggerGameObject(Textures.STARSET, bounds, aGameObject -> {
                    if (aGameObject==scene.player) {
                        //TODO ungenerate room and generate start
//                        generateRoom(scene, "Start", )
                    }
                });
                trigger.setSelfDestroyTrigger((c,a)->true);
                trigger.setTrigger(true);
                level.add(trigger);
                yield trigger;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + element.type());
        };
    }

    private Room getRandomRoomOfType(String type) {
        var rooms = reader.getRoomsOfType(type);
        return rooms[random.nextInt(rooms.length)];
    }

    public static record KeyElement(AGameObject element, int keyID) {}
}