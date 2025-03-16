package io.github.ngspace.topdownshooter.levelgenerator;

import io.github.ngspace.topdownshooter.GeneratedGameScene;

public class LevelGenerator {
    LevelReader reader;
    public LevelGenerator(GeneratedGameScene scene) {
        reader = new LevelReader(scene);
    }
}
