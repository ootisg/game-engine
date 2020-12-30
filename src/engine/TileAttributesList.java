package engine;

public class TileAttributesList {
	//A container class for a list of TileData objects
	private TileData[] list;
	public TileAttributesList (TileData[] list) {
		this.list = list;
	}
	public TileData[] getTiles () {
		return list;
	}
	public TileData getTile (String tileId) {
		for (int i = 0; i < list.length; i ++) {
			if (list [i].getName ().equals (tileId)) {
				return list [i];
			}
		}
		return null;
	}
}