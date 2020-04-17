package com.emssolutions.keybinding.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class MenuGui extends Screen {

    private GuiList controlList;

    public MenuGui() {
        super(new StringTextComponent("KeybindingMenu"));
    }


    @Override
    protected void init() {
        controlList = new GuiList(this, this.getMinecraft());
        this.children.add(controlList);
        this.setFocused(controlList);
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }



    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.controlList.render(mouseX, mouseY, partialTicks);
        super.render(mouseX, mouseY, partialTicks);
    }


    public static void open() {
        Minecraft.getInstance().displayGuiScreen(new MenuGui());
    }
}
