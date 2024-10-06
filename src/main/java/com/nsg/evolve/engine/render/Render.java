package com.nsg.evolve.engine.render;

import com.nsg.evolve.engine.Window;
import com.nsg.evolve.engine.interfaces.IRenderer;
import com.nsg.evolve.engine.interfaces.IResizableRenderer;
import com.nsg.evolve.engine.scene.Scene;
import org.lwjgl.opengl.GL;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Render {

    private List<IRenderer> renderers;
    private ShadowRender shadowRender;

    public Render(Window window) {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        shadowRender = new ShadowRender();
        renderers = List.of(
                new SceneRender(),
                new GuiRender(window),
                new SkyBoxRender(),
                shadowRender
        );
    }

    public void cleanup() {
        renderers.forEach(IRenderer::cleanup);
    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glViewport(0, 0, window.getWidth(), window.getHeight());

        for (IRenderer renderer : renderers) {
            renderer.render(scene, shadowRender);
        }
    }

    public void resize(int width, int height) {
        for (IRenderer renderer : renderers) {
            if (renderer instanceof IResizableRenderer resize) {
                resize.resize(width, height);
            }
        }
    }
}