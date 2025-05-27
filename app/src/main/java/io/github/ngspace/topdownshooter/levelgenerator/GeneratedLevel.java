package io.github.ngspace.topdownshooter.levelgenerator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.github.ngspace.topdownshooter.gameobjects.AGameObject;
import io.github.ngspace.topdownshooter.gameobjects.Sprite;

public class GeneratedLevel {

    public int fuelCount = 0;
    List<GeneratedElement> elements = new ArrayList<>();
    List<GeneratedRoom> rooms = new ArrayList<>();

    public void add(AGameObject element) {
        add(element, true);
    }
    public void add(AGameObject element, boolean physics) {
        elements.add(new GeneratedElement(element, physics));
    }
    public void add(GeneratedElement element) {elements.add(element);}
}
