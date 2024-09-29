package com.nsg.evolve.main;

import com.nsg.evolve.engine.Engine;
import com.nsg.evolve.engine.MouseInput;
import com.nsg.evolve.engine.Window;
import com.nsg.evolve.engine.interfaces.IAppLogic;
import com.nsg.evolve.engine.render.Render;
import com.nsg.evolve.engine.render.object.Entity;
import com.nsg.evolve.engine.render.object.Model;
import com.nsg.evolve.engine.scene.*;
import com.nsg.evolve.engine.scene.lighting.SceneLights;
import com.nsg.evolve.engine.scene.lighting.lights.AmbientLight;
import com.nsg.evolve.game.guis.LightControls;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Main implements IAppLogic {

    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.001f;

    public static void main(String[] args) {
        Main main = new Main();
        Engine gameEngine = new Engine("Evolve", new Window.WindowOptions(), main);
        try {
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        String terrainModelId = "terrain";
        Model terrainModel = ModelLoader.loadModel(terrainModelId, "resources/models/terrain/terrain.obj",
                scene.getTextureCache());

        String cubeModelId = "cube";
        Model cube = ModelLoader.loadModel(cubeModelId, "resources/models/cube/cube.obj", scene.getTextureCache());

        scene.addModel(cube);
        scene.addModel(terrainModel);

        Entity terrainEntity = new Entity("terrainEntity", terrainModelId);
        terrainEntity.setScale(10.0f);
        terrainEntity.updateModelMatrix();
        scene.addEntity(terrainEntity);

        Entity cubeEntity = new Entity("cubeEntity", cubeModelId);
        cubeEntity.setPosition(0,0.5f,1);
        cubeEntity.updateModelMatrix();
        scene.addEntity(cubeEntity);

        SceneLights sceneLights = new SceneLights();
        AmbientLight ambientLight = sceneLights.getAmbientLight();
        ambientLight.setIntensity(0.5f);
        ambientLight.setColor(0.3f, 0.3f, 0.3f);
        /*
        DirectionalLight dirLight = sceneLights.getDirLight();
        dirLight.setPosition(0, 1, 0);
        dirLight.setIntensity(1.0f);
         */
        scene.setSceneLights(sceneLights);

        SkyBox skyBox = new SkyBox("resources/models/skybox/skybox.obj", scene.getTextureCache());
        skyBox.getSkyBoxEntity().setScale(50);
        //scene.setSkyBox(skyBox);

        scene.setFog(new Fog(false, new Vector3f(0.5f, 0.5f, 0.5f), 0.5f));

        scene.getCamera().moveUp(1.8f);
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed) {
        if (inputConsumed) {
            return;
        }

        float move = diffTimeMillis * MOVEMENT_SPEED;
        Camera camera = scene.getCamera();

        //System.out.println(camera.getPosition());

        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(move);
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackwards(move);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(move);
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(move);
        }

        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            camera.moveUp(move);
        } else if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            camera.moveDown(move);
        }

        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation((float) Math.toRadians(
                            displVec.x * MOUSE_SENSITIVITY),
                    (float) Math.toRadians(displVec.y * MOUSE_SENSITIVITY));
        }
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {

    }
}