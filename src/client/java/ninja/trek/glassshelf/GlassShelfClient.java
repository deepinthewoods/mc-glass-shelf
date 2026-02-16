package ninja.trek.glassshelf;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class GlassShelfClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		GlassShelfConfig.load();
		BlockEntityRenderers.register(GlassShelf.GLASS_SHELF_BLOCK_ENTITY, GlassShelfRenderer::new);

		BlockRenderLayerMap.putBlock(GlassShelf.GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GlassShelf.OAK_GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GlassShelf.SPRUCE_GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GlassShelf.BIRCH_GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GlassShelf.JUNGLE_GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GlassShelf.ACACIA_GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GlassShelf.DARK_OAK_GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GlassShelf.MANGROVE_GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GlassShelf.CHERRY_GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GlassShelf.PALE_OAK_GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GlassShelf.CRIMSON_GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GlassShelf.WARPED_GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GlassShelf.BAMBOO_GLASS_SHELF_BLOCK, ChunkSectionLayer.CUTOUT);
	}
}
