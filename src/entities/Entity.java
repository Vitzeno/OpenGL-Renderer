package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

/**
 * Essentially an instance of a textured model, this allows us to have 
 * multiple entities sharing the same VAO but having different transformations
 * @author Mohamed
 *
 */
public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;
	
	/**
	 * An entity is an instance of a model and ensure than a single VAO is used for multiple of the same model
	 * @param model the textured model
	 * @param position the position of the model
	 * @param rotX model rotation in the x axis
	 * @param rotY model rotation in the y axis
	 * @param rotZ model rotation in the x axis
	 * @param scale model scale
	 */
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	/**
	 * Alters the position of the model
	 * @param dx x value to change by
	 * @param dy y value to change by
	 * @param dz z value to change by
	 */
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	/**
	 * Alters the rotation of the model
	 * @param dx x value to rotate by
	 * @param dy y value to rotate by
	 * @param dz z value to rotate by
	 */
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
}
