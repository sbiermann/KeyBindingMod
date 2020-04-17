package com.emssolutions.keybinding.json;

import java.util.HashMap;
import java.util.Map;

public class KeyMapping {

    private Map<String, String> commandMap = new HashMap();

    public Map<String, String> getCommandMap() {
        return commandMap;
    }

    public void setCommandMap(Map<String, String> commandMap) {
        this.commandMap = commandMap;
    }
}
