package defeatedcrow.showcase.plugin;

import java.util.ArrayList;

import defeatedcrow.showcase.common.ShowcaseConfig;
import shift.mceconomy2.api.MCEconomyAPI;
import shift.mceconomy2.api.shop.IProductItem;

/**
 * 夏のショップ。
 * 海と砂漠のものを買うことができる。
 */
public class SummerShopList extends BaseShopList {
	private static ArrayList<IProductItem> thisProducts = new ArrayList<IProductItem>();
	public static int thisShopId = -1;

	@Override
	public void load() {
		thisShopId = MCEconomyAPI.registerProductList(this);
		ShowcaseConfig.registerProducts(this, ShowcaseConfig.summerShop);
	}

	@Override
	public String getProductListName() {

		return "Summer Shop";
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
