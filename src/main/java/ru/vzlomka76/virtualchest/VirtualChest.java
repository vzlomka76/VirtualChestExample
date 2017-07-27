package ru.vzlomka76.virtualchest;

import cn.nukkit.plugin.PluginBase;

public class VirtualChest extends PluginBase {
    public void onEnable (){
        this.getServer().getPluginManager().registerEvents(new VListener(), this);
    }
}
