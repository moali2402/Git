package dev.vision.rave;

import android.graphics.Path;
import android.graphics.Region;

public class Share {

	public String name;
	public boolean drawn;
	Path path;
	Region region;
	public Share(String name){
		this.name = name;
		
	}

	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
}
