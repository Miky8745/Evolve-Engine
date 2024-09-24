package com.nsg.evolve.engine.render;

import com.nsg.evolve.engine.Window;
import com.nsg.evolve.engine.render.gui.GuiRender;
import com.nsg.evolve.engine.scene.Scene;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

public class Render {

    private SceneRender sceneRender;
    private GuiRender guiRender;

    public Render(Window window) {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        sceneRender = new SceneRender();
        guiRender = new GuiRender(window);
    }

    public void cleanup() {
        sceneRender.cleanup();
        guiRender.cleanup();
    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glViewport(0, 0, window.getWidth(), window.getHeight());
        sceneRender.render(scene);
        guiRender.render(scene);
    }

    public void resize(int width, int height) {
        guiRender.resize(width, height);
    }
}