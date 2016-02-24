package safari;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Date;
import java.util.List;

import pixelmon.battles.BattleRegistry;
import pixelmon.battles.controller.BattleController;
//import savior67.safariplugin.SafariPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements ITickHandler {
	
	private void onPlayerTick(EntityPlayer player) 
	{
		Date timeLeft = new Date();
		String currentStage = "initialize";
		try
		{
			Object EnumStages = Safari.getSafariPlugin().getClass().getField("currentStage").get(Safari.getSafariPlugin());
			currentStage = (String) EnumStages.getClass().getMethod("getName").invoke(EnumStages);
			timeLeft = ((Date) Safari.getSafariPlugin().getClass().getMethod("getRemainingTimeInStage").invoke(null));
		}
		catch (Exception e)
		{
			FMLLog.severe("[SafariMod] %s error getting time on server tick", e.getMessage());
		}
		int minsLeft = timeLeft.getMinutes();
		int secsLeft = timeLeft.getSeconds();
		if(minsLeft == 0 && secsLeft <= 5) 
		{
			endBattles();
			
		}
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.PLAYER))){
			onPlayerTick((EntityPlayer)tickData[0]);
		}
		
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
          
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER, TickType.SERVER);
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static void endBattles() {
		ArrayList<BattleController> bc = BattleRegistry.getBattleList();
		if(bc.size() == 0) return;
		for(int i=0; i<bc.size(); i++) {
			try {
			bc.get(i).endBattle();
			} catch(Exception e) {
				FMLLog.severe("[SafariMod] %s error ending battle for player", e.getMessage());
			}

		}
	}

}
