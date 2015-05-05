package defeatedcrow.showcase.blocks.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class InventoryShowcase implements ISidedInventory {

	private final int[] SLOT_TOP = { 0 };
	private final int[] SLOT_ALT = { 1 };
	protected ItemStack[] items = new ItemStack[2];
	protected TileShowcase tile;

	public InventoryShowcase(TileShowcase showcase) {
		this.tile = showcase;
	}

	@Override
	public int getSizeInventory() {
		return this.items.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return this.items[i];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amo) {
		if (this.items[slot] != null) {
			ItemStack itemstack;

			if (this.items[slot].stackSize <= amo) {
				itemstack = this.items[slot];
				this.items[slot] = null;
				return itemstack;
			} else {
				itemstack = this.items[slot].splitStack(amo);

				if (this.items[slot].stackSize == 0) {
					this.items[slot] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.items[slot] != null) {
			ItemStack itemstack = this.items[slot];
			this.items[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack item) {
		if (slot > 1)
			slot = 0;// 存在しないスロットに入れようとすると強制的に材料スロットに変更される。

		this.items[slot] = item;

		if (item != null && item.stackSize > this.getInventoryStackLimit()) {
			item.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return "ShowcaseInv";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return tile.getWorldObj().getTileEntity(tile.xCoord, tile.yCoord, tile.zCoord) != this.tile ? false
				: par1EntityPlayer.getDistanceSq(tile.xCoord + 0.5D, tile.yCoord + 0.5D, tile.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
		return false;
	}

	// ホッパーにアイテムの受け渡しをする際の優先度
	@Override
	public int[] getAccessibleSlotsFromSide(int par1) {
		return SLOT_TOP;
	}

	// ホッパーからアイテムを入れられるかどうか
	@Override
	public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
		return false;
	}

	// 隣接するホッパーにアイテムを送れるかどうか
	@Override
	public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
		return false;
	}

}
