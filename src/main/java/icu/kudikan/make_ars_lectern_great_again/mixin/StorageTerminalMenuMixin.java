package icu.kudikan.make_ars_lectern_great_again.mixin;

import com.hollingsworth.arsnouveau.client.container.StorageTerminalMenu;
import icu.kudikan.make_ars_lectern_great_again.utils.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(StorageTerminalMenu.class)
public class StorageTerminalMenuMixin {

    @ModifyConstant(method = "addStorageSlots(Z)V",
            constant = {
                    @Constant(intValue = 3, ordinal = 0),
                    @Constant(intValue = 3, ordinal = 1),
                    @Constant(intValue = 21, ordinal = 0),
                    @Constant(intValue = 7, ordinal = 0)
            })
    public int addStorageSlots(int original) {
        return switch (original) {
            case 3 -> Constants.ROW_COUNT;
            case 7 -> Constants.EXPEND_ROW_COUNT;
            case 21 -> 21 - (Constants.ROW_COUNT - 3) * 18; // Slot y Pos
            default -> original;
        };
    }
}
