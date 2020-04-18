package com.emssolutions.keybinding.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.client.gui.widget.list.KeyBindingList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.emssolutions.keybinding.Constants.KEYBINDING_CAT;
import static com.emssolutions.keybinding.Constants.KEYBINGING_MENU_DESCR;

@OnlyIn(Dist.CLIENT)
public class GuiList extends AbstractOptionList<KeyBindingList.Entry> {
    private final Minecraft mc;
    private int maxWidth = 0;

    public GuiList(Screen parent, Minecraft mc) {
        super(mc, parent.width, parent.height, 25, parent.height - 20, 20);
        this.mc = mc;

        KeyBinding[] keyBindings = mc.gameSettings.keyBindings;
        Arrays.sort(keyBindings);

        addEntry(new CategoryEntry(""));//dummy for better position of following category
        addEntry(new CategoryEntry(I18n.format("key.guilist.text")));

        for (KeyBinding keybinding : keyBindings) {
            String category = keybinding.getKeyCategory();
            if (!KEYBINDING_CAT.equals(category) || KEYBINGING_MENU_DESCR.equals(keybinding.getKeyDescription()))
                continue;
            int width = mc.fontRenderer.getStringWidth(I18n.format(keybinding.getKeyDescription()));
            if (width > maxWidth) {
                maxWidth = width;
            }
            addEntry(new KeyEntry(keybinding));
        }
    }

    @Override
    public int getWidth() {
        return super.getWidth() + 32;
    }

    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 15;
    }

    @OnlyIn(Dist.CLIENT)
    public class CategoryEntry extends KeyBindingList.Entry {
        private final String category;
        private final int width;

        CategoryEntry(String category) {
            this.category = I18n.format(category);
            this.width = GuiList.this.mc.fontRenderer.getStringWidth(this.category);
        }

        @Override
        public void render(int p_render_1_, int p_render_2_, int p_render_3_, int p_render_4_, int p_render_5_, int p_render_6_, int p_render_7_, boolean p_render_8_, float p_render_9_) {
            GuiList.this.mc.fontRenderer.drawString(this.category, (float) (Objects.requireNonNull(GuiList.this.mc.currentScreen).width / 2 - this.width / 2), (float) p_render_2_ - GuiList.this.mc.fontRenderer.FONT_HEIGHT + 1, 16777215);
        }

        @Override
        public boolean changeFocus(boolean changeFocus) {
            return false;
        }

        @Override
        @Nonnull
        public List<? extends IGuiEventListener> children() {
            return Collections.emptyList();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class KeyEntry extends KeyBindingList.Entry {
        private final KeyBinding keyBinding;
        private final String description;
        private final Button buttonSelect;

        KeyEntry(KeyBinding keyBinding) {
            this.keyBinding = keyBinding;
            this.description = I18n.format(keyBinding.getKeyDescription());
            this.buttonSelect = new Button(0, 0, 95, 18, description, (screen) -> {
                ConfigurationGui.open(keyBinding);
            });
        }

        @Override
        public void render(int p_render_1_, int p_render_2_, int p_render_3_, int p_render_4_, int p_render_5_, int p_render_6_, int p_render_7_, boolean p_render_8_, float p_render_9_) {
            GuiList.this.mc.fontRenderer.drawString(this.description, (float) (p_render_3_ + 90 - GuiList.this.maxWidth), (float) (p_render_2_ + p_render_5_ / 2 - GuiList.this.mc.fontRenderer.FONT_HEIGHT / 2), 16777215);
            this.buttonSelect.x = p_render_3_ + 105;
            this.buttonSelect.y = p_render_2_;
            this.buttonSelect.setMessage(this.keyBinding.getLocalizedName());
            this.buttonSelect.render(p_render_6_, p_render_7_, p_render_9_);
        }

        @Override
        @Nonnull
        public List<? extends IGuiEventListener> children() {
            return ImmutableList.of(this.buttonSelect);
        }

        @Override
        public boolean mouseClicked(double x, double y, int button) {
            return this.buttonSelect.mouseClicked(x, y, button);
        }

        @Override
        public boolean mouseReleased(double x, double y, int button) {
            return buttonSelect.mouseReleased(x, y, button);
        }
    }
}