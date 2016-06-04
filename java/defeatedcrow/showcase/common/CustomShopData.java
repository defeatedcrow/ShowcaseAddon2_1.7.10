package defeatedcrow.showcase.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import defeatedcrow.showcase.items.ItemCustomShopCard;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.StringUtils;
import shift.mceconomy2.api.MCEconomyAPI;
import shift.mceconomy2.api.shop.IProduct;
import shift.mceconomy2.api.shop.IShop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Belgabor on 30.05.2016.
 */
public class CustomShopData implements IShop {
    public static final String MAIN = "main";
    public static final String DEFAULT = "default";

    private static final String[] EMPTY_ARRAY = new String[0];

    private static final String LIST_CATEGORY = "lists";

    public final String tag;
    public final int thisShopId;
    private final Configuration config;
    private final HashMap<String, CustomShopList> lists;
    private final int type;
    public String name;
    private boolean modShop = false;
    public final ItemCustomShopCard item;

    public CustomShopData(File file) {
        this.config = new Configuration(file);
        this.tag = file.getName().split("\\.")[0];
        this.lists = new HashMap<String, CustomShopList>();
        this.thisShopId = MCEconomyAPI.ShopManager.registerShop(this);

        this.name = tag;
        if (name.equals(DEFAULT))
            this.name = "Custom Shop";
        else if (Loader.isModLoaded(tag)) {
            for (ModContainer mod : Loader.instance().getActiveModList()) {
                if (mod.getModId().equals(tag)) {
                    this.name = mod.getName() + " Shop";
                    this.modShop = true;
                    break;
                }
            }
        }

        this.name = config.getString("name", Configuration.CATEGORY_GENERAL, name, "Shop Name");
        this.type = config.getInt("type", Configuration.CATEGORY_GENERAL, 0, 0, 0, "Shop type, 0=single");

        // Needs name to be properly set
        this.item = new ItemCustomShopCard(this);
        // Allow screen override for default shop but don't add an item
        if (!tag.equals(DEFAULT))
            GameRegistry.registerItem(item, "defeatedcrow."+item.getBaseName());

        config.setCategoryComment(LIST_CATEGORY, "Configure inventories for the shop." + ShowcaseConfig.BR
                + "Type 0: requires one list called \"main\"" + ShowcaseConfig.BR
                + "\"ModID:RegisteredName:metadata:price\"");

        for (Property prop : config.getCategory("lists").getOrderedValues()) {
            String listName;
            if (prop.getName().equals(MAIN)) {
                listName = name;
            } else {
                listName = String.format("%s (%s)", name, StringUtils.capitalize(prop.getName()));
            }

            lists.put(prop.getName(), new CustomShopList(listName, prop.getStringList()));
        }

        if (config.hasChanged()) {
            config.save();
        }
    }

    public void load() {
        switch (type) {
            case 0:
                if (!lists.containsKey(MAIN)) {
                    CustomShopList shop = new CustomShopList(name, EMPTY_ARRAY);
                    lists.put(MAIN, shop);
                    if (tag.equals(DEFAULT)) {
                        for (CustomShopData shopData : CustomShopManager.shops.values()) {
                            if (!shopData.tag.equals(CustomShopData.DEFAULT))
                                shop.addProduct(new ItemStack(shopData.item, 1), 9999);
                        }
                    } else if (modShop) {
                        String modid = tag + ":";
                        for (Iterator<Block> it = GameData.getBlockRegistry().iterator(); it.hasNext(); ) {
                            Block block = it.next();
                            if (!GameData.getBlockRegistry().getNameForObject(block).startsWith(modid))
                                continue;
                            ItemStack item = new ItemStack(block, 1);
                            int price = -2;
                            try {
                                price = MCEconomyAPI.ShopManager.getPurchase(item);
                            } catch (NullPointerException e) {}
                            if (price <= 0)
                                price = 9999;
                            shop.addProduct(item, price);
                        }
                        for (Iterator<Item> it = GameData.getItemRegistry().iterator(); it.hasNext(); ) {
                            Item iItem = it.next();
                            if (!GameData.getItemRegistry().getNameForObject(iItem).startsWith(modid))
                                continue;
                            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                                iItem.getSubItems(iItem, null, items);
                                for (ItemStack item : items) {
                                    if (item == null)
                                        continue;
                                    int price = MCEconomyAPI.ShopManager.getPurchase(item);
                                    if (price <= 0)
                                        price = 9999;
                                    shop.addProduct(item, price);
                                }
                            } else {
                                ItemStack item = new ItemStack(iItem, 1);
                                int price = MCEconomyAPI.ShopManager.getPurchase(item);
                                if (price <= 0)
                                    price = 9999;
                                shop.addProduct(item, price);
                            }
                        }
                    } else {
                        shop.addProduct(new ItemStack(Items.stick, 1), 9999);
                    }
                    SCLogger.logger.warn(String.format("Custom shop '%s' has no main list, default generated.", tag));
                    save();
                }
        }
        for (CustomShopList list : lists.values()) {
            list.load();
        }
    }

    public void save() {
        for (Map.Entry<String, CustomShopList> list : lists.entrySet()) {
            String[] rawList = list.getValue().save();
            Property pList = config.get(LIST_CATEGORY, list.getKey(), EMPTY_ARRAY);
            pList.set(rawList);
        }
        if (config.hasChanged())
            config.save();
    }

    @Override
    public String getShopName(World world, EntityPlayer player) {
        switch (type) {
            case 0:
                if (lists.containsKey(MAIN))
                    return lists.get(MAIN).name;
        }
        return null;
    }

    @Override
    public void addProduct(IProduct product) {
        switch (type) {
            case 0:
                if (lists.containsKey(MAIN))
                    lists.get(MAIN).addProduct(product);
        }
    }

    @Override
    public ArrayList<IProduct> getProductList(World world, EntityPlayer player) {
        switch (type) {
            case 0:
                if (lists.containsKey(MAIN))
                    return lists.get(MAIN).products;
        }
        return null;
    }
}
