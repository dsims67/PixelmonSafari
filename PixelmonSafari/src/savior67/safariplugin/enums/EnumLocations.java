package savior67.safariplugin.enums;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import savior67.safariplugin.SafariPlugin;

public enum EnumLocations
{
	Lobby("Lobby"),
	Capture("Capture"),
	Preparation("Preparation"),
	Battle1A("Battle1A"),
	Battle1B("Battle1B"),
	Battle2A("Battle2A"),
	Battle2B("Battle2B"),
	Battle3A("Battle3A"),
	Battle3B("Battle3B");
	
	private String name;
	//SafariPlugin plugin;
	
	private EnumLocations(String s) 
	{
		name = s;
	}
	
	public String getName() 
	{
		return name;
	}
	
	//Returns the length in minutes of the phase, from the config
	public Location getLocation() {
		String place = this.name;
		try {
			Double x = SafariPlugin.config.getDouble("Safari.Coordinates."+place+".X");
			Double y = SafariPlugin.config.getDouble("Safari.Coordinates."+place+".Y");
			Double z = SafariPlugin.config.getDouble("Safari.Coordinates."+place+".Z");
			return(new Location(Bukkit.getWorld("world"), x, y, z));
						
			
		} catch(Exception e) {
			return(SafariPlugin.lobbyCoords);
		}
	}

}
