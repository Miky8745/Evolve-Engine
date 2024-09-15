package com.nsg.evolve.main;

import com.nsg.evolve.engine.Engine;
import com.nsg.evolve.engine.IAppLogic;
import com.nsg.evolve.engine.Window;
import com.nsg.evolve.engine.graph.Render;
import com.nsg.evolve.engine.scene.Scene;

public class Main implements IAppLogic {

    public static void main(String[] args) {
        Main main = new Main();
        Engine gameEng = new Engine("Evolve", new Window.WindowOptions(), main);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        // Nothing to be done yet
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {
        // Nothing to be done yet
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        // Nothing to be done yet
    }
}