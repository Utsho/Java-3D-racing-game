package toolBoxs;

import entities.Entity;
import entities.Player;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;

public class Maths {
	public static Matrix4f createTransformationMatrix(Vector2f translation,Vector2f scale){
		Matrix4f matrix=new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x,scale.y,1.f), matrix, matrix);
		return matrix;
	}
	public static Matrix4f createTransformationMatrix(Vector3f vector,float rx,float ry,float rz,float scale){
		Matrix4f matrix=new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(vector, matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}
	public static Matrix4f createViewMatrix(Camera camera){
		Matrix4f matrix=new Matrix4f();
		matrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), matrix, matrix);
		Vector3f  campos=camera.getPosition();
		Vector3f negCamPos=new Vector3f(-campos.x,-campos.y,-campos.z);
		Matrix4f.translate(negCamPos, matrix, matrix);
		return matrix;
	}
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	public static Vector4f matrixMultiply(Matrix4f matrix,Vector4f vector){
        float x;
        float y;
        float z;
        float w;
        x=matrix.m00*vector.x+matrix.m10*vector.y+matrix.m20*vector.z+matrix.m30*vector.w;
        y=matrix.m01*vector.x+matrix.m11*vector.y+matrix.m21*vector.z+matrix.m31*vector.w;
        z=matrix.m03*vector.x+matrix.m12*vector.y+matrix.m22*vector.z+matrix.m32*vector.w;
        w=matrix.m03*vector.x+matrix.m13*vector.y+matrix.m23*vector.z+matrix.m33*vector.w;
        Vector4f ans=new Vector4f(x,y,z,w);

        return ans;
    }

	public static float getDistance(Player p, Entity e){
		float x=p.getPosition().x-e.getPosition().x;
		float y=p.getPosition().y-e.getPosition().y;
		float z=p.getPosition().z-e.getPosition().z;
		float distance=(float)Math.sqrt(x*x+y*y+z*z);
		return distance;
	}

}
