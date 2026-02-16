package ninja.trek.glassshelf;

import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class GlassShelfBlockEntity extends BlockEntity implements ItemOwner, GameEventListener.Provider<GameEventListener> {
	public static final int MAX_DISPLAY_ITEMS = 9;
	private static final Logger LOGGER = LogUtils.getLogger();
	private final NonNullList<ItemStack> displayItems = NonNullList.withSize(MAX_DISPLAY_ITEMS, ItemStack.EMPTY);
	private static final int FALLBACK_INTERVAL = 200;
	private int tickCounter;
	private boolean listenerRegistered;
	private final ContainerCloseListener containerCloseListener;
	private final DynamicGameEventListener<ContainerCloseListener> dynamicListener;

	public GlassShelfBlockEntity(BlockPos pos, BlockState state) {
		super(GlassShelf.GLASS_SHELF_BLOCK_ENTITY, pos, state);
		this.tickCounter = Math.floorMod(pos.hashCode(), FALLBACK_INTERVAL);
		this.containerCloseListener = new ContainerCloseListener(pos);
		this.dynamicListener = new DynamicGameEventListener<>(this.containerCloseListener);
	}

	@Override
	public void setRemoved() {
		if (this.level instanceof ServerLevel serverLevel) {
			this.dynamicListener.remove(serverLevel);
		}
		super.setRemoved();
	}

	@Override
	public GameEventListener getListener() {
		return this.containerCloseListener;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, GlassShelfBlockEntity entity) {
		if (!entity.listenerRegistered && level instanceof ServerLevel serverLevel) {
			entity.dynamicListener.add(serverLevel);
			entity.listenerRegistered = true;
		}
		entity.tickCounter++;
		if (entity.tickCounter < FALLBACK_INTERVAL) return;
		entity.tickCounter = 0;
		entity.refreshDisplay();
	}

	public void refreshDisplay() {
		Level level = this.level;
		if (level == null || level.isClientSide()) return;
		BlockPos pos = this.getBlockPos();
		BlockState state = this.getBlockState();
		Direction facing = state.getValue(GlassShelfBlock.FACING);
		BlockPos chestPos = pos.relative(facing.getOpposite());

		Container container = getContainerAt(level, chestPos);
		if (container == null) {
			if (!allEmpty(this.displayItems)) {
				clearItems(this.displayItems);
				this.setChanged();
				level.sendBlockUpdated(pos, state, state, 3);
			}
			return;
		}

		Map<Item, Integer> counts = new HashMap<>();
		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (!stack.isEmpty()) {
				counts.merge(stack.getItem(), stack.getCount(), Integer::sum);
			}
		}

		List<Map.Entry<Item, Integer>> sorted = new ArrayList<>(counts.entrySet());
		sorted.sort(Map.Entry.<Item, Integer>comparingByValue().reversed());

		NonNullList<ItemStack> newItems = NonNullList.withSize(MAX_DISPLAY_ITEMS, ItemStack.EMPTY);
		for (int i = 0; i < Math.min(sorted.size(), MAX_DISPLAY_ITEMS); i++) {
			newItems.set(i, new ItemStack(sorted.get(i).getKey()));
		}

		if (!itemsEqual(this.displayItems, newItems)) {
			for (int i = 0; i < MAX_DISPLAY_ITEMS; i++) {
				this.displayItems.set(i, newItems.get(i));
			}
			this.setChanged();
			level.sendBlockUpdated(pos, state, state, 3);
		}
	}

	@Nullable
	private static Container getContainerAt(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof ChestBlock chestBlock) {
			return ChestBlock.getContainer(chestBlock, state, level, pos, false);
		}
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof Container container) {
			return container;
		}
		return null;
	}

	private static boolean allEmpty(NonNullList<ItemStack> items) {
		for (ItemStack item : items) {
			if (!item.isEmpty()) return false;
		}
		return true;
	}

	private static void clearItems(NonNullList<ItemStack> items) {
		for (int i = 0; i < items.size(); i++) {
			items.set(i, ItemStack.EMPTY);
		}
	}

	private static boolean itemsEqual(NonNullList<ItemStack> a, NonNullList<ItemStack> b) {
		if (a.size() != b.size()) return false;
		for (int i = 0; i < a.size(); i++) {
			if (!ItemStack.isSameItem(a.get(i), b.get(i))) return false;
		}
		return true;
	}

	public NonNullList<ItemStack> getDisplayItems() {
		return this.displayItems;
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.displayItems.clear();
		ContainerHelper.loadAllItems(input, this.displayItems);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		ContainerHelper.saveAllItems(output, this.displayItems, true);
	}

	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
		CompoundTag tag;
		try (ProblemReporter.ScopedCollector collector = new ProblemReporter.ScopedCollector(this.problemPath(), LOGGER)) {
			TagValueOutput output = TagValueOutput.createWithContext(collector, provider);
			ContainerHelper.saveAllItems(output, this.displayItems, true);
			tag = output.buildResult();
		}
		return tag;
	}

	@Override
	public Level level() {
		return this.level;
	}

	@Override
	public Vec3 position() {
		return this.getBlockPos().getCenter();
	}

	@Override
	public float getVisualRotationYInDegrees() {
		return ((Direction)this.getBlockState().getValue(GlassShelfBlock.FACING)).getOpposite().toYRot();
	}

	private class ContainerCloseListener implements GameEventListener {
		private final PositionSource positionSource;

		ContainerCloseListener(BlockPos pos) {
			this.positionSource = new BlockPositionSource(pos);
		}

		@Override
		public PositionSource getListenerSource() {
			return this.positionSource;
		}

		@Override
		public int getListenerRadius() {
			return 2;
		}

		@Override
		public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 pos) {
			if (event.is(GameEvent.CONTAINER_CLOSE)) {
				refreshDisplay();
				return true;
			}
			return false;
		}
	}
}
