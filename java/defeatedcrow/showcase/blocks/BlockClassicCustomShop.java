package defeatedcrow.showcase.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import defeatedcrow.showcase.blocks.tileentity.TileClassicCustomShop;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Belgabor on 31.05.2016.
 */
public class BlockClassicCustomShop extends BlockCustomShopBase {

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon("showcase:classiccustomshop");
        this.defaultScreen = par1IconRegister.registerIcon("showcase:default_display");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileClassicCustomShop();
    }


}
