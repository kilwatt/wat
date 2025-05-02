package com.kilowatt.Compiler.Builtins.Libraries.Arc;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/*
шрифт
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Arc2DText {
    // данные
    private final BitmapFont font;
    private int x = 0;
    private int y = 0;
    private String value;
}
