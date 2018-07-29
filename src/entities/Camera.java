package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * This class handles the camera view data
 * @author Mohamed
 *
 */
public class Camera {
	private Vector3f position = new Vector3f(400, 1, -400);
    private float pitch;
    private float yaw;
    private float roll;
     
    public Camera(){}
     
    /**
     * Handles player input to move the camera.
     * In reality the world is translated in the exact opposite
     * direction to simulate camera movement.
     */
    public void move(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.z -= 0.1f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            position.z += 0.1f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.x += 0.1f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            position.x -= 0.1f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            position.y += 0.1f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
            position.y -= 0.1f;
        }
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

}
