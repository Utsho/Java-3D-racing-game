package skybox;

/**
 * 
 */

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shader.ShaderProgram;
import toolBoxs.Maths;
import entities.Camera;

/**
 * @author Mahathir
 *
 */
public class SkyboxShader extends ShaderProgram{
	
	private float rotation=0;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColor;
	private int location_cubeMap;
	private int location_cubeMap2;
	private int location_blendFactor;
	
	public static final String VERTEX_FILE="src/skybox/skyboxVertexShader.txt";
	public static final String FRAGMENT_FILE="src/skybox/skyboxFragmentShader";

	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");		
	}

	@Override
	protected void getAllUniformLocation() {
		location_projectionMatrix=super.getUniformLocation("projectionMatrix");
		location_viewMatrix=super.getUniformLocation("viewMatrix");
		location_fogColor=super.getUniformLocation("fogColor");
		location_cubeMap=super.getUniformLocation("cubeMap");
		location_cubeMap2=super.getUniformLocation("cubeMap2");
		location_blendFactor=super.getUniformLocation("blendFactor");
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	public void connectTextureUnits(){
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}
	public void loadBlendFactor(float blend){
		super.loadFloat(location_blendFactor, blend);
	}
	public void loadSkyColor(Vector3f fogColor){
		super.loadVector(location_fogColor, fogColor);
	}
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix=Maths.createViewMatrix(camera);
		viewMatrix.m30=0;
		viewMatrix.m31=0;
		viewMatrix.m32=0;
		rotation+=.01;
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), viewMatrix, viewMatrix);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

}

