package defeatedcrow.showcase.blocks;

import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import defeatedcrow.showcase.blocks.tileentity.TileShopBasket;

public class BlockShopBasket extends BlockShowcase {

	protected Random rand = new Random();

	private String[] modeName = new String[] { "Public", "Private", "Villager", "Display-only" };

	public BlockShopBasket() {
		super();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("showcase:shopbasket");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileShopBasket();
	}
}
