package com.nsg.evolve.engine.render;

import com.nsg.evolve.engine.interfaces.IRenderer;
import com.nsg.evolve.engine.render.object.Entity;
import com.nsg.evolve.engine.render.object.Material;
import com.nsg.evolve.engine.render.object.Mesh;
import com.nsg.evolve.engine.render.object.Model;
import com.nsg.evolve.engine.render.shaders.Shaders;
import com.nsg.evolve.engine.render.shaders.Uniforms;
import com.nsg.evolve.engine.render.shadows.CascadeShadow;
import com.nsg.evolve.engine.render.shadows.ShadowBuffer;
import com.nsg.evolve.engine.scene.Scene;
import com.nsg.evolve.engine.scene.animations.AnimationData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.nsg.evolve.engine.Utilities.genPath;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.*;

public class ShadowRender implements IRenderer {

    private ArrayList<CascadeShadow> cascadeShadows;
    private Shaders shaderProgram;
    private ShadowBuffer shadowBuffer;
    private Uniforms uniformsMap;

    public ShadowRender() {
        List<Shaders.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new Shaders.ShaderModuleData(genPath("shaders/shadow/shadow.vert"), GL_VERTEX_SHADER));
        shaderProgram = new Shaders(shaderModuleDataList);

        shadowBuffer = new ShadowBuffer();

        cascadeShadows = new ArrayList<>();
        for (int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++) {
            CascadeShadow cascadeShadow = new CascadeShadow();
            cascadeShadows.add(cascadeShadow);
        }

        createUniforms();
    }

    @Override
    public void cleanup() {
        shaderProgram.cleanup();
        shadowBuffer.cleanup();
    }

    private void createUniforms() {
        uniformsMap = new Uniforms(shaderProgram.getProgramId());
        uniformsMap.createUniform("modelMatrix");
        uniformsMap.createUniform("projViewMatrix");
        uniformsMap.createUniform("bonesMatrices");
    }

    public List<CascadeShadow> getCascadeShadows() {
        return cascadeShadows;
    }

    public ShadowBuffer getShadowBuffer() {
        return shadowBuffer;
    }

    @Override
    public void render(Scene scene, ShadowRender ignored) {
        CascadeShadow.updateCascadeShadows(cascadeShadows, scene);

        glBindFramebuffer(GL_FRAMEBUFFER, shadowBuffer.getDepthMapFBO());
        glViewport(0, 0, ShadowBuffer.SHADOW_MAP_WIDTH, ShadowBuffer.SHADOW_MAP_HEIGHT);

        shaderProgram.bind();

        Collection<Model> models = scene.getModelMap().values();
        for (int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++) {
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, shadowBuffer.getDepthMapTexture().getIds()[i], 0);
            glClear(GL_DEPTH_BUFFER_BIT);

            CascadeShadow shadowCascade = cascadeShadows.get(i);
            uniformsMap.setUniform("projViewMatrix", shadowCascade.getProjViewMatrix());

            for (Model model : models) {
                List<Entity> entities = model.getEntitiesList();
                for (Material material : model.getMaterialList()) {
                    for (Mesh mesh : material.getMeshList()) {
                        glBindVertexArray(mesh.getVaoId());
                        for (Entity entity : entities) {
                            uniformsMap.setUniform("modelMatrix", entity.getModelMatrix());
                            AnimationData animationData = entity.getAnimationData();
                            if (animationData == null) {
                                uniformsMap.setUniform("bonesMatrices", AnimationData.DEFAULT_BONES_MATRICES);
                            } else {
                                uniformsMap.setUniform("bonesMatrices", animationData.getCurrentFrame().boneMatrices());
                            }
                            glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
                        }
                    }
                }
            }
        }

        shaderProgram.unbind();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}
