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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.io.File;
import java.util.HashMap;
import java.util.TreeSet;

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

        // Ensure files are sorted. listFiles only returns sorted file names on Windows
        TreeSet<String> sorted = new TreeSet<String>();
        for(File file : configFolder.listFiles()) {
            if (!file.getName().endsWith(".cfg"))
                continue;
            sorted.add(file.getName());
        }
        
        for(String filename : sorted) {
            CustomShopData shop = new CustomShopData(new File(configFolder, filename));
            shops.put(shop.tag, shop);
        }

        classicShop = new BlockClassicCustomShop().setBlockName("defeatedcrow.classicCustomShopBlock").setCreativeTab(
                CreativeTabs.tabDecorations);

        GameRegistry.registerBlock(classicShop, "defeatedcrow.classicCustomShopBlock");
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(classicShop, 1, 0), new Object[] { " X ", "XYX",
                "XZX", 'X', new ItemStack(Items.iron_ingot), 'Y', new ItemStack(Blocks.crafting_table), 'Z',
                new ItemStack(Blocks.redstone_torch) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(classicShop, 1, 0), new Object[] { " X ", "XYX",
                "XZX", 'X', "ingotIron", 'Y', new ItemStack(Blocks.crafting_table), 'Z', new ItemStack(Blocks.redstone_torch) }));
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
