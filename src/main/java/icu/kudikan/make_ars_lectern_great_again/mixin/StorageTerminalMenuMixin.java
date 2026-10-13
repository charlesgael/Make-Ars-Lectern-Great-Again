package icu.kudikan.make_ars_lectern_great_again.mixin;

import com.hollingsworth.arsnouveau.client.container.SlotStorage;
import com.hollingsworth.arsnouveau.client.container.StorageTerminalMenu;
import com.hollingsworth.arsnouveau.common.block.tile.StorageLecternTile;
import icu.kudikan.make_ars_lectern_great_again.utils.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(StorageTerminalMenu.class)
public class StorageTerminalMenuMixin {
    @Shadow(remap = false)
    protected StorageLecternTile te;
    @Shadow(remap = false)
    protected List<SlotStorage> storageSlotList;

    // Replaces the vanilla layout: the row count may change at runtime (window resize),
    // so slots are created on demand and repositioned on every call.
    @Inject(method = "addStorageSlots(Z)V", at = @At("HEAD"), cancellable = true, remap = false)
    public void addStorageSlots(boolean expanded, CallbackInfo ci) {
        int rows = Constants.rows();
        int lines = Constants.expandedRows();
        int top = 21 - Constants.extraRows() * Constants.ROW_HEIGHT; // Slot y Pos
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < 9; j++) {
                int index = i * 9 + j;
                int x = 13 + j * 18;
                int y = top + i * Constants.ROW_HEIGHT;
                boolean show = expanded || i < rows;
                if (index < storageSlotList.size()) {
                    SlotStorage slot = storageSlotList.get(index);
                    slot.xDisplayPosition = x;
                    slot.yDisplayPosition = y;
                    slot.show = show;
                } else {
                    storageSlotList.add(new SlotStorage(te, index, x, y, show));
                }
            }
        }
        // Slots left over from a previous, taller layout
        for (int index = lines * 9; index < storageSlotList.size(); index++) {
            SlotStorage slot = storageSlotList.get(index);
            slot.show = false;
            slot.setStack(null);
        }
        ci.cancel();
    }
}
