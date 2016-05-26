package defeatedcrow.showcase.plugin;

import cpw.mods.fml.common.registry.GameRegistry;
import defeatedcrow.showcase.common.SCLogger;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shift.mceconomy2.api.MCEconomyAPI;
import shift.mceconomy2.api.shop.IProductItem;
import shift.mceconomy2.api.shop.ProductItem;
import shift.mceconomy2.api.shop.ProductList;

import java.util.ArrayList;

/**
 * Created by Belgabor on 24.05.2016.
 */
abstract public class BaseShopList extends ProductList {

    abstract public void load();

    public void registerProducts(String[] itemList) {
        for (String name : itemList) {
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
                    SCLogger.logger.info(String.format("Failed to add item to %s: %s", this.getProductListName(), name));
                }

                if (input != null) {
                    this.addItemProduct(new ProductItem(input, price));
                    SCLogger.logger.info(String.format("Registered new item for %s: %s : %d", this.getProductListName(), name, price ));
                } else {
                    SCLogger.logger.info(String.format("Failed new item for %s: %s : Item unavailable", this.getProductListName(), name ));
                }
            }
        }
    }

}
