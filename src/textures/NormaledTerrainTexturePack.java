package textures;

/**
 * Created by Mahathir on 05-Nov-15.
 */
public class NormaledTerrainTexturePack {
    private TerrainTexture backgroundTexture;
    private TerrainTexture rTexture;
    private TerrainTexture gTexture;
    private TerrainTexture bTexture;
    private TerrainTexture rNormalTexture;
    private TerrainTexture gNormalTexture;
    private TerrainTexture bNormalTexture;
    private TerrainTexture bgNormalTexture;
    public NormaledTerrainTexturePack(TerrainTexture backgroundTexture,
                              TerrainTexture rTexture, TerrainTexture gTexture,
                              TerrainTexture bTexture,TerrainTexture rNormalTexture, TerrainTexture gNormalTexture,
                              TerrainTexture bNormalTexture,TerrainTexture bgNormalTexture) {
        this.backgroundTexture = backgroundTexture;
        this.rTexture = rTexture;
        this.gTexture = gTexture;
        this.bTexture = bTexture;
        this.rNormalTexture=rNormalTexture;
        this.gNormalTexture=gNormalTexture;
        this.bNormalTexture=bNormalTexture;
        this.bgNormalTexture=bgNormalTexture;
    }
    public TerrainTexture getBackgroundTexture() {
        return backgroundTexture;
    }
    public TerrainTexture getrTexture() {
        return rTexture;
    }
    public TerrainTexture getgTexture() {
        return gTexture;
    }
    public TerrainTexture getbTexture() {
        return bTexture;
    }

    public TerrainTexture getrNormalTexture() {
        return rNormalTexture;
    }

    public TerrainTexture getgNormalTexture() {
        return gNormalTexture;
    }

    public TerrainTexture getbNormalTexture() {
        return bNormalTexture;
    }

    public TerrainTexture getBgNormalTexture() {
        return bgNormalTexture;
    }
}
