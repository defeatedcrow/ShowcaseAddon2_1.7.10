package defeatedcrow.showcase.common;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

public class MessageUtil {

	private MessageUtil() {
	}

	/* オーナー情報 */
	public static ChatComponentText Owner(String owner) {
		String text = StatCollector.translateToLocal("dcshowcase.message.show_owner") + owner;
		if (ShowcaseCore.debugMode)
			SCLogger.debugInfo(text);
		return new ChatComponentText(text);
	}

	/* モード情報 */
	public static ChatComponentText mode(String mode) {
		String text = StatCollector.translateToLocal("dcshowcase.message.show_mode") + mode;
		if (ShowcaseCore.debugMode)
			SCLogger.debugInfo(text);
		return new ChatComponentText(text);
	}

	/* モードを変更しました */
	public static ChatComponentText modeChange(String mode) {
		String text = StatCollector.translateToLocal("dcshowcase.message.change_mode") + mode;
		if (ShowcaseCore.debugMode)
			SCLogger.debugInfo(text);
		return new ChatComponentText(text);
	}

	/* 販売アイテムを登録しました */
	public static ChatComponentText ItemRegister(ItemStack item) {
		String s = StatCollector.translateToLocal("dcshowcase.message.register_null");
		if (item == null) {
			s = StatCollector.translateToLocal("dcshowcase.message.register_null");
		} else {
			int mp = MPChecker.getSellMP(item);
			if (mp < 0) {
				s = StatCollector.translateToLocal("dcshowcase.message.register_notforsale");
			} else {
				s = StatCollector.translateToLocal("dcshowcase.message.register_item") + item.getDisplayName() + "x"
						+ item.stackSize + " : " + mp + "MP";
			}
		}
		if (ShowcaseCore.debugMode)
			SCLogger.debugInfo(s);
		return new ChatComponentText(s);
	}

	/* アイテム販売を取り下げました */
	public static ChatComponentText ItemCancel(ItemStack item) {
		String text = StatCollector.translateToLocal("dcshowcase.message.cancel_item") + item.getDisplayName();
		if (ShowcaseCore.debugMode)
			SCLogger.debugInfo(text);
		return new ChatComponentText(text);
	}

	/* 販売中アイテムはありません */
	public static ChatComponentText noItem() {
		String text = StatCollector.translateToLocal("dcshowcase.message.current_null");
		if (ShowcaseCore.debugMode)
			SCLogger.debugInfo(text);
		return new ChatComponentText(text);
	}

	/* 販売中アイテム */
	public static ChatComponentText currentItem(ItemStack item, String owner) {
		int mp = MPChecker.getSellMP(item);
		String text =

		StatCollector.translateToLocal("dcshowcase.message.current_item") + item.getDisplayName() + "x"
				+ item.stackSize + " : " + mp + "MP";
		if (ShowcaseCore.debugMode)
			SCLogger.debugInfo(text);
		return new ChatComponentText(text);
	}

	/* アイテムを購入しました */
	public static ChatComponentText boughtItem(ItemStack item, String owner) {
		int mp = MPChecker.getSellMP(item);
		String text = StatCollector.translateToLocal("dcshowcase.message.bought_item") + item.getDisplayName() + "x"
				+ item.stackSize + " : " + mp + "MP";
		if (ShowcaseCore.debugMode)
			SCLogger.debugInfo(text);
		return new ChatComponentText(text);
	}

	public static ChatComponentText boughtYourItem(ItemStack item, String buyer) {
		int mp = MPChecker.getSellMP(item);
		String text = buyer + StatCollector.translateToLocal("dcshowcase.message.bought_youritem")
				+ item.getDisplayName() + "x" + item.stackSize + " : " + mp + "MP";
		if (ShowcaseCore.debugMode)
			SCLogger.debugInfo(text);
		return new ChatComponentText(text);
	}

	public static ChatComponentText boughtByVillager(ItemStack item) {
		int mp = MPChecker.getSellMP(item);
		String text = StatCollector.translateToLocal("dcshowcase.message.bought_villager") + item.getDisplayName()
				+ "x" + item.stackSize + " : " + mp + "MP";
		if (ShowcaseCore.debugMode)
			SCLogger.debugInfo(text);
		return new ChatComponentText(text);
	}

	/* MPが不足しています */
	public static ChatComponentText boughtCancel() {
		String text = StatCollector.translateToLocal("dcshowcase.message.bought_cancel");
		if (ShowcaseCore.debugMode)
			SCLogger.debugInfo(text);
		return new ChatComponentText(text);
	}

	/* MPを取り出しました */
	public static ChatComponentText loadMP(int mp) {
		String text = StatCollector.translateToLocal("dcshowcase.message.load_mp") + mp + "MP";
		if (ShowcaseCore.debugMode)
			SCLogger.debugInfo(text);
		return new ChatComponentText(text);
	}

	/* あなたの保有MP */
	public static ChatComponentText checkMP(int mp) {
		String text = StatCollector.translateToLocal("dcshowcase.message.check_mp") + mp + "MP";
		if (ShowcaseCore.debugMode)
			SCLogger.debugInfo(text);
		return new ChatComponentText(text);
	}

}
