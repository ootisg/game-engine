package engine;

public class MapTile {
	//Container class for map tiles
	public String tileId;
	public int x;
	public int y;
	public MapTile (String tileId, int x, int y) {
		//tileId is in the format [tile name]
		this.tileId = tileId;
		this.x = x;
		this.y = y;
	}
}