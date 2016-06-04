package defeatedcrow.showcase.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import defeatedcrow.showcase.client.CustomIcon;
import defeatedcrow.showcase.common.CustomShopData;
import defeatedcrow.showcase.common.CustomShopManager;
import defeatedcrow.showcase.common.LanguageManager;
import defeatedcrow.showcase.common.ShowcaseCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * Created by Belgabor on 30.05.2016.
 */
public class ItemCustomShopCard extends Item {
    public final String tag;
    public final CustomShopData owner;
    @SideOnly(Side.CLIENT)
    private CustomIcon icon;
    public  CustomIcon screen;

    public ItemCustomShopCard(CustomShopData owner) {
        this.tag = owner.tag;
        this.owner = owner;
        this.setMaxStackSize(1);
        this.setTextureName("showcase:default_card");
        this.setUnlocalizedName("defeatedcrow." + getBaseName());
        LanguageManager.ensureTranslation(getUnlocalizedName()+".name", owner.name + " Card");
    }

    public String getBaseName() {
        return "shopcard." + tag;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, EntityPlayer player, List list,
                               boolean par4) {
        super.addInformation(item, player, list, par4);
        list.add("Shop : " + StatCollector.translateToLocal(owner.name));
        if (ShowcaseCore.proxy.isShiftKeyDown() && ShowcaseCore.proxy.getOP(player.getCommandSenderName())) {
            list.add("Tag : " + tag);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        icon = new CustomIcon(tag + "_card").register((TextureMap) register);
        screen = new CustomIcon(tag + "_screen").register((TextureMap) register);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        if (icon.failed)
            return super.getIconFromDamage(damage);
        return icon;
    }
}
