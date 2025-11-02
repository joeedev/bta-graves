package dev.joee.btagraves;

import dev.joee.btagraves.block.BlockLogicGrave;
import dev.joee.btagraves.item.ItemGrave;
import dev.joee.btagraves.tileentity.TileEntityGrave;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.util.collection.NamespaceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.UUID;

public class BtaGraves implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint {
    public static final String MOD_ID = "btagraves";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static int ARMOR_INVENTORY_SIZE;

	public static Block<BlockLogicGrave> graveBlock;
	public static ItemGrave graveItem;

	public static final Config CONFIG = new Config();

    @Override
    public void onInitialize() {
        LOGGER.info("BTA Graves initialized.");
    }

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}

	@Override
	public void beforeGameStart() {
		graveBlock = new BlockBuilder(MOD_ID)
			.setTileEntity(TileEntityGrave::getDefault)
			.setBlockItem((b) -> graveItem)
			.setBlockSound(BlockSounds.STONE)
			.setHardness(1.0F)
			.setResistance(10.0F)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build(
				"grave",
				CONFIG.getBlockId("graveId"),
				BlockLogicGrave::new
			);

		graveItem = new ItemBuilder(MOD_ID)
			.setStackSize(1)
			.build(new ItemGrave());

		EntityHelper.createTileEntity(
			TileEntityGrave.class,
			NamespaceID.getPermanent(MOD_ID, "grave")
		);
		if(FabricLoader.getInstance().isModLoaded("aether")){
			ARMOR_INVENTORY_SIZE = 8;
		}else{
			ARMOR_INVENTORY_SIZE = 4;
		}
	}

	@Override
	public void afterGameStart() {

	}
}
