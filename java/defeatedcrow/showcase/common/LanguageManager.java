package defeatedcrow.showcase.common;

import cpw.mods.fml.common.registry.LanguageRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Belgabor on 31.05.2016.
 */
public class LanguageManager {
    private static List<String> enKeys = new ArrayList<String>();

    public static void init() {
        File folder = new File(CustomShopManager.configFolder, "lang");

        if (!folder.exists())
            folder.mkdirs();

        for(File file : folder.listFiles()) {
            if (!file.getName().endsWith(".lang"))
                continue;

            try {
                String lang = file.getName().substring(0, file.getName().lastIndexOf("."));
                Properties properties = new Properties();
                properties.load(new FileInputStream(file));
                HashMap<String, String> map = new HashMap<String, String>();
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    map.put(entry.getKey().toString(), entry.getValue().toString());
                    if (lang.equals("en_US"))
                        enKeys.add(entry.getKey().toString());
                }
                LanguageRegistry.instance().injectLanguage(lang, map);
            } catch (IOException ex) {
                SCLogger.logger.warn("Failed to load localizations from %s (%s)", file.getName(), ex.toString());
            }
        }
    }

    public static void ensureTranslation(String key, String item) {
        if (!enKeys.contains(key)) {
            LanguageRegistry.instance().addStringLocalization(key, item);
        }
    }
}
