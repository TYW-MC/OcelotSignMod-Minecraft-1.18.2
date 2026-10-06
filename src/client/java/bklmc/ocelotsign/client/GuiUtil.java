package bklmc.ocelotsign.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import com.mojang.blaze3d.systems.RenderSystem;

/**
 * 1.18.2 GUI 绘制辅助工具。
 *
 * <p>1.19.4+ 的 {@code DrawContext} 提供的部分便捷方法（drawBorder、enableScissor、
 * 带阴影开关的 drawText、直接传 Identifier 的 drawTexture 等）在 1.18.2 中不存在，
 * 此工具类补齐这些能力。</p>
 */
public final class GuiUtil {
    /** DrawableHelper 的绘制方法为静态方法，可直接调用；这里用统一入口。 */
    private static final DrawableHelper HELPER = new DrawableHelper() {
    };

    private GuiUtil() {
    }

    /** 1.18.2 中 fill 为静态方法。 */
    public static void fill(MatrixStack matrices, int x1, int y1, int x2, int y2, int color) {
        DrawableHelper.fill(matrices, x1, y1, x2, y2, color);
    }

    /** 等价于 1.20 DrawContext#drawText(…, shadow)。 */
    public static void drawText(MatrixStack matrices, TextRenderer textRenderer, String text,
                                int x, int y, int color, boolean shadow) {
        drawOrdered(matrices, textRenderer, Text.of(text).asOrderedText(), x, y, color, shadow);
    }

    /** 等价于 1.20 DrawContext#drawText(…, shadow)。 */
    public static void drawText(MatrixStack matrices, TextRenderer textRenderer, Text text,
                                int x, int y, int color, boolean shadow) {
        drawOrdered(matrices, textRenderer, text.asOrderedText(), x, y, color, shadow);
    }

    /** 等价于 1.20 DrawContext#drawText(…, shadow)。 */
    public static void drawText(MatrixStack matrices, TextRenderer textRenderer, OrderedText text,
                                int x, int y, int color, boolean shadow) {
        drawOrdered(matrices, textRenderer, text, x, y, color, shadow);
    }

    private static void drawOrdered(MatrixStack matrices, TextRenderer textRenderer, OrderedText text,
                                    int x, int y, int color, boolean shadow) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        textRenderer.draw(text, x, y, color, shadow, matrix,
                MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(),
                false, 0, 0xF000F0);
        MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().draw();
    }

    public static void drawTextWithShadow(MatrixStack matrices, TextRenderer textRenderer, String text,
                                          int x, int y, int color) {
        DrawableHelper.drawStringWithShadow(matrices, textRenderer, text, x, y, color);
    }

    public static void drawTextWithShadow(MatrixStack matrices, TextRenderer textRenderer, Text text,
                                          int x, int y, int color) {
        DrawableHelper.drawTextWithShadow(matrices, textRenderer, text, x, y, color);
    }

    public static void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, String text,
                                        int centerX, int y, int color, boolean shadow) {
        drawOrdered(matrices, textRenderer, Text.of(text).asOrderedText(),
                centerX - textRenderer.getWidth(text) / 2, y, color, shadow);
    }

    public static void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text,
                                        int centerX, int y, int color, boolean shadow) {
        drawOrdered(matrices, textRenderer, text.asOrderedText(),
                centerX - textRenderer.getWidth(text) / 2, y, color, shadow);
    }

    public static void drawCenteredTextWithShadow(MatrixStack matrices, TextRenderer textRenderer, String text,
                                                  int centerX, int y, int color) {
        DrawableHelper.drawCenteredTextWithShadow(matrices, textRenderer, Text.of(text).asOrderedText(), centerX, y, color);
    }

    public static void drawCenteredTextWithShadow(MatrixStack matrices, TextRenderer textRenderer, Text text,
                                                  int centerX, int y, int color) {
        DrawableHelper.drawCenteredTextWithShadow(matrices, textRenderer, text.asOrderedText(), centerX, y, color);
    }

    /** 等价于 1.20 DrawContext#drawBorder。 */
    public static void drawBorder(MatrixStack matrices, int x, int y, int width, int height, int color) {
        fill(matrices, x, y, x + width, y + 1, color);
        fill(matrices, x, y + height - 1, x + width, y + height, color);
        fill(matrices, x, y + 1, x + 1, y + height - 1, color);
        fill(matrices, x + width - 1, y + 1, x + width, y + height - 1, color);
    }

    /** 等价于 1.20 DrawContext#drawTexture(Identifier, x, y, u, v, w, h, texW, texH)。 */
    public static void drawTexture(MatrixStack matrices, Identifier texture,
                                   int x, int y, float u, float v,
                                   int width, int height, int textureWidth, int textureHeight) {
        RenderSystem.setShaderTexture(0, texture);
        HELPER.drawTexture(matrices, x, y, u, v, width, height, textureWidth, textureHeight);
    }

    /** 等价于 1.20 DrawContext#enableScissor(x1, y1, x2, y2)。 */
    public static void enableScissor(int x1, int y1, int x2, int y2) {
        MinecraftClient client = MinecraftClient.getInstance();
        double scale = client.getWindow().getScaleFactor();
        int scaledHeight = client.getWindow().getScaledHeight();
        RenderSystem.enableScissor(
                (int) (x1 * scale),
                (int) ((scaledHeight - y2) * scale),
                (int) ((x2 - x1) * scale),
                (int) ((y2 - y1) * scale));
    }

    public static void disableScissor() {
        RenderSystem.disableScissor();
    }
}
