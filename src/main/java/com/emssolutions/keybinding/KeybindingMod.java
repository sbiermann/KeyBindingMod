package com.emssolutions.keybinding;

import com.emssolutions.keybinding.gui.MenuGui;
import com.emssolutions.keybinding.json.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static com.emssolutions.keybinding.Constants.*;
import static com.emssolutions.keybinding.utils.JsonFileHandler.load;


@Mod(Constants.MODID)
public class KeybindingMod
{
    private static final Logger logger = LogManager.getLogger(KeybindingMod.class);

    final KeyBinding managementKey = new KeyBinding(KEYBINGING_MENU_DESCR,
            KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM.getOrMakeInput(GLFW.GLFW_KEY_K), KEYBINDING_CAT);

    final List<KeyBinding> keyBindingList = new ArrayList();
    private static KeybindingMod instance;
    private KeyMapping keyMapping;

    public KeybindingMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        for(int i = 1; i < 10; i++)
        {
           keyBindingList.add( new KeyBinding("Keybinding "+i,
                    KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM.getOrMakeInput(319+i ), KEYBINDING_CAT) );
        }
        if( !MAPPING_FOLDER.exists() )
            MAPPING_FOLDER.mkdirs();
        reloadMappings();
        instance = this;
    }

    public void reloadMappings() {
        keyMapping = load(MAPPING_FILE).orElse(new KeyMapping());
    }


    @SubscribeEvent
    public void onTick(InputEvent.KeyInputEvent event) {
        if (managementKey.isPressed()) {
            MenuGui.open();
        }
        for (KeyBinding keyBinding : keyBindingList) {
            if(keyBinding.isPressed())
            {
                String command = keyMapping.getCommandMap().get(keyBinding.getKeyDescription());
                if(command != null)
                {
                    Minecraft.getInstance().player.sendChatMessage( command );
                }
                break;
            }
        }

    }

    public static KeybindingMod getInstance(){
        return instance;
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        logger.info("Preinit, registering keybindings");
        ClientRegistry.registerKeyBinding(managementKey);
        for (KeyBinding keyBinding : keyBindingList) {
            ClientRegistry.registerKeyBinding( keyBinding );
        }
    }

}
