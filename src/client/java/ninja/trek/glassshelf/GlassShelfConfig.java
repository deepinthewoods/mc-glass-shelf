package ninja.trek.glassshelf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public class GlassShelfConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("glass-shelf.json");

	public static boolean threeItemMode = false;

	private static class Data {
		boolean threeItemMode = false;
	}

	public static void load() {
		if (Files.exists(CONFIG_PATH)) {
			try {
				String json = Files.readString(CONFIG_PATH);
				Data data = GSON.fromJson(json, Data.class);
				if (data != null) {
					threeItemMode = data.threeItemMode;
				}
			} catch (IOException | com.google.gson.JsonSyntaxException e) {
				GlassShelf.LOGGER.warn("Failed to load glass-shelf config", e);
			}
		}
	}

	public static void save() {
		Data data = new Data();
		data.threeItemMode = threeItemMode;
		try {
			Files.writeString(CONFIG_PATH, GSON.toJson(data));
		} catch (IOException e) {
			GlassShelf.LOGGER.warn("Failed to save glass-shelf config", e);
		}
	}
}
