package ca.tweetzy.flight.settings;


import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

public  class TranslationManager extends FlightTranslator {

    private final JavaPlugin plugin;

    public TranslationManager(@NonNull final JavaPlugin plugin) {
        super("en_us");
        this.plugin = plugin;
    }

    @Override
    protected void registerLanguages() {
        registerLanguage(this.plugin, mainLanguage);
    }

    public static TranslationEntry NO_MONEY = create("not enough money", "&cYou do not have enough money!");

}
