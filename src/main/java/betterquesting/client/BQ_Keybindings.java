package betterquesting.client;

import net.minecraft.client.settings.KeyBinding;

import net.minecraft.init.Items;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import org.lwjgl.input.Keyboard;

import betterquesting.core.BetterQuesting;
import cpw.mods.fml.client.registry.ClientRegistry;

import static net.minecraft.stats.AchievementList.openInventory;

public class BQ_Keybindings {

    public static KeyBinding openQuests;

    public static void RegisterKeys() {
        openQuests = new KeyBinding("key.betterquesting.quests", Keyboard.KEY_GRAVE, BetterQuesting.NAME);
        AchievementList.openInventory = (new Achievement("achievement.openQuest", "openQuest", 0, 0, Items.book, null)).initIndependentStat().registerStat();

        ClientRegistry.registerKeyBinding(openQuests);
    }
}
