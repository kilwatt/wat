package com.kilowatt.Compiler.Builtins.Libraries.Arc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Спрайт
 */
@Getter
@AllArgsConstructor
public class Arc2DSprite {
    // спрайт
    private final Sprite sprite;

    // методы изменения
    public void set_x(float x) { sprite.setX(x); }
    public void set_y(float y) { sprite.setY(y); }
    public void set_scale(float x, float y) { sprite.setScale(x, y); }
    public void set_position(float x, float y) { sprite.setPosition(x, y); }
    public void set_rotation(float degrees) { sprite.setRotation(degrees); }
    public float get_rotation(float degrees) { return sprite.getRotation(); }
    public float get_x() { return sprite.getX(); }
    public float get_y() { return sprite.getY(); }
    public void set_origin(float x, float y) { sprite.setOrigin(x, y); }
    public float get_origin_x() { return sprite.getOriginX(); }
    public float get_origin_y() { return sprite.getOriginX(); }
    public float get_scale_x() { return sprite.getScaleX(); }
    public float get_scale_y() { return sprite.getScaleY(); }
    public Rectangle get_rect() { return sprite.getBoundingRectangle(); }
    public void set_texture(Texture texture) { this.sprite.setTexture(texture); }
    public Texture get_texture() { return this.sprite.getTexture(); }
}
