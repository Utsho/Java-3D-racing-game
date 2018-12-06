package gameOnline;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TextureModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import randerEngine.DisplayManager;
import randerEngine.Loader;
import randerEngine.MasterRenderer;
import terrains.NormaledTerrain;
import terrains.Terrain;
import textures.ModelTexture;
import textures.NormaledTerrainTexturePack;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolBoxs.Collision;
import toolBoxs.Maths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class MainGame {
	List<NormaledTerrain> terrains =new ArrayList<>();
	List<Entity> entities=new ArrayList<>();
	Player player;
	Camera camera;
	List<Entity> others=new ArrayList<>();
	Light light;
	Vector3f skycolor;
	MasterRenderer renderer;
	List<Entity> grassEntity=new ArrayList<>();
	List<Light> lights=new ArrayList<>();
	Loader loader;
	GuiRenderer guiRenderer;
	List<Vector3f> otherPos;
	List<Vector3f> otherRot;
	boolean isOK=false;



	public MainGame(Vector3f playerPos, Vector3f playerRot, List<Vector3f> otherPos, List<Vector3f> otherRot){
		DisplayManager.createDisplay();
		loader=new Loader();
		this.otherPos=otherPos;
		this.otherRot=otherRot;
		List<GuiTexture> guis=new ArrayList<GuiTexture>();
		GuiTexture guibg=new GuiTexture(loader.loadTexture("bg"),new Vector2f(0f,0.0f),new Vector2f(.5f,.1f));
		GuiTexture guipg=new GuiTexture(loader.loadTexture("pg"),new Vector2f(0f,0.0f),new Vector2f(0f,.1f));
		guis.add(guibg);
		guis.add(guipg);
		GuiRenderer guiRenderer=new GuiRenderer(loader);
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();

		List<Entity> grassEntity=new ArrayList<Entity>();


		ModelTexture treeTexture=new ModelTexture(loader.loadTexture("treeLeaf"));
		guipg.setScale(new Vector2f(.05f,.1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();
		ModelTexture houseTex=new ModelTexture(loader.loadTexture("wood house texture"));
		ModelTexture polytreeTexture=new ModelTexture(loader.loadTexture("lowpolytree"));
		ModelTexture fernTex=new ModelTexture(loader.loadTexture("grass model"));
		ModelTexture sunTex=new ModelTexture(loader.loadTexture("sun"));
		guipg.setScale(new Vector2f(.1f,.1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();
		sunTex.setShineDamper(100);
		sunTex.setReflectivity(100);
		ModelData houseData=OBJFileLoader.loadOBJ("wood house");
		RawModel houseRawModel=loader.LoadToVAO(houseData.getVertices(), houseData.getTextureCoords(), houseData.getNormals(), houseData.getIndices());
		ModelData polyTreeData=OBJFileLoader.loadOBJ("lowpolytree");
		RawModel polyTreeModel=loader.LoadToVAO(polyTreeData.getVertices(), polyTreeData.getTextureCoords(), polyTreeData.getNormals(), polyTreeData.getIndices());
		ModelData treeModelData=OBJFileLoader.loadOBJ("treeLeaf");
		RawModel treeModel=loader.LoadToVAO(treeModelData.getVertices(), treeModelData.getTextureCoords(), treeModelData.getNormals(), treeModelData.getIndices());
		ModelData fernData=OBJFileLoader.loadOBJ("grass model");
		RawModel fernModel=loader.LoadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
		TextureModel treeTexModel=new TextureModel(treeModel,treeTexture);
		TextureModel house=new TextureModel(houseRawModel,houseTex);
		TextureModel fernTexModel=new TextureModel(fernModel,fernTex);
		TextureModel polyTreeTexModel=new TextureModel(polyTreeModel,polytreeTexture);




		light=new Light(new Vector3f(0,1000,0),new Vector3f(0.95f,0.95f,0.95f ));
		Light redLight=new Light(new Vector3f(190,20,-300),new Vector3f(1f,1.0f,1.0f ),new Vector3f(1,.01f,.002f));
		Light blueLight=new Light(new Vector3f(250,20,-375),new Vector3f(0.0f,0.0f,1.0f ),new Vector3f(1,.01f,.002f));
		Light greenLight=new Light(new Vector3f(300,20,-300),new Vector3f(0.0f,1.0f,0.0f ),new Vector3f(1,.01f,.002f));
		lights.add(light);
		lights.add(redLight);
		lights.add(blueLight);
		lights.add(greenLight);

		guipg.setScale(new Vector2f(.2f,.1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();

		TerrainTexture backgroundTexture=new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture=new TerrainTexture(loader.loadTexture("pitch"));
		TerrainTexture gTexture=new TerrainTexture(loader.loadTexture("cross"));
		TerrainTexture bTexture=new TerrainTexture(loader.loadTexture("rood"));
		TerrainTexture rNorTexture=new TerrainTexture(loader.loadTexture("norpitch"));
		TerrainTexture gNorTexture=new TerrainTexture(loader.loadTexture("cross"));
		TerrainTexture bNorTexture=new TerrainTexture(loader.loadTexture("norGrass"));
		TerrainTexture bgNorTexture=new TerrainTexture(loader.loadTexture("norGrass"));
		TerrainTexture blendMap=new TerrainTexture(loader.loadTexture("rood map"));
		NormaledTerrainTexturePack texturePack=new NormaledTerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture,rNorTexture,gNorTexture,bNorTexture,bgNorTexture);


		for (int i=0;i<=1;i++){
			for (int j=0;j<=1;j++){
				NormaledTerrain terrain=new NormaledTerrain(i,j,loader,texturePack,blendMap,"plain height");
				terrains.add(terrain);
			}
		}


		guipg.setScale(new Vector2f(.3f,.1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();

		guipg.setScale(new Vector2f(.4f,.1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();

		guipg.setScale(new Vector2f(.5f,.1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();


		ModelTexture pTexture=new ModelTexture(loader.loadTexture("car texture"));
		ModelData pData=OBJFileLoader.loadOBJ("car final");
		RawModel pRawModel=loader.LoadToVAO(pData.getVertices(), pData.getTextureCoords(), pData.getNormals(), pData.getIndices());
		TextureModel pModel=new TextureModel(pRawModel,pTexture);
		Entity Eplayer=new Entity(pModel,playerPos,playerRot.x,playerRot.y,playerRot.z,5f,OBJFileLoader.loadOBJ("car collide"));
		player=new Player(Eplayer);	
		
		
		for (int i=0;i<otherPos.size();i++){
			Entity otherPlayer=new Entity(pModel,otherPos.get(i),otherRot.get(i).x,otherRot.get(i).y,otherRot.get(i).z,5f,OBJFileLoader.loadOBJ("car collide"));
			others.add(otherPlayer);
		}




		ModelTexture wallTexture=new ModelTexture(loader.loadTexture("grill wall"));
		ModelData wallData=OBJFileLoader.loadOBJ("grill wall");
		RawModel wallRawModel=loader.LoadToVAO(wallData.getVertices(), wallData.getTextureCoords(), wallData.getNormals(), wallData.getIndices());
		TextureModel wallModel=new TextureModel(wallRawModel,wallTexture);
		for (int i=-800;i<=450;i+=50){
			Entity wall=new Entity(wallModel,new Vector3f(i,1,-150),0,0,0,5,OBJFileLoader.loadOBJ("wall collide"));
			entities.add(wall);
			if (i==450) continue;
			wall=new Entity(wallModel,new Vector3f(i,1,-100),0,0,0,5,OBJFileLoader.loadOBJ("wall collide"));
			entities.add(wall);
		}
		for (int i=-800;i<400;i+=200){
			Entity houseEntity=new Entity(house,new Vector3f(i,-5,-220),0,0,0,5,OBJFileLoader.loadOBJ("wall collide"));
			entities.add(houseEntity);
			houseEntity=new Entity(house,new Vector3f(i,-5,-50),0,0,0,5,OBJFileLoader.loadOBJ("house collide"));
			entities.add(houseEntity);
		}

		for (int i=-125;i<450;i+=50){
			Entity wall=new Entity(wallModel,new Vector3f(475,1,i),0,90,0,5,OBJFileLoader.loadOBJ("wall collide"));
			entities.add(wall);
			if (i==-125) continue;
			wall=new Entity(wallModel,new Vector3f(425,1,i),0,90,0,5,OBJFileLoader.loadOBJ("wall collide"));
			entities.add(wall);
		}



		renderer=new MasterRenderer(loader);
		camera=new Camera(player);
		run();
		
	
	}
	
	private void setUpOthers(){
		for (int i=0;i<otherPos.size();i++){
			others.get(i).setPosition(otherPos.get(i));
			others.get(i).setRotX(otherRot.get(i).x);
			others.get(i).setRotY(otherRot.get(i).y);
			others.get(i).setRotZ(otherRot.get(i).z);
		}
	}
		
		
		
	public void run(){
		new InfoUpdater();
		try {
			Game.sendOK();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(!Display.isCloseRequested()){
			collision();
			otherPos=Game.getOtherPos();
			otherRot=Game.getOtherRot();
			if (!isOK) isOK=Game.isOK();
			setUpOthers();
			long ping=Game.getUpdateInfo();
            light.setPosition(new Vector3f(player.getPosition().x,500,player.getPosition().z));
			if (ping!=-1)DisplayManager.setTitle(ping);
			light.setColor(new Vector3f(.95f,.95f,.95f));
			skycolor=new Vector3f(0.544f,0.62f,.69f);
			//if (isOK)
				player.move(getTerrainIndex(player.getPosition().x,player.getPosition().z));
			camera.move();
			for (NormaledTerrain t:terrains){
				renderer.procesNormalTerrain(t);
			}
			for (Entity e:entities){
                if (Maths.getDistance(player,e)<300) renderer.processEntity(e);
			}
			for(Entity e:others){
				
				renderer.processEntity(e);
			}
			renderer.render(player,lights, camera,skycolor);
			DisplayManager.updateDisplay();
			
			
			
		}
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	NormaledTerrain getTerrainIndex(float x,float z){
		for (NormaledTerrain t:terrains){
			if (x<t.getX() && x>t.getX()-800 && z<t.getZ() && z>t.getZ()-800) return t;
		}
		return terrains.get(0);
	}
	private void collision(){
		player.getEntity().getModel().getModelTexture().setUsingFakeLighting(false);
		for (Entity entity:entities){
			entity.getModel().getModelTexture().setUsingFakeLighting(false);
			Vector3f vert=new Vector3f();
			Vector3f.sub(player.getPosition(), entity.getPosition(), vert);
			Vector3f speed =new Vector3f(player.getSpeedDirection().x*Math.abs(player.getCurrentRunSpeed()),0,
			player.getSpeedDirection().z*Math.abs(player.getCurrentRunSpeed()));
				if (Maths.getDistance(player,entity)<20){
				if (Collision.testInterSection(player.getEntity(),speed,entity,new Vector3f(),.1f)){
			//	if (Collision.testInterSection(player.getEntity(), entity)){
					player.setCurrentRunSpeed(-player.getCurrentRunSpeed());
					player.updatePosition();
				}
			}
			
		}
		for (Entity entity:others){
			entity.getModel().getModelTexture().setUsingFakeLighting(false);
			Vector3f vert=new Vector3f();
			Vector3f.sub(player.getPosition(), entity.getPosition(), vert);
			Vector3f speed =new Vector3f(player.getSpeedDirection().x*Math.abs(player.getCurrentRunSpeed()),0,
					player.getSpeedDirection().z*Math.abs(player.getCurrentRunSpeed()));
			if (Maths.getDistance(player,entity)<20){
			//	if (Collision.testInterSection(player.getEntity(),speed,entity,new Vector3f(),.1f)){
						if (Collision.testInterSection(player.getEntity(), entity)){
							player.setCurrentRunSpeed(-player.getCurrentRunSpeed());
							player.updatePosition();
				}
			}

		}
	}
	
	
	
	
	
	class InfoUpdater implements Runnable{
		
		Thread t;
		InfoUpdater(){
			t=new Thread(this);
			t.start();
		}

		@Override
		public void run() {
			while(true){
				Game.setPlayer(player);
				try {
					t.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
}