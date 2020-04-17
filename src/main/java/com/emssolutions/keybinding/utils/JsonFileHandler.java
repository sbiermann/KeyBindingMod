package com.emssolutions.keybinding.utils;

import com.emssolutions.keybinding.KeybindingMod;
import com.emssolutions.keybinding.json.KeyMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Optional;

public class JsonFileHandler {

    private static final Logger logger = LogManager.getLogger(KeybindingMod.class);

    private static final Gson GSON;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        GSON = builder.create();
    }

    public static Optional<KeyMapping> load(File file) {
        try {
            KeyMapping mapping = GSON.fromJson(new FileReader(file), KeyMapping.class);
            return Optional.of(mapping);
        } catch (FileNotFoundException e) {
            logger.warn("seems to be that the mapping file is not found",e);
            return Optional.empty();
        }
    }

    public static void save(File file, KeyMapping mapping) {
        if (file.exists()) {
            file.delete();
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.append(GSON.toJson(mapping));
            writer.close();
        } catch (IOException e) {
            logger.error("could not save mapping file", e);
        }
    }

}
