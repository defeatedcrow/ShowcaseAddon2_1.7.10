package defeatedcrow.showcase.common;

import defeatedcrow.showcase.common.ShowcaseConfig;
import defeatedcrow.showcase.plugin.BaseShopList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shift.mceconomy2.api.MCEconomyAPI;
import shift.mceconomy2.api.shop.IProduct;
import shift.mceconomy2.api.shop.IProductItem;
import shift.mceconomy2.api.shop.ProductBase;

import java.util.ArrayList;

/**
 * Created by Belgabor on 30.05.2016.
 */
public class CustomShopList implements IProductRegister {
    private final String[] rawProducts;
    public final String name;
    public final ArrayList<IProduct> products = new ArrayList<IProduct>();

    public CustomShopList(String name, String[] rawProducts) {
        this.name = name;
        this.rawProducts = rawProducts;
    }

    public void load() {
        load(rawProducts);
    }

    public void load(String[] list) {
        ShowcaseConfig.registerProducts(this, list);
    }

    public String[] save() {
        ArrayList<String> list = new ArrayList<String>();

        for (IProduct product : products) {
            ItemStack item = product.getItem(null, null, null);
            if (item.getItem() != null)
                list.add(String.format("%s:%d:%d", Item.itemRegistry.getNameForObject(item.getItem()), item.getItemDamage(), product.getCost(null, null, null)));
        }

        return list.toArray(new String[list.size()]);
    }

    public void addProduct(IProduct product) {
        products.add(product);
    }

    public void addProduct(ItemStack product, int price) {
        products.add(new ProductBase(product, price));
    }

    public void removeProduct(ItemStack product) {
        if (product == null)
            return;
        for (IProduct item : products) {
            if (item == null)
                continue;
            if (item.getItem(null, null, null) == null)
                continue;
            if (item.getItem(null, null, null).isItemEqual(product)) {
                products.remove(item);
                return;
            }
        }
    }

    @Override
    public void logProductRegisterSuccess(String logItem) {
        SCLogger.logger.info(String.format("Registered new item for %s: %s", name, logItem ));
    }

    @Override
    public void logProductRegisterFail(String logItem) {
        SCLogger.logger.info(String.format("Failed to add item to %s: %s", name, logItem));
    }

    @Override
    public void doProductRegister(Object item, int price) {
        if (item instanceof ItemStack)
            addProduct((ItemStack) item, price);
    }
}
