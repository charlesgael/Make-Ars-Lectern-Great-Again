package icu.kudikan.make_ars_lectern_great_again;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = MakeArsLecternGreatAgain.MODID, dist = Dist.CLIENT)

public class MakeArsLecternGreatAgainClient {
    public MakeArsLecternGreatAgainClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
