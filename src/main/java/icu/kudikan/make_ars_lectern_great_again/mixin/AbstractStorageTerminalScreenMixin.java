package icu.kudikan.make_ars_lectern_great_again.mixin;

import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.client.container.AbstractStorageTerminalScreen;
import com.hollingsworth.arsnouveau.client.container.StorageTerminalMenu;
import com.hollingsworth.arsnouveau.client.gui.NoShadowTextField;
import icu.kudikan.make_ars_lectern_great_again.Config;
import icu.kudikan.make_ars_lectern_great_again.utils.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractStorageTerminalScreen.class)
public abstract class AbstractStorageTerminalScreenMixin<T extends StorageTerminalMenu> extends AbstractContainerScreen<T> {
    @Shadow(remap = false)
    protected NoShadowTextField searchField;
    @Shadow
    protected boolean expanded;

//    @Unique
//    int maga$rowCount;

//    @Unique
//    private StateButton maga$buttonRowCount;

    public AbstractStorageTerminalScreenMixin(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Shadow
    public abstract ResourceLocation getGui();

    // Search Focused Section START
    @Redirect(method = "init()V",
            at = @At(value = "INVOKE",
                    target = "Lcom/hollingsworth/arsnouveau/client/container/AbstractStorageTerminalScreen;setFocused(Lnet/minecraft/client/gui/components/events/GuiEventListener;)V"))
    public void redirectSetFocused(AbstractStorageTerminalScreen instance, GuiEventListener guiEventListener) {
        if (Config.INSTANCE.disableSearchAutoFocus.isFalse()) {
            instance.setFocused(guiEventListener);
        }
//        maga$buttonRowCount = addRenderableWidget(new StorageSettingsButton(leftPos - 17, topPos - 14, 22, 12, 44, 13, 0, ArsNouveau.prefix("textures/gui/search_sync.png"), b -> {
//            maga$rowCount = (maga$rowCount + 1) % Util.ROW_COUNT;
//            maga$buttonRowCount.state = maga$rowCount;
//        }));
    }

    @Inject(method = "charTyped(CI)Z",
            at = @At(value = "INVOKE",
                    target = "Lcom/hollingsworth/arsnouveau/client/container/AbstractStorageTerminalScreen;clearFocus()V"),
            cancellable = true)
    public void injectChatTyped(char codePoint, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (Config.INSTANCE.disableSearchAutoFocus.isTrue()) {
            if (!searchField.isFocused()) cir.setReturnValue(false);
        }
    }

    @Inject(method = "keyPressed(III)Z",
            at = @At(value = "INVOKE",
                    target = "Lcom/hollingsworth/arsnouveau/client/container/AbstractStorageTerminalScreen;clearFocus()V",
                    ordinal = 0),
            cancellable = true)
    public void injectKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (Config.INSTANCE.disableSearchAutoFocus.isTrue()) {
            if (!searchField.isFocused()) cir.setReturnValue(false);
        }
    }
    // Search Focused Section END

    @ModifyConstant(
            method = "scrollTo(F)V",
            constant = {
                    @Constant(intValue = 3, ordinal = 0),
                    @Constant(intValue = 7, ordinal = 0)
            }
    )
    private int replaceLinesConstant(int original) {
        return switch (original) {
            case 3 -> Constants.ROW_COUNT;
            case 7 -> Constants.EXPEND_ROW_COUNT;
            default -> original;
        };
    }

    @Inject(method = "init()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;init()V",
                    shift = At.Shift.AFTER))
    public void injectAfterInit(CallbackInfo ci) {
        topPos -= (Constants.ROW_COUNT - 3) * 9;  // Setup topPos to change button's y Pos
    }

    @Inject(method = "init()V",
            at = @At("TAIL"))
    public void injectInitTail(CallbackInfo ci) {
        topPos += (Constants.ROW_COUNT - 3) * 9;  // Reverse topPos
    }

    @ModifyVariable(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(value = "STORE", ordinal = 0),
            name = "l")
    private int modifyScrollRenderTopPos(int value) {
        return value - (Constants.ROW_COUNT - 3) * 18;
    }

    @ModifyVariable(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(value = "STORE", ordinal = 0),
            name = "j1")
    private int modifyScrollRenderBottomPos(int value) {
        if (expanded) return value - 4 * 18;
        return value;
    }

    @Inject(method = "renderBg(Lnet/minecraft/client/gui/GuiGraphics;FII)V",
            at = @At(value = "TAIL"))
    public void injectRenderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        var top = this.topPos - (Constants.ROW_COUNT - 3) * 18;
        // Header
        graphics.blit(getGui(), this.leftPos, top, 0, 0, this.imageWidth, 38);
        // Extra Rows
        for (int i = 0; i < (Constants.ROW_COUNT - 3); i++) {
            graphics.blit(getGui(), this.leftPos, top + 38 + i * 18, 0, 38, this.imageWidth, 18);
        }
        // Search Bar
        graphics.blit(ArsNouveau.prefix("textures/gui/search_paper.png"), this.leftPos + 102, top + 3, 0, 0, 72, 15, 72, 15);
    }
}
