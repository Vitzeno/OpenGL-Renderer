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

		RawModel grassModel = OBJLoader.loadOBJModel("grass", loader);
		TexturedModel grass = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("green")));
		ModelTexture grassTexture = grass.getTexture(); 
		grassTexture.setUseFakeLighting(true);
		grassTexture.setShineDampner(10);
		grassTexture.setReflectivity(1);
		
		RawModel treeModel = OBJLoader.loadOBJModel("lowPolyTree", loader);
		TexturedModel tree = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("lowPolyTree")));
		//ModelTexture treeTexture = tree.getTexture(); 
		//treeTexture.setUseFakeLighting(true);
		//treeTexture.setShineDampner(10);
		//treeTexture.setReflectivity(1);
		
		RawModel fernModel = OBJLoader.loadOBJModel("fern", loader);
		TexturedModel fern = new TexturedModel(fernModel, new ModelTexture(loader.loadTexture("fern")));
		ModelTexture fernTexture = fern.getTexture(); 
		fernTexture.setHasTransparency(true);
		fernTexture.setUseFakeLighting(true);
		fernTexture.setShineDampner(10);
		fernTexture.setReflectivity(1);
		
		
		List<Entity> entities = new ArrayList<Entity>();
		Random rand = new Random();
		
		for(int i = 0;i < 5000;i++) {
			//float x = rand.nextFloat() * 800;
			//float z = rand.nextFloat() * -800;
			
			entities.add(new Entity(grass, new Vector3f(rand.nextFloat() * 1600, 0, rand.nextFloat() * -800), rand.nextFloat(), rand.nextFloat(), 0, 5));
		}
		
		for(int i = 0;i < 500;i++) {
			//float x = rand.nextFloat() * 800;
			//float z = rand.nextFloat() * -800;
			
			entities.add(new Entity(tree, new Vector3f(rand.nextFloat() * 1600, 0, rand.nextFloat() * -800), rand.nextFloat(), rand.nextFloat(), 0, 0.5f));
		}
		
		for(int i = 0;i < 10000;i++) {
			//float x = rand.nextFloat() * 800;
			//float z = rand.nextFloat() * -800;
			
			entities.add(new Entity(fern, new Vector3f(rand.nextFloat() * 1600, 0, rand.nextFloat() * -800), rand.nextFloat(), rand.nextFloat(), 0, 0.5f));
		}
		
		
		List<Terrain> terrains = new ArrayList<Terrain>();
			
		//terrains.add(new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")))); 	//Top Left
		terrains.add(new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass"))));	//Top Right
		//terrains.add(new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass"))));		//Bottom Right
		//terrains.add(new Terrain(-1, 0, loader, new ModelTexture(loader.loadTexture("grass"))));	//Bottom Left
		
		
		Light light = new Light(new Vector3f(400, 1000, -400), new Vector3f(1, 1, 1));
		
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
