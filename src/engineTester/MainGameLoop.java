package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();

		RawModel model = OBJLoader.loadOBJModel("dragon", loader);
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("grass01")));
		
		ModelTexture texture = staticModel.getTexture(); 
		texture.setShineDampner(100);
		texture.setReflectivity(100);
		
		//Entity entity = new Entity(staticModel, new Vector3f(0, 0, -25), 0, 0, 0, 1);
		
		List<Entity> entities = new ArrayList<Entity>();
		Random rand = new Random();
		
		for(int i = 0;i < 100;i++) {
			float x = rand.nextFloat() * 100 -5;
			float y = rand.nextFloat() * 100 -5;
			float z = rand.nextFloat() * -300;
			
			entities.add(new Entity(staticModel, new Vector3f(x, y, z), rand.nextFloat() * 180f, rand.nextFloat() * 180f, 0f, 1f));
		}
		
		Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));
		
		Camera camera = new Camera();
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			//Game logic
			//entity.setRotY(180);
			//entity.increaseRotation(0, 1, 0);
			//entity.increasePosition(0, 0, -0.1f);
			
			camera.move();
			
			for(Entity entity : entities) {
				renderer.processEntity(entity);
				entity.increaseRotation(0, 1, 0);
			}

			renderer.renderer(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
