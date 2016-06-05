package defeatedcrow.showcase.client;

import defeatedcrow.showcase.common.CustomShopManager;
import defeatedcrow.showcase.common.SCLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Belgabor on 31.05.2016.
 * Based on code from Quadrum by dmillerw.
 */
public class CustomIcon extends TextureAtlasSprite {
    private static File textureFolder;

    public boolean failed;

    public static void init() {
        textureFolder = new File(CustomShopManager.configFolder, "textures");
        if (!textureFolder.exists())
            textureFolder.mkdirs();
    }

    public CustomIcon(String name) {
        super(name);
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation location) {
        BufferedImage image;
        failed = true;
        try {
            image = ImageIO.read(new File(textureFolder, location.getResourcePath() + ".png"));
        } catch (IOException ex) {
            SCLogger.logger.warn(String.format("Failed to load texture %s (%s)", location.getResourcePath() + ".png", ex.getMessage()));
            return true;
        }

        if (image != null) {
            GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
            BufferedImage[] array = new BufferedImage[1 + gameSettings.mipmapLevels];
            array[0] = image;
            this.loadSprite(array, null, (float) gameSettings.anisotropicFiltering > 1.0F);
            failed = false;
            return false;
        }

        return true;
    }

    public boolean isEmpty() {
        return height == 0 || width == 0;
    }

    public CustomIcon register(TextureMap textureMap) {
        textureMap.setTextureEntry(getIconName(), this);
        return this;
    }

}
