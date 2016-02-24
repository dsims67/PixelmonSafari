package safari;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy implements IGuiHandler {
	public void registerRenderers() {
	}

	public World GetClientWorld() {
		return null;
	}



	public int addArmor(String name){
		return RenderingRegistry.addNewArmourRendererPrefix(name);
	}
	
	public void registerPacketHandlers() {
	}

	public void registerKeyBindings() {
	}

	public ModelBase loadModel(String name) {
		return null;
	}
	
	public ModelBase loadFlyingModel(String name) {
		return null;
	}


	public void registerSounds() {
	}

	public void registerTickHandlers() {
		 TickRegistry.registerTickHandler(new TickHandler(), Side.SERVER);
	}

	public void loadEvents() {
	}

	
	public void registerBossDropItem(Item item){
		//DropItemHelper.bossDropItems.add(item);

	}
	
	public void registerInteractions(){
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}
}
