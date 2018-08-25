package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
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
		
		
		data = OBJFileLoader.loadOBJ("person");
		RawModel playerModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		TexturedModel player = new TexturedModel(playerModel, new ModelTexture(loader.loadTexture("playerTexture")));
		
		Player playerEntity = new Player(player, new Vector3f(400, 1, -450), 0f, 0f, 0f, 0.5f);
		
		Camera camera = new Camera(playerEntity);
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturesPack texturePack = new TerrainTexturesPack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//terrains.add(new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")))); 	//Top Left
		terrains.add(new Terrain(0, -1, loader, texturePack, blendMap, "heightmap"));	//Top Right
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
			float x = rand.nextFloat() * 1600 - 400;
			float z = rand.nextFloat() * -600;
			
			float y = terrains.get(0).getHeightOfTerrain(x, z);
			
			entities.add(new Entity(grass, new Vector3f(x, y, z), 0, rand.nextInt(), 0, 1));
			//entities.add(new Entity(grass, new Vector3f(rand.nextFloat() * 1600, 0, rand.nextFloat() * -800), 0, rand.nextInt(), 0, 1));
		}
		
		for(int i = 0;i < 500;i++) {
			float x = rand.nextFloat() * 1600 - 400;
			float z = rand.nextFloat() * -600;
			
			float y = terrains.get(0).getHeightOfTerrain(x, z);
			
			entities.add(new Entity(tree, new Vector3f(x, y, z), 0, rand.nextInt(), 0, 2));
			//entities.add(new Entity(tree, new Vector3f(rand.nextFloat() * 1600, 0, rand.nextFloat() * -800), 0, rand.nextInt(), 0, 2));
		}
		
		for(int i = 0;i < 10000;i++) {
			float x = rand.nextFloat() * 1600 - 400;
			float z = rand.nextFloat() * -600;
			
			float y = terrains.get(0).getHeightOfTerrain(x, z);
			
			entities.add(new Entity(fern, new Vector3f(x, y, z), 0, rand.nextInt(), 0, 0.5f));
			//entities.add(new Entity(fern, new Vector3f(rand.nextFloat() * 1600, 0, rand.nextFloat() * -800), 0, rand.nextInt(), 0, 0.5f));
		}
		
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			//Game logic	
			camera.move();
			
			/**
			 * When multiple terrains exist, this will not sufficient,
			 * will need to test which terrain player is on then pass that
			 * to move method e.g:
			 * 
			 * if(player.getPosition().x > 800 || player.getPosition().z > 800){
    				player.move(terrain2);
				}else{
 					player.move(terrain);
				}
				
			 */

			playerEntity.move(terrains.get(0));
			
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
