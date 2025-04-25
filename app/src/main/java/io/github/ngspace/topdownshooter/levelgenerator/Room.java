package io.github.ngspace.topdownshooter.levelgenerator;

import android.graphics.Point;

public record Room(Element[] elements, String parentRoomName, Point offset) {
}
