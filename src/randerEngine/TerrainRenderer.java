package randerEngine;

import java.util.List;

import models.RawModel;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import shader.TerrainShader;
import terrains.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolBoxs.Maths;

public class TerrainRenderer {
	private TerrainShader shader;
	public TerrainRenderer(TerrainShader shader,Matrix4f projectionMatrix){
		this.shader=shader;
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	public void render(List<Terrain> terrains){
		for (Terrain terrain:terrains){
			prepareTerrain(terrain);
			loadModelMatrix(terrain);		
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unBindTexture();
		}
		
	}
	public void prepareTerrain(Terrain terrain){
		RawModel rawModel=terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTexture(terrain);
		shader.loadShiner(1, 5);
	}
	
	private void bindTexture(Terrain terrain){
		TerrainTexturePack texturePack=terrain.getTexturePack();
		TerrainTexture blendMap=terrain.getBlendMap();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureId());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureId());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureId());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureId());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, blendMap.getTextureId());
	}
	
	public void unBindTexture(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	public void loadModelMatrix(Terrain terrain){
		Matrix4f transformationMarix=Maths.createTransformationMatrix(new Vector3f(terrain.getX(),0,terrain.getZ()),
				0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMarix);
	}
}
