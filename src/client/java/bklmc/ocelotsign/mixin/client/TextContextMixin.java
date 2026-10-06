package bklmc.ocelotsign.mixin.client;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.text.TextContext;

/**
 * 修复 mishanguc 在 1.18.2 下渲染文本时丢失字体的问题。
 *
 * <p>mishanguc 的 {@code TextContext.reformatText()} 用
 * {@code formattedText = text.copy();} 生成渲染用的文本对象，但 1.18.2 的
 * {@code LiteralText.copy()} 只复制字符串、<b>不复制样式</b>
 * （1.19+ 的 {@code Text.copy()} 才会连同样式一起复制，mishanguc 高版本因此没有问题）。
 * 于是在 1.18.2 上，通过 {@code -json {"font":"...","text":"..."}} 指定的字体
 * 会在渲染前被静默丢弃，全部回退成原版字体 —— 表现为"字体怎么选都不生效"。
 * （样式同样会被丢弃，因此 {@code -json} 里的颜色、加粗等同理，这里只补回字体。）
 *
 * <p>{@code TextContext} 另有自己的 {@code bold}/{@code italic}/{@code color} 等字段，
 * 它们在 {@code copy()} 之后才被写入 {@code formattedText}，所以这里在
 * {@code reformatText()} 返回后只补回字体，不会覆盖它们。
 *
 * @see TextContext
 */
@Mixin(value = TextContext.class, remap = false)
public abstract class TextContextMixin {

    /** mishanguc 中存放"原始文本对象"的字段（其样式保存着字体）。 */
    @Shadow
    public MutableText text;

    /** mishanguc 中真正用于渲染的文本对象。 */
    @Shadow
    private MutableText formattedText;

    /**
     * 在 {@code reformatText()} 重建渲染文本之后，把原始文本的字体补回渲染文本。
     *
     * @param ci 回调信息
     */
    @Inject(method = "reformatText", at = @At("RETURN"), remap = false)
    private void ocelotsign$keepFont(CallbackInfo ci) {
        if (formattedText == null || text == null) {
            return;
        }
        final Identifier font = text.getStyle().getFont();
        if (Style.DEFAULT_FONT_ID.equals(font)) {
            // 没有指定自定义字体，保持原版行为。
            return;
        }
        final Style current = formattedText.getStyle();
        if (!font.equals(current.getFont())) {
            formattedText.setStyle(current.withFont(font));
        }
    }
}
