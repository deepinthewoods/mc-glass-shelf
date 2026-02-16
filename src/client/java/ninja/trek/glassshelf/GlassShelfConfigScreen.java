package ninja.trek.glassshelf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class GlassShelfConfigScreen extends Screen {
	private final Screen parent;

	public GlassShelfConfigScreen(Screen parent) {
		super(Component.translatable("glass-shelf.config.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		CycleButton<Boolean> modeButton = CycleButton.onOffBuilder(GlassShelfConfig.threeItemMode)
			.create(this.width / 2 - 100, this.height / 2 - 12, 200, 20,
				Component.translatable("glass-shelf.config.three_item_mode"),
				(button, value) -> GlassShelfConfig.threeItemMode = value);
		this.addRenderableWidget(modeButton);

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
			GlassShelfConfig.save();
			this.minecraft.setScreen(this.parent);
		}).bounds(this.width / 2 - 100, this.height / 2 + 24, 200, 20).build());
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(this.parent);
	}
}
