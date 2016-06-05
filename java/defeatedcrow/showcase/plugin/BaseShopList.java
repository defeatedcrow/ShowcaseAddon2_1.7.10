package defeatedcrow.showcase.plugin;

import cpw.mods.fml.common.registry.GameRegistry;
import defeatedcrow.showcase.common.IProductRegister;
import defeatedcrow.showcase.common.SCLogger;
import defeatedcrow.showcase.common.ShowcaseConfig;
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
abstract public class BaseShopList extends ProductList implements IProductRegister {

    abstract public void load();

    @Override
    public void logProductRegisterSuccess(String logItem) {
        SCLogger.logger.info(String.format("Registered new item for %s: %s", this.getProductListName(), logItem ));
    }

    @Override
    public void logProductRegisterFail(String logItem) {
        SCLogger.logger.info(String.format("Failed to add item to %s: %s", this.getProductListName(), logItem));
    }

    @Override
    public void doProductRegister(Object item, int price) {
        if (item instanceof ItemStack)
            this.addItemProduct(new ProductItem((ItemStack) item, price));
    }

}
