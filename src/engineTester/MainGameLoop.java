/**
 * 
 */
package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TextureModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import randerEngine.DisplayManager;
import randerEngine.Loader;
import randerEngine.MasterRenderer;
import randerEngine.XrayRender;
import terrains.NormaledTerrain;
import terrains.Terrain;
import textures.ModelTexture;
import textures.NormaledTerrainTexturePack;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolBoxs.Collision;
import toolBoxs.Maths;
import toolBoxs.MousePicker;


public class MainGameLoop {
	/**
	 * @param args
	 */


	static List<NormaledTerrain> terrains =new ArrayList<>();
	static List<Entity> entities=new ArrayList<Entity>();
	static Player player;
	public static void main(String[] args) {
		Random rand=new Random();
		
		DisplayManager.createDisplay();
		List<Light> lights=new ArrayList<Light>();
		
		Loader loader=new Loader();
		
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
		ModelTexture polytreeTexture=new ModelTexture(loader.loadTexture("lowPolyTree"));
		ModelTexture fernTex=new ModelTexture(loader.loadTexture("grass"));
		ModelTexture sunTex=new ModelTexture(loader.loadTexture("sun"));
		guipg.setScale(new Vector2f(.1f,.1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();
		sunTex.setShineDamper(100);
		sunTex.setReflectivity(100);
		ModelData houseData=OBJFileLoader.loadOBJ("wood house");
		RawModel houseRawModel=loader.LoadToVAO(houseData.getVertices(), houseData.getTextureCoords(), houseData.getNormals(), houseData.getIndices());
		ModelData polyTreeData=OBJFileLoader.loadOBJ("lowpolyTree");
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
		lights.add(light);
		
		guipg.setScale(new Vector2f(.2f,.1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();
		
		TerrainTexture backgroundTexture=new TerrainTexture(loader.loadTexture("sand"));
		TerrainTexture rTexture=new TerrainTexture(loader.loadTexture("pitch"));
		TerrainTexture gTexture=new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture bTexture=new TerrainTexture(loader.loadTexture("mud"));
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
		for (int i=0;i<20;i++){
			float x=rand.nextFloat()*1600-800;
			float z=rand.nextFloat()*1600-800;
			float y=getTerrainIndex(x,z).getTerrainHeight(x, z);
			Entity entity=new Entity(fernTexModel,new Vector3f(x,y,z),rand.nextFloat(),rand.nextFloat(),0,10f,fernData);
			grassEntity.add(entity);
		}
		
		guipg.setScale(new Vector2f(.3f,.1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();
		
		guipg.setScale(new Vector2f(.4f,.1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();
		for (int i=0;i<200;i++){
			float x=rand.nextFloat()*1600-800;
			float z=rand.nextFloat()*1600-800;
			
			float y=getTerrainIndex(x,z).getTerrainHeight(x, z);
			Entity entity=new Entity(polyTreeTexModel,new Vector3f(x,y,z),0,0,0,3,OBJFileLoader.loadOBJ("tree collide"));
			//entities.add(entity);
		}
		guipg.setScale(new Vector2f(.5f,.1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();
	
		
		ModelTexture pTexture=new ModelTexture(loader.loadTexture("car texture"));
		pTexture.setReflectivity(2);
		pTexture.setShineDamper(100);
		ModelData pData=OBJFileLoader.loadOBJ("car final");
		RawModel pRawModel=loader.LoadToVAO(pData.getVertices(), pData.getTextureCoords(), pData.getNormals(), pData.getIndices());
		TextureModel pModel=new TextureModel(pRawModel,pTexture);
		Entity Eplayer=new Entity(pModel,new Vector3f(-400,1,-400),0,180,0,5f,OBJFileLoader.loadOBJ("car collide"));
		player=new Player(Eplayer);


		ModelTexture wallTexture=new ModelTexture(loader.loadTexture("grill wall"));
		ModelData wallData=OBJFileLoader.loadOBJ("grill wall");
		RawModel wallRawModel=loader.LoadToVAO(wallData.getVertices(), wallData.getTextureCoords(), wallData.getNormals(), wallData.getIndices());
		TextureModel wallModel=new TextureModel(wallRawModel,wallTexture);
		for (int i=-800;i<=450;i+=50){
			Entity wall=new Entity(wallModel,new Vector3f(i,1,-150),0,0,0,5,OBJFileLoader.loadOBJ("wall collide"));
			entities.add(wall);
			if (i==450) continue;
			wall=new Entity(wallModel,new Vector3f(i,1,-100),0,0,0,5,OBJFileLoader.loadOBJ("wall collide"));
			//entities.add(wall);
		}
		for (int i=-800;i<400;i+=200){
			Entity houseEntity=new Entity(house,new Vector3f(i,-5,-220),0,0,0,5,OBJFileLoader.loadOBJ("wall collide"));
			entities.add(houseEntity);
			houseEntity=new Entity(house,new Vector3f(i,-5,-50),0,0,0,5,OBJFileLoader.loadOBJ("wall collide"));
			//entities.add(houseEntity);
		}
		for (int i=-800;i<400;i+=200){
			Entity entity=new Entity(polyTreeTexModel,new Vector3f(i+60,0,-200),rand.nextFloat(),rand.nextFloat(),0,rand.nextInt(3)+1,OBJFileLoader.loadOBJ("tree collide"));
			entities.add(entity);
			entity=new Entity(polyTreeTexModel,new Vector3f(i+60,0,-90),rand.nextFloat(),rand.nextFloat(),0,rand.nextInt(3)+1,OBJFileLoader.loadOBJ("tree collide"));
			//entities.add(entity);
		}
		for (int i=-125;i<450;i+=50){
			Entity wall=new Entity(wallModel,new Vector3f(475,1,i),0,90,0,5,OBJFileLoader.loadOBJ("wall collide"));
			entities.add(wall);
			if (i==-125) continue;
			wall=new Entity(wallModel,new Vector3f(425,1,i),0,90,0,5,OBJFileLoader.loadOBJ("wall collide"));
			//entities.add(wall);
		}
		Entity test=new Entity(polyTreeTexModel,new Vector3f(0,0,0),0,0,0,1,OBJFileLoader.loadOBJ("tree collide"));
		entities.add(test);

		                            		/*Player*/
		ModelTexture terrainTexture=new ModelTexture(loader.loadTexture("grass"));
		terrainTexture.setShineDamper(1.0f);
		
		
		
		
		MasterRenderer renderer=new MasterRenderer(loader);

		
		
		/* Terrain Texture*/
		boolean check=false;
		Camera camera=new Camera(player);
		MousePicker picker=new MousePicker(camera,renderer.getProjectionMatrix(),terrains);
		while(!Display.isCloseRequested()){
			//simpleCollision(entities,player);
			//setLightColor();
			collision();
			///System.out.println(player.getEntity().getPosition().x+" "+player.getEntity().getPosition().z);
			
			light.setColor(new Vector3f(1f,1f,1f));
			light.setPosition(new Vector3f(player.getPosition().x,500,player.getPosition().z));
			//skycolor=new Vector3f(0.544f,0.62f,.69f);
			skycolor=new Vector3f(0.6f,.6f,.85f);
			player.move(getTerrainIndex(player.getPosition().x,player.getPosition().z));
			camera.move();
			picker.update();
			//test.setPosition(picker.getCurrentTerrainPoint());
			for (NormaledTerrain t:terrains){
				renderer.procesNormalTerrain(t);
			}

			for(Entity e:entities){

			//	if (Maths.getDistance(player,e)<300)
				renderer.processEntity(e);
				
			}
			for(Entity e:grassEntity){
				
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
	
	static NormaledTerrain getTerrainIndex(float x,float z){
		for (NormaledTerrain t:terrains){
			if (x<t.getX() && x>t.getX()-800 && z<t.getZ() && z>t.getZ()-800) return t;
		}
		return terrains.get(0);
	}
	private static void collision(){
		player.getEntity().getModel().getModelTexture().setUsingFakeLighting(false);
		for (Entity entity:entities){
				if (Maths.getDistance(player,entity)<20){
			//	if (Collision.testInterSection(player.getEntity(),speed,entity,new Vector3f(),.01f)){
				if (Collision.testInterSection(player.getEntity(), entity)){
					/*Vector3f reaction=new Vector3f();
					Vector3f.sub(player.getPosition(),entity.getPosition(),reaction);
					reaction.normalise(reaction);
					float reactionValue=Math.abs(Vector3f.dot(player.getVelocity(),reaction));
					reaction.x*=reactionValue*100;
					reaction.y*=reactionValue*100;
					reaction.z*=reactionValue*100;
					System.out.println("Collision :"+reactionValue);
					player.addForces(reaction,300000000);*/
					player.setCurrentRunSpeed(-player.getCurrentRunSpeed());
					player.updatePosition();
				}
			}
			
		}
	}
	
	static Light light;
	private static Vector3f skycolor;

}


