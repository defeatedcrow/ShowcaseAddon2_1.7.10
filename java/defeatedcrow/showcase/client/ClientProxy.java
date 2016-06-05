package defeatedcrow.showcase.client;

import defeatedcrow.showcase.common.ShowcaseConfig;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import defeatedcrow.showcase.blocks.tileentity.TileSeasonShop;
import defeatedcrow.showcase.blocks.tileentity.TileShowcase;
import defeatedcrow.showcase.common.CommonProxy;

public class ClientProxy extends CommonProxy {

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}

	@Override
	public void registerTileEntity() {
		ClientRegistry.registerTileEntity(TileShowcase.class, "tileShowcase", new RenderShowCase());
		ClientRegistry.registerTileEntity(TileSeasonShop.class, "tileSeasonShop", new RenderSeasonShop());
	}

	@Override
	public int getRenderID() {
		return RenderingRegistry.getNextAvailableRenderId();
	}

	@Override
	public void registerRenderers() {
		RenderingRegistry.registerBlockHandler(new RenderBlockDummy());
	}

	@Override
	public boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
	}

	@Override
	public boolean getOP(String name) {
		return  ShowcaseConfig.spIsOp;
	}

}
