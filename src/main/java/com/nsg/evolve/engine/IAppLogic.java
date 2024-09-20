package com.nsg.evolve.engine;

import com.nsg.evolve.engine.render.Render;
import com.nsg.evolve.engine.scene.Scene;

public interface IAppLogic {

    void cleanup();

    void init(Window window, Scene scene, Render render);

    void input(Window window, Scene scene, long diffTimeMillis);

    void update(Window window, Scene scene, long diffTimeMillis);
}