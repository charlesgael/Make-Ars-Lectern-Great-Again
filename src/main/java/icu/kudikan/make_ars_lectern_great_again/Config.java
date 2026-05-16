package icu.kudikan.make_ars_lectern_great_again;

import icu.kudikan.make_ars_lectern_great_again.utils.ConfigBuilder;
import net.neoforged.neoforge.common.ModConfigSpec;


public class Config {
    public static final Config INSTANCE;
    public static final ModConfigSpec SPEC;

    static {
        var builder = new ConfigBuilder();
        INSTANCE = new Config(builder);
        SPEC = builder.build();
    }

    public ModConfigSpec.BooleanValue disableSearchAutoFocus;

    private Config(ConfigBuilder builder) {
        disableSearchAutoFocus = builder.start("disableSearchAutoFocus",
                        "Disable Search Filed Auto Focus",
                        "Whether to disable search filed auto focus.")
                .define("disableSearchAutoFocus", true);
    }
}
