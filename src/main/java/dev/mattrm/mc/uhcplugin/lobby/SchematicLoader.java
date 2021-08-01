package dev.mattrm.mc.uhcplugin.lobby;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.DataException;
import dev.mattrm.mc.uhcplugin.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.IOException;

public class SchematicLoader {
    public static boolean loadLobby() {
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

    public static boolean clearLobby() {
        Vector v1 = new Vector(-10, 200, -10);
        Vector v2 = new Vector(11, 221, 11);
        World world = Bukkit.getWorlds().get(0);
        BukkitWorld bukkitWorld = new BukkitWorld(world);
        EditSession es = new EditSession(bukkitWorld, 20000000);

        try {
            es.setBlocks(new CuboidRegion(v1, v2), new BaseBlock(Material.AIR.getId()));
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean loadSuddenDeath() {
        Vector v = new Vector(-19, 254, -19);
        World world = Bukkit.getWorlds().get(0);
        BukkitWorld bukkitWorld = new BukkitWorld(world);
        EditSession es = new EditSession(bukkitWorld, 20000000);

        try {
            CuboidClipboard cb = ((MCEditSchematicFormat) SchematicFormat.MCEDIT).load(FileUtils.class.getResourceAsStream("/suddendeath.schematic"));
            cb.paste(es, v, false);
        } catch (IOException | DataException | MaxChangedBlocksException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean clearSuddenDeath() {
        Vector v1 = new Vector(-20, 245, -20);
        Vector v2 = new Vector(20, 255, 20);
        World world = Bukkit.getWorlds().get(0);
        BukkitWorld bukkitWorld = new BukkitWorld(world);
        EditSession es = new EditSession(bukkitWorld, 20000000);

        try {
            es.setBlocks(new CuboidRegion(v1, v2), new BaseBlock(Material.AIR.getId()));
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
