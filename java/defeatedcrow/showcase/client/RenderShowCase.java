package defeatedcrow.showcase.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import defeatedcrow.showcase.blocks.tileentity.TileShowcase;
import defeatedcrow.showcase.common.MPChecker;
import defeatedcrow.showcase.common.ShowcaseCore;

@SideOnly(Side.CLIENT)
public class RenderShowCase extends TileEntitySpecialRenderer {
	private static final ResourceLocation CASE_TEX = new ResourceLocation("showcase:textures/tiles/showcase.png");
	public static RenderShowCase caseRenderer;
	private ModelShowCase model = new ModelShowCase();

	public void renderTileEntityCaseAt(TileShowcase par1Tile, double par2, double par4, double par6, float par8) {
		this.setRotation(par1Tile, (float) par2, (float) par4, (float) par6);
	}

	/**
	 * Associate a TileEntityRenderer with this TileEntitySpecialRenderer
	 */
	public void setTileEntityRenderer(TileEntityRendererDispatcher par1TileEntityRenderer) {
		super.func_147497_a(par1TileEntityRenderer);
		caseRenderer = this;
	}

	public void setRotation(TileShowcase par0Tile, float par1, float par2, float par3) {
		ModelShowCase model = this.model;
		byte l = (byte) par0Tile.getBlockMetadata();
		byte mode = (byte) par0Tile.getMode();
		byte id = (byte) (par0Tile.getLocation() == ShowcaseCore.showcaseBlock ? 1 : 0);
		this.bindTexture(CASE_TEX);

		// base
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		GL11.glTranslatef(par1 + 0.5F, par2 + 1.5F, par3 + 0.5F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		GL11.glRotatef(0.0F, 0.0F, 0.0F, 0.0F);
		model.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, id);

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();

		// inner
		if (par0Tile.getWorldObj() != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(par1 + 0.5F, par2 + 0.35F, par3 + 0.5F);
			GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);

			this.renderInner(par0Tile, l);
			if (mode != 3)
				this.renderFont(par0Tile, l);

			GL11.glPopMatrix();
		}

		// glass
		this.bindTexture(CASE_TEX);

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glPolygonOffset(-1, -1);
		GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);

		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
		GL11.glStencilFunc(GL11.GL_NOTEQUAL, 1, 1);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);

		GL11.glTranslatef(par1 + 0.5F, par2 + 1.5F, par3 + 0.5F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		GL11.glRotatef(0.0F, 0.0F, 0.0F, 0.0F);
		GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.25F);
		model.renderGlass((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, l);

		GL11.glDisable(GL11.GL_STENCIL_TEST);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();

	}

	private void renderInner(TileShowcase tile, int side) {
		ItemStack item = tile.getInv().getStackInSlot(1);

		if (item != null) {
			EntityItem entityitem = new EntityItem(tile.getWorldObj(), 0.0D, 0.0D, 0.0D, item);
			// entityitem.getEntityItem().stackSize = 1;
			entityitem.hoverStart = 0.0F;

			float f = 0.0F;
			switch (side) {
			case 0:
				f = 0.0F;
				break;
			case 1:
				f = -90.0F;
				break;
			case 2:
				f = 180.0F;
				break;
			case 3:
				f = 90.0F;
				break;
			}

			if (item.getItem() instanceof ItemBlock) {
				GL11.glScalef(1.3F, 1.3F, 1.3F);
				GL11.glRotatef(f + 90.0F, 0.0F, 1.0F, 0.0F);

			} else {
				GL11.glScalef(1.0F, 1.0F, 1.0F);
				GL11.glRotatef(f, 0.0F, 1.0F, 0.0F);

			}

			RenderItem.renderInFrame = false;
			RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
			RenderItem.renderInFrame = false;

		}
	}

	private void renderFont(TileShowcase tile, int side) {
		ItemStack item = tile.getInv().getStackInSlot(1);
		int price = MPChecker.getSellMP(item);

		if (item != null && price > 0) {
			// render font
			FontRenderer fontrenderer = this.func_147498_b();
			float f1 = 0.67F;
			float f2 = 0.03F * f1;

			float f = 180.0F;
			float ajtX = 0.2F;
			float ajtZ = -0.2F;

			if (item.getItem() instanceof ItemBlock) {
				GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			}

			GL11.glTranslatef(ajtX, 0.5F, ajtZ);
			GL11.glRotatef(f, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(f2, -f2, f2);
			GL11.glNormal3f(0.0F, 0.0F, -1.0F * f2);
			GL11.glDepthMask(false);
			byte b0 = 0;

			String s = price + "MP";
			fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 10 - s.length(), b0);

			GL11.glDepthMask(true);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		}
	}

	@Override
	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
		this.renderTileEntityCaseAt((TileShowcase) par1TileEntity, par2, par4, par6, par8);
	}
}
