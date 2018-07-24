package renderEngine;

public class RawModel {
	
	private int vaoID;
	private int vertexCount; //Number of vertices in model
	
	/**
	 * Model data is stored in Vertex Buffer Objects (VBO) which is in turn stored in the
	 * attributes lists of Vertex Array Objects (VAO).
	 * @param vaoID
	 * @param vertexCount
	 */
	public RawModel (int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

}
