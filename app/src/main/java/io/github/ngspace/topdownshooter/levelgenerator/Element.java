package io.github.ngspace.topdownshooter.levelgenerator;

import io.github.ngspace.topdownshooter.utils.Bounds;

public record Element(Bounds bounds, String type, boolean collision) {

}
