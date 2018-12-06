package entities;


import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;



public class Camera {
	
	private float distanceFromPlayer=50;
	private float angleAroundPlayer=0;
	private Vector3f position=new Vector3f(0,10,0);
	private float pitch=20,yaw=0,roll;
	private Player player;
	private float mouseDX,mouseDY;
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
	public void move(){
		calculateZoom();
		calculatePitch();
		calculateAroundAngle();
		float horizontalDistance=calculateHorizontalDistance();
		float verticalDistance=calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance,verticalDistance);
        this.yaw=180-(player.getRotY()+this.angleAroundPlayer);
        if (pitch <1){
        	pitch=1;
        }
        if (pitch>90) pitch=90;

	}
	 public Camera(Player player) {
		 this.player=player;
	 }
	 private void calculateCameraPosition(float horizontalDistance,float verticalDistance){
		 float theta=player.getRotY()+angleAroundPlayer;
		 float offsetX=(float) (horizontalDistance*Math.sin(Math.toRadians(theta)));
		 float offsetZ=(float) (horizontalDistance*Math.cos(Math.toRadians(theta)));
		 position.y=player.getPosition().y+verticalDistance;
		 position.x=player.getPosition().x-offsetX;
		 position.z=player.getPosition().z-offsetZ;
		// this.yaw=180-theta;
	 }
	 private float calculateHorizontalDistance(){
		 return (float) (this.distanceFromPlayer*Math.cos(Math.toRadians(pitch)));
	 }
	 private float calculateVerticalDistance(){
		 return (float) (this.distanceFromPlayer*Math.sin(Math.toRadians(pitch)));
	 }
	 
	 
	 private void calculateZoom(){
		 float zoomLevel=Mouse.getDWheel()*.1f;
		 this.distanceFromPlayer-=zoomLevel;
		 if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) this.distanceFromPlayer-=.25f;
		 if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) this.distanceFromPlayer+=.25f;
	 }
	 private void calculatePitch(){
		 if (Mouse.isButtonDown(1)){
			 mouseDY=Mouse.getDY()*.1f;
			 this.pitch-=mouseDY;
		 }
	 }
	 
	 private void calculateAroundAngle(){
		 if (Mouse.isButtonDown(0)){
			  mouseDX=Mouse.getDX()*.3f;
			 this.angleAroundPlayer-=mouseDX;
		 }
	 }

}
