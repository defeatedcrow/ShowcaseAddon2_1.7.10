package defeatedcrow.showcase.common;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import shift.mceconomy2.api.MCEconomyAPI;
import cpw.mods.fml.common.registry.GameRegistry;

public class ShowcaseConfig {

	private final String BR = System.getProperty("line.separator");

	public static String[] mpList = { "DCsAppleMilk:defeatedcrow.clam:0:50" };

	public void load(Configuration cfg) {
		try {
			cfg.load();

			cfg.setCategoryComment("mp", "Add sell price to ItemStack." + BR
					+ "\"ModID:RegisteredName:metadata:price\"");

			Property mpListP = cfg.get("mp", "PriceRegister", mpList);

			mpList = mpListP.getStringList();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cfg.save();
		}
	}

	static void addMP() {
		for (String name : mpList) {
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
					SCLogger.logger.info("Failed to register price to new item: " + name);
				}

				if (input != null) {
					MCEconomyAPI.addPurchaseItem(input, price);
					SCLogger.logger.info("Registered new price to item: " + input.toString() + " : " + split[3]);
				} else {
					SCLogger.logger.info("Failed to register price to new item: null item");
				}
			}
		}
	}
}
