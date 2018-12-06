/**
 * 
 */
package randerEngine;



import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * Created by Mahathir
 */
public class DisplayManager {
	private static final int WIDTH=1000;
	private static final int HEIGHT=700;
	private static final int FPS_CAP=60;
	
	private static long lastFrameTime=0;
	private static float delta;
	
	
	public static void  createDisplay(){
		
		ContextAttribs attribs=new ContextAttribs(3,2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			//Display.setFullscreen(true);
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setLocation(0, 0);
			
			Display.setTitle("OpenGL Learning");		
			
			Display.create(new PixelFormat(), attribs);
			Display.setFullscreen(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		//GL11.glViewport(0, 0, WIDTH, HEIGHT);
	
	}
	public static float getFrameTimeSeconds(){
		return delta;
	}
	
	public static void updateDisplay(){
		long currentFrameTime=getCurrentTime();
		delta=(currentFrameTime-lastFrameTime)/1000f;
		lastFrameTime=currentFrameTime;
		double a=(int) Math.pow(delta, -1);
	//	Display.setTitle(String.valueOf(a));
		Display.sync(FPS_CAP);
		Display.update();
		
		
	}
	public static void setTitle(long last){
		Display.setTitle("Ping:"+(System.currentTimeMillis()-last)+" milli seconds"+"FPS :"+Math.pow(delta,-1));

	}
	public static void setTitle(String str){
		Display.setTitle(str);

	}
	public static void closeDisplay(){
		Display.destroy();
	}
	private static long getCurrentTime(){
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}

}
