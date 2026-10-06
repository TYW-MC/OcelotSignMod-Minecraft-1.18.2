package bklmc.ocelotsign.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.LiteralText;

/**
 * 空白编辑界面
 *
 * @see Screen
 */
public class PatternAndFontBlankScreen extends Screen {

    public PatternAndFontBlankScreen() {
        super(new LiteralText(""));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void renderBackground(net.minecraft.client.util.math.MatrixStack context) {
    }

    @Override
    public void close() {
        PatternAndFontOverlay.isVisible = false;
        super.close();
    }
}
