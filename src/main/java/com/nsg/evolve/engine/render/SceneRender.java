package com.nsg.evolve.engine.render;

import com.nsg.evolve.engine.render.object.Entity;
import com.nsg.evolve.engine.render.object.Material;
import com.nsg.evolve.engine.render.object.Mesh;
import com.nsg.evolve.engine.render.object.Model;
import com.nsg.evolve.engine.render.object.texture.Texture;
import com.nsg.evolve.engine.render.object.texture.TextureCache;
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
        uniformsMap.createUniform("txtSampler");
        uniformsMap.createUniform("viewMatrix");
    }

    public void cleanup() {
        shaderProgram.cleanup();
    }

    public void render(Scene scene) {
        shaderProgram.bind();

        uniformsMap.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());
        uniformsMap.setUniform("viewMatrix", scene.getCamera().getViewMatrix());

        uniformsMap.setUniform("txtSampler", 0);

        Collection<Model> models = scene.getModelMap().values();
        TextureCache textureCache = scene.getTextureCache();
        for (Model model : models) {
            List<Entity> entities = model.getEntitiesList();

            for (Material material : model.getMaterialList()) {
                Texture texture = textureCache.getTexture(material.getTexturePath());
                glActiveTexture(GL_TEXTURE0);
                texture.bind();

                for (Mesh mesh : material.getMeshList()) {
                    glBindVertexArray(mesh.getVaoId());
                    for (Entity entity : entities) {
                        uniformsMap.setUniform("modelMatrix", entity.getModelMatrix());
                        glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
                    }
                }
            }
        }

        glBindVertexArray(0);

        shaderProgram.unbind();
    }
}