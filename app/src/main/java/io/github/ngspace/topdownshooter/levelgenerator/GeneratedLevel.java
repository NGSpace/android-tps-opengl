package io.github.ngspace.topdownshooter.levelgenerator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.github.ngspace.topdownshooter.gameobjects.AGameObject;

public class GeneratedLevel {

    List<AGameObject> elements = new ArrayList<>();
    Queue<LevelGenerator.KeyElement> keys = new ArrayDeque<>();
    boolean hasExitDoor = false;

    public void add(AGameObject element) {
        elements.add(element);
    }
}
