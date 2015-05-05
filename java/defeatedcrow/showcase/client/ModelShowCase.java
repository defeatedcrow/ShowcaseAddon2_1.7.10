package defeatedcrow.showcase.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelShowCase extends ModelBase {
	// fields
	ModelRenderer base;
	ModelRenderer boad;
	ModelRenderer maincase;

	public ModelShowCase() {
		textureWidth = 128;
		textureHeight = 64;

		base = new ModelRenderer(this, 0, 0);
		base.addBox(-8F, 4F, -8F, 16, 4, 16);
		base.setRotationPoint(0F, 16F, 0F);
		base.setTextureSize(64, 32);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		boad = new ModelRenderer(this, 64, 0);
		boad.addBox(-7.5F, 3F, -7.5F, 15, 1, 15);
		boad.setRotationPoint(0F, 16F, 0F);
		boad.setTextureSize(64, 32);
		boad.mirror = true;
		setRotation(boad, 0F, 0F, 0F);
		maincase = new ModelRenderer(this, 0, 24);
		maincase.addBox(-8F, -8F, -8F, 16, 16, 16);
		maincase.setRotationPoint(0F, 16F, 0F);
		maincase.setTextureSize(64, 32);
		maincase.mirror = true;
		setRotation(maincase, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, byte b0) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(entity, f, f1, f2, f3, f4, f5);
		boad.render(f5);
		if ((b0 & 1) == 0)
			base.render(f5);
	}

	public void renderGlass(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, byte b0) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(entity, f, f1, f2, f3, f4, f5);
		maincase.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
