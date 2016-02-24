package savior67.safariplugin.stages;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Initialize extends StageBase{
	
	public static void init() {
		String kickMsg = "Thanks for playing! Preparing world for the next round..";
		kickAllPlayers(kickMsg);
		clearPokemon();
	}
	
	//deletes all pokemon files
	private static void clearPokemon() {
		String path = "world/pokemon";
		File savesDir = new File(path);
		try {
			FileUtils.deleteDirectory(savesDir);
			FileUtils.forceMkdir(savesDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

}
