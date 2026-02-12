package ninja.trek.glassshelf.mixin;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;
import ninja.trek.glassshelf.GlassShelfBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ContainerCloseShelfRefreshMixin {

	@Inject(method = "doCloseContainer", at = @At("HEAD"))
	private void glassShelf$onCloseContainer(CallbackInfo ci) {
		Player player = (Player) (Object) this;
		Set<BlockPos> containerPositions = new HashSet<>();

		for (Slot slot : player.containerMenu.slots) {
			if (slot.container instanceof BlockEntity be) {
				containerPositions.add(be.getBlockPos());
			}
		}

		for (BlockPos containerPos : containerPositions) {
			for (Direction dir : Direction.values()) {
				BlockPos neighborPos = containerPos.relative(dir);
				if (player.level().getBlockEntity(neighborPos) instanceof GlassShelfBlockEntity shelf) {
					shelf.refreshDisplay();
				}
			}
		}
	}
}
