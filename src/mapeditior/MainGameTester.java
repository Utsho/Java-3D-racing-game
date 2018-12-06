package mapeditior;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TextureModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import randerEngine.DisplayManager;
import randerEngine.Loader;
import randerEngine.MasterRenderer;
import terrains.NormaledTerrain;
import textures.ModelTexture;
import textures.NormaledTerrainTexturePack;
import textures.TerrainTexture;

import toolBoxs.MousePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Mahathir on 03-Dec-15.
 */
public class MainGameTester {
    private List<NormaledTerrain> terrains =new ArrayList<>();
    static private List<Entity> entities=new ArrayList<>();

    static private Player player;

    static private Entity test;
    private boolean lock;
    static Loader loader;
    Camera camera;
    MasterRenderer renderer;
    MousePicker picker;
    List<Light> lights;
    static HashMap<String,TextureModel> modelList=new HashMap<>();
    public MainGameTester() {
        DisplayManager.createDisplay();
        lights=new ArrayList<>();
        loader=new Loader();

        light=new Light(new Vector3f(0,1000,0),new Vector3f(0.95f,0.95f,0.95f ));
        lights.add(light);


        ModelTexture houseTex=new ModelTexture(loader.loadTexture("wood house texture"));
        ModelTexture polytreeTexture=new ModelTexture(loader.loadTexture("lowPolyTree"));
        ModelTexture fernTex=new ModelTexture(loader.loadTexture("grass"));
        ModelData houseData=OBJFileLoader.loadOBJ("wood house");
        RawModel houseRawModel=loader.LoadToVAO(houseData.getVertices(), houseData.getTextureCoords(), houseData.getNormals(), houseData.getIndices());
        ModelData polyTreeData=OBJFileLoader.loadOBJ("lowpolyTree");
        RawModel polyTreeModel=loader.LoadToVAO(polyTreeData.getVertices(), polyTreeData.getTextureCoords(), polyTreeData.getNormals(), polyTreeData.getIndices());
        ModelData fernData=OBJFileLoader.loadOBJ("grass model");
        RawModel fernModel=loader.LoadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
        TextureModel house=new TextureModel(houseRawModel,houseTex);
        TextureModel fernTexModel=new TextureModel(fernModel,fernTex);
        TextureModel polyTreeTexModel=new TextureModel(polyTreeModel,polytreeTexture);
        modelList.put("house",house);
        modelList.put("grass",fernTexModel);
        modelList.put("tree",polyTreeTexModel);




        lock=true;




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



        ModelTexture pTexture=new ModelTexture(loader.loadTexture("car texture"));
        ModelData pData=OBJFileLoader.loadOBJ("car final");
        RawModel pRawModel=loader.LoadToVAO(pData.getVertices(), pData.getTextureCoords(), pData.getNormals(), pData.getIndices());
        TextureModel pModel=new TextureModel(pRawModel,pTexture);
        Entity Eplayer=new Entity(pModel,new Vector3f(-400,1,-400),0,180,0,.25f,OBJFileLoader.loadOBJ("car collide"));
        player=new Player(Eplayer);



		                            		/*Player*/
        ModelTexture terrainTexture=new ModelTexture(loader.loadTexture("grass"));
        terrainTexture.setShineDamper(1.0f);




        renderer=new MasterRenderer(loader);



		/* Terrain Texture*/
        camera=new Camera(player);
        picker=new MousePicker(camera,renderer.getProjectionMatrix(),terrains);
        while(!Display.isCloseRequested()){

            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) lock=!lock;
            if (lock) DisplayManager.setTitle("Locked");
            else DisplayManager.setTitle("Unlocked");

            light.setColor(new Vector3f(1f,1f,1f));
            light.setPosition(new Vector3f(player.getPosition().x,500,player.getPosition().z));
            skycolor=new Vector3f(0.6f,.6f,.85f);
            player.move(getTerrainIndex(player.getPosition().x,player.getPosition().z));
            camera.move();
            picker.update();
            if (!lock && test!=null) test.setPosition(picker.getCurrentTerrainPoint());
            for (NormaledTerrain t:terrains){
                renderer.procesNormalTerrain(t);
            }

            for(Entity e:entities){
                renderer.processEntity(e);
            }

            renderer.render(player,lights, camera,skycolor);


            DisplayManager.updateDisplay();



        }
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();



    }

    private NormaledTerrain getTerrainIndex(float x,float z){
        for (NormaledTerrain t:terrains){
            if (x<t.getX() && x>t.getX()-800 && z<t.getZ() && z>t.getZ()-800) return t;
        }
        return terrains.get(0);
    }


    static Light light;
    private static Vector3f skycolor;

    static public int addEntity(TextureModel model,Vector3f position,float rx,float ry,float rz,float scale){
        Entity entity=new Entity(model,position,rx,ry,rz,scale,null);
        entities.add(entity);
        return entities.indexOf(entity);
    }
    static public void setTestEntity(int index){
        test=entities.get(index);
    }
    static public void setTestParam(float rx, float ry, float rz, float size){
        test.setScale(size);
        test.setRotX(rx);
        test.setRotY(ry);
        test.setRotZ(rz);
    }
    static public void removeEntity(int index){
        if (test==entities.get(index)) test=null;
        entities.remove(index);
    }

    static public Entity getEntity(int index){
        return entities.get(index);
    }

    static public void setCamera(Vector3f positon){
        player.getEntity().setPosition(positon);
    }

    static public List<Entity> getEntities() {
        return entities;
    }

    public static HashMap<String, TextureModel> getModelList() {
        return modelList;
    }
    public  static void nsetTest(){
        test=null;
    }
}
