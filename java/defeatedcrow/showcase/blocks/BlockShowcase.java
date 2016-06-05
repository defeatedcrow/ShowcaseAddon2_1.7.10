package defeatedcrow.showcase.blocks;

import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import shift.mceconomy2.api.MCEconomyAPI;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import defeatedcrow.showcase.blocks.tileentity.TileShowcase;
import defeatedcrow.showcase.common.MPChecker;
import defeatedcrow.showcase.common.MessageUtil;
import defeatedcrow.showcase.common.SCLogger;
import defeatedcrow.showcase.common.ShowcaseCore;
import defeatedcrow.showcase.common.TimeUtil;

public class BlockShowcase extends BlockContainer {

	protected Random rand = new Random();

	private String[] modeName = new String[] {
			"Public",
			"Private",
			"Villager",
			"Display-only",
			"Permanent Shop" };

	public BlockShowcase() {
		super(Material.clay);
		this.setHardness(0.2F);
		this.setResistance(6000.0F);
		this.setTickRandomly(true);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float fx,
			float fy, float fz) {
		if (world.isRemote) {
			return true;
		} else {
			TileEntity any = world.getTileEntity(x, y, z);
			ItemStack current = player.inventory.getCurrentItem();
			String name = player.getCommandSenderName();
			UUID id = player.getUniqueID();

			if (any instanceof TileShowcase) {
				TileShowcase tile = (TileShowcase) any;
				String blockOwner = tile.getBlockOwner();
				String currentOwner = tile.getCurrentOwner();
				String ownerUUID = tile.getBlockOwnerUUID();
				String currentUUID = tile.getCurrentOwnerUUID();
				int currentMp = tile.getCurrentMP();
				int mode = tile.getMode();

				ItemStack sellItem = tile.getInv().getStackInSlot(1);
				boolean isOwner = id.toString().equals(ownerUUID);
				boolean isOP = ShowcaseCore.proxy.getOP(name);
				boolean isItemOwner = id.toString().equals(currentUUID);

				/**
				 * Ownerの場合 <br>
				 * currentItem == null : 登録アイテム確認 / mode・オーナー情報確認 <br>
				 * sneak : 取り下げ <br>
				 * == 紙 : MPロード <br>
				 * == それ以外 : 中身がある　->　登録アイテム確認　/　中身がnull　->　販売アイテム投入
				 * Ownerでない場合 <br>
				 * currentItem == null : 登録アイテム確認 / mode・オーナー情報確認 <br>
				 * sneak : 購入<br>
				 * == 紙 : MPロード
				 * ServerOPの場合<br>
				 * == RStorch : mode変更 / 強制取り下げ<br>
				 */

				if (current == null) { // Empty
					if (player.isSneaking()) { // sneaking
						if (sellItem == null) { // no item
							player.addChatMessage(MessageUtil.noItem());

							/*
							 * On debug mode:
							 * Player can force change the owner of this block.
							 */
							if (ShowcaseCore.debugMode) {
								tile.setBlockOwner(name);
								tile.setBlockOwnerID(id.toString());
							}

							int holdMP = tile.drawMP(name, true);
							if (holdMP > 0) {
								player.addChatMessage(MessageUtil.checkMP(holdMP));
							}
						} else { // sell item
							if (isItemOwner) { // 取り下げ
								ItemStack ret = sellItem.copy();
								this.dropSellItem(world, player, ret);

								tile.getInv().setInventorySlotContents(1, (ItemStack) null);
								tile.markDirty();
								world.markBlockForUpdate(x, y, z);

								player.addChatMessage(MessageUtil.ItemCancel(ret));

							} else if (mode != 3) { // 購入
								int require = MPChecker.getSellMP(sellItem);
								int playerMP = MPChecker.PlayerMP(player);

								if (playerMP < require) {
									player.addChatMessage(MessageUtil.boughtCancel());
								} else {
									ItemStack ret = sellItem.copy();
									int get = MCEconomyAPI.reducePlayerMP(player, require, false);
									this.dropSellItem(world, player, ret);

									if (mode != 4) {
										tile.getInv().setInventorySlotContents(1, (ItemStack) null);
										tile.addMP(require);
									}

									player.addChatMessage(MessageUtil.boughtItem(sellItem, currentOwner));

									if (mode != 4) {
										EntityPlayer ownerPlayer = world.getPlayerEntityByName(currentOwner);
										if (ownerPlayer != null) {
											ownerPlayer.addChatMessage(MessageUtil.boughtYourItem(sellItem, name));
										}
									}

									tile.markDirty();
									world.markBlockForUpdate(x, y, z);
								}
							} else {
								player.addChatMessage(MessageUtil.Owner(blockOwner));
								player.addChatMessage(MessageUtil.currentItem(sellItem, currentOwner));
							}
						}
					} else { // standing
						if (sellItem == null) {
							player.addChatMessage(MessageUtil.Owner(blockOwner));
							player.addChatMessage(MessageUtil.mode(modeName[mode]));
							int holdMP = tile.drawMP(name, true);
							player.addChatMessage(MessageUtil.checkMP(holdMP));
						} else {
							player.addChatMessage(MessageUtil.Owner(blockOwner));
							player.addChatMessage(MessageUtil.currentItem(sellItem, currentOwner));
							int holdMP = tile.drawMP(name, true);
							player.addChatMessage(MessageUtil.checkMP(holdMP));
						}
					}
				} else if (current.getItem() == Items.paper) { // paper
					int ret = tile.drawMP(name, true);
					if (ret > 0) {
						ItemStack card = new ItemStack(ShowcaseCore.mpCard);
						NBTTagCompound nbt = new NBTTagCompound();
						nbt.setString("Owner", name);
						nbt.setString("OwnerID", id.toString());
						nbt.setInteger("Mp", ret);
						card.setTagCompound(nbt);

						if (!player.capabilities.isCreativeMode && (current.stackSize--) <= 0) {
							player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack) null);
						}

						this.dropSellItem(world, player, card);

						tile.drawMP(name, false);
						tile.markDirty();
						world.markBlockForUpdate(x, y, z);
						player.addChatMessage(MessageUtil.loadMP(ret));

					} else {
						player.addChatMessage(MessageUtil.boughtCancel());
					}
				} else if (current.getItem() == Items.book) { // OP_only
					if (isOP || ShowcaseCore.debugMode) {
						if (sellItem != null) { // OPによる強制取り下げ
							ItemStack ret = sellItem.copy();
							this.dropSellItem(world, player, ret);

							tile.getInv().setInventorySlotContents(1, (ItemStack) null);
							tile.markDirty();
							world.markBlockForUpdate(x, y, z);

							player.addChatMessage(MessageUtil.ItemCancel(ret));
						} else { // モード変更
							int next = mode + 1;
							if (next > 4)
								next = 0;
							tile.setMode(next);
							player.addChatMessage(MessageUtil.modeChange(modeName[next]));
							tile.markDirty();
						}
					} else if (isOwner) { // モード変更
						int next = mode + 1;
						if (next > 4)
							next = 0;
						tile.setMode(next);
						player.addChatMessage(MessageUtil.modeChange(modeName[next]));
						tile.markDirty();
					}
				} else { // sell
					if (tile.canInsertSellItem()) {
						ItemStack ret = current.copy();
						int price = MPChecker.getSellMP(ret);

						if (price > 0) {
							if (mode == 0 || isOwner) {
								tile.setSellItem(ret, name, id.toString());
								player.inventory.setInventorySlotContents(player.inventory.currentItem,
										(ItemStack) null);
								player.inventory.markDirty();
								player.addChatMessage(MessageUtil.ItemRegister(ret));
							}
						}
					} else {
						if (sellItem == null) {
							player.addChatMessage(MessageUtil.Owner(blockOwner));
							player.addChatMessage(MessageUtil.mode(modeName[mode]));
						} else {
							player.addChatMessage(MessageUtil.Owner(blockOwner));
							player.addChatMessage(MessageUtil.currentItem(sellItem, currentOwner));
						}
					}
				}
			}
		}
		return true;
	}

	private void dropSellItem(World world, EntityPlayer player, ItemStack item) {
		if (item != null && !world.isRemote) {
			double posX = player.posX;
			double posY = player.posY + 0.5D;
			double posZ = player.posZ;
			EntityItem drop = new EntityItem(world, posX, posY, posZ, item);
			world.spawnEntityInWorld(drop);
		}

	}

	/* villager mode */

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		super.updateTick(world, x, y, z, rand);

		if (rand.nextInt(3) == 0 && !world.isRemote) {
			TileEntity any = world.getTileEntity(x, y, z);
			if (any instanceof TileShowcase) {
				int time = TimeUtil.currentTime(world);
				SCLogger.debugInfo("now time : " + time);
				TileShowcase tile = (TileShowcase) any;
				ItemStack sellItem = tile.getInv().getStackInSlot(1);
				int mode = tile.getMode();
				String owner = tile.getCurrentOwner();

				Village village = world.villageCollectionObj.findNearestVillage(x, y, z, 30);
				boolean inVillage = village != null;
				SCLogger.debugInfo("village : " + inVillage);

				if (village != null && mode == 2 && sellItem != null && TimeUtil.isDayTime(world)) {
					SCLogger.debugInfo("in village");
					int get = MPChecker.getSellMP(sellItem);
					if (TimeUtil.isDayTime(world)) {
						tile.addMP(get);
						tile.getInv().setInventorySlotContents(1, (ItemStack) null);
						tile.markDirty();
						world.markBlockForUpdate(x, y, z);

						EntityPlayer currentOwner = world.getPlayerEntityByName(owner);
						if (currentOwner != null && currentOwner.isEntityAlive()) {
							currentOwner.addChatMessage(MessageUtil.boughtByVillager(sellItem));

							if (rand.nextInt(5) == 0) {
								int rep = village.getReputationForPlayer(owner);
								rep++;
								village.setReputationForPlayer(owner, rep);
							}
						}
					}
				}
			}
		}

	}

	/* 外見 */

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return ShowcaseCore.renderShowcase;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("showcase:showcaseblock");

	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileShowcase();
	}

	/* 設置動作 */

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase,
			ItemStack par6ItemStack) {
		int playerFacing = MathHelper.floor_double((par5EntityLivingBase.rotationYaw * 4F) / 360F + 0.5D) & 3;

		int facing = 0;
		if (playerFacing == 0) {
			facing = 0;
		}
		if (playerFacing == 1) {
			facing = 1;
		}
		if (playerFacing == 2) {
			facing = 2;
		}
		if (playerFacing == 3) {
			facing = 3;
		}

		// ItemStackのNBT
		NBTTagCompound nbt = par6ItemStack.getTagCompound();
		NBTTagList items = new NBTTagList();
		NBTTagList mpMap = new NBTTagList();
		String owner = "none";
		String currentOwner = "none";
		String ownerId = "";
		String currentId = "";
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
			if (nbt.hasKey("BlockOwnerID")) {
				ownerId = nbt.getString("BlockOwnerID");
			}
			if (nbt.hasKey("CurrentOwnerID")) {
				currentId = nbt.getString("OwnerID");
			}
			if (nbt.hasKey("Mp")) {
				currentMp = nbt.getShort("Mp");
			}
			if (nbt.hasKey("Mode")) {
				mode = nbt.getByte("Mode");
			}
		}

		if (owner.equals("none") && par5EntityLivingBase instanceof EntityPlayer) { // 初期化されていなかった場合
			owner = ((EntityPlayer) par5EntityLivingBase).getCommandSenderName();
			ownerId = ((EntityPlayer) par5EntityLivingBase).getUniqueID().toString();
		}

		TileEntity tile = par1World.getTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof TileShowcase) {
			TileShowcase showcase = (TileShowcase) tile;

			showcase.setCurrentOwner(currentOwner);
			showcase.setBlockOwner(owner);
			showcase.setCurrentMP(currentMp);
			showcase.setMode(mode);
			showcase.setBlockOwnerID(ownerId);
			showcase.setCurrentOwnerID(currentId);

			ItemStack[] ret = new ItemStack[showcase.getInv().getSizeInventory()];
			for (int i = 0; i < items.tagCount(); i++) {
				NBTTagCompound tag = items.getCompoundTagAt(i);
				byte b0 = tag.getByte("Slot");
				if (b0 >= 0 && b0 < showcase.getInv().getSizeInventory()) {
					showcase.getInv().setInventorySlotContents(b0, ItemStack.loadItemStackFromNBT(tag));
				}
			}

			for (int j = 0; j < mpMap.tagCount(); j++) {
				NBTTagCompound tag = mpMap.getCompoundTagAt(j);
				String user = tag.getString("UserName");
				int mp = tag.getInteger("UserMp");
				showcase.addUserMP(user, mp);
				SCLogger.debugInfo("User : " + user + ", MP : " + mp);
			}

			showcase.markDirty();
			par1World.markBlockForUpdate(par2, par3, par4);
		}

		par1World.setBlockMetadataWithNotify(par2, par3, par4, facing, 3);
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
		TileEntity tile = par1World.getTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof TileShowcase) {
			TileShowcase showcase = (TileShowcase) tile;
			ItemStack block = new ItemStack(this, 1, 0);
			NBTTagCompound nbt = new NBTTagCompound();

			float a = par1World.rand.nextFloat() * 0.8F + 0.1F;
			float a1 = par1World.rand.nextFloat() * 0.8F + 0.1F;
			float a2 = par1World.rand.nextFloat() * 0.8F + 0.1F;
			EntityItem drop = new EntityItem(par1World, par2 + a, par3 + a1, par4 + a2, block);

			nbt.setInteger("Mp", showcase.getCurrentMP());
			nbt.setString("BlockOwner", showcase.getBlockOwner());
			nbt.setString("Owner", showcase.getCurrentOwner());
			nbt.setString("BlockOwnerID", showcase.getBlockOwnerUUID());
			nbt.setString("CurrentOwnerID", showcase.getCurrentOwnerUUID());
			nbt.setByte("Mode", (byte) showcase.getMode());

			// アイテムの書き込み
			NBTTagList nbttaglist = new NBTTagList();

			for (int i = 0; i < showcase.getInv().getSizeInventory(); ++i) {
				if (showcase.getInv().getStackInSlot(i) != null) {
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setByte("Slot", (byte) i);
					showcase.getInv().getStackInSlot(i).writeToNBT(nbttagcompound1);
					nbttaglist.appendTag(nbttagcompound1);
				}
			}
			nbt.setTag("Items", nbttaglist);

			// MP保管リスト
			NBTTagList taglist2 = new NBTTagList();
			for (Entry ent : showcase.getMap().entrySet()) {
				String n = (String) ent.getKey();
				int mp = (Integer) ent.getValue();

				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("UserName", n);
				tag.setInteger("UserMp", mp);
				taglist2.appendTag(tag);
			}
			nbt.setTag("MpMap", taglist2);

			drop.getEntityItem().setTagCompound(nbt);

			float a3 = 0.05F;
			drop.motionX = (float) par1World.rand.nextGaussian() * a3;
			drop.motionY = (float) par1World.rand.nextGaussian() * a3 + 0.2F;
			drop.motionZ = (float) par1World.rand.nextGaussian() * a3;
			par1World.spawnEntityInWorld(drop);
		}
	}

	@Override
	public Item getItemDropped(int metadata, Random rand, int fortune) {
		return null;
	}

	@Override
	public int damageDropped(int par1) {
		return 0;
	}

}
