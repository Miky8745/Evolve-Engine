package com.nsg.evolve.engine.render;

import com.nsg.evolve.engine.Shaders;
import com.nsg.evolve.engine.scene.Scene;

import java.util.*;

import static com.nsg.evolve.engine.Utilities.genPath;
import static org.lwjgl.opengl.GL30.*;

public class SceneRender {

    private Shaders shaderProgram;

    public SceneRender() {
        List<Shaders.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new Shaders.ShaderModuleData(genPath("shaders/scene.vert"), GL_VERTEX_SHADER));
        shaderModuleDataList.add(new Shaders.ShaderModuleData(genPath("shaders/scene.frag"), GL_FRAGMENT_SHADER));
        shaderProgram = new Shaders(shaderModuleDataList);
    }

    public void cleanup() {
        shaderProgram.cleanup();
    }

    public void render(Scene scene) {
        shaderProgram.bind();

        scene.getMeshMap().values().forEach(mesh -> {
                    glBindVertexArray(mesh.getVaoId());
                    glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
                }
        );

        glBindVertexArray(0);

        shaderProgram.unbind();
    }
}