package draylar.tiered.reforge;

import com.mojang.blaze3d.systems.RenderSystem;

import draylar.tiered.config.ConfigInit;
import draylar.tiered.network.TieredClientPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ReforgeScreen<T extends ReforgeScreenHandler> extends HandledScreen<T> implements ScreenHandlerListener {

    public static final Identifier TEXTURE = new Identifier("tiered", "textures/gui/reforging_screen.png");

    public ReforgeScreen(T handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
        this.titleX = 60;
        TieredClientPacket.writeC2SSyncPosPacket(false);
    }

    @Override
    protected void init() {
        super.init();
        ((ReforgeScreenHandler) this.handler).addListener(this);
    }

    @Override
    public void removed() {
        super.removed();
        ((ReforgeScreenHandler) this.handler).removeListener(this);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableBlend();
        // this.renderForeground(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    // protected void renderForeground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    // }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        // this.drawTexture(matrices, i + 59, j + 20, 0, this.backgroundHeight + (((ReforgeScreenHandler) this.handler).getSlot(0).hasStack() ? 0 : 16), 110, 16);
        // if ((((ReforgeScreenHandler) this.handler).getSlot(0).hasStack() || ((ReforgeScreenHandler) this.handler).getSlot(1).hasStack())
        // && !((ReforgeScreenHandler) this.handler).getSlot(2).hasStack()) {
        // this.drawTexture(matrices, i + 99, j + 45, this.backgroundWidth, 0, 28, 21);
        // }

        if (this.isPointWithinBounds(6 + ConfigInit.CONFIG.xIconPosition, -17 + ConfigInit.CONFIG.yIconPosition, 20, 20, (double) mouseX, (double) mouseY))
            ReforgeScreen.drawTexture(matrices, i + 6 + ConfigInit.CONFIG.xIconPosition, j - 17 + ConfigInit.CONFIG.yIconPosition, 236, 0, 20, 18, 256, 256);
        else
            ReforgeScreen.drawTexture(matrices, i + 6 + ConfigInit.CONFIG.xIconPosition, j - 17 + ConfigInit.CONFIG.yIconPosition, 216, 0, 20, 18, 256, 256);
    }

    @Override
    public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
    }

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isPointWithinBounds(6 + ConfigInit.CONFIG.xIconPosition, -17 + ConfigInit.CONFIG.yIconPosition, 20, 18, (double) mouseX, (double) mouseY))
            TieredClientPacket.writeC2SScreenPacket(handler.pos, (int) this.client.mouse.getX(), (int) this.client.mouse.getY(), false);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    // class DoneButtonWidget extends IconButtonWidget {
    // public DoneButtonWidget(int x, int y) {
    // super(x, y, 90, 220, ScreenTexts.DONE);
    // }

    // @Override
    // public void onPress() {
    // BeaconScreen.this.client.getNetworkHandler()
    // .sendPacket(new UpdateBeaconC2SPacket(StatusEffect.getRawId(BeaconScreen.this.primaryEffect), StatusEffect.getRawId(BeaconScreen.this.secondaryEffect)));
    // ((BeaconScreen) BeaconScreen.this).client.player.closeHandledScreen();
    // }

    // @Override
    // public void tick(int level) {
    // this.active = ((BeaconScreenHandler) BeaconScreen.this.handler).hasPayment() && BeaconScreen.this.primaryEffect != null;
    // }
    // }

    private class IconButtonWidget extends PressableWidget {
        // private final int x;
        // private final int v;
        private boolean disabled;

        // protected IconButtonWidget(int x, int y, int u, int v, Text message) {
        // super(x, y, message);
        // this.u = u;
        // this.v = v;
        // }

        public IconButtonWidget(int x, int y) {
            super(x, y, 90, 220, LiteralText.EMPTY);
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            // int i = 219;
            int j = 0;
            if (!this.active) {
                j += this.width * 2;
            } else if (this.disabled) {
                j += this.width * 1;
            } else if (this.isHovered()) {
                j += this.width * 3;
            }
            this.drawTexture(matrices, this.x, this.y, j, 219, this.width, this.height);
            // this.renderExtra(matrices);
        }

        // @Override
        // protected void renderExtra(MatrixStack matrices) {
        // this.drawTexture(matrices, this.x + 2, this.y + 2, this.u, this.v, 18, 18);
        // }

        public boolean isDisabled() {
            return this.disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        @Override
        public void appendNarrations(NarrationMessageBuilder var1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPress() {
            System.out.println("TEST");
        }

        // @Override
        // public boolean shouldRenderTooltip() {
        // return this.hovered;
        // }

        // @Override
        // public void appendNarrations(NarrationMessageBuilder builder) {
        // this.appendDefaultNarrations(builder);
        // }

        // @Override
        // public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
        // BeaconScreen.this.renderTooltip(matrices, BeaconScreen.this.title, mouseX, mouseY);
        // }

    }

    // static abstract class BaseButtonWidget extends PressableWidget implements BeaconButtonWidget {
    // private boolean disabled;

    // protected BaseButtonWidget(int x, int y) {
    // super(x, y, 22, 22, LiteralText.EMPTY);
    // }

    // protected BaseButtonWidget(int x, int y, Text message) {
    // super(x, y, 22, 22, message);
    // }

    // @Override
    // public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    // RenderSystem.setShader(GameRenderer::getPositionTexShader);
    // RenderSystem.setShaderTexture(0, TEXTURE);
    // RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    // int i = 219;
    // int j = 0;
    // if (!this.active) {
    // j += this.width * 2;
    // } else if (this.disabled) {
    // j += this.width * 1;
    // } else if (this.isHovered()) {
    // j += this.width * 3;
    // }
    // this.drawTexture(matrices, this.x, this.y, j, 219, this.width, this.height);
    // this.renderExtra(matrices);
    // }

    // protected abstract void renderExtra(MatrixStack var1);

    // }

}
