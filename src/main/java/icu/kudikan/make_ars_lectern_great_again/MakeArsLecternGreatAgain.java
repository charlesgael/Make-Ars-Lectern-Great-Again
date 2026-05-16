package icu.kudikan.make_ars_lectern_great_again;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(MakeArsLecternGreatAgain.MODID)
public class MakeArsLecternGreatAgain {
    public static final String MODID = "make_ars_lectern_great_again";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MakeArsLecternGreatAgain(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }
}
