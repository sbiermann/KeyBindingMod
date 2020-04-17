package com.emssolutions.keybinding;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class Constants {
    public static final String MODID = "keybindingmod";
    public static final String KEYBINDING_CAT = "key.categories.keybindingmod";
    public static final String KEYBINGING_MENU_DESCR = "Keybinding Management Menu";
    public static final File MAPPING_FOLDER = new File( FMLPaths.GAMEDIR.get().toFile(), MODID );
    public static final File MAPPING_FILE = new File( MAPPING_FOLDER, "keybindings.json" );
}
