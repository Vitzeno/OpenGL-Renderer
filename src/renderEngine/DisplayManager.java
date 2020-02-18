package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
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
	
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public static final int FPS_CAP = 120;
	
	private static long lastFrameTime;
	private static float delta;
	
	
	public static void createDisplay() {
		
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true); //OpenGL version 3.2
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Mayflower");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT); //Bottom left to top right of display
		lastFrameTime = getCurrentTime();
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP); //Sync game to steady fps
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	/**
	 * Returns how long frame took to render in seconds
	 * @return
	 */
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	/**
	 * Returns current time in ms
	 * @return
	 */
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

}
