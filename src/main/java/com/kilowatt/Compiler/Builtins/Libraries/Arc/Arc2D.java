package com.kilowatt.Compiler.Builtins.Libraries.Arc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kilowatt.Compiler.Builtins.Libraries.Std.Fs.FsPath;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Entities.VmFunction;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.VmAddress;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
    // рендерер для отрисовки фигур
    private ShapeRenderer shapeRenderer;
    // заливать ли фигуры
    private boolean shapesFilled = false;
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
    // при столкновении
    private final List<Arc2DCollision> onCollision = new ArrayList<>();
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

    public void on_collision(Arc2DSprite first, Arc2DSprite second, VmFunction onCollision) {
        this.onCollision.add(new Arc2DCollision(first, second, onCollision));
    }

    // создание спрайта по пути
    public Arc2DSprite sprite(FsPath path) {
        return new Arc2DSprite(
            new Sprite(new Texture(Gdx.files.internal(path.toString())))
        );
    }

    // создание шрифта по пути
    public BitmapFont font(FsPath path, int size) {
        // генератор
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path.toString()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // генерация
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();

        // возвращаем шрифт
        return font;
    }

    // создание текста
    public Arc2DText text(BitmapFont font) {
        // возвращаем шрифт
        return new Arc2DText(
                font
        );
    }

    // создание звука
    public Arc2DSound sound(FsPath path) {
        return new Arc2DSound(Gdx.audio.newSound(Gdx.files.internal(path.toString())));
    }

    // отрисовка спрайта
    public void draw_sprite(Arc2DSprite sprite) {
        sprite.getSprite().draw(batch);
    }

    // отрисовка спрайта
    public void draw_text(Arc2DText text) {
        text.getFont().draw(batch, text.getValue(), text.getX(), text.getY());
    }

    // отрисовка линии
    public void draw_line(float from_x, float from_y, float to_x,
                          float to_y, VmInstance color) {
        // адрес
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        // цвет
        float r = ((Number) color.getFields().lookup(address, "r")).floatValue();
        float g = ((Number) color.getFields().lookup(address, "g")).floatValue();
        float b = ((Number) color.getFields().lookup(address, "b")).floatValue();
        float a = ((Number) color.getFields().lookup(address, "a")).floatValue();
        // установка цвета
        shapeRenderer.setColor(r, g, b, a);
        // отрисовка
        shapeRenderer.line(from_x, from_y, to_x, to_y);
    }

    // отрисовка прямоугольника
    public void draw_rectangle(float x, float y, float width,
                               float height, VmInstance color) {
        // адрес
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        // цвет
        float r = ((Number) color.getFields().lookup(address, "r")).floatValue();
        float g = ((Number) color.getFields().lookup(address, "g")).floatValue();
        float b = ((Number) color.getFields().lookup(address, "b")).floatValue();
        float a = ((Number) color.getFields().lookup(address, "a")).floatValue();
        // установка цвета
        shapeRenderer.setColor(r, g, b, a);
        // отрисовка
        shapeRenderer.rect(x, y, width, height);
    }

    // отрисовка дуги
    public void draw_arc(float x, float y, float radius,
                         float start, float degree, VmInstance color) {
        // адрес
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        // цвет
        float r = ((Number) color.getFields().lookup(address, "r")).floatValue();
        float g = ((Number) color.getFields().lookup(address, "g")).floatValue();
        float b = ((Number) color.getFields().lookup(address, "b")).floatValue();
        float a = ((Number) color.getFields().lookup(address, "a")).floatValue();
        // установка цвета
        shapeRenderer.setColor(r, g, b, a);
        // отрисовка
        shapeRenderer.arc(x, y, radius, start, degree);
    }

    // отрисовка круга
    public void draw_circle(float x, float y, float radius, VmInstance color) {
        // адрес
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        // цвет
        float r = ((Number) color.getFields().lookup(address, "r")).floatValue();
        float g = ((Number) color.getFields().lookup(address, "g")).floatValue();
        float b = ((Number) color.getFields().lookup(address, "b")).floatValue();
        float a = ((Number) color.getFields().lookup(address, "a")).floatValue();
        // установка цвета
        shapeRenderer.setColor(r, g, b, a);
        // отрисовка
        shapeRenderer.circle(x, y, radius);
    }

    // отрисовка треугольника
    public void draw_triangle(float x, float y, float x_2, float y_2,
                              float x_3, float y_3, VmInstance color) {
        // адрес
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        // цвет
        float r = ((Number) color.getFields().lookup(address, "r")).floatValue();
        float g = ((Number) color.getFields().lookup(address, "g")).floatValue();
        float b = ((Number) color.getFields().lookup(address, "b")).floatValue();
        float a = ((Number) color.getFields().lookup(address, "a")).floatValue();
        // установка цвета
        shapeRenderer.setColor(r, g, b, a);
        // отрисовка
        shapeRenderer.triangle(x, y, x_2, y_2, x_3, y_3);
    }

    // установка заливки фигур
    public void set_filled(boolean filled) {
        this.shapesFilled = filled;
    }

    // очистка экрана
    public void clear() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    // получение дельта тайма
    public float get_delta_time() {
        return Gdx.graphics.getDeltaTime();
    }

    // запуск
    public void run() {
        new Lwjgl3Application(this, config);
    }

    @Override
    public void create() {
         batch = new SpriteBatch();
         shapeRenderer = new ShapeRenderer();
         Gdx.input.setInputProcessor(input);
         if (onStart != null) onStart.exec(WattCompiler.vm, false);
    }

    @Override
    public void resize(int width, int height) {
        if (onResize != null) onResize.exec(WattCompiler.vm, false);
    }

    @Override
    public void render() {
        // очистка
        clear();
        // рендер
        batch.begin();
        shapeRenderer.begin(shapesFilled ? ShapeRenderer.ShapeType.Filled : ShapeRenderer.ShapeType.Line);
        if (onUpdate != null) onUpdate.exec(WattCompiler.vm, false);
        batch.end();
        shapeRenderer.end();
        // коллизии
        for (Arc2DCollision handler : onCollision) {
            // ректы
            var firstRect = handler.getFirst()
                .getSprite().getBoundingRectangle();
            var secondRect = handler.getSecond()
                .getSprite().getBoundingRectangle();
            // проверка коллизии
            if (firstRect.overlaps(secondRect)) {
                WattCompiler.vm.push(handler.getFirst());
                WattCompiler.vm.push(handler.getSecond());
                handler.getFn().exec(WattCompiler.vm, false);
            }
        }
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
