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
		//(type of data to render, where to start, where to end)
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		GL20.glDisableVertexAttribArray(0); //Deactivate attribList
		GL30.glBindVertexArray(0); //Unbind VAO
	}
	

}
