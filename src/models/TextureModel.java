/**
 * 
 */
package models;

import textures.ModelTexture;

/**
 * Created by Mahathir on 03-Dec-15.
 */
public class TextureModel {
	RawModel rawModel;
	ModelTexture modelTexture;
	
	public TextureModel(RawModel model,ModelTexture texture){
		this.rawModel=model;
		this.modelTexture=texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getModelTexture() {
		return modelTexture;
	}
}
