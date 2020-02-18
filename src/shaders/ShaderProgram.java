package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Abstract class used for handling shaders and loading uniform variables.
 * @author Mohamed
 *
 */
public abstract class ShaderProgram {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	//Used for loadMatrix() //size 16 since we have a 4x4 Matrix
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocation();
	}
	
	protected abstract void getAllUniformLocation();
	
	/**
	 * Gets the location of uniform variables in shader code
	 * @return
	 */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	/**
	 * Used to start program
	 */
	public void start(){
        GL20.glUseProgram(programID);
    }
     
	/**
	 * Used to stop program
	 */
    public void stop(){
        GL20.glUseProgram(0);
    }
    
    public void cleanUp(){
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName){
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }
	
	/**
	 * Loads an int into shader code
	 * @param loaction to load to
	 * @param value to load
	 */
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	/**
	 * Loads a float into shader code
	 * @param loaction to loaded to
	 * @param value to load into shader
	 */
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	/**
	 * Loads a vector into shader code
	 * @param loaction to loaded to
	 * @param value to load into shader
	 */
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	/**
	 * Loads a boolean(0/1) into shader code
	 * since Booleans don't exist in shader code
	 * @param loaction to loaded to
	 * @param value to load into shader
	 */
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		
		if(value)
			toLoad = 1;
		
		GL20.glUniform1f(location, toLoad);	
	}
	
	/**
	 * Loads a matrix into shader code
	 * @param loaction to loaded to
	 * @param value to load into shader
	 */
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	/**
	 * Loads shader file and compiles 
	 * @param file shader file
	 * @param type GL_fragment or GL_vertex
	 * @return shader ID
	 */
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
	}
}
