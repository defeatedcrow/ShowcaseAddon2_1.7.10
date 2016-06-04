package defeatedcrow.showcase.common;

import defeatedcrow.showcase.blocks.tileentity.TileClassicCustomShop;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import defeatedcrow.showcase.blocks.tileentity.TileSeasonShop;
import defeatedcrow.showcase.blocks.tileentity.TileShowcase;

public class CommonProxy {

	public World getClientWorld() {

		return null;
	}

	public void registerTileEntity() {
		GameRegistry.registerTileEntity(TileShowcase.class, "tileShowcase");
		GameRegistry.registerTileEntity(TileSeasonShop.class, "tileSeasonShop");
		if (ShowcaseConfig.customShops)
			GameRegistry.registerTileEntity(TileClassicCustomShop.class, "tileClassicCustomShop");
	}

	public int getRenderID() {
		return RenderingRegistry.getNextAvailableRenderId();
	}

	public void registerRenderers() {

	}

	public boolean isShiftKeyDown() {
		return false;
	}

	public boolean getOP(String name) {
		if (!MinecraftServer.getServer().isSinglePlayer()) {
			String ops[] = MinecraftServer.getServer().getConfigurationManager().func_152603_m().func_152685_a();
			for (String op : ops) {
				if (op != null && op.equalsIgnoreCase(name))
					SCLogger.debugInfo("Server OP was detected. " + op);
				return true;
			}
		} else {
			return ShowcaseConfig.spIsOp;
		}
		return false;
	}

}
