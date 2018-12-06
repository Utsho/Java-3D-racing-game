/**
 * 
 */
package textures;

/**
 * @author Mahathir
 *
 */
public class ModelTexture {
	private int textureID;
	
	private float shineDamper=1;
	private float reflectivity=0;
	private boolean hasTransparency=false;
	private boolean usingFakeLighting=false;
	
	public ModelTexture(int id){
		this.textureID=id;
	}
	public boolean isHasTransparency() {
		return hasTransparency;
	}
	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}
	
	public boolean isUsingFakeLighting() {
		return usingFakeLighting;
	}
	public void setUsingFakeLighting(boolean usingFakeLighting) {
		this.usingFakeLighting = usingFakeLighting;
	}
	public int getId(){
		return this.textureID;
	}
	public float getShineDamper() {
		return shineDamper;
	}
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}
	public float getReflectivity() {
		return reflectivity;
	}
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	

}
