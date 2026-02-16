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

	private static Block registerShelfBlock(String name) {
		Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
		return Registry.register(
			BuiltInRegistries.BLOCK, id,
			new GlassShelfBlock(BlockBehaviour.Properties.of()
				.setId(ResourceKey.create(Registries.BLOCK, id))
				.sound(SoundType.GLASS)
				.strength(0.3F)
				.noOcclusion())
		);
	}

	private static Item registerShelfItem(String name, Block block) {
		Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
		return Registry.register(
			BuiltInRegistries.ITEM, id,
			new BlockItem(block, new Item.Properties()
				.setId(ResourceKey.create(Registries.ITEM, id))
				.useBlockDescriptionPrefix())
		);
	}

	public static final Block GLASS_SHELF_BLOCK = registerShelfBlock("glass_shelf");
	public static final Block OAK_GLASS_SHELF_BLOCK = registerShelfBlock("oak_glass_shelf");
	public static final Block SPRUCE_GLASS_SHELF_BLOCK = registerShelfBlock("spruce_glass_shelf");
	public static final Block BIRCH_GLASS_SHELF_BLOCK = registerShelfBlock("birch_glass_shelf");
	public static final Block JUNGLE_GLASS_SHELF_BLOCK = registerShelfBlock("jungle_glass_shelf");
	public static final Block ACACIA_GLASS_SHELF_BLOCK = registerShelfBlock("acacia_glass_shelf");
	public static final Block DARK_OAK_GLASS_SHELF_BLOCK = registerShelfBlock("dark_oak_glass_shelf");
	public static final Block MANGROVE_GLASS_SHELF_BLOCK = registerShelfBlock("mangrove_glass_shelf");
	public static final Block CHERRY_GLASS_SHELF_BLOCK = registerShelfBlock("cherry_glass_shelf");
	public static final Block PALE_OAK_GLASS_SHELF_BLOCK = registerShelfBlock("pale_oak_glass_shelf");
	public static final Block CRIMSON_GLASS_SHELF_BLOCK = registerShelfBlock("crimson_glass_shelf");
	public static final Block WARPED_GLASS_SHELF_BLOCK = registerShelfBlock("warped_glass_shelf");
	public static final Block BAMBOO_GLASS_SHELF_BLOCK = registerShelfBlock("bamboo_glass_shelf");

	public static final Item GLASS_SHELF_ITEM = registerShelfItem("glass_shelf", GLASS_SHELF_BLOCK);
	public static final Item OAK_GLASS_SHELF_ITEM = registerShelfItem("oak_glass_shelf", OAK_GLASS_SHELF_BLOCK);
	public static final Item SPRUCE_GLASS_SHELF_ITEM = registerShelfItem("spruce_glass_shelf", SPRUCE_GLASS_SHELF_BLOCK);
	public static final Item BIRCH_GLASS_SHELF_ITEM = registerShelfItem("birch_glass_shelf", BIRCH_GLASS_SHELF_BLOCK);
	public static final Item JUNGLE_GLASS_SHELF_ITEM = registerShelfItem("jungle_glass_shelf", JUNGLE_GLASS_SHELF_BLOCK);
	public static final Item ACACIA_GLASS_SHELF_ITEM = registerShelfItem("acacia_glass_shelf", ACACIA_GLASS_SHELF_BLOCK);
	public static final Item DARK_OAK_GLASS_SHELF_ITEM = registerShelfItem("dark_oak_glass_shelf", DARK_OAK_GLASS_SHELF_BLOCK);
	public static final Item MANGROVE_GLASS_SHELF_ITEM = registerShelfItem("mangrove_glass_shelf", MANGROVE_GLASS_SHELF_BLOCK);
	public static final Item CHERRY_GLASS_SHELF_ITEM = registerShelfItem("cherry_glass_shelf", CHERRY_GLASS_SHELF_BLOCK);
	public static final Item PALE_OAK_GLASS_SHELF_ITEM = registerShelfItem("pale_oak_glass_shelf", PALE_OAK_GLASS_SHELF_BLOCK);
	public static final Item CRIMSON_GLASS_SHELF_ITEM = registerShelfItem("crimson_glass_shelf", CRIMSON_GLASS_SHELF_BLOCK);
	public static final Item WARPED_GLASS_SHELF_ITEM = registerShelfItem("warped_glass_shelf", WARPED_GLASS_SHELF_BLOCK);
	public static final Item BAMBOO_GLASS_SHELF_ITEM = registerShelfItem("bamboo_glass_shelf", BAMBOO_GLASS_SHELF_BLOCK);

	public static final BlockEntityType<GlassShelfBlockEntity> GLASS_SHELF_BLOCK_ENTITY = Registry.register(
		BuiltInRegistries.BLOCK_ENTITY_TYPE,
		Identifier.fromNamespaceAndPath(MOD_ID, "glass_shelf"),
		FabricBlockEntityTypeBuilder.create(GlassShelfBlockEntity::new,
			GLASS_SHELF_BLOCK, OAK_GLASS_SHELF_BLOCK, SPRUCE_GLASS_SHELF_BLOCK,
			BIRCH_GLASS_SHELF_BLOCK, JUNGLE_GLASS_SHELF_BLOCK, ACACIA_GLASS_SHELF_BLOCK,
			DARK_OAK_GLASS_SHELF_BLOCK, MANGROVE_GLASS_SHELF_BLOCK, CHERRY_GLASS_SHELF_BLOCK,
			PALE_OAK_GLASS_SHELF_BLOCK, CRIMSON_GLASS_SHELF_BLOCK, WARPED_GLASS_SHELF_BLOCK,
			BAMBOO_GLASS_SHELF_BLOCK
		).build()
	);

	@Override
	public void onInitialize() {
		LOGGER.info("Glass Shelf mod initialized");
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
			entries.accept(GLASS_SHELF_ITEM);
			entries.accept(OAK_GLASS_SHELF_ITEM);
			entries.accept(SPRUCE_GLASS_SHELF_ITEM);
			entries.accept(BIRCH_GLASS_SHELF_ITEM);
			entries.accept(JUNGLE_GLASS_SHELF_ITEM);
			entries.accept(ACACIA_GLASS_SHELF_ITEM);
			entries.accept(DARK_OAK_GLASS_SHELF_ITEM);
			entries.accept(MANGROVE_GLASS_SHELF_ITEM);
			entries.accept(CHERRY_GLASS_SHELF_ITEM);
			entries.accept(PALE_OAK_GLASS_SHELF_ITEM);
			entries.accept(CRIMSON_GLASS_SHELF_ITEM);
			entries.accept(WARPED_GLASS_SHELF_ITEM);
			entries.accept(BAMBOO_GLASS_SHELF_ITEM);
		});
	}
}
