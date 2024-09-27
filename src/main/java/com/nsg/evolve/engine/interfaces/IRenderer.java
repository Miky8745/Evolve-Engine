package com.nsg.evolve.engine.interfaces;

import com.nsg.evolve.engine.scene.Scene;

public interface IRenderer {
    void render(Scene scene);

    void cleanup();
}
