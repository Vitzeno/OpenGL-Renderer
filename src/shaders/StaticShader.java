package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	private int location_transformationMatrix;
	private int loaction_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {	
		super.bindAttribute(0, "position"); //Bind attribList 0 of VAO to position in shader
		super.bindAttribute(1, "textureCoords"); //Bind attribList 1 of VAO to textureCoords in shader
		super.bindAttribute(2, "normal"); //Bind attribList 1 of VAO to textureCoords in shader
	}

	@Override
	protected void getAllUniformLocation() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loaction_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
	}
	
	public void loadShineVariable(float shineDamper, float reflectivity) {
		super.loadFloat(location_shineDamper, shineDamper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadLight(Light light) {
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(loaction_projectionMatrix, projection);
	}

}
