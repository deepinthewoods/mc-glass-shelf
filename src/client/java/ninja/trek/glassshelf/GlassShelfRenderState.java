package ninja.trek.glassshelf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

@Environment(EnvType.CLIENT)
public class GlassShelfRenderState extends BlockEntityRenderState {
	public final ItemStackRenderState[] items = new ItemStackRenderState[9];
	public int displayCount;
	public boolean threeItemMode;

	{
		for (int i = 0; i < items.length; i++) {
			items[i] = new ItemStackRenderState();
		}
	}
}
