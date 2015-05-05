package defeatedcrow.showcase.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import shift.mceconomy2.api.MCEconomyAPI;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import defeatedcrow.showcase.common.ShowcaseCore;

public class ItemMPCard extends Item {

	public ItemMPCard() {
		this.setMaxStackSize(1);
		this.setTextureName("showcase:MPCard");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, World world,
			EntityPlayer player) {
		NBTTagCompound nbt = item.getTagCompound();
		String owner = "none";
		String id = "";
		int mp = 0;

		if (nbt != null) {
			if (nbt.hasKey("Owner")) {
				owner = nbt.getString("Owner");
			}
			if (nbt.hasKey("OwnerID")) {
				id = nbt.getString("OwnerID");
			}
			if (nbt.hasKey("Mp")) {
				mp = nbt.getShort("Mp");
			}

			if (player.getUniqueID().toString().equals(id)) {
				int ret = MCEconomyAPI.addPlayerMP(player, mp, true);
				if (ret < mp) {
					int newMp = mp - ret;
					nbt.setInteger("Mp", ret);
					item.setTagCompound(nbt);
					MCEconomyAPI.addPlayerMP(player, ret, false);
					return item;
				} else {
					MCEconomyAPI.addPlayerMP(player, mp, false);
					return new ItemStack(Items.paper, 1, 0);
				}
			}
		}

		return super.onItemRightClick(item, world, player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	// マウスオーバー時の表示情報
	public void addInformation(ItemStack item, EntityPlayer player, List list,
			boolean par4) {
		super.addInformation(item, player, list, par4);
		NBTTagCompound nbt = item.getTagCompound();
		String owner = "none";
		String id = "";
		int mp = 0;

		if (nbt != null) {
			if (nbt.hasKey("Owner")) {
				owner = nbt.getString("Owner");
			}
			if (nbt.hasKey("OwnerID")) {
				id = nbt.getString("OwnerID");
			}
			if (nbt.hasKey("Mp")) {
				mp = nbt.getShort("Mp");
			}
		}

		if (ShowcaseCore.proxy.isShiftKeyDown()) {
			list.add(new String("Owner : " + owner));
			list.add(new String("MP : " + mp));
		} else {
			list.add(EnumChatFormatting.ITALIC + "LShift: Expand tooltip.");
		}
	}

}
