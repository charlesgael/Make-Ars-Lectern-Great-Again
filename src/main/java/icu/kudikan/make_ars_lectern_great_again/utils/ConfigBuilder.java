package icu.kudikan.make_ars_lectern_great_again.utils;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.CheckReturnValue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigBuilder {
    public static final Map<String, String> configTranslations = new ConcurrentHashMap<>();
    private final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    private static String configTranslationKey(String key) {
        return "make_ars_lectern_great_again.configuration." + key;
    }

    @CheckReturnValue
    public ModConfigSpec.Builder start(String key, String title, String... comment) {
        if (comment.length == 0) {
            throw new IllegalArgumentException("Comment cannot be empty");
        } else {
            String translationKey = configTranslationKey(key);
            configTranslations.put(translationKey, title);
            configTranslations.put(translationKey + ".tooltip", String.join(" ", comment));
            return this.builder.translation(translationKey).comment(comment);
        }
    }

    public ModConfigSpec build() {
        return this.builder.build();
    }
}
