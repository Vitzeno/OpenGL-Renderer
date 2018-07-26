package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * This class sets up the display.
 * This includes setting resolution and framerate cap.
 * @author Mohamed
 *
 */
public class DisplayManager {
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int FPS_CAP = 120;
	
	
	public static void createDisplay() {
		
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true); //OpenGL version 3.2
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("First 3D Game");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT); //Bottom left to top right of display
		
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP); //Sync game to steady fps
		Display.update();
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}

}
