package defeatedcrow.showcase.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import defeatedcrow.showcase.blocks.tileentity.TileCustomShopBase;
import defeatedcrow.showcase.common.*;
import defeatedcrow.showcase.items.ItemCustomShopCard;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import shift.mceconomy2.api.MCEconomyAPI;

public class BlockCustomShopBase extends BlockContainer {

	private String[] modeName = new String[] { "Normal", "Locked", "Edit" };
	@SideOnly(Side.CLIENT)
	protected IIcon defaultScreen;

	public BlockCustomShopBase() {
		super(Material.clay);
		this.setHardness(0.2F);
		this.setResistance(15.0F);
		this.setTickRandomly(true);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float fx,
			float fy, float fz) {
		if (world.isRemote) {
			return true;
		} else {
			TileEntity any = world.getTileEntity(x, y, z);
			if (any instanceof TileCustomShopBase) {
				TileCustomShopBase tile = (TileCustomShopBase) any;
				String shop = tile.getShop();
				int mode = tile.getMode();
				boolean isOP = ShowcaseCore.proxy.getOP(player.getCommandSenderName());
				ItemStack current = player.inventory.getCurrentItem();
				CustomShopData shopData = CustomShopManager.get(shop.equals("")?CustomShopData.DEFAULT:shop);

				if (isOP && (current != null) && (mode == 2)) {
					// TODO: Edit mode

				} else if (((current == null) && player.isSneaking()) || ((current != null) && (current.getItem() instanceof ItemCustomShopCard))) {
					if (mode == 0) {
						if (!shop.equals("")) {
							if (shopData != null) {
								dropCardItem(world, player, new ItemStack(shopData.item, 1));
							} else {
								player.addChatMessage(MessageUtil.customInvalid(shop, true));
							}
						}
						if (current == null) {
							tile.setShop("");
						} else {
							ItemCustomShopCard item = (ItemCustomShopCard) current.getItem();
							tile.setShop(item.tag);
							player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
							player.inventory.markDirty();
							player.addChatMessage(MessageUtil.customSet(item.tag));
						}
						tile.markDirty();
						world.markBlockForUpdate(x, y, z);
					} else {
						player.addChatMessage(MessageUtil.customLocked());
					}
				} else if (isOP && (current != null) && (current.getItem() == Items.book)) {
					System.out.println("Book");
					if (mode == 1) {
						tile.setMode(0);
					} else {
						tile.setMode(1);
					}
					player.addChatMessage(MessageUtil.modeChange(modeName[tile.getMode()]));
					tile.markDirty();
				} else if (isOP && (current != null) && (current.getItem() == Items.paper)) {
					System.out.println("Paper");
					player.addChatMessage(MessageUtil.shop(shop));
					player.addChatMessage(MessageUtil.mode(modeName[mode]));
				} else {
					if (tile.hasEnergy() || !(ShowcaseCore.SS2Loaded && ShowcaseConfig.requireGF)) {
						if (shopData == null) {
							if (!shop.equals(""))
								player.addChatMessage(MessageUtil.customInvalid(shop, false));
						} else {
							if (shopData.getProductList(world, player).size() == 0) {
								player.addChatMessage(MessageUtil.customEmpty(shop));
							} else {
								MCEconomyAPI.openShopGui(shopData.thisShopId, player, world, x, y, z);
							}
						}
					}
				}

			}
			return true;
		}
	}

	private void dropCardItem(World world, EntityPlayer player, ItemStack item) {
		if (item != null && !world.isRemote) {
			double posX = player.posX;
			double posY = player.posY + 0.5D;
			double posZ = player.posZ;

			EntityItem drop = new EntityItem(world, posX, posY, posZ, item);
			world.spawnEntityInWorld(drop);
		}
	}

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
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileCustomShopBase();
	}

	@Override
	public int damageDropped(int par1) {
		return 0;
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase,
			ItemStack par6ItemStack) {
		int playerFacing = MathHelper.floor_double((par5EntityLivingBase.rotationYaw * 4F) / 360F + 0.5D) & 3;

		boolean tall = false;

		if (par5EntityLivingBase != null && par5EntityLivingBase instanceof EntityPlayer) {
			tall = par5EntityLivingBase.isSneaking();
		}

		byte facing = 0;
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

		par1World.setBlockMetadataWithNotify(par2, par3, par4, facing, 3);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
		TileEntity any = world.getTileEntity(x, y, z);
		if (any != null && any instanceof TileCustomShopBase) {
			TileCustomShopBase tile = (TileCustomShopBase) any;
			String shop = tile.getShop();
			CustomShopData shopData = CustomShopManager.get(shop.equals("")?CustomShopData.DEFAULT:shop);
			if (!shop.equals("")) {
				if (shopData != null) {
					float a = world.rand.nextFloat() * 0.8F + 0.1F;
					float a1 = world.rand.nextFloat() * 0.8F + 0.1F;
					float a2 = world.rand.nextFloat() * 0.8F + 0.1F;
					EntityItem drop = new EntityItem(world, x + a, y + a1, z + a2, new ItemStack(shopData.item, 1));
					world.spawnEntityInWorld(drop);
				}
			}
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		return par1 > 0 ? this.blockIcon : this.defaultScreen;
	}
}
