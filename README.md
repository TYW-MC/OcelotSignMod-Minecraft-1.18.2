# 豹猫指示牌模组 (Ocelot Sign Mod) — Minecraft 1.18.2 / Fabric

[![Release](https://img.shields.io/github/v/release/TYW-MC/OcelotSignMod-Minecraft-1.18.2?include_prereleases&color=orange)](https://github.com/TYW-MC/OcelotSignMod-Minecraft-1.18.2/releases)

作为"迷上城建"生态的重要拓展，**"豹猫指示牌模组"（Ocelot Sign Mod）**是一款基于 Fabric 平台开发的专业指示牌模组。该模组以道路交通指引为核心，继承了"迷上城建"中高度灵活的文字与图案编辑系统，旨在为玩家的城市建设提供更真实的交通细节。

本仓库是原作者 [Creeper-Cola123/OcelotSignMod-Minecraft](https://github.com/Creeper-Cola123/ocelotsignmod-minecraft) 的 **Minecraft 1.18.2 / Fabric** 移植版本。

## ✨ 核心特性 (Key Features)

* **🗺️ 本土化图案库**：内置深度还原的中国大陆道路指示牌图案，并收录了大量实用的公共提示符号。
* **🛠️ 专属定制方块**：新增支持自定义编辑的专属道路指示牌方块，以及配套的指示牌立柱，让路牌搭建更加自然。
* **🛣️ 丰富的路面标线**：在道路方块的基础上，特别补充了地面的"预告导向箭头"、醒目的橙色箭头以及各类禁止类箭头，进一步完善路面交通的引导细节。

## 📚 使用文档 (Documentation)

结合官方提供的**《图案与字体列表》**，玩家可以精准查阅并自由调用海量图案资源。

> 🔗 详情请查阅文档：[点击此处查看《图案与字体列表》](https://creeper-cola123.github.io/OcelotSignMod_Docs/)

## ⚙️ 前置依赖 (Dependencies)

| | |
| --- | --- |
| Minecraft | 1.18.2 |
| 加载器 | Fabric Loader 0.14.25 或更新 |
| Fabric API | 0.77.0+1.18.2 或更新 |
| 前置模组 | [迷上城建 (Mishang Urban Construction) 1.6.5](https://github.com/TYW-MC/Mishang-Urban-Construction-1.18.2-1.6.5) |
| Java | 17 |

> 本模组也被证实可在 Forge 1.18.2 + Sinytra Connector 环境下运行。

## 🔨 从源码构建 (Building)

```bash
./gradlew build
```

需要 JDK 17。产物位于 `build/libs/ocelotsignmod-<版本>.jar`。
每次 push 会自动构建，打 `v*` 标签会自动发布 Release。

## 📄 开源协议 (License)

本模组是"迷上城建"的附属模组，根据前置模组的开源协议，本项目主体采用 **LGPL-3.0** 协议开源。

原作者为 [Creeper-Cola123](https://github.com/Creeper-Cola123/ocelotsignmod-minecraft)，本项目是其在 Minecraft 1.18.2 / Fabric 平台上的移植。
**请不要向上游作者提交本移植版的问题**，相关问题请在本仓库的 [Issues](https://github.com/TYW-MC/OcelotSignMod-Minecraft-1.18.2/issues) 反馈。

模组内包含的第三方资源与修改遵循以下协议：

* 第三方字体：遵循 **OFL-1.1** (Open Font License) 协议。
* 前置代码修改：遵循 **LGPL-3.0** 协议。

完整协议与详细的版权声明，请参阅本仓库的 [LICENSE](LICENSE) 文件。

---

欢迎加入 QQ 群 871229937 进行交流。你也可以在 GitHub 中报告问题。
