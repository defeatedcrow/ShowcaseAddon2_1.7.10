package defeatedcrow.showcase.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import defeatedcrow.showcase.blocks.ItemShowcase;

public class OnCraftingEvent {

	@SubscribeEvent
	public void onCraftingEvent(PlayerEvent.ItemCraftedEvent event) {
		EntityPlayer player = event.player;
		IInventory craftMatrix = event.craftMatrix;
		ItemStack crafting = event.crafting;

		if (crafting != null && player != null) {
			if (crafting.getItem() instanceof ItemShowcase) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("BlockOwner", player.getCommandSenderName());
				nbt.setString("BlockOwnerID", player.getUniqueID().toString());
				crafting.setTagCompound(nbt);
			}
		}
	}

}
