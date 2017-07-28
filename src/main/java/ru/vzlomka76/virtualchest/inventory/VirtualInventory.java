package ru.vzlomka76.virtualchest.inventory;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.inventory.CustomInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;

public class VirtualInventory extends CustomInventory {

   protected String customName;
   protected String tile;
   protected int block;


   protected static InventoryType getInventoryType(int size) {
      InventoryType type = InventoryType.CHEST;
      switch(size) {
         case 5:
            type = InventoryType.HOPPER;
            break;
         case 9:
            type = InventoryType.DISPENSER;
            break;
         case 27:
            type = InventoryType.CHEST;
            break;
         case 54:
            type = InventoryType.DOUBLE_CHEST;
      }

      return type;
   }

   public VirtualInventory(Player player) {
      this(player, 27, "");
   }

   public VirtualInventory(Player player, int size) {
      this(player, size, "");
   }

   public VirtualInventory(Player player, int size, String name) {
      super(new VirtualInventoryHolder(player.getFloorX(), player.getFloorY() - 3, player.getFloorZ(), (Inventory)null), getInventoryType(size));
      this.customName = "";
      ((VirtualInventoryHolder)this.holder).setInventory(this);
      this.tile = BlockEntity.CHEST;
      this.block = 54;
      switch(size) {
         case 5:
            this.tile = BlockEntity.HOPPER;
            this.block = 154;
            break;
         case 9:
            this.tile = BlockEntity.CHEST; //Nukkit don't have dropper blockentity
            this.block = 23;
            break;
         case 27:
            this.tile = BlockEntity.CHEST;
            this.block = 54;
            break;
         case 54:
            this.tile = BlockEntity.CHEST; //Uhm -_-
            this.block = 54;
            break;
      }

      this.customName = name;
   }

   public void onOpen(Player who) {
      this.holder = new VirtualInventoryHolder(who.getFloorX(), who.getFloorY() - 3, who.getFloorZ(), this);
      VirtualInventoryHolder holder = (VirtualInventoryHolder)this.holder;
      UpdateBlockPacket pk = new UpdateBlockPacket();
      pk.x = (int)holder.x;
      pk.y = (int)holder.y;
      pk.z = (int)holder.z;
      pk.blockId = this.block;
      pk.blockData = 0;
      pk.flags = 3;
      who.dataPacket(pk);
      CompoundTag c = new CompoundTag("")
              .putList(new ListTag("Items"))
              .putString("id", this.tile)
              .putInt("x", (int)holder.x)
              .putInt("y", (int)holder.y)
              .putInt("z", (int)holder.z);
      if(!this.customName.equals("")) {
         c.putString("CustomName", this.customName);
      }

      try {
         BlockEntityDataPacket e = new BlockEntityDataPacket();
         e.x = (int)holder.x;
         e.y = (int)holder.y;
         e.z = (int)holder.z;
         e.namedTag = NBTIO.write(c);
         who.dataPacket(e);
      } catch (Exception var6) {
         who.getServer().getLogger().logException(var6);
      }

      super.onOpen(who);
      this.sendContents(who);
   }

   public void onClose(Player who) {
      VirtualInventoryHolder holder = (VirtualInventoryHolder)this.holder;
      UpdateBlockPacket pk = new UpdateBlockPacket();
      pk.x = (int)holder.x;
      pk.y = (int)holder.y;
      pk.z = (int)holder.z;
      pk.blockId = who.getLevel().getBlockIdAt((int)holder.x, (int)holder.y, (int)holder.z);
      pk.blockData = who.getLevel().getBlockDataAt((int)holder.x, (int)holder.y, (int)holder.z);
      pk.flags = 3;
      who.dataPacket(pk);
      super.onClose(who);
   }
}
