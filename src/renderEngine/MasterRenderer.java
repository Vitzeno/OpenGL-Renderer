package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;

/**
 * This class is used to optimise rendering.
 * It allows multiple entities to be rendered without having to call the 
 * main render method multiple times a second.
 * @author Mohamed
 *
 */
public class MasterRenderer {

	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	
	//Maps textured models to entities meaning each model can have many entities, this will help drastically improve rendering
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	
	/**
	 * Thid method renders the actual scene, by passing it onto the render class.
	 * It also starts shaders
	 * @param sun main light source
	 * @param camera camera view
	 */
	public void renderer(Light sun, Camera camera) {
		renderer.prepare();
		
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();
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
	 * Cleans up shaders
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
			
}
