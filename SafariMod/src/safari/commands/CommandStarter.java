package safari.commands;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import pixelmon.config.PixelmonEntityList;
import pixelmon.entities.pixelmon.EntityPixelmon;
import pixelmon.enums.EnumPokeballs;
import pixelmon.enums.EnumPokemon;
import pixelmon.storage.PixelmonStorage;
import pixelmon.storage.PlayerNotLoadedException;
import safari.Safari;

public class CommandStarter extends CommandBase {
	
	@Override
	public String getCommandName() {
		return "starter";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/starter <pokemon>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		
		
		
		if(args.length < 1) {
			sender.sendChatToPlayer(ChatMessageComponent.createFromText("Incorrect usage. " + getCommandUsage(sender)));
			return;
		}
		
		EntityPlayerMP player = getPlayer(sender, sender.getCommandSenderName());
		if (player == null) {
			sender.sendChatToPlayer(ChatMessageComponent.createFromText(sender.getCommandSenderName() + " does not exist."));
			return;
		}
		
		String name = args[0];
		if (EnumPokemon.hasPokemon(name)) {
			
			//CHECK FOR PERMISSIONS
			boolean hasPerm = false;
			try 
			{
				Safari.getSafariPlugin().getClass().getMethod("hasStarterPermission", new Class[] {String.class, String.class}).setAccessible(true);
				hasPerm = (Boolean) (Safari.getSafariPlugin().getClass().getMethod("hasStarterPermission", new Class[] {String.class, String.class}).invoke(Safari.getSafariPlugin(), new Object[] {sender.getCommandSenderName(), name.toLowerCase()}));
			}
			catch(Exception e) 
			{
				FMLLog.severe("%s Something went seriously wrong with the reflection in "
						+ "the processCommand method in CommandStarter.\n"
						+ "The Exception Message: %s\nThe Cause: %s",
						"[SafariMod]", e.getMessage(), e.getCause());
				e.printStackTrace();
			}
			
			if(!hasPerm) {
				return;
			}
			//END OF PERMISSIONS CHECK
			
			EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityByName(name, player.worldObj);
			pokemon.getLvl().setLevel(35);
			pokemon.caughtBall = EnumPokeballs.PokeBall;
			try {
				PixelmonStorage.PokeballManager.getPlayerStorage(player).replace(PixelmonStorage.PokeballManager.getPlayerStorage(player).getFirstAblePokemon(player.worldObj), pokemon);//changePokemonAndAssignID(0, pokemon.getEntityData());
			} catch (PlayerNotLoadedException e) {
				e.printStackTrace();
			}
			sender.sendChatToPlayer(ChatMessageComponent.createFromText("Your starter has been changed to "+EnumPokemon.get(name).name));
			
		}
		else {
			sender.sendChatToPlayer(ChatMessageComponent.createFromText(name + " is not in the game!"));
		}
		

	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		ArrayList<String> pokemon = new ArrayList<String>();
		if (args.length == 1)
			return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
		else if (args.length == 2) {
			for (EnumPokemon p : EnumPokemon.values())
				pokemon.add(p.name);
			return getListOfStringsMatchingLastWord(args, pokemon.toArray(new String[] {}));
		}
		return null;
	}
	
	

}
