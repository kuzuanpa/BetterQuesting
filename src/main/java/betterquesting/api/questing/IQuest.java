package betterquesting.api.questing;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import betterquesting.api.enums.EnumQuestState;
import betterquesting.api.properties.IPropertyContainer;
import betterquesting.api.questing.rewards.IReward;
import betterquesting.api.questing.tasks.ITask;
import betterquesting.api2.client.gui.themes.presets.PresetIcon;
import betterquesting.api2.storage.IDatabaseNBT;
import betterquesting.api2.storage.INBTProgress;
import betterquesting.api2.storage.INBTSaveLoad;
import betterquesting.api2.utils.QuestTranslation;

public interface IQuest extends INBTSaveLoad<NBTTagCompound>, INBTProgress<NBTTagCompound>, IPropertyContainer {

    EnumQuestState getState(EntityPlayer player);

    @Nullable
    NBTTagCompound getCompletionInfo(UUID uuid);

    void setCompletionInfo(UUID uuid, @Nullable NBTTagCompound nbt);

    void update(EntityPlayer player);

    void detect(EntityPlayer player);

    boolean isUnlocked(UUID uuid);

    boolean canSubmit(EntityPlayer player);

    /**
     * Test if this quest can be unlocked at all as required by quest logic for given participant.
     *
     * If this return false, it means whatever the player do, this quest cannot be unlocked without bq_admin commands
     * or hand editing save file.
     * This assumes quest cannot be un-completed.
     */
    boolean isUnlockable(UUID uuid);

    boolean isComplete(UUID uuid);

    void setComplete(UUID uuid, long timeStamp);

    /**
     * Can claim now. (Basically includes info from rewards (is choice reward chosen, for example))
     */
    boolean canClaim(EntityPlayer player, boolean forceChoice);

    default boolean canClaim(EntityPlayer player) {
        return canClaim(player, false);
    }

    /**
     * Can we claim reward at all. (If reward available but we can't claim cuz a rewards not ready (choice reward not
     * chosen, for example))
     */
    boolean canClaimBasically(EntityPlayer player);

    boolean hasClaimed(UUID uuid);

    void claimReward(EntityPlayer player, boolean forceChoice);

    default void claimReward(EntityPlayer player) {
        claimReward(player, false);
    }

    void setClaimed(UUID uuid, long timestamp);

    void resetUser(@Nullable UUID uuid, boolean fullReset);

    IDatabaseNBT<ITask, NBTTagList, NBTTagList> getTasks();

    IDatabaseNBT<IReward, NBTTagList, NBTTagList> getRewards();

    /** Returns a mutable set. Changes made to the returned set will be reflected in the quest! */
    @Nonnull
    Set<UUID> getRequirements();

    void setRequirements(@Nonnull Iterable<UUID> req);

    @Nonnull
    RequirementType getRequirementType(UUID req);

    void setRequirementType(UUID req, @Nonnull RequirementType kind);

    enum RequirementType {

        NORMAL(PresetIcon.ICON_VISIBILITY_NORMAL),
        IMPLICIT(PresetIcon.ICON_VISIBILITY_IMPLICIT),
        HIDDEN(PresetIcon.ICON_VISIBILITY_HIDDEN);

        private final PresetIcon icon;
        private final String buttonTooltip;

        private static final RequirementType[] VALUES = values();

        RequirementType(PresetIcon icon) {
            this.icon = icon;
            buttonTooltip = "betterquesting.btn.prereq_visbility." + name().toLowerCase(Locale.ROOT);
        }

        public byte id() {
            return (byte) ordinal();
        }

        public PresetIcon getIcon() {
            return icon;
        }

        public String getButtonTooltip() {
            return QuestTranslation.translate(buttonTooltip);
        }

        public RequirementType next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        public static RequirementType from(byte id) {
            return id >= 0 && id < VALUES.length ? VALUES[id] : NORMAL;
        }
    }
}
