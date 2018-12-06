/**
 * 
 */
package randerEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import models.RawModel;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import textures.TextureData;

/**
 * @author Mahathir
 *
 */
public class Loader {
	List<Integer> vaos=new ArrayList<Integer>();
	List<Integer> vbos=new ArrayList<Integer>();
	List<Integer> textures=new ArrayList<Integer>();
	
	public RawModel LoadToVAO(float[] positions,float[] texCoords,float[] normals,int[] indices){
		int vaoID=createVAO();
		bindIndicesBuffer(indices);
		storedDataInAttributeList(0,3,positions);
		storedDataInAttributeList(1,2,texCoords);
		storedDataInAttributeList(2,3,normals); 
		unbindVAO();
		return new RawModel(vaoID,indices.length);
	}
	public RawModel LoadToVAO(float[] position,int dimention){
		int vaoID=createVAO();
		this.storedDataInAttributeList(0, dimention, position);
		unbindVAO();
		return new RawModel(vaoID,position.length/2);
	}
	
	public int loadTexture(String fileName){
		Texture texture=null;
		try {
			texture=TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID=texture.getTextureID();
		return textureID;
	}
	
	public void cleanUp()
	{
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo:vbos){
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture:textures){
			GL11.glDeleteTextures(texture);
		}
	}
	private int createVAO(){
		int vaoID=GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	private void bindIndicesBuffer(int[] indices){
		int vboID=GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer=storedDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private void storedDataInAttributeList(int attributeNumber,int coordinateNumber,float[] data){
		int vboID=GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer=storedDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateNumber, GL11.GL_FLOAT, false, 0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}
	
	private FloatBuffer storedDataInFloatBuffer(float[] data){
		FloatBuffer buffer=BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	private IntBuffer storedDataInIntBuffer(int[] data){
		IntBuffer buffer=BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private void unbindVAO(){
		GL30.glBindVertexArray(0);
	}
	public int loadCubeMap(String[] fileNames){
		int texID=GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		for(int i=0;i<fileNames.length;i++){
			TextureData data=decodeTextureFile("res/"+fileNames[i]+".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0,
					GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(texID);
		return texID;
	}
	
	private TextureData decodeTextureFile(String fileName){
		int width=0;
		int height=0;
		ByteBuffer buffer=null;
		try {
			FileInputStream in=new FileInputStream(fileName);
			PNGDecoder decoder=new PNGDecoder(in);
			width=decoder.getWidth();
			height=decoder.getHeight();
			buffer=ByteBuffer.allocateDirect(4*width*height);
			decoder.decode(buffer, width*4,Format.RGBA);
			buffer.flip();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new TextureData(width,height,buffer);
	}
	

}
