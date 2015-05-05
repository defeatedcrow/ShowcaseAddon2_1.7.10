package defeatedcrow.showcase.blocks;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import shift.mceconomy2.api.MCEconomyAPI;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import defeatedcrow.showcase.blocks.tileentity.TileSeasonShop;
import defeatedcrow.showcase.common.ShowcaseCore;
import defeatedcrow.showcase.common.TimeUtil;
import defeatedcrow.showcase.plugin.AutumnShopList;
import defeatedcrow.showcase.plugin.SpringShopList;
import defeatedcrow.showcase.plugin.SummerShopList;
import defeatedcrow.showcase.plugin.WinterShopList;

public class BlockSeasonShop extends BlockContainer {

	protected Random rand = new Random();

	@SideOnly(Side.CLIENT)
	private IIcon[] monitorTex;

	private String[] modeName = new String[] { "Spring", "Summer", "Autumn", "Winter" };

	public BlockSeasonShop() {
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
			if (any instanceof TileSeasonShop) {
				TileSeasonShop tile = (TileSeasonShop) any;
				if (tile.hasEnergy() || !ShowcaseCore.SS2Loaded) {
					int season = TimeUtil.getSeason(world);
					switch (season) {
					case 0:
						MCEconomyAPI.openShopGui(SpringShopList.thisShopId, player, world, x, y, z);
						break;
					case 1:
						MCEconomyAPI.openShopGui(SummerShopList.thisShopId, player, world, x, y, z);
						break;
					case 2:
						MCEconomyAPI.openShopGui(AutumnShopList.thisShopId, player, world, x, y, z);
						break;
					case 3:
						MCEconomyAPI.openShopGui(WinterShopList.thisShopId, player, world, x, y, z);
						break;
					default:
						MCEconomyAPI.openShopGui(SpringShopList.thisShopId, player, world, x, y, z);
						break;
					}
				}
			}
			return true;
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
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		int i = par2;
		if (i < 0 || i > 3)
			i = 3;
		return par1 > 0 ? this.blockIcon : this.monitorTex[i];

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("showcase:seasonshop");
		this.monitorTex = new IIcon[4];
		for (int i = 0; i < this.modeName.length; i++) {
			this.monitorTex[i] = par1IconRegister.registerIcon("showcase:season_" + modeName[i]);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileSeasonShop();
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

}
