package defeatedcrow.showcase.common;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import shift.mceconomy2.api.MCEconomyAPI;
import cpw.mods.fml.common.registry.GameRegistry;

public class ShowcaseConfig implements IProductRegister {
	private static ShowcaseConfig instance;

	public static final String BR = System.getProperty("line.separator");

	public static String[] mpList = { "DCsAppleMilk:defeatedcrow.clam:0:50" };
	public static String[] springShop = {
			"minecraft:sapling:3:250",
			"minecraft:waterlily:0:100",
			"minecraft:vine:0:50",
			"minecraft:melon_seeds:0:200",
			"minecraft:dye:3:200",
			"DCsAppleMilk:defeatedcrow.saplingTea:0:50",
			"DCsAppleMilk:defeatedcrow.leafTea:3:200"
	};
	public static String[] summerShop = {
			"minecraft:sapling:4:250",
			"minecraft:cactus:0:100",
			"minecraft:reeds:0:50",
			"minecraft:dye:0:50",
			"minecraft:double_plant:0:100",
			"DCsAppleMilk:defeatedcrow.clam:0:50",
			"DCsAppleMilk:defeatedcrow.leafTea:1:500"
	};
	public static String[] autumnShop = {
			"minecraft:sapling:2:250",
			"minecraft:sapling:5:250",
			"minecraft:dirt:2:100",
			"minecraft:pumpkin_seeds:0:10",
			"minecraft:double_plant:4:100",
			"minecraft:double_plant:5:100",
			"DCsAppleMilk:defeatedcrow.leafTea:2:200"
	};
	public static String[] winterShop = {
			"minecraft:sapling:1:250",
			"minecraft:packed_ice:5:200",
			"minecraft:snow:1:100",
			"minecraft:mycelium:500",
			"DCsAppleMilk:defeatedcrow.leafTea:4:200"
	};
	public static boolean spIsOp = false;
	public static boolean requireGF = true;
	public static boolean customShops = false;

	public ShowcaseConfig() {
		instance = this;
	}

	public void load(Configuration cfg) {
		try {
			cfg.load();

			cfg.setCategoryComment("mp", "Add sell price to ItemStack." + BR
					+ "\"ModID:RegisteredName:metadata:price\"");

			Property mpListP = cfg.get("mp", "PriceRegister", mpList);

			mpList = mpListP.getStringList();

			spIsOp = cfg.getBoolean("spIsOP", "general", false, "Determines if the player in single player is always considered an OP. Only use for making maps.");
			requireGF = cfg.getBoolean("shopsRequireGF", "general", true, "Determines if shops need GF if SextiarySector2 is present.");
			customShops = cfg.getBoolean("customShops", "general", false, "Enables custom shops.");

			cfg.setCategoryComment("seasonshop", "Configure inventories for the seasonal shop." + BR
					+ "\"ModID:RegisteredName:metadata:price\"");

			Property springShopP = cfg.get("seasonshop", "spring", springShop);
			springShop = springShopP.getStringList();
			Property summerShopP = cfg.get("seasonshop", "summer", summerShop);
			summerShop = summerShopP.getStringList();
			Property autumnShopP = cfg.get("seasonshop", "autumn", autumnShop);
			autumnShop = autumnShopP.getStringList();
			Property winterShopP = cfg.get("seasonshop", "winter", winterShop);
			winterShop = winterShopP.getStringList();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cfg.hasChanged())
				cfg.save();
		}
	}

	static void addMP() {
		registerProducts(instance, mpList);
	}

	public static void registerProducts(IProductRegister register, String[] list) {
		for (String name : list) {
			if (name.contains(":")) {
				String[] split = name.split(":");
				ItemStack input = null;
				int price = 0;

				try {
					if (split.length == 4) {
						Item item = GameRegistry.findItem(split[0], split[1]);
						int m = Integer.parseInt(split[2]);
						price = Integer.parseInt(split[3]);
						if (item != null)
							input = new ItemStack(item, 1, m);
					}
				} catch (Exception e) {
					register.logProductRegisterFail(name);
				}

				if (input != null) {
					register.doProductRegister(input, price);
					register.logProductRegisterSuccess(input.toString() + " : " + split[3]);
				} else {
					register.logProductRegisterFail("null item");
				}
			}
		}
	}

	@Override
	public void logProductRegisterSuccess(String logItem) {
		SCLogger.logger.info("Registered new price to item: " + logItem);
	}

	@Override
	public void logProductRegisterFail(String logItem) {
		SCLogger.logger.info("Failed to register price to new item: " + logItem);
	}

	@Override
	public void doProductRegister(Object item, int price) {
		if (item instanceof String)
			MCEconomyAPI.addPurchaseItem((String) item, price);
		else if (item instanceof ItemStack)
			MCEconomyAPI.addPurchaseItem((ItemStack) item, price);
	}
}
