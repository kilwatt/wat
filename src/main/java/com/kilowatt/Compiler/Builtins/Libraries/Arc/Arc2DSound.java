package com.kilowatt.Compiler.Builtins.Libraries.Arc;

import com.badlogic.gdx.audio.Sound;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
Звук
 */
@Getter
@Setter
@AllArgsConstructor
public class Arc2DSound {
    private final Sound sound;
    public long play(float volume) { return sound.play(volume); }
    public long loop(float volume) { return sound.loop(volume); }
    public void stop(long id) { sound.stop(id); }
    public void pause(long id) { sound.pause(id); }
    public void resume(long id) { sound.resume(id); }
}
