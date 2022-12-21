package ca.tweetzy.flight.settings;

import ca.tweetzy.flight.hooks.PlaceholderAPIHook;
import ca.tweetzy.flight.utils.Replacer;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class FlightTranslator {

    protected String mainLanguage;

    private static final Map<String, TranslationFile> translationFiles = new HashMap<>();
    private static final Map<String, Object> translations = new HashMap<>();

    public FlightTranslator(@NotNull final String mainLanguage) {
        this.mainLanguage = mainLanguage;
    }

    protected static TranslationEntry create(@NotNull final String key, @NonNull final String... contents) {
        if (!translations.containsKey(key.toLowerCase())) {
            translations.put(key.toLowerCase(), contents);
        }
        return new TranslationEntry(key, contents);
    }

    private static TranslationFile getTranslationFile(@NonNull final String language) {
        return translationFiles.getOrDefault(language, getMainTranslationFile());
    }

    private static TranslationFile getMainTranslationFile() {
        return translationFiles.get("en_us");
    }

    protected static void registerLanguage(@NonNull JavaPlugin javaPlugin, @NonNull final String languageCode) {
        translationFiles.put(languageCode, new TranslationFile(javaPlugin, languageCode));
    }

    public static String string(Player player, @NonNull String language, @NotNull TranslationEntry entry, Object... variables) {
        final TranslationFile translations = getTranslationFile(language);

        String content = (String) translations.getOr(entry.key, entry.string());
        if (content == null) return "";

        content = Replacer.replaceVariables(content, variables);

        if (player != null)
            content = PlaceholderAPIHook.tryReplace(player, content);

        // do some placeholder shit
        return content;
    }

    public static String string(Player player, @NotNull TranslationEntry entry, Object... variables) {
        return string(player, "en_us", entry, variables);
    }

    public static String string(@NotNull TranslationEntry entry, Object... variables) {
        return string(null, "en_us", entry, variables);
    }

    protected abstract void registerLanguages();

    public void setup() {
        registerLanguages();

        for (TranslationFile translationFile : translationFiles.values()) {
            translations.forEach((key, value) -> {
                final String[] contents = (String[]) value;
                translationFile.createEntry(key, contents.length > 1 ? contents : contents[0]);
            });

            translationFile.init();
        }
    }
}
