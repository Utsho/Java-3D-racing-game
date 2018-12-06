package entities;

import models.TextureModel;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import randerEngine.DisplayManager;
import terrains.NormaledTerrain;
import terrains.Terrain;
import toolBoxs.Maths;


public class Player {
	
	private static final float TURN_SPEED=160;
	
	private static final float GRAVITY=-50;
	private Vector3f frontDerection;
	private Vector3f totalForce;
	private float engineForce;
	private float maxEngineForce;
	private Vector3f velocity;
	private Vector3f acceleration;
	private Vector3f frictionForce;

	private float currentRunSpeed=0;
	private float currentTurnSpeed=0;
	private float upwardSpeed=0;
	Entity entity;
	float prevAngle;
	public Player(Entity entity) {
		this.entity=entity;
		totalForce=new Vector3f(0,0,0);
		engineForce=0;
		maxEngineForce=200;
		velocity=new Vector3f(0,0,0);
		acceleration=new Vector3f(0,0,0);
		frictionForce=new Vector3f(0,0,0);
		frontDerection=new Vector3f(0,0,0);
		prevAngle=this.getRotY();
	}
	public void increaseRotation(float dx,float dy,float dz){
		this.entity.increaseRotation(dx, dy, dz);
	}
	public Vector3f getPosition() {
		return this.entity.getPosition();
	}
	public float getRotX() {
		return entity.getRotX();
	}
	public float getRotY() {
		return entity.getRotY();
	}
	public float getRotZ() {
		return entity.getRotZ();
	}
	public float getScale() {
		return entity.getScale();
	}
	private void  setRotZ(float rz){
		this.entity.setRotZ(rz);		
	}
	private void  setRotX(float rx){
		this.entity.setRotX(rx);
		
	}
	
	public void updatePosition(){
		float sx,sy,sz;
		float time=DisplayManager.getFrameTimeSeconds();
		acceleration.x=totalForce.x;
		acceleration.y=totalForce.y;
		acceleration.z=totalForce.z;
		if (velocity.x>20) velocity.x=20;
		if (velocity.x<-20) velocity.x=-20;
		if (velocity.z>20) velocity.z=20;
		if (velocity.z<-20) velocity.z=-20;
		sx=velocity.x*time+.5f*acceleration.x*time*time;
		sz=velocity.z*time+.5f*acceleration.z*time*time;
		float speed=(float)Math.pow((Math.pow(velocity.x,2)+Math.pow(velocity.z,2)),.5);
		velocity.x=speed*frontDerection.x;
		velocity.z=speed*frontDerection.z;

		velocity.x=(velocity.x+acceleration.x*time);
		velocity.z=(velocity.z+acceleration.z*time);

		prevAngle=this.getRotY();
		entity.increasePosition(sx,0,sz);

		totalForce.x=0;
		totalForce.y=0;
		totalForce.z=0;

	}



	public void addForces(Vector3f forces,int type){
		System.out.println(type+" "+forces.x+" "+forces.y+" "+forces.z);

		totalForce.x+=forces.x;
		totalForce.y+=forces.y;
		totalForce.z+=forces.z;
	}
	public TextureModel getModel(){
		return entity.getModel();
	}
	public void moveCalculation(){
        entity.increaseRotation(0,this.currentTurnSpeed*DisplayManager.getFrameTimeSeconds(),0);
		frontDerection.x=(float) Math.sin(Math.toRadians(getRotY()));
		frontDerection.z=(float) Math.cos(Math.toRadians(getRotY()));
		float dx= (float) (currentRunSpeed*Math.sin(Math.toRadians(getRotY())))*DisplayManager.getFrameTimeSeconds();
		float dz= (float) (currentRunSpeed*Math.cos(Math.toRadians(getRotY())))*DisplayManager.getFrameTimeSeconds();
		this.entity.increasePosition(dx,0,dz);

		/*

		frontDerection.y=0;
		frontDerection.normalise(frontDerection);
		addForces(new Vector3f(frontDerection.x*engineForce,0,frontDerection.z*engineForce),1);
        float speed=(float)Math.pow((Math.pow(velocity.x,2)+Math.pow(velocity.z,2)),.5);
        if (velocity.x*frontDerection.x>0)   frictionForce.x= -.25f*frontDerection.x*speed*speed;
        else frictionForce.x=.47f*frontDerection.x*speed*speed;

		if(velocity.z*frontDerection.z>0)frictionForce.z= -.25f*frontDerection.z*speed*speed;
        else frictionForce.z=.47f*frontDerection.z*speed*speed;
        addForces(frictionForce,2);
		this.updatePosition();*/
	}




    public void move(NormaledTerrain terrain){
        checkInput();
        increaseRotation(0, this.currentTurnSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		moveCalculation();
		upwardSpeed+=GRAVITY*DisplayManager.getFrameTimeSeconds();
		entity.increasePosition(0, upwardSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight=terrain.getTerrainHeight(getPosition().x, getPosition().z);
		if (getPosition().y<terrainHeight){
			upwardSpeed=0;
			getPosition().y=terrainHeight;
		}

    }
	public void move(Terrain terrain){
		checkInput();

		moveCalculation();
		upwardSpeed+=GRAVITY*DisplayManager.getFrameTimeSeconds();
		entity.increasePosition(0, upwardSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight=terrain.getTerrainHeight(getPosition().x, getPosition().z);
		if (getPosition().y<terrainHeight){
			upwardSpeed=0;
			getPosition().y=terrainHeight;
		}
	}
	private void checkInput(){
		if (Keyboard.isKeyDown(Keyboard.KEY_W)||Keyboard.isKeyDown(Keyboard.KEY_UP)){
			if (engineForce<16000) engineForce+=50;
			if (currentRunSpeed<400)currentRunSpeed+=10;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)||Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
            if (engineForce>-16000) engineForce-=50;
			if (currentRunSpeed>-400)currentRunSpeed-=10;
		}
		else
		{
			if (Math.abs(currentRunSpeed)>20) currentRunSpeed+=-(currentRunSpeed/Math.abs(currentRunSpeed))*15;
			else currentRunSpeed=0;
		}

		if (engineForce!=0){
			if (Keyboard.isKeyDown(Keyboard.KEY_A)||Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
				this.currentTurnSpeed=TURN_SPEED;
			}else if (Keyboard.isKeyDown(Keyboard.KEY_D)||Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
				this.currentTurnSpeed=-TURN_SPEED;
			}else{
				this.currentTurnSpeed=0;
			}
		}else{
			this.currentTurnSpeed=0;
		}
		
		
		
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public Vector3f getSpeedDirection() {
		return velocity;
	}
	public Entity getEntity() {
		return entity;
	}
	public float getCurrentRunSpeed() {
		return currentRunSpeed;
	}


	public void setCurrentRunSpeed(float currentRunSpeed) {
		this.currentRunSpeed = currentRunSpeed;
	}
}
