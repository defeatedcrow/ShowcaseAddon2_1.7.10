package defeatedcrow.showcase.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import defeatedcrow.showcase.blocks.BlockClassicCustomShop;
import defeatedcrow.showcase.blocks.BlockSeasonShop;
import defeatedcrow.showcase.blocks.BlockShowcase;
import defeatedcrow.showcase.blocks.ItemShowcase;
import defeatedcrow.showcase.client.CustomIcon;
import defeatedcrow.showcase.items.ItemMPCard;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Belgabor on 28.05.2016.
 */
public class CustomShopManager {
    public static File configFolder;
    public static Block classicShop;
    public static final HashMap<String, CustomShopData> shops = new HashMap<String, CustomShopData>();

    public static void init(FMLPreInitializationEvent event) {
        configFolder = new File(event.getModConfigurationDirectory(), "Showcase");

        if (!configFolder.exists())
            configFolder.mkdirs();

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            CustomIcon.init();
        LanguageManager.init();

        for(File file : configFolder.listFiles()) {
            if (!file.getName().endsWith(".cfg"))
                continue;

            CustomShopData shop = new CustomShopData(file);
            shops.put(shop.tag, shop);
        }

        classicShop = new BlockClassicCustomShop().setBlockName("defeatedcrow.classicCustomShopBlock").setCreativeTab(
                CreativeTabs.tabDecorations);

        GameRegistry.registerBlock(classicShop, "defeatedcrow.classicCustomShopBlock");
    }

    public static void load() {
        for (CustomShopData shop : shops.values()) {
            shop.load();
        }
    }

    public static CustomShopData get(String shopId) {
        return shops.get(shopId);
    }
}
