package com.nsg.evolve.engine.scene;

import com.nsg.evolve.engine.render.object.Entity;
import com.nsg.evolve.engine.render.object.Model;
import com.nsg.evolve.engine.render.object.cache.TextureCache;

public class SkyBox {

    private Entity skyBoxEntity;
    private Model skyBoxModel;

    public SkyBox(String skyBoxModelPath, TextureCache textureCache) {
        skyBoxModel = ModelLoader.loadModel("skybox-model", skyBoxModelPath, textureCache, false);
        skyBoxEntity = new Entity("skyBoxEntity-entity", skyBoxModel.getId());
    }

    public Entity getSkyBoxEntity() {
        return skyBoxEntity;
    }

    public Model getSkyBoxModel() {
        return skyBoxModel;
    }
}
