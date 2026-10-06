package bklmc.ocelotsign.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;
import net.minecraft.text.TranslatableText;

/**
 * 指示牌编辑界面
 *
 * @param <T> 关联的方块实体类型
 * @see AbstractSignBlockEditScreen
 * @see PatternAndFontOverlay
 */
@Environment(EnvType.CLIENT)
public class OcelotSignEditScreen<T extends BlockEntityWithText> extends AbstractSignBlockEditScreen<T> {
    public final ButtonWidget patternAndFontListButton;

    public OcelotSignEditScreen(T entity, BlockPos blockPos, List<TextContext> textContextsEditing) {
        super(entity, blockPos, textContextsEditing);
        this.patternAndFontListButton = new ButtonWidget(0, 0, 100, 20,
                new TranslatableText("message.ocelotsign.pattern_and_font_list"),
                button -> openPatternAndFontList());
    }

    /**
     * 打开图样和字体列表悬浮层。
     */
    private void openPatternAndFontList() {
        PatternAndFontBlankScreen.openOverlay();
    }

    @Override
    protected void init() {
        super.init();
        patternAndFontListButton.x = width / 2 - 100;
        patternAndFontListButton.y = height - 30;
        finishButton.x = width / 2;
        finishButton.y = height - 30;
        finishButton.setWidth(80);
        addDrawableChild(patternAndFontListButton);
    }

    @Override
    public void render(MatrixStack context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }
}
