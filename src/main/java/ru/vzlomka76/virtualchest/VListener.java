package ru.vzlomka76.virtualchest;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.SimpleTransactionGroup;
import cn.nukkit.inventory.Transaction;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemApple;
import cn.nukkit.item.ItemArrow;
import cn.nukkit.level.Position;
import ru.vzlomka76.virtualchest.inventory.VirtualInventory;

public class VListener implements Listener{
    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getItem().getId() == Item.COMPASS){ //if item is compass
            VirtualInventory inventory = new VirtualInventory(player, 27); //create class of inventory
            inventory.addItem(new ItemArrow(), new ItemApple()); //Add items to this inventory

            player.addWindow(inventory); //Add inventory to player

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTransaction (InventoryTransactionEvent event){
        for (Transaction transaction : event.getTransaction().getTransactions()){
            if (transaction instanceof SimpleTransactionGroup && transaction.getInventory() instanceof VirtualInventory){
                Player player = ((SimpleTransactionGroup) transaction).getSource();
                Item item = transaction.getSourceItem(); //Transacted item

                switch (item.getId()){
                    case Item.ARROW:
                        player.sendMessage("Hello!! It's arrow");
                        break;
                    case Item.APPLE:
                        player.teleport(new Position(0, 0, 0, Server.getInstance().getLevelByName("levelName")));
                        break;
                }

                event.setCancelled(true);
            }
        }
    }
}
