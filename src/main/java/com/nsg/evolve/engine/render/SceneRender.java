package com.nsg.evolve.engine.render;

import com.nsg.evolve.engine.render.shaders.Shaders;
import com.nsg.evolve.engine.render.shaders.Uniforms;
import com.nsg.evolve.engine.scene.Scene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.nsg.evolve.engine.Utilities.genPath;
import static org.lwjgl.opengl.GL30.*;

public class SceneRender {

    private Shaders shaderProgram;
    private Uniforms uniformsMap;

    public SceneRender() {
        List<Shaders.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new Shaders.ShaderModuleData(genPath("shaders/scene.vert"), GL_VERTEX_SHADER));
        shaderModuleDataList.add(new Shaders.ShaderModuleData(genPath("shaders/scene.frag"), GL_FRAGMENT_SHADER));

        shaderProgram = new Shaders(shaderModuleDataList);
        createUniforms();
    }

    private void createUniforms() {
        uniformsMap = new Uniforms(shaderProgram.getProgramId());
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("modelMatrix");
    }

    public void cleanup() {
        shaderProgram.cleanup();
    }

    public void render(Scene scene) {
        shaderProgram.bind();

        uniformsMap.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());

        Collection<Model> models = scene.getModelMap().values();
        for (Model model : models) {
            model.getMeshList().stream().forEach(mesh -> {
                glBindVertexArray(mesh.getVaoId());
                List<Entity> entities = model.getEntitiesList();
                for (Entity entity : entities) {
                    uniformsMap.setUniform("modelMatrix", entity.getModelMatrix());
                    glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
                }
            });
        }

        glBindVertexArray(0);

        shaderProgram.unbind();
    }
}