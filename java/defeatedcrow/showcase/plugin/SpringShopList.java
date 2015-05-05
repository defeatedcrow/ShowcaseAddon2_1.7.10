package defeatedcrow.showcase.plugin;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import shift.mceconomy2.api.MCEconomyAPI;
import shift.mceconomy2.api.shop.IProductItem;
import shift.mceconomy2.api.shop.ProductItem;
import shift.mceconomy2.api.shop.ProductList;

/**
 * 春のショップ。
 * 沼地・ジャングルのものを買うことができる。
 */
public class SpringShopList extends ProductList {

	private static ArrayList<IProductItem> thisProducts = new ArrayList<IProductItem>();
	public static int thisShopId = -1;

	public void load() {
		thisShopId = MCEconomyAPI.registerProductList(this);
		registerProducts();
	}

	@Override
	public String getProductListName() {

		return "Spring Shop";
	}

	static void registerProducts() {
		thisProducts.add(new ProductItem(new ItemStack(Blocks.sapling, 1, 3), 250));
		thisProducts.add(new ProductItem(new ItemStack(Blocks.waterlily, 1, 0), 100));
		thisProducts.add(new ProductItem(new ItemStack(Blocks.vine, 1, 0), 50));
		thisProducts.add(new ProductItem(new ItemStack(Items.melon_seeds, 1, 0), 200));
		thisProducts.add(new ProductItem(new ItemStack(Items.dye, 1, 3), 200));
	}

	@Override
	public void addItemProduct(IProductItem item) {
		thisProducts.add(item);
	}

	@Override
	public int getItemProductSize() {
		return thisProducts.size();
	}

	@Override
	public ArrayList<IProductItem> getProductList() {
		return thisProducts;
	}
}
