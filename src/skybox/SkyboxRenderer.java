package skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import randerEngine.DisplayManager;
import randerEngine.Loader;
import models.RawModel;

public class SkyboxRenderer {
	private static final float SIZE=500f;
	private static final float[] VEETICES={
		-SIZE,SIZE,-SIZE,
		-SIZE,-SIZE,-SIZE,
		SIZE,-SIZE,-SIZE,
		SIZE,-SIZE,-SIZE,
		SIZE,SIZE,-SIZE,
		-SIZE,SIZE,-SIZE,
		
		-SIZE,-SIZE,SIZE,
		-SIZE,-SIZE,-SIZE,
		-SIZE,SIZE,-SIZE,
		-SIZE,SIZE,-SIZE,
		-SIZE,SIZE,SIZE,
		-SIZE,-SIZE,SIZE,
		
		
		SIZE,-SIZE,-SIZE,
		SIZE,-SIZE,SIZE,
		SIZE,SIZE,SIZE,
		SIZE,SIZE,SIZE,
		SIZE,SIZE,-SIZE,
		SIZE,-SIZE,-SIZE,
		
		-SIZE,-SIZE,SIZE,
		-SIZE,SIZE,SIZE,
		SIZE,SIZE,SIZE,
		SIZE,SIZE,SIZE,
		SIZE,-SIZE,SIZE,
		-SIZE,-SIZE,SIZE,
		
		
		-SIZE,SIZE,-SIZE,
		SIZE,SIZE,-SIZE,
		SIZE,SIZE,SIZE,
		SIZE,SIZE,SIZE,
		-SIZE,SIZE,SIZE,
		-SIZE,SIZE,-SIZE,
		
		-SIZE,-SIZE,-SIZE,
		-SIZE,-SIZE,SIZE,
		SIZE,-SIZE,-SIZE,
		SIZE,-SIZE,-SIZE,
		-SIZE,-SIZE,SIZE,
		SIZE,-SIZE,SIZE,
	};
	
	private static String[] TEXTURE_FILES={"right","left","top","bottom","back","front"};
	private static String[] NIGHT_TEXTURE_FILES={"nightright","nightleft","nighttop","nightbottom","nightback","nightfront"};
	
	private RawModel cube;
	private int texture;
	private int nightTexture;
	private static float time=12000;
	private static float blendFactor;
	private SkyboxShader shader;

	
	public static float getBlendFactor() {
		return blendFactor;
	}
	public SkyboxRenderer(Loader loader,Matrix4f projectionMatrix){
		cube=loader.LoadToVAO(VEETICES, 3);
		texture=loader.loadCubeMap(TEXTURE_FILES);
		nightTexture=loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader=new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		
	}
	public void renderer(Camera camera,Vector3f fogColor){
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadSkyColor(fogColor);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	private void bindTextures(){
		time += DisplayManager.getFrameTimeSeconds() * 1000;
		time %= 24000;
		int texture1;
		int texture2;	
		if(time >= 0 && time < 5000){
			texture1 = nightTexture;
			texture2 = nightTexture;
			blendFactor = (time - 0)/(5000 - 0);
		}else if(time >= 5000 && time < 8000){
			texture1 = nightTexture;
			texture2 = texture;
			blendFactor = (time - 5000)/(8000 - 5000);
		}else if(time >= 8000 && time < 21000){
			texture1 = texture;
			texture2 = texture;
			blendFactor = (time - 8000)/(21000 - 8000);
		}else{
			texture1 = texture;
			texture2 = nightTexture;
			blendFactor = (time - 21000)/(24000 - 21000);
		}

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}
	
}
