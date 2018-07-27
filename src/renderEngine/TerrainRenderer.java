package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import toolbox.Maths;

/**
 * This class handles rendering of terrain only.
 * @author Mohamed
 *
 */
public class TerrainRenderer {
	
	private TerrainShader shader;
	
	/**
	 * Constructor used for loading up projection matrix and shaders.
	 * @param shader
	 */
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
	}
	
	public void render(List<Terrain> terrains) {
		for(Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

			unbindTexturedModel();
		}
	}
	
	/**
	 * Preparing final model by activating VAO attribute lists
	 * @param model
	 */
	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getModel();
		
		GL30.glBindVertexArray(rawModel.getVaoID()); //VAO's must be activated via binding before use
		GL20.glEnableVertexAttribArray(0); //Activate arribList, placed positions in list 0
		GL20.glEnableVertexAttribArray(1); //Activate arribList, placed textureCoords in list 1
		GL20.glEnableVertexAttribArray(2); //Activate arribList, placed normals in list 2
		
		ModelTexture texture = terrain.getTexture();
		shader.loadShineVariable(texture.getShineDampner(), texture.getReflectivity());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);	//Activate texture to provided texture bank
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
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
	
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
