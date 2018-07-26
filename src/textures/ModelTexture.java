package textures;

/**
 * This class stores data about the texture to be applied to models
 * @author Mohamed
 *
 */
public class ModelTexture {
	
	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	public float getShineDampner() {
		return shineDamper;
	}

	public void setShineDampner(float shineDampner) {
		this.shineDamper = shineDampner;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public ModelTexture(int ID) {
		this.textureID = ID;
	}

	public int getID() {
		return textureID;
	}
}
