package com.nsg.evolve.engine.interfaces;

import com.nsg.evolve.engine.render.ShadowRender;
import com.nsg.evolve.engine.scene.Scene;

public interface IRenderer {
    void render(Scene scene, ShadowRender render);

    void cleanup();
}
