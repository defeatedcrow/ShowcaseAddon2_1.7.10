package defeatedcrow.showcase.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import defeatedcrow.showcase.blocks.BlockSeasonShop;
import defeatedcrow.showcase.blocks.BlockShowcase;
import defeatedcrow.showcase.blocks.ItemShowcase;
import defeatedcrow.showcase.items.ItemMPCard;
import defeatedcrow.showcase.plugin.AutumnShopList;
import defeatedcrow.showcase.plugin.SpringShopList;
import defeatedcrow.showcase.plugin.SummerShopList;
import defeatedcrow.showcase.plugin.WinterShopList;

@Mod(
		modid = "DCsShowcase",
		name = "ShowcaseAddon2",
		version = "1.7.10_beta4",
		dependencies = "required-after:Forge@[10.13.2.1291,);required-after:mceconomy2;after:DCsAppleMilk;after:SextiarySector")
public class ShowcaseCore {

	@SidedProxy(
			clientSide = "defeatedcrow.showcase.client.ClientProxy",
			serverSide = "defeatedcrow.showcase.common.CommonProxy")
	public static CommonProxy proxy;

	@Instance("ShowcaseCore")
	public static ShowcaseCore instance;

	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static boolean debugMode = false;

	// showcase
	public static Block showcaseBlock;
	public static Block displayBasket;
	// shop
	public static Block seasonsShopBlock;

	// item
	public static Item mpCard;

	// renderNum
	public static int renderShowcase = -1;
	public static int renderBasket = -1;
	public static int renderSafe = -1;
	public static int renderShop = -1;

	public static boolean SS2Loaded = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		(new ShowcaseConfig()).load(cfg);

		if (ShowcaseConfig.customShops) {
			CustomShopManager.init(event);
		}

		registerMaterials();
		registerRecipes();

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// TileEntityの登録
		proxy.registerTileEntity();

		// レンダー登録
		renderShowcase = proxy.getRenderID();
		proxy.registerRenderers();

		FMLCommonHandler.instance().bus().register(new OnCraftingEvent());
		FMLInterModComms.sendMessage("Waila", "register", "defeatedcrow.showcase.plugin.WailaHandler.callbackRegister");
	}

	static void registerMaterials() {
		showcaseBlock = new BlockShowcase().setBlockName("defeatedcrow.showcaseBlock").setCreativeTab(
				CreativeTabs.tabDecorations);
		seasonsShopBlock = new BlockSeasonShop().setBlockName("defeatedcrow.seasonShopBlock").setCreativeTab(
				CreativeTabs.tabDecorations);
		mpCard = new ItemMPCard().setUnlocalizedName("defeatedcrow.mpCard").setCreativeTab(CreativeTabs.tabMisc);

		GameRegistry.registerBlock(showcaseBlock, ItemShowcase.class, "defeatedcrow.showcase");
		GameRegistry.registerBlock(seasonsShopBlock, "defeatedcrow.seasonShopBlock");
		GameRegistry.registerItem(mpCard, "defeatedcrow.mpCard");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		(new SpringShopList()).load();
		(new SummerShopList()).load();
		(new AutumnShopList()).load();
		(new WinterShopList()).load();

		if (Loader.isModLoaded("SextiarySector")) {
			this.SS2Loaded = true;
		}

		ShowcaseConfig.addMP();

		if (ShowcaseConfig.customShops) {
			CustomShopManager.load();
		}
	}

	static void registerRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(showcaseBlock, 1, 0), new Object[] { "XXX", "XZX",
				"YYY", 'X', new ItemStack(Blocks.glass), 'Y', new ItemStack(Items.iron_ingot), 'Z',
				new ItemStack(Blocks.carpet, 1, 32767) }));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(showcaseBlock, 1, 0),
				new Object[] { "XXX", "XZX", "YYY", 'X', new ItemStack(Blocks.glass), 'Y', "ingotIron", 'Z',
						new ItemStack(Blocks.carpet, 1, 32767) }));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(seasonsShopBlock, 1, 0), new Object[] { " X ", "XYX",
				"XZX", 'X', new ItemStack(Items.iron_ingot), 'Y', new ItemStack(Blocks.quartz_block), 'Z',
				new ItemStack(Blocks.redstone_torch) }));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(seasonsShopBlock, 1, 0), new Object[] { " X ", "XYX",
				"XZX", 'X', "ingotIron", 'Y', "blockQuartz", 'Z', new ItemStack(Blocks.redstone_torch) }));
	}

}
