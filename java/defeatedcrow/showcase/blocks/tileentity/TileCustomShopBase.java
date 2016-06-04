package defeatedcrow.showcase.blocks.tileentity;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import defeatedcrow.showcase.blocks.BlockCustomShopBase;
import defeatedcrow.showcase.common.*;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import shift.sextiarysector.api.gearforce.tileentity.IGearForceHandler;

@Optional.InterfaceList({ @Optional.Interface(
		iface = "shift.sextiarysector.api.gearforce.tileentity.IGearForceHandler", modid = "SextiarySector") })
public class TileCustomShopBase extends TileEntity implements IGearForceHandler {

	protected Short currentGF = 0;
	protected Short cooltime = 0;
	protected String shop = "";
	protected int mode = 0;
	public final Short MAX_SPEED = 100;
	public final Short MAX_POWER = 1;
	public boolean active = false;

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.currentGF = par1NBTTagCompound.getShort("CurrentGF");
		this.cooltime = par1NBTTagCompound.getShort("CoolTime");
		this.active = par1NBTTagCompound.getBoolean("Active");
		this.shop = par1NBTTagCompound.getString("Shop");
		this.mode = par1NBTTagCompound.getInteger("Mode");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("CurrentGF", this.currentGF);
		par1NBTTagCompound.setShort("CoolTime", this.cooltime);
		par1NBTTagCompound.setBoolean("Active", this.active);
		par1NBTTagCompound.setString("Shop", this.shop);
		par1NBTTagCompound.setInteger("Mode", this.mode);
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

	public void setGF(short par1) {
		int ret = Math.min(par1, this.MAX_SPEED);
		this.currentGF = (short) ret;
	}

	public short getGF() {
		return this.currentGF;
	}

	public boolean hasEnergy() {
		return currentGF > 0;
	}

	public String getShop() {
		return shop;
	}

	public int getMode() {
		return mode;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getShopIcon() {
		CustomShopData shopData = CustomShopManager.get(shop.equals("")?CustomShopData.DEFAULT:shop);
		if ((shopData == null) || (shopData.item.screen == null) || (shopData.item.screen.failed)) {
			Block block = worldObj.getBlock(xCoord, yCoord, zCoord);
			return block.getIcon(0, 0);
		} else {
			return shopData.item.screen;
		}
	}

	@Override
	public void updateEntity() {
		if (currentGF > 0) {

			if (cooltime > 0) {
				currentGF--;
			} else {
				cooltime = 2;
			}

			if (currentGF > MAX_SPEED) {
				currentGF = MAX_SPEED;
			}
		} else {
			currentGF = 0;
		}

		if (!worldObj.isRemote) {
			this.onServerUpdate();
		}
	}

	private void onServerUpdate() {
		boolean flag = this.currentGF > 0;
		if (active != flag) {
			active = flag;
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	public boolean isActive() {
		CustomShopData shopData = CustomShopManager.get(shop.equals("")?CustomShopData.DEFAULT:shop);
		if (shopData == null)
			return false;
		return active || !(ShowcaseCore.SS2Loaded && ShowcaseConfig.requireGF);
	}

	/* plugin for SextiarySector2 */

	@Optional.Method(
			modid = "SextiarySector")
	@Override
	public int addEnergy(ForgeDirection from, int power, int speed, boolean simulate) {
		short eng = this.currentGF;
		int get = speed;
		if (eng >= MAX_SPEED)
			return 0;

		int ret = Math.min((MAX_SPEED - eng), get);

		if (!simulate) {
			this.setGF((short) (eng + ret));
		}
		return ret;
	}

	@Optional.Method(
			modid = "SextiarySector")
	@Override
	public int drawEnergy(ForgeDirection from, int power, int speed, boolean simulate) {
		return 0;
	}

	@Optional.Method(
			modid = "SextiarySector")
	@Override
	public boolean canInterface(ForgeDirection from) {
		return true;
	}

	@Optional.Method(
			modid = "SextiarySector")
	@Override
	public int getPowerStored(ForgeDirection from) {
		return MAX_POWER;
	}

	@Override
	public int getSpeedStored(ForgeDirection from) {
		return currentGF;
	}

	@Optional.Method(
			modid = "SextiarySector")
	@Override
	public int getMaxPowerStored(ForgeDirection from) {
		return MAX_POWER;
	}

	@Optional.Method(
			modid = "SextiarySector")
	@Override
	public int getMaxSpeedStored(ForgeDirection from) {
		return MAX_SPEED;
	}

}
