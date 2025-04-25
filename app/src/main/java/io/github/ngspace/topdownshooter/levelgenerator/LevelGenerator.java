package io.github.ngspace.topdownshooter.levelgenerator;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.ngspace.topdownshooter.GeneratedGameScene;
import io.github.ngspace.topdownshooter.gameobjects.AGameObject;
import io.github.ngspace.topdownshooter.gameobjects.Sprite;
import io.github.ngspace.topdownshooter.gameobjects.TriggerGameObject;
import io.github.ngspace.topdownshooter.renderer.renderer.Textures;
import io.github.ngspace.topdownshooter.utils.Bounds;

public class LevelGenerator {
    LevelReader reader;
    public LevelGenerator(GeneratedGameScene scene) throws JSONException, IOException {
        reader = new LevelReader(scene);
    }
    public void generateRoom(GeneratedGameScene scene, String type, int x, int y) {
        generateRoom(scene, getRandomRoomOfType(scene, type, x, y), x, y);
    }
    public void generateRoom(GeneratedGameScene scene, Room room, int x, int y) {
        if (room.parentRoomName()!=null) generateRoom(scene, reader.getRoomOfName(room.parentRoomName()), x, y);
        for (Element element : room.elements()) {
            var uielem = generateElement(scene, element, x + room.offset().x, y + room.offset().y);
            if (uielem==null) continue;
            scene.addPhysicsObject(uielem);
            uielem.setVisible(element.visible());
        }
    }

    private AGameObject generateElement(GeneratedGameScene scene, Element element, int x, int y) {
        Bounds bounds = element.bounds().add(x, y);
        return switch (element.type()) {
            case "sprite": yield new Sprite(Textures.STARSET, bounds);
            case "door": {
                AtomicBoolean b = new AtomicBoolean();
                String roomType = element.getString("genRoomType");
                int genOffsetX = element.getInt("genOffsetX");
                int genOffsetY = element.getInt("genOffsetY");
                Sprite door = null;
                if (element.getBoolean("playerTriggered")) {
                    door = new TriggerGameObject(Textures.FUCKOPENGL, bounds.x(), bounds.y(), bounds.width(), bounds.height(), c -> {
                        if (b.get()) return;
                        b.set(true);
                        generateRoom(scene, roomType, (int) bounds.x() + genOffsetX, (int) bounds.y() + genOffsetY);
                    });
                    ((TriggerGameObject)door).setSelfDestroyCondition((c,a)->true);
                } else {
                    generateRoom(scene, roomType, (int) bounds.x() + genOffsetX, (int) bounds.y() + genOffsetY);
                    door = new Sprite(Textures.FUCKOPENGL, bounds);
                    door.setTrigger(true);
                }
                yield door;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + element.type());
        };
    }

    private Room getRandomRoomOfType(GeneratedGameScene scene, String type, int x, int y) {
        return reader.getRoomsOfType(type)[0];
    }
}