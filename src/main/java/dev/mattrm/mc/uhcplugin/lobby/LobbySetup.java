package dev.mattrm.mc.uhcplugin.lobby;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.DataException;
import dev.mattrm.mc.uhcplugin.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;

public class LobbySetup {
    public static boolean setupLobby() {
        Vector v = new Vector(-10, 200, -10);
        World world = Bukkit.getWorlds().get(0);
        BukkitWorld bukkitWorld = new BukkitWorld(world);
        EditSession es = new EditSession(bukkitWorld, 20000000);

        try {
            CuboidClipboard cb = ((MCEditSchematicFormat) SchematicFormat.MCEDIT).load(FileUtils.class.getResourceAsStream("/uhclobby.schematic"));
            cb.paste(es, v, false);
        } catch (IOException | DataException | MaxChangedBlocksException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
