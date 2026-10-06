package bklmc.ocelotsign.client;

import bklmc.ocelotsign.mixin_interfaces.ISignEditorExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * 图案与字体选择界面。
 *
 * <p>历史上这个类只是一个"空白界面"，图案与字体浮层由
 * {@code ScreenEvents.afterRender} 绘制、输入则依赖
 * {@code ScreenMouseEvents.allowMouseClick} / {@code allowMouseScroll} /
 * {@code ScreenKeyboardEvents.allowKeyPress} 派发。
 *
 * <p>但 Fabric 的这几个 Allow 事件是挂在 {@code Mouse}/{@code Keyboard} 的注入上的，
 * 而 Forge + Connector + ForgifiedFabricAPI 环境下 FFAPI 的 MouseMixin / KeyboardMixin
 * 是空壳（没有任何注入），事件永远不会触发 —— 结果就是浮层能画出来，却收不到点击、
 * 滚轮和 ESC：按钮点不动、内容不能滚动、界面关不掉。
 *
 * <p>因此这里改为"浮层自己就是一个 Screen"：渲染与输入都由本类的
 * {@link Screen} 标准回调直接处理，不依赖任何 Fabric 屏幕事件，
 * 在 Fabric 与 Forge(Connector) 下行为一致。
 *
 * @see Screen
 * @see PatternAndFontOverlay
 */
public class PatternAndFontBlankScreen extends Screen {

    /** 打开浮层的告示牌编辑界面，关闭浮层时返回该界面；可能为 {@code null}。 */
    private static Screen editorScreen = null;

    public PatternAndFontBlankScreen(Screen editorScreen) {
        super(new LiteralText(""));
        PatternAndFontBlankScreen.editorScreen = editorScreen;
    }

    // ==================== 打开 / 关闭 ====================

    /**
     * 打开图案与字体选择界面，并记录当前界面作为关闭后要返回的界面。
     *
     * <p><b>注意：这里刻意不使用 {@link MinecraftClient#setScreen}。</b>
     * {@code setScreen} 会先对原界面调用 {@link Screen#removed()}，而 mishanguc
     * 告示牌编辑界面的 {@code removed()} 会向服务器发送 {@code edit_sign_finish}
     * 数据包 —— 这会立即结束本次编辑会话并清空服务端记录的编辑者。
     * 之后真正关闭编辑界面时，服务器会因"编辑者不匹配"而丢弃全部修改，
     * 表现为：插入的图案/字体全都"不生效"，告示牌还是原样，且该行无法继续编辑。
     *
     * <p>因此这里直接替换 {@code currentScreen} 字段：原编辑界面的控件、焦点与
     * 编辑会话原封不动，关闭编辑界面时 mishanguc 会一次性提交全部文本。
     */
    public static void openOverlay() {
        MinecraftClient client = MinecraftClient.getInstance();
        Screen current = client.currentScreen;
        if (current instanceof PatternAndFontBlankScreen) return;

        PatternAndFontOverlay.isVisible = true;
        // 强制互斥：避免上一次会话残留的任意顶层项标志导致多个侧边栏项同时高亮
        PatternAndFontOverlay.selectSidebarTop(PatternAndFontOverlay.SIDEBAR_TOP_DOCS);
        PatternAndFontBlankScreen overlay = new PatternAndFontBlankScreen(current);
        client.currentScreen = overlay;
        // 仅初始化浮层自身的字段（width/height/textRenderer 等），
        // 不会对编辑界面触发任何生命周期回调。
        overlay.init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
    }

    /**
     * 获取当前浮层所依附的告示牌编辑界面。
     *
     * @return 编辑界面，不存在时返回 {@code null}
     */
    public static Screen getEditorScreen() {
        return editorScreen;
    }

    /**
     * 插入文本到告示牌编辑界面。成功后浮层保持"待关闭"状态（{@code isVisible=false}），
     * 由 {@link #mouseClicked} 统一负责返回编辑界面。
     *
     * @param text 要插入的文本
     * @return 是否插入成功
     */
    static boolean insertTextToEditor(String text) {
        if (editorScreen instanceof ISignEditorExtension extension) {
            extension.ocelotsign$insertText(text);
            PatternAndFontOverlay.isVisible = false;
            return true;
        }
        return false;
    }

    /**
     * 插入纹理到告示牌编辑界面。成功后浮层保持"待关闭"状态。
     *
     * @param identifier 纹理标识符
     * @return 是否插入成功
     */
    static boolean insertTextureToEditor(Identifier identifier) {
        if (editorScreen instanceof ISignEditorExtension extension) {
            extension.ocelotsign$insertTexture(identifier);
            PatternAndFontOverlay.isVisible = false;
            return true;
        }
        return false;
    }

    /**
     * 关闭浮层并返回打开它之前的告示牌编辑界面。
     *
     * <p>与 {@link #openOverlay()} 对应，同样直接替换 {@code currentScreen}，
     * 不调用 {@code setScreen}，避免对编辑界面触发多余的 {@code init()} 重载
     * （编辑界面的控件、焦点、滚动位置与编辑会话需要原样保留）。
     */
    public void returnToEditor() {
        MinecraftClient client = MinecraftClient.getInstance();
        Screen target = editorScreen;
        editorScreen = null;
        PatternAndFontOverlay.isVisible = false;
        if (client.currentScreen == this) {
            client.currentScreen = target;
        }
    }

    @Override
    public void close() {
        returnToEditor();
    }

    // ==================== Screen 标准回调 ====================

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void renderBackground(MatrixStack context) {
        // 浮层自带整屏背景，不需要原版的泥土/模糊背景
    }

    @Override
    public void render(MatrixStack context, int mouseX, int mouseY, float delta) {
        if (PatternAndFontOverlay.isVisible) {
            PatternAndFontOverlay.render(context, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!PatternAndFontOverlay.isVisible) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        PatternAndFontOverlay.mouseClicked(mouseX, mouseY, button);

        // 插入成功或点了"返回"按钮时浮层已被标记为不可见，此时回到告示牌编辑界面
        if (!PatternAndFontOverlay.isVisible) {
            returnToEditor();
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!PatternAndFontOverlay.isVisible) {
            return super.mouseReleased(mouseX, mouseY, button);
        }
        PatternAndFontOverlay.mouseReleased(mouseX, mouseY, button);
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!PatternAndFontOverlay.isVisible) {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        // 滚动条拖动在每帧渲染时按鼠标当前位置更新，这里只需消费事件
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!PatternAndFontOverlay.isVisible) {
            return super.mouseScrolled(mouseX, mouseY, amount);
        }
        PatternAndFontOverlay.mouseScrolled(mouseX, mouseY, amount);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            returnToEditor();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
