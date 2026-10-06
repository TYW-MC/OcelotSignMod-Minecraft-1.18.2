package bklmc.ocelotsign.item;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import net.minecraft.text.TranslatableText;

/**
 * 墙面道路指示牌方块对应的物品
 *
 * @see AbstractRoadSignBlockItem
 * @see bklmc.ocelotsign.block.WallRoadSignBlocks
 */
public class WallRoadSignBlockItem extends AbstractRoadSignBlockItem {

    private static final Text HINT_LINE_1 = new TranslatableText(Tooltip.WALL_ROAD_SIGN_TOOLTIP_3).formatted(Formatting.GRAY);
    private static final Text HINT_LINE_2 = new TranslatableText(Tooltip.WALL_ROAD_SIGN_TOOLTIP_4).formatted(Formatting.GRAY);
    private static final Text HINT_LINE_3 = new TranslatableText(Tooltip.WALL_ROAD_SIGN_TOOLTIP_5).formatted(Formatting.GRAY);
    private static final Text HINT_LINE_4 = new TranslatableText(Tooltip.WALL_ROAD_SIGN_TOOLTIP_6).formatted(Formatting.GRAY);
    private static final Text HINT_LINE_5 = new TranslatableText(Tooltip.WALL_ROAD_SIGN_TOOLTIP_7).formatted(Formatting.GRAY);

    public WallRoadSignBlockItem(net.minecraft.block.Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    protected void addHintTooltip(List<Text> tooltip) {
        tooltip.add(HINT_LINE_1);
        tooltip.add(HINT_LINE_2);
        tooltip.add(HINT_LINE_3);
        tooltip.add(HINT_LINE_4);
        tooltip.add(HINT_LINE_5);
    }
}
