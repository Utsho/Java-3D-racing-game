package sounds;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.alBufferData;

import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alSourcei;

import java.io.BufferedInputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import org.lwjgl.util.vector.Vector3f;

import entities.Player;

public class Sound extends Thread{
	private int soundSource;
	private Vector3f position;
	private int buffer;
	Thread t;
	String fileName;
	public Sound(String fileName,Vector3f position){
		buffer = AL10.alGenBuffers();
		this.position=position;
		this.fileName=fileName;
		this.t=this;
		t.start();
	}
	
	public void playSound(Player player){
		AL10.alListener3f(AL10.AL_POSITION,player.getPosition().x, player.getPosition().y, player.getPosition().z);
		AL10.alSourcePause(this.soundSource);
	}
	
	
	public void cleanUp(){
	AL10.alSourceStop(soundSource);
		AL10.alDeleteSources(soundSource);
		AL10.alDeleteBuffers(buffer);
	}

	@Override
	public void run() {
			WaveData data=null;
			try {
				data = WaveData.create(new BufferedInputStream(new FileInputStream("res/thump.wav")));
				System.out.println("data created0");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("data created101");
	        
	    	System.out.println("data created1");
	        alBufferData(buffer, data.format, data.data, data.samplerate);
	        data.dispose();
	    	System.out.println("data created2");
	        int source = alGenSources();
	        alSourcei(source, AL_BUFFER, buffer);
	    	System.out.println("data created3");
	        AL10.alSource3f(source, AL10.AL_POSITION, position.x,position.y, position.z);
	    	System.out.println("data created4");
	        this.soundSource=source;
		
	}

}
