package dev.joee.btagraves.tileentity;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import dev.joee.btagraves.render.FetchSkinThread;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketTileEntityData;
import net.minecraft.core.player.inventory.container.ContainerInventory;

import java.util.UUID;

import static dev.joee.btagraves.BtaGraves.ARMOR_INVENTORY_SIZE;

public class TileEntityGrave extends TileEntity {
	private UUID playerUuid;
	public String skinUrl;
	public String deathMessage;
	public ItemStack[] mainInventory;
	public ItemStack[] armorInventory;

	public static TileEntityGrave getDefault() {
		return new TileEntityGrave(
			UUID.fromString("f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2"),
			TextFormatting.WHITE + "Herobrine " + TextFormatting.RED + "is watching.",
			new ItemStack[ContainerInventory.playerMainInventorySize()],
			new ItemStack[ARMOR_INVENTORY_SIZE]
		);
	}

	public TileEntityGrave() {}

	public TileEntityGrave(UUID uuid, String deathMessage, ItemStack[] mainInventory, ItemStack[] armorInventory) {
		this.setUUID(uuid);
		this.deathMessage = deathMessage;
		this.mainInventory = mainInventory;
		this.armorInventory = armorInventory;
	}

	public UUID getUUID() {
		return this.playerUuid;
	}

	public void setUUID(UUID uuid) {
		this.playerUuid = uuid;
		new FetchSkinThread(this);
	}

	@Override
	public Packet getDescriptionPacket() {
		return new PacketTileEntityData(this);
	}

	@Override
	public void readFromNBT(CompoundTag nbt) {
		super.readFromNBT(nbt);

		this.setUUID(UUID.fromString(nbt.getString("PlayerUUID")));
		this.deathMessage = nbt.getString("DeathMessage");


		this.mainInventory = new ItemStack[ContainerInventory.playerMainInventorySize()];
		ListTag mainItemsNbt = nbt.getList("MainItems");

		for (int i = 0; i < mainItemsNbt.tagCount(); i++) {
			CompoundTag itemNbt = (CompoundTag) mainItemsNbt.tagAt(i);
			int j = itemNbt.getByte("Slot") & 255;
			if (j < this.mainInventory.length) {
				this.mainInventory[j] = ItemStack.readItemStackFromNbt(itemNbt);
			}
		}

		this.armorInventory = new ItemStack[ARMOR_INVENTORY_SIZE];
		ListTag armorItemsNbt = nbt.getList("ArmorItems");

		for (int i = 0; i < armorItemsNbt.tagCount(); i++) {
			CompoundTag itemNbt = (CompoundTag) armorItemsNbt.tagAt(i);
			int j = itemNbt.getByte("Slot") & 255;
			if (j < this.armorInventory.length) {
				this.armorInventory[j] = ItemStack.readItemStackFromNbt(itemNbt);
			}
		}

	}

	@Override
	public void writeToNBT(CompoundTag nbt) {
		super.writeToNBT(nbt);

		nbt.putString("PlayerUUID", this.playerUuid.toString());
		nbt.putString("DeathMessage", this.deathMessage);

		ListTag mainItemsNbt = new ListTag();

		for (int i = 0; i < this.mainInventory.length; i++) {
			if (this.mainInventory[i] != null) {
				CompoundTag itemNbt = new CompoundTag();
				itemNbt.putByte("Slot", (byte) i);
				this.mainInventory[i].writeToNBT(itemNbt);
				mainItemsNbt.addTag(itemNbt);
			}
		}

		ListTag armorItemsNbt = new ListTag();

		for (int i = 0; i < this.armorInventory.length; i++) {
			if (this.armorInventory[i] != null) {
				CompoundTag itemNbt = new CompoundTag();
				itemNbt.putByte("Slot", (byte) i);
				this.armorInventory[i].writeToNBT(itemNbt);
				armorItemsNbt.addTag(itemNbt);
			}
		}

		nbt.put("MainItems", mainItemsNbt);
		nbt.put("ArmorItems", armorItemsNbt);
	}
}
