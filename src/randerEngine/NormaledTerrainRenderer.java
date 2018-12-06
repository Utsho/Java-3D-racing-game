package randerEngine;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shader.NormaledTerrainShader;
import shader.TerrainShader;
import terrains.NormaledTerrain;
import terrains.Terrain;
import textures.NormaledTerrainTexturePack;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolBoxs.Maths;

import java.util.List;

/**
 * Created by Mahathir on 05-Nov-15.
 */
public class NormaledTerrainRenderer {
    private NormaledTerrainShader shader;
    public NormaledTerrainRenderer(NormaledTerrainShader shader,Matrix4f projectionMatrix){
        this.shader=shader;
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    public void render(List<NormaledTerrain> terrains){
        for (NormaledTerrain terrain:terrains){
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unBindTexture();
        }

    }
    public void prepareTerrain(NormaledTerrain terrain){
        RawModel rawModel=terrain.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        bindTexture(terrain);
        shader.loadShiner(1, 5);
    }

    private void bindTexture(NormaledTerrain terrain){
        NormaledTerrainTexturePack texturePack=terrain.getTexturePack();
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
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrNormalTexture().getTextureId());
        GL13.glActiveTexture(GL13.GL_TEXTURE6);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgNormalTexture().getTextureId());
        GL13.glActiveTexture(GL13.GL_TEXTURE7);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbNormalTexture().getTextureId());
        GL13.glActiveTexture(GL13.GL_TEXTURE8);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBgNormalTexture().getTextureId());
    }

    public void unBindTexture(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void loadModelMatrix(NormaledTerrain terrain){
        Matrix4f transformationMarix= Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()),
                0, 0, 0, 1);
        shader.loadTransformationMatrix(transformationMarix);
    }
}
