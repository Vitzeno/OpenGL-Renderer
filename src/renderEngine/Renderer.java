package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
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
 * This class handles rendering of scenes. Also creates projection matrix therefore handles FOV, NEAR_PLANE and FAR_PLANE values.
 * @author Mohamed
 *
 */
public class Renderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	
	private Matrix4f projectionMatrix;
	private StaticShader shader;
	
	/**
	 * Constructor used for setting up projection matrix and enabling culling.
	 * @param shader
	 */
	public Renderer(StaticShader shader){
		this.shader = shader;
		GL11.glEnable(GL11.GL_CULL_FACE);	//Enable culling of triangle
		GL11.glCullFace(GL11.GL_BACK);		//Culls triangle facing away from camera
		
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
	
	/**
	 * Called every frame from update to clear screen
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST); //Tests to ensure farther objects don't render in front of closer ones
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); //Clear both colour and depth buffer
		GL11.glClearColor(1, 0, 0, 1);
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
		shader.loadShineVariable(texture.getShineDampner(), texture.getReflectivity());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);	//Activate texture to provided texture bank
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	/**
	 * Unbinding VAO attribute lists
	 */
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0); //Deactivate attribList
		GL20.glDisableVertexAttribArray(1); //Deactivate attribList
		GL20.glDisableVertexAttribArray(2); //Deactivate attribList
		GL30.glBindVertexArray(0); //Unbind VAO
	}
	
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
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
