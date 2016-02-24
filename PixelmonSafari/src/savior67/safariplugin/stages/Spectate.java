package savior67.safariplugin.stages;

import java.util.ArrayList;


//Only applies to players who aren't in the round/finish early/between rounds
public class Spectate extends StageBase{

	private static ArrayList<String> spectators = new ArrayList<String>();
	
	public static void setSpectator(String pName) {
		StageBase.addSpectator(pName);
	}
	
	public static int getTotalSpectators() {
		return spectators.size();
	}
	
	public static void addSpectatorToList(String name) {
		spectators.add(name);
	}
	
	public static void clearSpectators() {
		spectators.clear();
	}

}
