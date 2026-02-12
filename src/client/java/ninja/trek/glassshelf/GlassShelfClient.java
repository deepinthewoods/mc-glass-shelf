package ninja.trek.glassshelf;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class GlassShelfClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRenderers.register(GlassShelf.GLASS_SHELF_BLOCK_ENTITY, GlassShelfRenderer::new);
		BlockRenderLayerMap.putBlock(GlassShelf.GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
	}
}
