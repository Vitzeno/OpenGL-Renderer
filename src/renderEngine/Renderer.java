package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {
	
	/**
	 * Called every frame from update to clear screen
	 */
	public void prepare() {
		GL11.glClearColor(1, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	public void render(RawModel model) {
		GL30.glBindVertexArray(model.getVaoID()); //VAO's but by activated via binding before use
		GL20.glEnableVertexAttribArray(0); //Activate arribList, we placed data in list 0
		//(type of data to render, number of indices, type of data, where to start)
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0); //Deactivate attribList
		GL30.glBindVertexArray(0); //Unbind VAO
	}
	

}
