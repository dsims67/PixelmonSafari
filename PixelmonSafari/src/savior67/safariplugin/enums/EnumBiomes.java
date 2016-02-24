package savior67.safariplugin.enums;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import savior67.safariplugin.SafariPlugin;


public enum EnumBiomes {

	Plains("Plains", 1),
	Ocean("Ocean", 2),
	Desert("Desert", 3),
	Hills("Hills", 4),
	Swamp("Swamp", 5),
	Forest("Forest", 6),
	Jungle("Jungle", 7),
	Snow("Snow", 8);
	
	private String name;
	private int id;
	
	private EnumBiomes(String s, int identifier) 
	{
		name = s;
		id = identifier;
		
	}
	
	public String getName() {
		return name;
	}
	
	public int getID() {
		return id;
	}
	
	public Location getLocation() {
		String place = this.name;
		Location loc = null;
		try {
			Double x = SafariPlugin.config.getDouble("Safari.Coordinates."+place+".X");
			Double y = SafariPlugin.config.getDouble("Safari.Coordinates."+place+".Y");
			Double z = SafariPlugin.config.getDouble("Safari.Coordinates."+place+".Z");
			Float pitch = (float) SafariPlugin.config.getDouble("Safari.Coordinates."+place+".Pitch");
			Float yaw = (float) SafariPlugin.config.getDouble("Safari.Coordinates."+place+".Yaw");
			loc = new Location(Bukkit.getWorld("world"), x, y, z);
			loc.setPitch(pitch);
			loc.setYaw(yaw);
						
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return(loc);
	}
}
