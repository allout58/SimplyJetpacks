package tonius.simplyjetpacks.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import tonius.simplyjetpacks.util.StackUtils;
import cofh.api.energy.IEnergyContainerItem;

public class ItemSJArmorEnergy extends ItemSJArmor implements ISpecialArmor, IEnergyContainerItem {

    protected int maxEnergy;
    protected int maxInput;
    protected int maxOutput;
    protected ArmorProperties properties = new ArmorProperties(0, 1, 0);

    public ItemSJArmorEnergy(int id, EnumArmorMaterial material, int renderIndex, int armorType, String name, int maxEnergy, int maxInput, int maxOutput) {
        super(id, material, renderIndex, armorType, name, name);
        this.setMaxDamage(30);
        this.setNoRepair();
        this.maxEnergy = maxEnergy;
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
    }

    @Override
    public boolean getIsRepairable(ItemStack itemToRepair, ItemStack stack) {
        return false;
    }

    public void toggle(ItemStack itemStack, EntityPlayer player) {
        if (isOn(itemStack)) {
            player.addChatMessage(this.getDeactivateMsg());
            itemStack.stackTagCompound.setBoolean("On", false);
        } else {
            player.addChatMessage(this.getActivateMsg());
            itemStack.stackTagCompound.setBoolean("On", true);
        }
    }

    public String getActivateMsg() {
        return "Enabled";
    }

    public String getDeactivateMsg() {
        return "Disabled";
    }

    public boolean isOn(ItemStack itemStack) {
        return StackUtils.getNBT(itemStack).getBoolean("On");
    }

    public void updateEnergyDisplay(ItemStack itemStack) {
        double displayedDamage = 31 - (((double) getEnergyStored(itemStack) / (double) getMaxEnergyStored(itemStack)) * 30);
        itemStack.setItemDamage((int) Math.floor(displayedDamage));
    }

    public int addEnergy(ItemStack container, int energyToAdd, boolean simulate) {
        StackUtils.getNBT(container);
        int energyCurrent = container.stackTagCompound.getInteger("Energy");
        int energyAdded = Math.min(getMaxEnergyStored(container) - energyCurrent, energyToAdd);

        if (!simulate) {
            energyCurrent += energyAdded;
            container.stackTagCompound.setInteger("Energy", energyCurrent);
            updateEnergyDisplay(container);
        }

        return energyAdded;
    }

    public int subtractEnergy(ItemStack container, int energyToSubtract, boolean simulate) {
        StackUtils.getNBT(container);
        int energyCurrent = container.stackTagCompound.getInteger("Energy");
        int energySubtracted = Math.min(energyCurrent, energyToSubtract);

        if (!simulate) {
            energyCurrent -= energySubtracted;
            container.stackTagCompound.setInteger("Energy", energyCurrent);
            updateEnergyDisplay(container);
        }

        return energySubtracted;
    }

    public ItemStack getChargedItem(ItemSJArmorEnergy item) {
        ItemStack full = new ItemStack(item);
        item.addEnergy(full, item.getMaxEnergyStored(full), false);
        return full;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return properties;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        int energy = getEnergyStored(container);
        int energyReceived = Math.min(getMaxEnergyStored(container) - energy, Math.min(maxReceive, maxInput));

        if (!simulate) {
            energy += energyReceived;
            StackUtils.getNBT(container).setInteger("Energy", energy);
            updateEnergyDisplay(container);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        int energy = getEnergyStored(container);
        int energyExtracted = Math.min(energy, Math.min(maxExtract, maxOutput));

        if (!simulate) {
            energy -= energyExtracted;
            StackUtils.getNBT(container).setInteger("Energy", energy);
            updateEnergyDisplay(container);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return StackUtils.getNBT(container).getInteger("Energy");
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return maxEnergy;
    }
}
