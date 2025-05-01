package com.kilowatt.Compiler.Builtins.Libraries.Arc;

import com.badlogic.gdx.InputProcessor;
import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.WattVM.Entities.VmFunction;
import lombok.Getter;
import lombok.Setter;

/*
Инпут
 */
@Getter
public class Arc2DInput implements InputProcessor {
    // хэндлеры
    private VmFunction onKeyDown;
    private VmFunction onKeyUp;
    private VmFunction onTyped;
    private VmFunction onMouseDown;
    private VmFunction onMouseUp;
    private VmFunction onMouseDragged;
    private VmFunction onScroll;
    private VmFunction onMouseMoved;

    /*
    Установка обработчиков
     */
    public void on_key_down(VmFunction fn) {
        onKeyDown = fn;
    }
    public void on_key_up(VmFunction fn) {
        onKeyUp = fn;
    }
    public void on_typed(VmFunction fn) {
        onTyped = fn;
    }
    public void on_mouse_down(VmFunction fn) {
        onMouseDown = fn;
    }
    public void on_mouse_up(VmFunction fn) {
        onMouseUp = fn;
    }
    public void on_mouse_dragged(VmFunction fn) {
        onMouseDragged = fn;
    }
    public void on_scroll(VmFunction fn) {
        onScroll = fn;
    }
    public void on_mouse_moved(VmFunction fn) {
        onMouseMoved = fn;
    }

    /*
    Обработка
     */

    @Override
    public boolean keyDown(int key) {
        if (onKeyDown == null) return false;
        WattCompiler.vm.push(key);
        onKeyDown.exec(WattCompiler.vm, false);
        return false;
    }

    @Override
    public boolean keyUp(int key) {
        if (onKeyUp == null) return false;
        WattCompiler.vm.push(key);
        onKeyUp.exec(WattCompiler.vm, false);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (onTyped == null) return false;
        WattCompiler.vm.push(character);
        onTyped.exec(WattCompiler.vm, false);
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (onMouseDown == null) return false;
        WattCompiler.vm.push(x);
        WattCompiler.vm.push(y);
        WattCompiler.vm.push(pointer);
        WattCompiler.vm.push(button);
        onMouseDown.exec(WattCompiler.vm, false);
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (onMouseUp == null) return false;
        WattCompiler.vm.push(x);
        WattCompiler.vm.push(y);
        WattCompiler.vm.push(pointer);
        WattCompiler.vm.push(button);
        onMouseUp.exec(WattCompiler.vm, false);
        return false;
    }

    @Override
    public boolean touchCancelled(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        if (onMouseDragged == null) return false;
        onMouseDragged.exec(WattCompiler.vm, false);
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        if (onMouseMoved == null) return false;
        WattCompiler.vm.push(x);
        WattCompiler.vm.push(y);
        onMouseMoved.exec(WattCompiler.vm, false);
        return false;
    }

    @Override
    public boolean scrolled(float xAmount, float yAmount) {
        if (onScroll == null) return false;
        WattCompiler.vm.push(xAmount);
        WattCompiler.vm.push(yAmount);
        onScroll.exec(WattCompiler.vm, false);
        return false;
    }
}
