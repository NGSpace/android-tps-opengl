package io.github.ngspace.topdownshooter.levelgenerator;

import android.graphics.Point;

import io.github.ngspace.topdownshooter.utils.Bounds;

public record Room(Element[] elements, String parentRoomName, Point offset, Bounds bounds) {
}
