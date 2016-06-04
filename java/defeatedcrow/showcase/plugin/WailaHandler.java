package defeatedcrow.showcase.plugin;

import defeatedcrow.showcase.blocks.BlockCustomShopBase;
import defeatedcrow.showcase.blocks.tileentity.TileCustomShopBase;
import defeatedcrow.showcase.common.CustomShopData;
import defeatedcrow.showcase.common.CustomShopManager;
import defeatedcrow.showcase.common.SCLogger;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Belgabor on 02.06.2016.
 */
public class WailaHandler implements IWailaDataProvider {
    private static String OPTION_REPLACE_TITLE = "dcshowcase.replacetitle";
    
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileCustomShopBase) {
            TileCustomShopBase tile = (TileCustomShopBase) accessor.getTileEntity();
            String shop = tile.getShop();
            if (!shop.equals("")) {
                CustomShopData data = CustomShopManager.get(shop);
                if (data != null) {
                    if (config.getConfig(OPTION_REPLACE_TITLE))
                        currenttip.clear();
                    currenttip.add(StatCollector.translateToLocal(data.name));
                }
            }
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        return tag;
    }
    
    public static void callbackRegister(IWailaRegistrar registrar)
    {
        WailaHandler instance = new WailaHandler();
        
        registrar.addConfig("Showcase2", OPTION_REPLACE_TITLE, OPTION_REPLACE_TITLE+".waila", true);
        registrar.registerHeadProvider(instance, BlockCustomShopBase.class);
        SCLogger.logger.info("Registered WAILA handler");
    }
}
