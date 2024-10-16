package com.nsg.evolve.engine.render.object;

import com.nsg.evolve.engine.render.buffers.RenderBuffers;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private final String id;
    private List<Animation> animationList;
    private List<Entity> entitiesList;
    private List<MeshData> meshDataList;
    private List<MeshData> interactionsMeshDataList;
    private List<RenderBuffers.MeshDrawData> meshDrawDataList;

    public Model(String id, List<MeshData> meshDataList, List<Animation> animationList) {
        entitiesList = new ArrayList<>();
        this.id = id;
        this.meshDataList = meshDataList;
        interactionsMeshDataList = new ArrayList<>(meshDataList);
        this.animationList = animationList;
        meshDrawDataList = new ArrayList<>();
    }

    public List<Animation> getAnimationList() {
        return animationList;
    }

    public List<Entity> getEntitiesList() {
        return entitiesList;
    }

    public String getId() {
        return id;
    }

    public List<MeshData> getMeshDataList() {
        return meshDataList;
    }

    public List<RenderBuffers.MeshDrawData> getMeshDrawDataList() {
        return meshDrawDataList;
    }

    public boolean isAnimated() {
        return animationList != null && !animationList.isEmpty();
    }

    public List<MeshData> getInteractionsMeshDataList() {
        return interactionsMeshDataList;
    }

    public record Animation(String name, double duration, List<AnimatedFrame> frames) {
    }

    public static class AnimatedFrame {
        private Matrix4f[] bonesMatrices;
        private int offset;

        public AnimatedFrame(Matrix4f[] bonesMatrices) {
            this.bonesMatrices = bonesMatrices;
        }

        public void clearData() {
            bonesMatrices = null;
        }

        public Matrix4f[] getBonesMatrices() {
            return bonesMatrices;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }
}