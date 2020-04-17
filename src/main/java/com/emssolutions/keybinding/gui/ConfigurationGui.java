package com.emssolutions.keybinding.gui;

import com.emssolutions.keybinding.KeybindingMod;
import com.emssolutions.keybinding.json.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

import java.util.Optional;

import static com.emssolutions.keybinding.Constants.MAPPING_FILE;
import static com.emssolutions.keybinding.utils.JsonFileHandler.load;
import static com.emssolutions.keybinding.utils.JsonFileHandler.save;

public class ConfigurationGui extends Screen {

    private TextFieldWidget textCommand;
    private KeyBinding keyBinding;


    public ConfigurationGui() {
        super(new StringTextComponent("ConfigurationMenu"));
    }

    @Override
    public void tick() {
        textCommand.tick();
    }

    @Override
    @Nullable
    public IGuiEventListener getFocused() {
            return textCommand;
    }

    @Override
    public void init() {
        addButton( new Button(this.width / 2 - 4 - 150, 80, 150, 20, "Speichern", (screen) ->
            saveMapping()
        ));
        addButton( new Button(this.width / 2 + 4, 80, 150, 20, "Abbruch",
                (screen) -> Minecraft.getInstance().displayGuiScreen(null) ));
        textCommand = new TextFieldWidget(this.font, this.width / 2 - 150, 50, 300, 20, "Befehl eingeben");
        textCommand.setMaxStringLength(32767);
        textCommand.changeFocus(true);
        textCommand.setText("");
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean charTyped(char key, int keycode) {
        this.textCommand.charTyped(key, keycode);
        return true;
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        super.mouseClicked(mx, my, button);
        this.textCommand.mouseClicked(mx, my, button);
        return true;
    }

    @Override
    public void render(int mouseX, int mouseY, float partial) {
        this.renderBackground();
        this.textCommand.render(mouseX, mouseY, partial);
        super.render(mouseX, mouseY, partial);

    }

    void saveMapping(){
        Optional<KeyMapping> keymappingFile = load(MAPPING_FILE);
        KeyMapping keyMapping = keymappingFile.orElse(new KeyMapping());
        keyMapping.getCommandMap().put( keyBinding.getKeyDescription(), textCommand.getText() );
        save( MAPPING_FILE, keyMapping );
        KeybindingMod.getInstance().reloadMappings();
        Minecraft.getInstance().displayGuiScreen(null);
    }

    public static void open( KeyBinding keyBinding ) {
        ConfigurationGui configurationGui = new ConfigurationGui();
        configurationGui.keyBinding = keyBinding;
        Minecraft.getInstance().displayGuiScreen(configurationGui);
    }
}
