#!/usr/bin/env python3
"""OcelotSignMod 1.20.1 -> 1.18.2 机械重映射脚本"""
import re
import pathlib
import sys

SRC = pathlib.Path(r"F:/OcelotSignMod-Minecraft-1.18.2-Fabric/src")

# (regex, replacement) 应用于整个文件文本
RULES = [
    # ---- import 重定向 ----
    (r'import net\.minecraft\.registry\.Registry;', 'import net.minecraft.util.registry.Registry;'),
    (r'import net\.minecraft\.registry\.Registries;', 'import net.minecraft.util.registry.Registry;'),
    (r'import net\.minecraft\.registry\.RegistryKey;', 'import net.minecraft.util.registry.RegistryKey;'),
    (r'import net\.minecraft\.registry\.RegistryKeys;\n', ''),
    (r'import net\.minecraft\.client\.gui\.DrawContext;', 'import net.minecraft.client.util.math.MatrixStack;'),
    (r'import net\.minecraft\.util\.math\.RotationAxis;', 'import net.minecraft.util.math.Vec3f;'),
    (r'import net\.minecraft\.client\.gui\.tooltip\.Tooltip;\n', ''),
    (r'import net\.minecraft\.loot\.context\.LootContextParameterSet;', 'import net.minecraft.loot.context.LootContext;'),
    (r'import net\.fabricmc\.fabric\.api\.itemgroup\.v1\.[A-Za-z]+;\n', ''),
    (r'import net\.fabricmc\.fabric\.api\.client\.model\.loading\.v1\.ModelLoadingPlugin;',
     'import net.fabricmc.fabric.api.client.model.ModelLoadingPlugin;'),

    # ---- 正文 ----
    (r'\bRegistries\.', 'Registry.'),
    (r'\bText\.translatable\(', 'new TranslatableText('),
    (r'\bText\.literal\(', 'new LiteralText('),
    (r'\bRotationAxis\.(POSITIVE|NEGATIVE)_(X|Y|Z)\.rotationDegrees\(', r'Vec3f.\1_\2.getDegreesQuaternion('),
    (r'\bLootContextParameterSet\.Builder\b', 'LootContext.Builder'),
    (r'\bDrawContext\b', 'MatrixStack'),
    (r'\b(context|ctx)\.getMatrices\(\)', r'\1'),
    (r'\b(context|ctx)\.fill\(', r'GuiUtil.fill(\1, '),
    (r'\b(context|ctx)\.drawText\(', r'GuiUtil.drawText(\1, '),
    (r'\b(context|ctx)\.drawTextWithShadow\(', r'GuiUtil.drawTextWithShadow(\1, '),
    (r'\b(context|ctx)\.drawCenteredText\(', r'GuiUtil.drawCenteredText(\1, '),
    (r'\b(context|ctx)\.drawCenteredTextWithShadow\(', r'GuiUtil.drawCenteredTextWithShadow(\1, '),
    (r'\b(context|ctx)\.drawBorder\(', r'GuiUtil.drawBorder(\1, '),
    (r'\b(context|ctx)\.enableScissor\(', r'GuiUtil.enableScissor('),
    (r'\b(context|ctx)\.disableScissor\(\)', 'GuiUtil.disableScissor()'),
    (r'\b(context|ctx)\.drawTexture\(', r'GuiUtil.drawTexture(\1, '),
]


def add_imports(text):
    """按需补充 TranslatableText / LiteralText / GuiUtil import"""
    need = []
    if 'new TranslatableText(' in text and 'import net.minecraft.text.TranslatableText;' not in text:
        need.append('net.minecraft.text.TranslatableText')
    if 'new LiteralText(' in text and 'import net.minecraft.text.LiteralText;' not in text:
        need.append('net.minecraft.text.LiteralText')
    if 'GuiUtil.' in text and 'import bklmc.ocelotsign.client.GuiUtil;' not in text:
        need.append('bklmc.ocelotsign.client.GuiUtil')
    if not need:
        return text
    lines = text.split('\n')
    last_import = -1
    for i, ln in enumerate(lines):
        if ln.startswith('import '):
            last_import = i
    if last_import == -1:
        return text  # 没有 import 段，跳过（不太可能）
    inserts = [f'import {n};' for n in need]
    lines[last_import + 1:last_import + 1] = inserts
    return '\n'.join(lines)


def main():
    changed = 0
    for f in SRC.rglob('*.java'):
        orig = f.read_text(encoding='utf-8')
        text = orig
        for pat, rep in RULES:
            text = re.sub(pat, rep, text)
        if 'GuiUtil.' in text:
            text = add_imports(text)
        # TranslatableText/LiteralText imports 也要补
        text2 = add_imports_only_text(text)
        if text2 != orig:
            f.write_text(text2, encoding='utf-8')
            changed += 1
    print(f'updated {changed} files')


def add_imports_only_text(text):
    need = []
    if 'new TranslatableText(' in text and 'import net.minecraft.text.TranslatableText;' not in text:
        need.append('net.minecraft.text.TranslatableText')
    if 'new LiteralText(' in text and 'import net.minecraft.text.LiteralText;' not in text:
        need.append('net.minecraft.text.LiteralText')
    if not need:
        return text
    lines = text.split('\n')
    last_import = -1
    for i, ln in enumerate(lines):
        if ln.startswith('import '):
            last_import = i
    if last_import == -1:
        return text
    inserts = [f'import {n};' for n in need]
    lines[last_import + 1:last_import + 1] = inserts
    return '\n'.join(lines)


if __name__ == '__main__':
    main()
