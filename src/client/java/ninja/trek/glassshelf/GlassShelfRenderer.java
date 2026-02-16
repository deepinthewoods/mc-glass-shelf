package ninja.trek.glassshelf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.HashCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class GlassShelfRenderer implements BlockEntityRenderer<GlassShelfBlockEntity, GlassShelfRenderState> {
	private static final float ITEM_SIZE = 0.2F;
	private final ItemModelResolver itemModelResolver;

	public GlassShelfRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	public GlassShelfRenderState createRenderState() {
		return new GlassShelfRenderState();
	}

	public void extractRenderState(
		GlassShelfBlockEntity entity, GlassShelfRenderState state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay
	) {
		BlockEntityRenderer.super.extractRenderState(entity, state, partialTick, cameraPos, crumblingOverlay);
		NonNullList<ItemStack> displayItems = entity.getDisplayItems();
		int seed = HashCommon.long2int(entity.getBlockPos().asLong());

		boolean threeItem = GlassShelfConfig.threeItemMode;
		state.threeItemMode = threeItem;
		int maxItems = threeItem ? 3 : 9;
		int count = 0;

		for (int i = 0; i < displayItems.size() && count < maxItems; i++) {
			ItemStack itemStack = displayItems.get(i);
			if (!itemStack.isEmpty()) {
				this.itemModelResolver.updateForTopItem(state.items[count], itemStack, ItemDisplayContext.ON_SHELF, entity.level(), entity, seed + i);
				count++;
			}
		}
		state.displayCount = count;
	}

	public void submit(GlassShelfRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		Direction direction = (Direction)state.blockState.getValue(GlassShelfBlock.FACING);
		float rotation = direction.getAxis().isHorizontal() ? -direction.toYRot() : 180.0F;

		for (int i = 0; i < state.displayCount; i++) {
			ItemStackRenderState itemState = state.items[i];
			int col, row;
			if (state.threeItemMode) {
				col = i;
				row = 1;
			} else {
				col = i % 3;
				row = i / 3;
			}
			submitItem(state, itemState, poseStack, collector, col, row, rotation);
		}
	}

	private void submitItem(
		GlassShelfRenderState state, ItemStackRenderState itemState, PoseStack poseStack, SubmitNodeCollector collector,
		int col, int row, float rotation
	) {
		float x = (col - 1) * 0.25F;
		float y = (1 - row) * 0.34375F;
		float z = -0.375F;

		poseStack.pushPose();
		poseStack.translate(0.5F, 0.5F, 0.5F);
		poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
		poseStack.translate(x, y, z);
		poseStack.scale(ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);

		AABB box = itemState.getModelBoundingBox();
		double yOffset = -box.minY + -(box.maxY - box.minY) / 2.0;
		poseStack.translate(0.0, yOffset, 0.0);

		itemState.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		poseStack.popPose();
	}
}
