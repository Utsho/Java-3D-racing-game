package toolBoxs;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;

public class Collision {
	public static float[] returnValue=new float[2];
	private static float tlast=1000000;
	private static float tfirst=0;
	public static boolean testInterSection(Entity c0,Entity c1){
		
        for (int i=0;i<c0.getNormalNumber();i++){
            Vector3f D=c0.getNormal(i);
            float[] r0,r1;
            r0=computeInterval(c0,D);
            float min0=r0[0],max0=r0[1];
            r1=computeInterval(c1,D);
            float min1=r1[0],max1=r1[1];
            if (max1<min0 || max0<min1) return false;
        }
        for (int i=0;i<c1.getNormalNumber();i++){
            Vector3f D=c1.getNormal(i);
            float[] r0,r1;
            r0=computeInterval(c0,D);
            float min0=r0[0],max0=r0[1];
            r1=computeInterval(c1,D);
            float min1=r1[0],max1=r1[1];
            if (max1<min0 || max0<min1) return false;
        }
        return true;
    }
	private static float[] computeInterval(Entity C,Vector3f D){
        float max,min;
        min=Vector3f.dot(D,C.getVertex(0));
        max=min;
        for (int i=1;i<C.getVertexNumber();i++){
            float v=Vector3f.dot(D,C.getVertex(i));
            
            if (v<min) min=v;
            else if (v>max) max=v;
        }
        float[] r=new float[2];
        r[0]=min;
        r[1]=max;
        return r;
    }
	
	public static boolean testInterSection(Entity c0,Vector3f v0,Entity c1,Vector3f v1,float tMax){
		Vector3f v=new Vector3f();
		Vector3f.sub(v1, v0, v);
		tfirst=0;
		tlast=100000000;
		// test edges of C0 for separation
		for (int i=0;i<c0.getNormalNumber();i++){
            Vector3f D=c0.getNormal(i);
            float[] r0,r1;
            r0=computeInterval(c0,D);
            float min0=r0[0],max0=r0[1];
            r1=computeInterval(c1,D);
            float min1=r1[0],max1=r1[1];
            float speed=Vector3f.dot(D, v);
            if (NoIntersect(tMax,speed,min0,max0,min1,max1)) return false;
        }
        for (int i=0;i<c1.getNormalNumber();i++){
            Vector3f D=c1.getNormal(i);
            float[] r0,r1;
            r0=computeInterval(c0,D);
            float min0=r0[0],max0=r0[1];
            r1=computeInterval(c1,D);
            float min1=r1[0],max1=r1[1];
            float speed=Vector3f.dot(D, v);
            if (NoIntersect(tMax,speed,min0,max0,min1,max1)) return false;
        }	
        return true;
	}
	
	private static boolean NoIntersect(float tmax, float speed,float min0,
			float max0, float min1, float max1){
		float t;
		if (max1 < min0)
		{
			if (speed <= 0) 
			{
				return true;
			}
			t = (min0 - max1)/speed;
			if (t > tfirst)
			{
				tfirst = t;
			}
			if (tfirst > tmax)
			{
				return true;
			}
			t = (max0 - min1)/speed;
			if (t < tlast)
			{
				tlast = t;
			}
			if (tfirst > tlast)
			{
				return true;
			}
		}
		else if ( max0 < min1 )
		{
			// interval(C1) initially on ‘right’ of interval(C0)
			if (speed >= 0) // intervals moving apart
			{
				return true;
			}
			t = (max0 - min1)/speed;
			if (t > tfirst)
			{
				tfirst = t;
			}
			if (tfirst > tmax)
			{
				return true;
			}
			t = (min0 - max1)/speed;
			if (t < tlast)
			{
				tlast = t;
			}
			if (tfirst > tlast)
			{
				return true;
			}
		}
		else
		{
			// interval(C0) and interval(C1) overlap
			if (speed > 0)
			{
				t = (max0 - min1)/speed;
				if (t < tlast)
				{
					tlast = t;
				}
				if (tfirst > tlast)
				{
					return true;
				}
			}
			else if (speed < 0)
			{
				t = (min0 - max1)/speed;
				if (t < tlast)
				{
					tlast = t;
				}
				if (tfirst > tlast)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	

}
