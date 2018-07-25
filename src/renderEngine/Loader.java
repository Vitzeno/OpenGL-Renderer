package renderEngine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Loads 3D objects into memory by storing positional 
 * data about objects into a VAO
 * @author Mohamed
 *
 */
public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	
	/**
	 * Takes positional data and converts to a VAO 
	 * @param positions of vertices of object
	 * @return returns a RawModel objects of VAO's
	 */
	public RawModel loadToVAO(float[] positions, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAtrtibuteList(0, positions);
		unbindVAO();	
		return new RawModel(vaoID, indices.length); //indices represent the index buffer
	}
	
	/**
	 * Clears VAO and VBO ArrayLists
	 */
	public void cleanUp() {
		for(int vao : vaos)
			GL30.glDeleteVertexArrays(vao);
		for(int vbo : vbos)
			GL15.glDeleteBuffers(vbo);
	}
	
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays(); //Creates empty VAO and returns ID
		vaos.add(vaoID); //Store in ArrayList
		GL30.glBindVertexArray(vaoID); //VAO's must be activated via binding before use
		return vaoID;
	}
	
	private void storeDataInAtrtibuteList(int AttributeListNumber, float[] data) {
		int vboID = GL15.glGenBuffers(); //Created empty VBO and returns ID
		vbos.add(vboID); //Store in ArrayList
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); //Like VAO's must bound before use, must also specify type
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		
		//(VBO type, data in buffer format, usage static since I wont be altering it)
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); // Store in VBO
		//(attribList to store in, vertex length, data type, normalised, distance between vertices e.g any data in between, offset)
		GL20.glVertexAttribPointer(AttributeListNumber, 3, GL11.GL_FLOAT, false, 0, 0); 
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //Unbind VBO
	}

	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer (int[] indices) {
		int vboID = GL15.glGenBuffers(); //Created empty VBO and returns ID
		vbos.add(vboID); //Store in ArrayList
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID); //type element array tells openGL to use as an index buffer
		IntBuffer buffer = storeDataInIntBuffer(indices);
		
		//(VBO type, data in buffer format, usage static since I wont be altering it)
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); // Store in VBO
	}
	
	/**
	 * Data must be stored in a int buffer format before
	 * storing in VBO
	 * @param data
	 * @return
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip(); //Finished writing to prepare for reading
		return buffer;
	}
	
	/**
	 * Data must be stored in a float buffer format before
	 * storing in VBO
	 * @param data
	 * @return
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip(); //Finished writing to prepare for reading
		return buffer;
	}
}
