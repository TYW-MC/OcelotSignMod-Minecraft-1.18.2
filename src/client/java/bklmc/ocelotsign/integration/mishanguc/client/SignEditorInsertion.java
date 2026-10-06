package bklmc.ocelotsign.integration.mishanguc.client;

import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.screen.AbstractSignBlockEditScreen;
import pers.solid.mishang.uc.screen.TextFieldListWidget;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.text.TextureSpecialDrawable;

import java.util.List;

/**
 * 向 mishanguc 告示牌编辑界面插入纹理或文本行。
 */
public final class SignEditorInsertion {
    private SignEditorInsertion() {
    }

    /**
     * 插入一个纹理条目到编辑界面末尾。
     *
     * @param screen      编辑界面。
     * @param identifier  纹理标识符。
     */
    public static void insertTexture(AbstractSignBlockEditScreen<?> screen, Identifier identifier) {
        TextFieldListWidget textFieldListWidget = screen.textFieldListWidget;
        int index = textFieldListWidget.children().size();
        TextContext textContext = createTextContextForInsert(textFieldListWidget, index);

        TextureSpecialDrawable textureDrawable = new TextureSpecialDrawable(identifier, textContext);
        textContext.extra = textureDrawable;

        TextFieldListWidget.Entry newEntry = textFieldListWidget.addTextField(index, textContext, false);
        SignTextCommandApplier.apply(newEntry, screen);
        syncSignPreview(screen);
        focusOnNewEntry(screen, textFieldListWidget, index);
    }

    /**
     * 插入一段文本内容到编辑界面末尾。
     *
     * @param screen 编辑界面。
     * @param text   要插入的文本。
     */
    public static void insertText(AbstractSignBlockEditScreen<?> screen, String text) {
        TextFieldListWidget textFieldListWidget = screen.textFieldListWidget;
        int index = textFieldListWidget.children().size();
        TextContext textContext = createTextContextForInsert(textFieldListWidget, index);

        TextFieldListWidget.Entry newEntry = textFieldListWidget.addTextField(index, textContext, false);
        newEntry.textFieldWidget.setText(text);
        // 1.18.2 无 setCursorToEnd
        newEntry.textFieldWidget.setCursor(newEntry.textFieldWidget.getText().length());
        SignTextCommandApplier.apply(newEntry, screen);
        syncSignPreview(screen);
        focusOnNewEntry(screen, textFieldListWidget, index);
    }

    /**
     * 标记方块实体已修改并通知客户端更新。
     */
    private static void syncSignPreview(AbstractSignBlockEditScreen<?> screen) {
        screen.entity.markDirty();
    }

    /**
     * 将焦点移至新添加的条目并滚动至可见。
     */
    private static void focusOnNewEntry(
            AbstractSignBlockEditScreen<?> screen,
            TextFieldListWidget textFieldListWidget,
            int index
    ) {
        TextFieldListWidget.Entry newEntry = textFieldListWidget.children().get(index);
        textFieldListWidget.setScrollAmount(textFieldListWidget.getScrollAmount());
        if (!textFieldListWidget.children().isEmpty()) {
            // 先让编辑界面把焦点交给列表，再由列表把焦点交给新行，
            // 顺序反过来会被随后设置的父级焦点覆盖。
            screen.setFocused(textFieldListWidget);
        }
        // 1.18.2 中 setFocused 为 protected，使用 mishanguc 提供的 setFocusedAndSelected。
        textFieldListWidget.setFocusedAndSelected(newEntry, false, false);
        // 双保险：在部分加载器组合（Forge + Connector）下 setSelected 的焦点同步可能未生效，
        // 这里直接把输入框标记为聚焦并把光标放到末尾，保证插入的这行可以立刻继续输入。
        newEntry.textFieldWidget.setTextFieldFocused(true);
        newEntry.textFieldWidget.setCursor(newEntry.textFieldWidget.getText().length());
    }

    /**
     * 为插入的新条目创建 {@link TextContext}。若不是首行，则从前一行继承样式。
     */
    private static TextContext createTextContextForInsert(TextFieldListWidget textFieldListWidget, int index) {
        List<? extends TextFieldListWidget.Entry> children = textFieldListWidget.children();
        if (index > 0) {
            TextContext prevTextContext = children.get(index - 1).textContext;
            TextContext textContext = prevTextContext.clone();
            textContext.text = null;
            textContext.extra = null;
            return textContext;
        }
        return new TextContext();
    }
}
