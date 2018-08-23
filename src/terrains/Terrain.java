package terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturesPack;
/**
 * Each terrain has a set size and is arranged in a grid like fashion
 * @author Mohamed
 *
 */
public class Terrain {
	
	private static final float SIZE = 800;
	private static final float MAX_HEIGHT = 60;
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;
	
	
	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturesPack texturePack;
	private TerrainTexture blendMap;
	
	/**
	 * The constructor generates a terrain in the grid positions provided
	 * @param gridX
	 * @param gridZ
	 * @param loader
	 * @param texturePack
	 * @param blendMap
	 */
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturesPack texturePack, TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, heightMap);
	}
	
	/**
	 * 
	 * @param loader
	 * @return
	 */
	private RawModel generateTerrain(Loader loader, String heightMap){
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/" + heightMap + ".png"));
		} catch (IOException e) {
			System.out.println("Could not read height map file.");
			e.printStackTrace();
		}
		
		int VERTEX_COUNT = image.getHeight();
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = getHeight(j, i, image);
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormals(j, i, image);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	/**
	 * This method calculates the normals of the terrain vertices after applying
	 * the height map
	 * @param x
	 * @param z
	 * @param image
	 * @return
	 */
	private Vector3f calculateNormals(int x, int z, BufferedImage image) {
		float heightL = getHeight(x - 1, z, image);
		float heightR = getHeight(x + 1, z, image);
		float heightD = getHeight(x, z - 1, image);
		float heightU = getHeight(x, z + 1, image);
		
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		
		return normal;
	}
	
	private float getHeight(int x, int z, BufferedImage image) {
		if(x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) {
			return 0;
		}
		
		float height = image.getRGB(x, z);
		height +=  MAX_PIXEL_COLOUR / 2f;
		height /= MAX_PIXEL_COLOUR /2f;
		height *= MAX_HEIGHT;
		
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	public TerrainTexturesPack getTexturePack() {
		return texturePack;
	}

}
