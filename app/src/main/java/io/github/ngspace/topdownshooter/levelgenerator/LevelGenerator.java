package io.github.ngspace.topdownshooter.levelgenerator;

import org.json.JSONException;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.ngspace.topdownshooter.GeneratedGameScene;
import io.github.ngspace.topdownshooter.gameobjects.AGameObject;
import io.github.ngspace.topdownshooter.gameobjects.Sprite;
import io.github.ngspace.topdownshooter.gameobjects.Torrent;
import io.github.ngspace.topdownshooter.gameobjects.TriggerGameObject;
import io.github.ngspace.topdownshooter.renderer.renderer.Textures;
import io.github.ngspace.topdownshooter.utils.Bounds;

public class LevelGenerator {
    LevelReader reader;
    Random random = new Random();
    int keys;

    public LevelGenerator(GeneratedGameScene scene) throws JSONException, IOException {
        reader = new LevelReader(scene);
    }
    public void generateLevel(GeneratedGameScene scene, String type, int x, int y) throws FailedToGenLevelException {
        GeneratedLevel level = new GeneratedLevel();
        generateRoom(scene, getRandomRoomOfType(type), x, y, level);
        if (level.fuelCount<3)
            throw new FailedToGenLevelException("Fuel count too low: "+level.fuelCount+"!");
        for (var element : level.elements) {
            if (element.element() instanceof Torrent torrent) {
                scene.torrents.add(torrent);
            }
            if (element.element() instanceof Sprite sprite&&sprite.getTextureInfo()==Textures.EXIT_DOOR) {
                scene.exitDoor = element.element();
            }
            if (element.physics())
                scene.addPhysicsObject(element.element());
            else
                scene.addObject(element.element());
        }
        scene.fuelLeftToLeave = level.fuelCount;
    }
    public void generateRoom(GeneratedGameScene scene, String type, int x, int y, GeneratedLevel level) throws FailedToGenLevelException {
        generateRoom(scene, getRandomRoomOfType(type), x, y, level);
    }
    public void generateRoom(GeneratedGameScene scene, Room room, int x, int y, GeneratedLevel level) throws FailedToGenLevelException {
        if (room.parentRoomName()!=null) generateRoom(scene, reader.getRoomOfName(room.parentRoomName()), x, y, level);
        if (room.bounds()!=null) {
            Bounds roomBounds = room.bounds().add(x, y);
            for (GeneratedRoom room1 : level.rooms) {
                if (room1.bounds() != null && roomBounds.intersects(room1.bounds()))
                    throw new FailedToGenLevelException("Intersecting rooms: " + roomBounds + " and " + room1.bounds());
            }
            level.rooms.add(new GeneratedRoom(roomBounds));
        }
        for (Element element : room.elements()) {
            var uielems = addElement(scene, element, x + room.offset().x, y + room.offset().y, level);
            for (var uielem : uielems) {
                level.add(uielem);
                uielem.element().setVisible(element.visible());
            }
        }
    }

    private GeneratedElement[] addElement(GeneratedGameScene scene, Element element, int x, int y, GeneratedLevel level) throws FailedToGenLevelException {
        Bounds bounds = element.bounds().add(x, y);
        return switch (element.type()) {
            case "wall", "sprite": {
                var wall = new Sprite(Textures.WALL, bounds);
                yield new GeneratedElement[] {new GeneratedElement(wall, true)};
            }
            case "exit": {
                var scen = scene;
                var exit = new Sprite(Textures.EXIT_DOOR, bounds) {
                    @Override
                    public void collidedWith(AGameObject collider) {
                        super.collidedWith(collider);
                        if (!visible&&collider==scen.player)
                            scen.nextLevel(true);
                    }
                };
                yield new GeneratedElement[] {new GeneratedElement(exit, true)};
            }
            case "torrent": {
                var torrent = new Torrent(bounds.x(), bounds.y());
                yield new GeneratedElement[] {new GeneratedElement(torrent, true)};
            }
            case "door": {
                boolean blocked = random.nextInt(100) < element.getInt("blockedChance");

                if (blocked)
                    yield new GeneratedElement[]{new GeneratedElement(new Sprite(Textures.BLOCK, bounds), true)};

                // Generate next room
                generateRoom(scene, element.getString("genRoomType"),
                        (int) bounds.x() + element.getInt("genOffsetX"),
                        (int) bounds.y() + element.getInt("genOffsetY"), level);
                yield new GeneratedElement[] {};
            }
            case "fuel": {
                TriggerGameObject fuel = new TriggerGameObject(Textures.FUEL, bounds, aGameObject -> {
                    if (aGameObject==scene.player) {
                        scene.fuelLeftToLeave--;
                        if (scene.fuelLeftToLeave==0) {
                            scene.exitDoor.setVisible(false);
                        }
                    }
                });
                fuel.setSelfDestroyTrigger((c,a)->true);
                fuel.setTrigger(true);
                level.fuelCount++;
                yield new GeneratedElement[] {new GeneratedElement(fuel, true)};
            }
            default:
                throw new IllegalStateException("Unexpected value: " + element.type());
        };
    }

    private Room getRandomRoomOfType(String type) {
        var rooms = reader.getRoomsOfType(type);
        return rooms[random.nextInt(rooms.length)];
    }
}