# Screen 类 Fabric 到 Forge API 迁移指南

## 已完成的迁移文件

以下 Screen 类已成功从 Fabric API 迁移到 Forge API:

1. ✅ `OvenScreen.java`
2. ✅ `FreezerScreen.java`
3. ✅ `AdvanceFurnaceScreen.java`
4. ✅ `WoodenBasinScreen.java`

## 待迁移的文件列表

以下 16 个 Screen 类仍需迁移:

1. `ACDCConverterScreen.java`
2. `BambooSteamerScreen.java`
3. `BiogasDigesterControllerScreen.java`
4. `BiogasDigesterIOScreen.java`
5. `CabinetScreen.java`
6. `ChargingPostScreen.java`
7. `CuisineTableScreen.java`
8. `DeepFryerScreen.java`
9. `ElectricSteamerScreen.java`
10. `ElectriciansDeskScreen.java`
11. `FaradayGeneratorScreen.java`
12. `GasCanisterScreen.java`
13. `IceCreamMakerScreen.java`
14. `PhotovoltaicGeneratorScreen.java`
15. `TeslaCoilScreen.java`
16. `WindTurbineControllerScreen.java`

## 迁移模式对照表

### 1. 导入语句替换

| Fabric (旧) | Forge (新) |
|------------|-----------|
| `net.minecraft.client.gui.screen.ingame.HandledScreen` | `net.minecraft.client.gui.screens.inventory.AbstractContainerScreen` |
| `net.minecraft.client.gui.DrawContext` | `net.minecraft.client.gui.GuiGraphics` |
| `net.minecraft.entity.player.PlayerInventory` | `net.minecraft.world.entity.player.Inventory` |
| `net.minecraft.text.Text` | `net.minecraft.network.chat.Component` |
| `net.minecraft.util.Identifier` | `net.minecraft.resources.ResourceLocation` |
| `net.minecraft.client.MinecraftClient` | `net.minecraft.client.Minecraft` |
| `net.minecraft.client.render.GameRenderer` | `net.minecraft.client.renderer.GameRenderer` |
| `net.minecraft.sound.SoundEvents` | `net.minecraft.sounds.SoundEvents` |
| `net.minecraft.util.Formatting` | (移除,使用 Component 的样式方法) |

### 2. 类声明和方法签名

```java
// Fabric (旧)
public class XXXScreen extends HandledScreen<XXXScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(ModernDelightMain.MOD_ID, "...");
    public XXXScreen(XXXScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }
}

// Forge (新)
public class XXXScreen extends AbstractContainerScreen<XXXScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "...");
    public XXXScreen(XXXScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }
}
```

### 3. 字段名称映射

| Fabric (旧) | Forge (新) |
|------------|-----------|
| `backgroundWidth` | `imageWidth` |
| `backgroundHeight` | `imageHeight` |
| `titleX` | `titleLabelX` |
| `titleY` | `titleLabelY` |
| `playerInventoryTitleY` | `inventoryLabelY` |
| `textRenderer` | `font` |

### 4. 方法名称映射

| Fabric (旧) | Forge (新) |
|------------|-----------|
| `drawBackground(DrawContext, ...)` | `renderBg(GuiGraphics, ...)` |
| `drawMouseoverTooltip(DrawContext, ...)` | `renderTooltip(GuiGraphics, ...)` |
| `context.drawTexture(...)` | `guiGraphics.blit(...)` |
| `context.drawText(textRenderer, ...)` | `guiGraphics.drawString(font, ...)` |
| `context.drawTooltip(textRenderer, ...)` | `guiGraphics.renderTooltip(font, ...)` 或 `guiGraphics.renderComponentTooltip(font, ...)` |
| `textRenderer.getWidth(title)` | `font.width(title)` |
| `Text.translatable(...).formatted(Formatting.XXX)` | `Component.translatable(...)` (样式通过其他方式设置) |
| `MinecraftClient.getInstance()` | `Minecraft.getInstance()` |
| `SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON` | `SoundEvents.STONE_BUTTON_CLICK` |
| `GameRenderer::getPositionTexProgram` | `GameRenderer::getPositionTexShader` |

### 5. 特殊注意事项

#### FluidStackRenderer 相关
如果 Screen 使用了 `FluidStackRenderer`,需要修改:
```java
// Fabric (旧)
context.drawTooltip(textRenderer, fluidStackRenderer.getTooltip(...), Optional.empty(), x, y);

// Forge (新)
guiGraphics.renderComponentTooltip(font, fluidStackRenderer.getTooltip(...), x, y);
```

#### renderBackground 调用
```java
// Fabric (旧)
renderBackground(context);

// Forge (新)
renderBackground(guiGraphics);
```

## 批量迁移步骤

对于每个待迁移的 Screen 文件,执行以下步骤:

1. **替换导入语句**: 按照上面的对照表替换所有 Fabric 相关的导入
2. **修改类声明**: 将 `HandledScreen` 改为 `AbstractContainerScreen`
3. **修改构造函数**: 参数类型从 `PlayerInventory` 改为 `Inventory`,从 `Text` 改为 `Component`
4. **替换常量类型**: `Identifier` → `ResourceLocation`
5. **修改 init() 方法**: 更新字段名称 (titleX → titleLabelX 等)
6. **重绘背景方法**: `drawBackground` → `renderBg`,参数 `DrawContext` → `GuiGraphics`
7. **重绘提示方法**: `drawMouseoverTooltip` → `renderTooltip`
8. **替换所有绘制调用**: 
   - `context.drawTexture` → `guiGraphics.blit`
   - `context.drawText` → `guiGraphics.drawString`
   - `context.drawTooltip` → `guiGraphics.renderTooltip` 或 `renderComponentTooltip`
9. **替换渲染方法**: `render(DrawContext, ...)` → `render(GuiGraphics, ...)`
10. **更新声音事件**: `BLOCK_STONE_BUTTON_CLICK_ON` → `STONE_BUTTON_CLICK`
11. **更新 GameRenderer**: `getPositionTexProgram` → `getPositionTexShader`

## 示例: GasCanisterScreen 迁移

```java
// 关键修改点:
- 继承类: HandledScreen → AbstractContainerScreen
- 纹理类型: Identifier → ResourceLocation
- 构造函数参数: PlayerInventory → Inventory, Text → Component
- init(): titleX → titleLabelX, textRenderer.getWidth → font.width
- drawBackground → renderBg, DrawContext → GuiGraphics
- context.drawTexture → guiGraphics.blit
- drawMouseoverTooltip → renderTooltip
- context.drawTooltip → guiGraphics.renderTooltip
- Text.literal(...).formatted(Formatting.XXX) → Component.literal(...)
- render(DrawContext) → render(GuiGraphics)
```

## 验证迁移

迁移完成后,运行以下命令验证编译:
```bash
./gradlew build
```

检查是否还有编译错误,特别是:
- MenuScreens.register 的类型匹配问题
- Screen 构造函数签名是否正确
- 所有 Fabric API 调用是否已替换

## 后续工作

完成所有 Screen 类迁移后,还需要:
1. 检查并修复 `ModernDelightClient.java` 中的 MenuScreens.register 调用
2. 确保所有 ScreenHandler 类也已正确迁移到 Forge API
3. 测试所有 GUI 是否正常显示和工作
