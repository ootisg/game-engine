package engine;

public class TileData {
	//Container class for tile metadata
	private String name;
	private boolean isSolid;
	public TileData (String name, boolean isSolid) {
		//Name is in the format [tileset name].[position in tileset]
		this.name = name;
		this.isSolid = isSolid;
	}
	public String getName () {
		return name;
	}
	public boolean isSolid () {
		return isSolid;
	}
}