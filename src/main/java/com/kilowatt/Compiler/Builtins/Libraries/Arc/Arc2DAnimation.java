package com.kilowatt.Compiler.Builtins.Libraries.Arc;

import com.badlogic.gdx.graphics.Texture;
import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import lombok.AllArgsConstructor;
import lombok.Getter;

// анимация
@AllArgsConstructor
@Getter
public class Arc2DAnimation {
    private final WattList sprites;
    public Texture keyframe(int index) {
        return (Texture) sprites.get(index);
    }
    public Arc2DAnimationInstance play(Arc2DSprite sprite, boolean loop) {
        return new Arc2DAnimationInstance(sprite, this, loop);
    }
}
