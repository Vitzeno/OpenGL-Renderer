package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import models.TexturedModel;

public class Renderer {
	
	/**
	 * Called every frame from update to clear screen
	 */
	public void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(1, 0, 0, 1);
	}
	
	public void render(TexturedModel texturedModel) {
		RawModel model = texturedModel.getRawModel();
		
		GL30.glBindVertexArray(model.getVaoID()); //VAO's but by activated via binding before use
		GL20.glEnableVertexAttribArray(0); //Activate arribList, placed positions in list 0
		GL20.glEnableVertexAttribArray(1); //Activate arribList, placed textureCoords in list 1
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);	//Activate texture to provided texture bank
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());
		
		//(type of data to render, number of indices, type of data, where to start)
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0); //Deactivate attribList
		GL20.glDisableVertexAttribArray(1); //Deactivate attribList
		GL30.glBindVertexArray(0); //Unbind VAO
	}
	

}
