package entities;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import models.TextureModel;
import objConverter.ModelData;
import toolBoxs.Maths;

public class Entity {
	private TextureModel model;
	private Vector3f position;
	private float rotX,rotY,rotZ;
	private float scale;
	private ModelData data;
	public Entity(TextureModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale,ModelData data) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.data=data;
	}
	public void increasePosition(float dx,float dy,float dz){
		this.position.x+=dx;
		this.position.y+=dy;
		this.position.z+=dz;
	}
	public void increaseRotation(float dx,float dy,float dz){
		this.rotX+=dx;
		this.rotY+=dy;
		this.rotZ+=dz;
	}
	public TextureModel getModel() {
		return model;
	}
	public void setModel(TextureModel model) {
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
	public Vector3f getVertex(int i){
		Vector3f vert;
		vert=new Vector3f(data.getVertices()[3*i],data.getVertices()[3*i+1],data.getVertices()[3*i+2]);
		Vector4f raw=new Vector4f(vert.x,vert.y,vert.z,1);
		raw=Maths.matrixMultiply(Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale),raw);
		return new Vector3f(raw.x,raw.y,raw.z);
	}
	public int getVertexNumber(){
		return data.getVertices().length/3;
	}
	public int getNormalNumber(){
		return data.getNormals().length/3;
	}
	public Vector3f getNormal(int i){
		Vector3f vert;
		vert=new Vector3f(data.getNormals()[3*i],data.getNormals()[3*i+1],data.getNormals()[3*i+2]);
		Vector4f raw=new Vector4f(vert.x,vert.y,vert.z,0);
		raw=Maths.matrixMultiply(Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale),raw);
		return new Vector3f(raw.x,raw.y,raw.z);
	}
	
}
