package dev.mattrm.mc.uhcplugin.overrides;

import dev.mattrm.mc.gametools.util.ISB;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;

public class RecipeOverrides {
    public static void override(JavaPlugin plugin) {
        // Remove enchanted golden apple recipe
        removeRecipe(plugin, ISB.stack(Material.GOLDEN_APPLE, (short) 1));

        // Enchanted head recipe
        plugin.getServer().addRecipe(
            new ShapedRecipe(
                ISB.material(Material.GOLDEN_APPLE, (short) 1)
                    .name(ChatColor.LIGHT_PURPLE + "Golden Head")
                    .lore("Cooked with the blood of your foes")
                    .build()
            )
                .shape("###", "#*#", "###")
                .setIngredient('#', Material.GOLD_INGOT)
                .setIngredient('*', Material.SKULL_ITEM, 3)
        );
    }

    private static void removeRecipe(JavaPlugin plugin, ItemStack result) {
        Iterator<Recipe> it = plugin.getServer().recipeIterator();
        Recipe recipe;
        while (it.hasNext()) {
            recipe = it.next();
            if (recipe != null && recipe.getResult().getType() == result.getType() && recipe.getResult().getDurability() == result.getDurability()) {
                it.remove();
            }
        }
    }
}
