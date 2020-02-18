package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

/**
 * This class is used to optimise rendering.
 * It allows multiple entities to be rendered without having to call the 
 * main render method multiple times a second.
 *  Also creates projection matrix therefore handles FOV, NEAR_PLANE and FAR_PLANE values.
 * @author Mohamed
 *
 */
public class MasterRenderer {	
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	//Used for sky colours
	private static final float RED = 0.624f;
	private static final float GREEN = 0.847f;
	private static final float BLUE = 0.953f;
	
	
	private Matrix4f projectionMatrix;

	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer; 
	
	
	//Maps textured models to entities meaning each model can have many entities, this will help drastically improve rendering
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	/**
	 * Required so that projection matrix can be created and passed to all other renderers
	 */
	public MasterRenderer() {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}
	
	/**
	 * Enables backface culling if model texture does not have any transparency
	 */
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);	//Enable culling of triangle
		GL11.glCullFace(GL11.GL_BACK);		//Culls triangle facing away from camera
	}
	
	/**
	 * Disables backface culling if model texture has transparency
	 */
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);	//Disable culling of triangle
	}
	
	/**
	 * Thid method renders the actual scene, by passing it onto the render class.
	 * It also starts shaders
	 * @param sun main light source
	 * @param camera camera view
	 */
	public void renderer(Light sun, Camera camera) {
		prepare();
		
		shader.start();
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		terrains.clear();
		entities.clear();
	}
	
	/**
	 * Called every frame from update to clear screen
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST); //Tests to ensure farther objects don't render in front of closer ones
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); //Clear both colour and depth buffer
		GL11.glClearColor(RED, GREEN, BLUE, 1f);
	}
	
	/**
	 * Adds terrains to list of terrains to be rendered.
	 */
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	/**
	 * Takes entities which share the same model and texture and places them in a
	 * List to be renderer all at once.
	 * @param entity current entity to be processed
	 */
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		
		if(batch != null) {
			batch.add(entity);
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	/**
	 * Creates a matrix used to project 3D scene onto 2D display
	 */
	private void createProjectionMatrix(){
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
 
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
	}
	
	/**
	 * Cleans up shaders
	 */
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
			
}
