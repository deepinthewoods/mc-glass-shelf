package ninja.trek.glassshelf;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlassShelf implements ModInitializer {
	public static final String MOD_ID = "glass-shelf";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Identifier GLASS_SHELF_ID = Identifier.fromNamespaceAndPath(MOD_ID, "glass_shelf");

	public static final Block GLASS_SHELF_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		GLASS_SHELF_ID,
		new GlassShelfBlock(BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, GLASS_SHELF_ID))
			.sound(SoundType.GLASS)
			.strength(0.3F)
			.noOcclusion())
	);

	public static final Item GLASS_SHELF_ITEM = Registry.register(
		BuiltInRegistries.ITEM,
		GLASS_SHELF_ID,
		new BlockItem(GLASS_SHELF_BLOCK, new Item.Properties()
			.setId(ResourceKey.create(Registries.ITEM, GLASS_SHELF_ID))
			.useBlockDescriptionPrefix())
	);

	public static final BlockEntityType<GlassShelfBlockEntity> GLASS_SHELF_BLOCK_ENTITY = Registry.register(
		BuiltInRegistries.BLOCK_ENTITY_TYPE,
		Identifier.fromNamespaceAndPath(MOD_ID, "glass_shelf"),
		FabricBlockEntityTypeBuilder.create(GlassShelfBlockEntity::new, GLASS_SHELF_BLOCK).build()
	);

	@Override
	public void onInitialize() {
		LOGGER.info("Glass Shelf mod initialized");
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
			entries.accept(GLASS_SHELF_ITEM);
		});
	}
}
