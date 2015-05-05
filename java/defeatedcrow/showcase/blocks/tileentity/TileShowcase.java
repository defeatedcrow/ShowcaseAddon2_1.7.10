package defeatedcrow.showcase.blocks.tileentity;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileShowcase extends TileEntity {

	protected int currentMPStorage = 0;
	protected String blockOwner = "none";
	protected String currentOwner = "none";
	protected String blockOwnerID = "";
	protected String currentOwnerID = "";
	protected int Mode = 0;

	// playerごとのMP
	protected HashMap<String, Integer> mpMap = new HashMap<String, Integer>();

	protected final InventoryShowcase inv;

	public TileShowcase() {
		inv = new InventoryShowcase(this);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);

		// アイテムの読み込み
		NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
		inv.items = new ItemStack[inv.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < inv.getSizeInventory()) {
				inv.items[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		NBTTagList taglist2 = par1NBTTagCompound.getTagList("MpMap", 10);
		for (int i = 0; i < taglist2.tagCount(); i++) {
			NBTTagCompound tag = taglist2.getCompoundTagAt(i);
			String key = tag.getString("UserName");
			int val = tag.getInteger("UserMp");
			if (key.length() > 1) {
				mpMap.put(key, val);
			}
		}

		this.currentMPStorage = par1NBTTagCompound.getInteger("Mp");
		this.blockOwner = par1NBTTagCompound.getString("BlockOwner");
		this.currentOwner = par1NBTTagCompound.getString("Owner");
		this.blockOwnerID = par1NBTTagCompound.getString("BlockOwnerID");
		this.currentOwnerID = par1NBTTagCompound.getString("CurrentOwnerID");
		this.Mode = par1NBTTagCompound.getByte("Mode");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);

		// 燃焼時間や調理時間などの書き込み
		par1NBTTagCompound.setInteger("Mp", this.currentMPStorage);
		par1NBTTagCompound.setString("BlockOwner", this.blockOwner);
		par1NBTTagCompound.setString("Owner", this.currentOwner);
		par1NBTTagCompound.setString("BlockOwnerID", this.blockOwnerID);
		par1NBTTagCompound.setString("CurrentOwnerID", this.currentOwnerID);
		par1NBTTagCompound.setByte("Mode", (byte) this.Mode);

		// アイテムの書き込み
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			if (inv.items[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				inv.items[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		par1NBTTagCompound.setTag("Items", nbttaglist);

		// MP保管リスト
		NBTTagList taglist2 = new NBTTagList();
		for (Entry ent : mpMap.entrySet()) {
			String n = (String) ent.getKey();
			int mp = (Integer) ent.getValue();

			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("UserName", n);
			tag.setInteger("UserMp", mp);
			taglist2.appendTag(tag);
		}
		par1NBTTagCompound.setTag("MpMap", taglist2);

	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		this.writeToNBT(nbtTagCompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.func_148857_g());
	}

	// getter, setter

	public ISidedInventory getInv() {
		return inv;
	}

	public int getMode() {
		return this.Mode;
	}

	public int getCurrentMP() {
		return this.currentMPStorage;
	}

	public String getCurrentOwner() {
		return this.currentOwner;
	}

	public String getBlockOwner() {
		return this.blockOwner;
	}

	public String getBlockOwnerUUID() {
		return this.blockOwnerID;
	}

	public String getCurrentOwnerUUID() {
		return this.currentOwnerID;
	}

	public HashMap<String, Integer> getMap() {
		HashMap<String, Integer> copy = new HashMap<String, Integer>();
		copy.putAll(mpMap);
		return copy;
	}

	public int drawMP(String name, boolean simulate) {
		Set<String> names = mpMap.keySet();
		if (names.isEmpty())
			return 0;

		int ret = 0;
		String removeName = "none";
		for (String n : names) {
			if (n.equals(name)) {
				ret = mpMap.get(n);
				removeName = n;
			}
		}

		if (!removeName.equals("none") && !simulate) {
			mpMap.put(removeName, 0);
			if (removeName.equals(currentOwner)) {
				currentMPStorage = 0;
			}
		}
		markDirty();
		return ret;
	}

	public void addMP(int get) {
		int next = this.currentMPStorage + get;
		if (next > 100000)
			next = 100000;
		this.currentMPStorage = next;
	}

	public void addUserMP(String name, int get) {
		mpMap.put(name, get);
	}

	public void setSellItem(ItemStack item, String name, String uuid) {
		this.currentOwner = name;
		this.currentOwnerID = uuid;
		inv.setInventorySlotContents(0, item);
		markDirty();
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	public boolean canInsertSellItem() {
		return inv.items[0] == null && inv.items[1] == null;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		inv.markDirty();
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (mpMap.containsKey(currentOwner)) {
			int get = mpMap.get(currentOwner);

			if (get != currentMPStorage) {
				mpMap.put(currentOwner, currentMPStorage);
				setCurrentMP(0);
			}
		} else {
			mpMap.put(currentOwner, currentMPStorage);
			setCurrentMP(0);
		}

		if (inv.items[0] != null && inv.items[1] == null) {
			if (inv.items[1] == null) {
				inv.items[1] = inv.items[0].copy();
				inv.items[0] = null;
				inv.markDirty();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}

		if (inv.items[1] == null && currentMPStorage == 0) {
			this.currentOwner = "none";
			this.currentOwnerID = "";
		}
	}

	public void setMode(int i) {
		this.Mode = i;
	}

	public void setCurrentMP(int mp) {
		this.currentMPStorage = mp;
	}

	public void setCurrentOwner(String name) {
		this.currentOwner = name;
	}

	public void setBlockOwner(String name) {
		this.blockOwner = name;
	}

	public void setBlockOwnerID(String id) {
		this.blockOwnerID = id;
	}

	public void setCurrentOwnerID(String id) {
		this.currentOwnerID = id;
	}

	public void setUserMp(String name, int mp) {
		mpMap.put(name, mp);
	}

	public int getMetadata() {
		return this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}

	public Block getLocation() {
		return this.worldObj.getBlock(xCoord, yCoord - 1, zCoord);
	}
}
