package shaders;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {	
		super.bindAttribute(0, "position"); //Bind attribList 0 of VAO to position in shader
		super.bindAttribute(1, "textureCoords"); //Bind attribList 1 of VAO to textureCoords in shader
	}

}
