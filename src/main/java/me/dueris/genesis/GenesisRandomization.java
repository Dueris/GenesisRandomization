package me.dueris.genesis;

import me.dueris.genesismc.event.OriginChoosePromptEvent;
import me.dueris.genesismc.factory.CraftApoli;
import me.dueris.genesismc.registry.LayerContainer;
import me.dueris.genesismc.registry.OriginContainer;
import me.dueris.genesismc.util.LangConfig;
import me.dueris.genesismc.util.entity.OriginPlayerAccessor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Random;

import static org.bukkit.Bukkit.getLogger;

public final class GenesisRandomization extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getLogger().info("Starting GenesisRandomization plugin..");
        this.getServer().getPluginManager().registerEvents(this, this);
        File config = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisRandomization").getDataFolder() + File.separator + "config.yml");
        if(!config.exists()){
            try (InputStream inputStream = Bukkit.getServer().getPluginManager().getPlugin("GenesisRandomization").getResource("config.yml")) {
                config.getParentFile().mkdirs();
                Files.copy(inputStream, config.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.getLogger().info("GenesisRandomization plugin has finished startup.");
    }

    @EventHandler
    public void open(OriginChoosePromptEvent e){
        Player p = e.getPlayer();
        e.setCanceled(true);

        for(LayerContainer layerContainer : CraftApoli.getLayers()){
            OriginContainer origin = CraftApoli.getOrigin(layerContainer.getOrigins().get(new Random().nextInt(layerContainer.getOrigins().size() - 1)));
            OriginPlayerAccessor.setOrigin(p, layerContainer, origin);
            if(this.getConfig().getBoolean("tell-users-origin")){
                p.sendMessage(Component.text("Your origin is: " + origin.getName() + "!").color(TextColor.color(0x76F7FF)));
            }
            if(this.getConfig().getBoolean("tell-console-origin")){
                Bukkit.getLogger().info(p.getName() + " random origin is: " + origin.getName());
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info("Disabling GenesisRandomization...");
    }
}
