/**
 * 
 */
package randerEngine;

import java.util.List;
import java.util.Map;

import models.RawModel;
import models.TextureModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import entities.Player;
import shader.StaticShader;
import toolBoxs.Maths;

/**
 * @author Mahathir
 *
 */
public class EntityRenderer {
	
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader,Matrix4f projectionMatrix){
		this.shader=shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Map<TextureModel,List<Entity>> entities){
		for (TextureModel model:entities.keySet()){
			
			prepareTextureModel(model);
			List<Entity> batch=entities.get(model);
			for (Entity entity:batch){
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0); 
			}
			unBindTexture();
			
		}
	}
	public void render(Player player){
		TextureModel textureModel=player.getModel();
		RawModel rawModel=textureModel.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		shader.loadFackLight(textureModel.getModelTexture().isUsingFakeLighting());
		shader.loadShiner(textureModel.getModelTexture().getShineDamper(), textureModel.getModelTexture().getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureModel.getModelTexture().getId());
		Matrix4f transformationMarix=Maths.createTransformationMatrix(player.getPosition(), player.getRotX(),
				player.getRotY(), player.getRotZ(), player.getScale());
		shader.loadTransformationMatrix(transformationMarix);
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	public void prepareTextureModel(TextureModel textureModel){
		RawModel rawModel=textureModel.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		if (textureModel.getModelTexture().isHasTransparency()){
			MasterRenderer.enableCulling();
		}
		shader.loadFackLight(textureModel.getModelTexture().isUsingFakeLighting());
		shader.loadShiner(textureModel.getModelTexture().getShineDamper(), textureModel.getModelTexture().getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureModel.getModelTexture().getId());
	}
	
	public void unBindTexture(){
		MasterRenderer.disableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	public void prepareInstance(Entity entity){
		Matrix4f transformationMarix=Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMarix);
	}
	
	
	
	

}
