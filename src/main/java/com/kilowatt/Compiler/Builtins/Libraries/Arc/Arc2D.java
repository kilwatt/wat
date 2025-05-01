package com.kilowatt.Compiler.Builtins.Libraries.Arc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Entities.VmFunction;
import lombok.Getter;

import java.nio.file.Path;

/*
Библиотека для 2Д графики
 */
@Getter
public class Arc2D implements ApplicationListener {
    // размеры окна
    private final int width;
    private final int height;
    // заголовок
    private final String title;
    // конфигурация
    private final Lwjgl3ApplicationConfiguration config;
    // батч для отрисовки спрайтов
    private SpriteBatch batch;
    // при старте
    private VmFunction onStart;
    // при апдейте
    private VmFunction onUpdate;
    // при паузе
    private VmFunction onPause;
    // при выходе из паузы
    private VmFunction onResume;
    // при ресайзе окна
    private VmFunction onResize;
    // при уничтожении
    private VmFunction onDispose;
    // процессор инпута
    private final Arc2DInput input = new Arc2DInput();

    // конструктор
    public Arc2D(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.config = new Lwjgl3ApplicationConfiguration();
        config.setTitle(title);
        config.setWindowedMode(width, height);
    }

    // установка функций
    public void on_update(VmFunction onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void on_start(VmFunction onStart) {
        this.onStart = onStart;
    }

    public void on_pause(VmFunction onPause) {
        this.onPause = onPause;
    }

    public void on_resume(VmFunction onResume) {
        this.onResume = onResume;
    }

    public void on_resize(VmFunction onResize) {
        this.onResize = onResize;
    }

    public void on_dispose(VmFunction onDispose) {
        this.onDispose = onDispose;
    }

    // создание спрайта по пути
    public Arc2DSprite sprite(Path path) {
        return new Arc2DSprite(
            new Sprite(new Texture(Gdx.files.internal(path.toString())))
        );
    }

    // создание шрифта по пути
    public Arc2DText freetype(Path path, int size) {
        // генератор
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path.toString()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // генерация
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();

        // возвращаем шрифт
        return new Arc2DText(
            font
        );
    }

    // отрисовка спрайта
    public void draw_sprite(Arc2DSprite sprite) {
        sprite.getSprite().draw(batch);
    }

    // отрисовка спрайта
    public void draw_font(Arc2DText text) {
        text.getFont().draw(batch, text.getValue(), text.getX(), text.getY());
    }

    // запуск
    public void run() {
        new Lwjgl3Application(this, config);
    }

    @Override
    public void create() {
         batch = new SpriteBatch();
         Gdx.input.setInputProcessor(input);
         if (onStart != null) onStart.exec(WattCompiler.vm, false);
    }

    @Override
    public void resize(int width, int height) {
        if (onResize != null) onResize.exec(WattCompiler.vm, false);
    }

    @Override
    public void render() {
        batch.begin();
        if (onUpdate != null) onUpdate.exec(WattCompiler.vm, false);
        batch.end();
    }

    @Override
    public void pause() {
        if (onPause != null) onPause.exec(WattCompiler.vm, false);
    }

    @Override
    public void resume() {
        if (onResume != null) onResume.exec(WattCompiler.vm, false);
    }

    @Override
    public void dispose() {
        if (onDispose != null) onDispose.exec(WattCompiler.vm, false);
    }
}
