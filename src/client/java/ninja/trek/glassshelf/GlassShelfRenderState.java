package ninja.trek.glassshelf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

@Environment(EnvType.CLIENT)
public class GlassShelfRenderState extends BlockEntityRenderState {
	public ItemStackRenderState[] items = new ItemStackRenderState[9];
}
