package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;

/**
 * This class handles the camera view data
 * @author Mohamed
 *
 */
public class Camera {
	
	private boolean holdToLook = false;
	
	private float distanceFromPlayer = 25;
	private float angleAroundPlayer = 0;
	//Angle above player is the same ass pitch angle
	
	private Vector3f position = new Vector3f(400, 10, -500);
    private float pitch = 15;
    private float yaw;
    private float roll;
    
    private static final float TERRAIN_HEIGHT = 1;
    private static final float DEFAULT_ZOOM = 25;
    private static final float MINIMUM_ZOOM = 15;
    private static final float MAXIMUM_PITCH = 170;
    
    private Player player;
     
    public Camera(Player player){
    	this.player = player;
    }
     
    /**
     * Handles player input to move the camera.
     * In reality the world is translated in the exact opposite
     * direction to simulate camera movement.
     */
    public void move(){
    	calculateZoom();
    	calculatePitch(holdToLook);
    	calculateAngleAround(holdToLook);
    	
    	float horizontalDistance = calculateHorizontalDistance();
    	float verticalDistance = calculateVerticalDistance();
    	calculateCameraPosition(horizontalDistance, verticalDistance);
    	this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }
 
    public Vector3f getPosition() {
        return position;
    }
 
    public float getPitch() {
        return pitch;
    }
 
    public float getYaw() {
        return yaw;
    }
 
    public float getRoll() {
        return roll;
    }
    
    /**
     * Basic trig to calculate new camera poistion
     * @param horizontalDistance
     * @param verticalDistance
     */
    private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
    	float theta = player.getRotY() + angleAroundPlayer;
    	float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
    	float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
    	
    	position.x = player.getPosition().x - offsetX;
    	position.y = player.getPosition().y + verticalDistance;
    	position.z = player.getPosition().z - offsetZ;
    	
    	checkClamping();
    	if(!holdToLook)
    		centerCursor();
    }
    
    /**
     * Centres mouse cursor to prevent going off screen
     * Only called when not using hold to look
     */
    private void centerCursor() {
    	Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
    	player.increaseRotation(0, -Mouse.getDX() * DisplayManager.getFrameTimeSeconds(), 0);
    	Mouse.setGrabbed(true);
    }
    
    /**
     * Limits certain variables like y pos, pitch and zoom levels
     */
    private void checkClamping() {
    	if(position.y < TERRAIN_HEIGHT)
    		position.y = TERRAIN_HEIGHT;
    	if(distanceFromPlayer < MINIMUM_ZOOM)
    		distanceFromPlayer = MINIMUM_ZOOM;
    	if(pitch > MAXIMUM_PITCH)
    		pitch = MAXIMUM_PITCH;
    }
    
    /**
     * @return Horizontal distance from player
     */
    private float calculateHorizontalDistance() {
    	return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }
    
    /**
     * @return Vertical distance from player
     */
    private float calculateVerticalDistance() {
    	return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }
    
    /**
     * Uses mouse wheel to increment zoom/distance from player
     */
    private void calculateZoom() {
    	float zoomLevel = Mouse.getDWheel() * 0.01f;
    	distanceFromPlayer -= zoomLevel;
    	if(Mouse.isButtonDown(2))
    		distanceFromPlayer = DEFAULT_ZOOM;
    }

    /**
     * calculates pitch, i.e angle between floor and camera
     * @param holdToLook determines if player needs to hold to look
     */
    private void calculatePitch(boolean holdToLook) {
    	if(holdToLook) {
    		if(Mouse.isButtonDown(1)) {
        		float pitchChange = Mouse.getDY() * 0.1f;
        		pitch -= pitchChange;
        	}
    	}else {
    		float pitchChange = Mouse.getDY() * 0.1f;
    		pitch -= pitchChange;
    	}
    	
    }
    
    /**
     * calculates angle around player, not including yaw/facing
     * @param holdToLook determines if player needs to hold to look
     */
    private void calculateAngleAround(boolean holdToLook) {
    	if(holdToLook) {
    		if(Mouse.isButtonDown(1)) {
        		float angleChange = Mouse.getDX() * 0.3f;
        		angleAroundPlayer -= angleChange;
        	}
    	}else {
    		float angleChange = Mouse.getDX() * 0.3f;
    		angleAroundPlayer -= angleChange;
    	}
    	
    }
}
