package defeatedcrow.showcase.plugin;

import java.util.ArrayList;

import defeatedcrow.showcase.common.ShowcaseConfig;
import shift.mceconomy2.api.MCEconomyAPI;
import shift.mceconomy2.api.shop.IProductItem;

/**
 * 春のショップ。
 * 沼地・ジャングルのものを買うことができる。
 */
public class SpringShopList extends BaseShopList {
	private static ArrayList<IProductItem> thisProducts = new ArrayList<IProductItem>();
	public static int thisShopId = -1;

	@Override
	public void load() {
		thisShopId = MCEconomyAPI.registerProductList(this);
		ShowcaseConfig.registerProducts(this, ShowcaseConfig.springShop);
	}

	@Override
	public String getProductListName() {

		return "Spring Shop";
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
