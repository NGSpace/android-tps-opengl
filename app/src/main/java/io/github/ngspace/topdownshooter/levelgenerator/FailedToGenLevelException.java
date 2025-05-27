package io.github.ngspace.topdownshooter.levelgenerator;

public class FailedToGenLevelException extends Exception {
    public FailedToGenLevelException(String intersectingElements) {
        super(intersectingElements);
    }
}
