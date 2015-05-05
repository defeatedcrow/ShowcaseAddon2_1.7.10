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
 * 秋のショップ。
 * 森のものを買うことができる。
 */
public class AutumnShopList extends ProductList {

	private static ArrayList<IProductItem> thisProducts = new ArrayList<IProductItem>();
	public static int thisShopId = -1;

	public void load() {
		thisShopId = MCEconomyAPI.registerProductList(this);
		registerProducts();
	}

	@Override
	public String getProductListName() {

		return "Autumn Shop";
	}

	static void registerProducts() {
		thisProducts.add(new ProductItem(new ItemStack(Blocks.sapling, 1, 2), 250));
		thisProducts.add(new ProductItem(new ItemStack(Blocks.sapling, 1, 5), 250));
		thisProducts.add(new ProductItem(new ItemStack(Blocks.dirt, 1, 2), 100));
		thisProducts.add(new ProductItem(new ItemStack(Items.pumpkin_seeds, 1, 0), 10));
		thisProducts.add(new ProductItem(new ItemStack(Blocks.tallgrass, 1, 4), 100));
		thisProducts.add(new ProductItem(new ItemStack(Blocks.tallgrass, 1, 5), 100));
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
