package randerEngine;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;

public class XrayRender {
	public static void XrayPointRenderer(Entity e){
		for (int i=0;i<e.getVertexNumber();i++){
            GL11.glBegin(GL11.GL_POINTS);
            Vector3f vert1,vert2;
            vert1=e.getVertex(i);
            vert2=e.getVertex((i + 1) % e.getVertexNumber());

            GL11.glVertex3f(vert1.x,vert1.y,vert1.z);
            GL11.glVertex3f(vert2.x,vert2.y,vert2.z);
            GL11.glEnd();
        }
        for (int i=0;i<e.getNormalNumber();i++){
            GL11.glBegin(GL11.GL_POINTS);
            Vector3f vert1,vert2,normal;
            vert1=e.getVertex(i);
            vert2=e.getVertex((i + 1) % e.getVertexNumber());
            normal=e.getNormal(i);
            Vector3f vert3=new Vector3f((vert1.x+vert2.x)/2,(vert1.y+vert2.y)/2,(vert1.z+vert2.z)/2);
            GL11.glVertex3f(vert3.x,vert3.y,vert3.z);
            GL11.glVertex3f(vert3.x+normal.x*.25f,vert3.y+normal.y*.25f,vert3.z+normal.z*.25f);
            GL11.glEnd();
        }
	}
	
	public static void XrayCollideRenderer(){
		
	}
	public static void XrayViewRenderer(){
		
	}
	

}
