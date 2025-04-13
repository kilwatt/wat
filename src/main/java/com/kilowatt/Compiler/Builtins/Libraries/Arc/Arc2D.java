package com.kilowatt.Compiler.Builtins.Libraries.Arc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.VmAddress;
import lombok.Getter;

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
    private SpriteBatch sprites;
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
    public Sprite sprite(String path) {
        return new Sprite(new Texture(Gdx.files.internal(path)));
    }

    // отрисовка спрайта
    public void draw(Sprite sprite) {
        sprite.draw(sprites);
    }

    // запуск
    public void run(VmFunction onStart,
                    VmFunction onUpdate) {
        // проверяем
        if (onStart == null ||
            onUpdate == null) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        }
        // создаём приложение
        this.onStart = onStart;
        this.onUpdate = onUpdate;
        new Lwjgl3Application(this, config);
    }

    @Override
    public void create() {
         sprites = new SpriteBatch();
         onStart.exec(WattCompiler.vm, false);
    }

    @Override
    public void resize(int width, int height) {
        onResize.exec(WattCompiler.vm, false);
    }

    @Override
    public void render() {
        sprites.begin();
        onUpdate.exec(WattCompiler.vm, false);
        sprites.end();
    }

    @Override
    public void pause() {
        onPause.exec(WattCompiler.vm, false);
    }

    @Override
    public void resume() {
        onResume.exec(WattCompiler.vm, false);
    }

    @Override
    public void dispose() {
        onDispose.exec(WattCompiler.vm, false);
    }
}
