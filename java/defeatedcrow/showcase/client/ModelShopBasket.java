package defeatedcrow.showcase.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelShopBasket extends ModelBase
{
	  //fields
	    ModelRenderer base;
	    ModelRenderer sideF;
	    ModelRenderer sideB;
	    ModelRenderer sideR;
	    ModelRenderer sideL;
	    ModelRenderer legR;
	    ModelRenderer legL;
	  
	  public ModelShopBasket()
	  {
	    textureWidth = 64;
	    textureHeight = 64;
	    
	      base = new ModelRenderer(this, 0, 0);
	      base.addBox(-8F, 5F, -9.5F, 16, 1, 16);
	      base.setRotationPoint(0F, 16F, 0F);
	      base.setTextureSize(64, 64);
	      base.mirror = true;
	      setRotation(base, 0.2094395F, 0F, 0F);
	      sideF = new ModelRenderer(this, 0, 18);
	      sideF.addBox(-8F, 1F, -9.5F, 16, 4, 1);
	      sideF.setRotationPoint(0F, 16F, 0F);
	      sideF.setTextureSize(64, 64);
	      sideF.mirror = true;
	      setRotation(sideF, 0.2094395F, 0F, 0F);
	      sideB = new ModelRenderer(this, 0, 24);
	      sideB.addBox(-8F, 1F, 5.5F, 16, 4, 1);
	      sideB.setRotationPoint(0F, 16F, 0F);
	      sideB.setTextureSize(64, 64);
	      sideB.mirror = true;
	      setRotation(sideB, 0.2094395F, 0F, 0F);
	      sideR = new ModelRenderer(this, 0, 30);
	      sideR.addBox(-8F, 1F, -8.5F, 1, 4, 14);
	      sideR.setRotationPoint(0F, 16F, 0F);
	      sideR.setTextureSize(64, 64);
	      sideR.mirror = true;
	      setRotation(sideR, 0.2094395F, 0F, 0F);
	      sideL = new ModelRenderer(this, 32, 30);
	      sideL.addBox(7F, 1F, -8.5F, 1, 4, 14);
	      sideL.setRotationPoint(0F, 16F, 0F);
	      sideL.setTextureSize(64, 64);
	      sideL.mirror = true;
	      setRotation(sideL, 0.2094395F, 0F, 0F);
	      legR = new ModelRenderer(this, 36, 18);
	      legR.addBox(5F, 6F, 3F, 2, 3, 2);
	      legR.setRotationPoint(0F, 16F, 0F);
	      legR.setTextureSize(64, 64);
	      legR.mirror = true;
	      setRotation(legR, 0.2094395F, 0F, 0F);
	      legL = new ModelRenderer(this, 36, 18);
	      legL.addBox(-7F, 6F, 3F, 2, 3, 2);
	      legL.setRotationPoint(0F, 16F, 0F);
	      legL.setTextureSize(64, 64);
	      legL.mirror = true;
	      setRotation(legL, 0.2094395F, 0F, 0F);
	  }
	  
	  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	  {
	    super.render(entity, f, f1, f2, f3, f4, f5);
	    setRotationAngles(f, f1, f2, f3, f4, f5);
	    base.render(f5);
	    sideF.render(f5);
	    sideB.render(f5);
	    sideR.render(f5);
	    sideL.render(f5);
	    legR.render(f5);
	    legL.render(f5);
	  }
	  
	  private void setRotation(ModelRenderer model, float x, float y, float z)
	  {
	    model.rotateAngleX = x;
	    model.rotateAngleY = y;
	    model.rotateAngleZ = z;
	  }
	  
	  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
	  {
	    super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
	  }

	}
