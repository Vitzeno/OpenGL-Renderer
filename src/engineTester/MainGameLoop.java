package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturesPack;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		ModelData data;
		
		Loader loader = new Loader();
		
		Light light = new Light(new Vector3f(400, 1000, -400), new Vector3f(1, 1, 1));
		Camera camera = new Camera();
		
		data = OBJFileLoader.loadOBJ("person");
		RawModel playerModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		TexturedModel player = new TexturedModel(playerModel, new ModelTexture(loader.loadTexture("playerTexture")));
		
		Player playerEntity = new Player(player, new Vector3f(400, 1, -450), 0f, 0f, 0f, 0.5f);
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturesPack texturePack = new TerrainTexturesPack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//terrains.add(new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")))); 	//Top Left
		terrains.add(new Terrain(0, -1, loader, texturePack, blendMap));	//Top Right
		//terrains.add(new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass"))));		//Bottom Right
		//terrains.add(new Terrain(-1, 0, loader, new ModelTexture(loader.loadTexture("grass"))));	//Bottom Left
	
		data = OBJFileLoader.loadOBJ("grassModel");
		RawModel grassModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		TexturedModel grass = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("flower")));
		ModelTexture grassTexture = grass.getTexture(); 
		grassTexture.setUseFakeLighting(true);
		grassTexture.setShineDampner(10);
		grassTexture.setReflectivity(1);
		
		data = OBJFileLoader.loadOBJ("lowPolyTree");
		RawModel treeModel =loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		TexturedModel tree = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("lowPolyTree")));
		//ModelTexture treeTexture = tree.getTexture(); 
		//treeTexture.setUseFakeLighting(true);
		//treeTexture.setShineDampner(10);
		//treeTexture.setReflectivity(1);
		
		data = OBJFileLoader.loadOBJ("fern");
		RawModel fernModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		TexturedModel fern = new TexturedModel(fernModel, new ModelTexture(loader.loadTexture("fern")));
		ModelTexture fernTexture = fern.getTexture(); 
		fernTexture.setHasTransparency(true);
		fernTexture.setUseFakeLighting(true);
		fernTexture.setShineDampner(10);
		fernTexture.setReflectivity(1);
		
		
		List<Entity> entities = new ArrayList<Entity>();
		Random rand = new Random();
		
		for(int i = 0;i < 10000;i++) {
			//float x = rand.nextFloat() * 800;
			//float z = rand.nextFloat() * -800;
			
			entities.add(new Entity(grass, new Vector3f(rand.nextFloat() * 1600, 0, rand.nextFloat() * -800), 0, rand.nextInt(), 0, 1));
		}
		
		for(int i = 0;i < 2000;i++) {
			//float x = rand.nextFloat() * 800;
			//float z = rand.nextFloat() * -800;
			
			entities.add(new Entity(tree, new Vector3f(rand.nextFloat() * 1600, 0, rand.nextFloat() * -800), 0, rand.nextInt(), 0, 2));
		}
		
		for(int i = 0;i < 10000;i++) {
			//float x = rand.nextFloat() * 800;
			//float z = rand.nextFloat() * -800;
			
			entities.add(new Entity(fern, new Vector3f(rand.nextFloat() * 1600, 0, rand.nextFloat() * -800), 0, rand.nextInt(), 0, 0.5f));
		}
		
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			//Game logic
			//entity.setRotY(180);
			//entity.increaseRotation(0, 1, 0);
			//entity.increasePosition(0, 0, -0.1f);
			
			camera.move();
			playerEntity.move();
			
			renderer.processEntity(playerEntity);
			
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
