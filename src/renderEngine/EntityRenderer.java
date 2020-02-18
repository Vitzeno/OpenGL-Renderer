package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

/**
 * This class handles rendering of entities only.
 * @author Mohamed
 *
 */
public class EntityRenderer {
	
	private StaticShader shader;
	
	/**
	 * Constructor used for loading up projection matrix and shaders.
	 * @param shader
	 */
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
	
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for(TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	/**
	 * Preparing final model by activating VAO attribute lists
	 * @param model
	 */
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		
		GL30.glBindVertexArray(rawModel.getVaoID()); //VAO's must be activated via binding before use
		GL20.glEnableVertexAttribArray(0); //Activate arribList, placed positions in list 0
		GL20.glEnableVertexAttribArray(1); //Activate arribList, placed textureCoords in list 1
		GL20.glEnableVertexAttribArray(2); //Activate arribList, placed normals in list 2
		
		ModelTexture texture = model.getTexture();
		if(texture.isHasTransparency()) {
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariable(texture.getShineDampner(), texture.getReflectivity());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);	//Activate texture to provided texture bank
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	/**
	 * Unbinding VAO attribute lists
	 */
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0); //Deactivate attribList
		GL20.glDisableVertexAttribArray(1); //Deactivate attribList
		GL20.glDisableVertexAttribArray(2); //Deactivate attribList
		GL30.glBindVertexArray(0); //Unbind VAO
	}
	
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	
	//Old inefficient render method
	/*	
	public void render(Entity entity, StaticShader shader) {
		
		TexturedModel model = entity.getModel();
		RawModel rawModel = model.getRawModel();
		
		GL30.glBindVertexArray(rawModel.getVaoID()); //VAO's must be activated via binding before use
		GL20.glEnableVertexAttribArray(0); //Activate arribList, placed positions in list 0
		GL20.glEnableVertexAttribArray(1); //Activate arribList, placed textureCoords in list 1
		GL20.glEnableVertexAttribArray(2); //Activate arribList, placed normals in list 2
		
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		
		ModelTexture texture = model.getTexture();
		shader.loadShineVariable(texture.getShineDampner(), texture.getReflectivity());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);	//Activate texture to provided texture bank
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		
		//(type of data to render, number of indices, type of data, where to start)
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0); //Deactivate attribList
		GL20.glDisableVertexAttribArray(1); //Deactivate attribList
		GL20.glDisableVertexAttribArray(2); //Deactivate attribList
		GL30.glBindVertexArray(0); //Unbind VAO
	}
*/

}
