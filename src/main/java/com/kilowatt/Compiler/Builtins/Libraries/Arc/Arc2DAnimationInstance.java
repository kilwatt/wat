package com.kilowatt.Compiler.Builtins.Libraries.Arc;

import com.badlogic.gdx.graphics.Texture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*
Интсанс анимации
 */
@RequiredArgsConstructor
@Getter
public class Arc2DAnimationInstance {
    private final Arc2DSprite sprite;
    private final Arc2DAnimation animation;
    private final boolean loop;
    private int keyframe;

    public void reset() {
        keyframe = 0;
    }

    public void step() {
        if (keyframe + 1 < animation.getSprites().size()) {
            keyframe += 1;
        } else if (loop) {
            keyframe = 0;
        }
    }

    public void back() {
        keyframe -= 1;
    }

    public Texture texture() {
        return animation.keyframe(keyframe);
    }
}
