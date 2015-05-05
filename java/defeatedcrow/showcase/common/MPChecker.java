package defeatedcrow.showcase.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import shift.mceconomy2.api.MCEconomyAPI;

/**
 * MCE2のAPIから、価格などを確認するための中継クラス
 * */
public class MPChecker {

	private MPChecker() {
	}

	public static int getSellMP(ItemStack item) {
		if (item == null)
			return -1;
		int ret = -1;
		if (MCEconomyAPI.hasPurchase(item)) {
			ret = MCEconomyAPI.getPurchase(item);
			if (ret == 0)
				ret = 1;
			ret *= item.stackSize;
		}
		return ret;
	}

	public static int PlayerMP(EntityPlayer player) {
		return MCEconomyAPI.getPlayerMP(player);
	}

}
