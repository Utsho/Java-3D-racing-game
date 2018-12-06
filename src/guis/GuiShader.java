package guis;

import org.lwjgl.util.vector.Matrix4f;

public class GuiShader extends shader.ShaderProgram{
	
	public static final String VERTEX_FILE="src/guis/vertexShader.txt";
	public static final String FRAGMENT_FILE="src/guis/fragmentShader.txt";
	
	private int loaction_transformationMatrix;

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformation(Matrix4f matrix){
		super.loadMatrix(loaction_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocation() {
		loaction_transformationMatrix=super.getUniformLocation("trasnformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		
	}

}
