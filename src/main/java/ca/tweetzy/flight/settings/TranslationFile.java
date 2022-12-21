package ca.tweetzy.flight.settings;

import ca.tweetzy.flight.config.tweetzy.TweetzyYamlConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class TranslationFile extends TweetzyYamlConfig {

	@Getter
	public String language;

	public TranslationFile(@NotNull JavaPlugin plugin, @NotNull String language) {
		super(plugin, "/locales/" + language + ".yml");
		this.language = language;
	}
}
