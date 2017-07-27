package ru.vzlomka76.virtualchest.inventory;

import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.math.Vector3;

public class VirtualInventoryHolder extends Vector3 implements InventoryHolder {

   protected Inventory inventory;


   public VirtualInventoryHolder(int x, int y, int z, Inventory inventory) {
      super((double)x, (double)y, (double)z);
      this.inventory = inventory;
   }

   public Inventory getInventory() {
      return this.inventory;
   }

   public void setInventory(Inventory inv) {
      this.inventory = inv;
   }
}
