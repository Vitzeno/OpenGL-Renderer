package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);

		RawModel model = OBJLoader.loadOBJModel("stall", loader);
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("stallTexture")));
		
		ModelTexture texture = staticModel.getTexture(); 
		texture.setShineDampner(10);
		texture.setReflectivity(0);
		
		Entity entity = new Entity(staticModel, new Vector3f(0, 0, -25), 0, 0, 0, 1);
		Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));
		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested()) {
			//Game logic
			entity.increaseRotation(0, 1, 0);
			//entity.increasePosition(0, 0, -0.1f);
			
			camera.move();
			
			renderer.prepare();
			
			shader.start();
			shader.loadLight(light);
			shader.loadViewMatrix(camera);
			renderer.render(entity, shader);
			shader.stop();
				
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
