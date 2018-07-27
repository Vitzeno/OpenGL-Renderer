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
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();

		RawModel model = OBJLoader.loadOBJModel("Tree", loader);
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree")));
		
		ModelTexture texture = staticModel.getTexture(); 
		texture.setShineDampner(10);
		texture.setReflectivity(1);
		
		//Entity entity = new Entity(staticModel, new Vector3f(0, 0, -25), 0, 0, 0, 1);
		
		List<Entity> entities = new ArrayList<Entity>();
		Random rand = new Random();
		
		for(int i = 0;i < 100;i++) {
			float x = rand.nextFloat() * 100 -5;
			float z = rand.nextFloat() * -300;
			
			entities.add(new Entity(staticModel, new Vector3f(x, 0, z), 0, 0, 0, 5));
		}
		
		Light light = new Light(new Vector3f(0, 50, -50), new Vector3f(1, 1, 1));
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		for(int i = 0;i > -5;i--) {
			terrains.add(new Terrain(i, -1, loader, new ModelTexture(loader.loadTexture("grass01"))));
		}
		
		Camera camera = new Camera();
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			//Game logic
			//entity.setRotY(180);
			//entity.increaseRotation(0, 1, 0);
			//entity.increasePosition(0, 0, -0.1f);
			
			camera.move();
			
			for(Terrain terrain : terrains){
				renderer.processTerrain(terrain);
			}
			
			for(Entity entity : entities) {
				renderer.processEntity(entity);
				//entity.increaseRotation(0, 1, 0);
			}

			renderer.renderer(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
