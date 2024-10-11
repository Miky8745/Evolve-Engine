package com.nsg.evolve.engine.render;

import com.nsg.evolve.engine.Window;
import com.nsg.evolve.engine.scene.Scene;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

public class Render {

    private GuiRender guiRender;
    private SceneRender sceneRender;
    private ShadowRender shadowRender;
    private SkyBoxRender skyBoxRender;

    public Render(Window window) {
        GL.createCapabilities();
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_DEPTH_TEST);

        // Support for transparencies
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        sceneRender = new SceneRender();
        guiRender = new GuiRender(window);
        skyBoxRender = new SkyBoxRender();
        shadowRender = new ShadowRender();
    }

    public void cleanup() {
        sceneRender.cleanup();
        guiRender.cleanup();
        skyBoxRender.cleanup();
        shadowRender.cleanup();
    }

    public void render(Window window, Scene scene) {
        shadowRender.render(scene, shadowRender);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, window.getWidth(), window.getHeight());
        skyBoxRender.render(scene, shadowRender);
        sceneRender.render(scene, shadowRender);
        guiRender.render(scene, shadowRender);
    }

    public void resize(int width, int height) {
        guiRender.resize(width, height);
    }
}