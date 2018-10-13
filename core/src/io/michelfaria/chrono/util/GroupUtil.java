package io.michelfaria.chrono.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.function.Function;

public class GroupUtil {

    public static float getWidth(Group group) {
        return getGroupLength(group, Actor::getX, Actor::getWidth);
    }

    public static float getHeight(Group group) {
        return getGroupLength(group, Actor::getY, Actor::getHeight);
    }

    private static float getGroupLength(Group group, Function<Actor, Float> getActorAxisValue, Function<Actor, Float> getActorLength) {
        // AV = Axis Value
        SnapshotArray<Actor> children = group.getChildren();
        float minAV = 0.0f;
        float maxAV = 0.0f;

        for (Actor child : children) {
            float childMinAV = getActorAxisValue.apply(child);
            float childMaxAV;
            if (child instanceof Group) {
                childMaxAV = childMinAV + getGroupLength(group, getActorAxisValue, getActorLength);
            } else {
                childMaxAV = childMinAV + getActorLength.apply(child);
            }

            if (childMinAV < minAV) {
                minAV = childMinAV;
            }
            if (childMaxAV > maxAV) {
                maxAV = childMaxAV;
            }
        }

        return Math.abs(minAV) + Math.abs(maxAV);
    }
}