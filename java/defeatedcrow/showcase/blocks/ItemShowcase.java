package defeatedcrow.showcase.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import defeatedcrow.showcase.common.ShowcaseCore;

public class ItemShowcase extends ItemBlock {

	public ItemShowcase(Block block) {
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	// マウスオーバー時の表示情報
			public
			void addInformation(ItemStack item, EntityPlayer player, List list, boolean par4) {
		super.addInformation(item, player, list, par4);
		NBTTagCompound nbt = item.getTagCompound();
		NBTTagList items = new NBTTagList();
		NBTTagList mpMap = new NBTTagList();
		String owner = "none";
		String currentOwner = "none";
		int currentMp = 0;
		int mode = 0;

		if (nbt != null) {
			items = nbt.getTagList("Items", 10);
			mpMap = nbt.getTagList("MpMap", 10);

			if (nbt.hasKey("BlockOwner")) {
				owner = nbt.getString("BlockOwner");
			}
			if (nbt.hasKey("Owner")) {
				currentOwner = nbt.getString("Owner");
			}
			if (nbt.hasKey("Mp")) {
				currentMp = nbt.getShort("Mp");
			}
			if (nbt.hasKey("Mode")) {
				mode = nbt.getByte("Mode");
			}
		}

		ItemStack[] ret = new ItemStack[2];
		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound tag = items.getCompoundTagAt(i);
			byte b0 = tag.getByte("Slot");
			if (b0 >= 0 && b0 < ret.length) {
				ret[b0] = ItemStack.loadItemStackFromNBT(tag);
			}
		}

		list.add(new String("Block Owner : " + owner));

		if (ShowcaseCore.proxy.isShiftKeyDown()) {
			list.add(new String("Current Owner : " + currentOwner));
			list.add(new String("Current MP : " + currentMp));
			list.add(new String("Mode : " + mode));
			list.add(new String("Registration player : " + mpMap.tagCount()));
			for (int i = 0; i < ret.length; i++) {
				if (ret[i] != null)
					list.add(ret[i].getDisplayName() + "x" + ret[i].stackSize);
			}
			for (int i = 0; i < mpMap.tagCount(); i++) {
				NBTTagCompound tag = mpMap.getCompoundTagAt(i);
				String key = tag.getString("UserName");
				int val = tag.getInteger("UserMp");
				list.add(new String(" - " + key + " : " + val + "MP"));
			}
		} else {
			list.add(EnumChatFormatting.ITALIC + "LShift: Expand tooltip.");
		}
	}

}
