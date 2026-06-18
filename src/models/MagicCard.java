package models;

public class MagicCard extends Card {
    private int healAmount;
    private int damageAmount;
    private int armorAmount;
    private int drawAmount;

    public MagicCard(String id, String title, int energyCost, String description, String iconPath, int healAmount, int damageAmount, int armorAmount) {
        this(id, title, energyCost, description, iconPath, healAmount, damageAmount, armorAmount, 0);
    }

    public MagicCard(String id, String title, int energyCost, String description, String iconPath, int healAmount, int damageAmount, int armorAmount, int drawAmount) {
        super(id, title, energyCost, description, iconPath);
        this.healAmount = healAmount;
        this.damageAmount = damageAmount;
        this.armorAmount = armorAmount;
        this.drawAmount = drawAmount;
    }

    @Override
    public void applyEffect(Player player, Enemy enemy) {
        if (healAmount > 0) player.heal(this.healAmount);
        if (damageAmount > 0) enemy.takeDamage(this.damageAmount);
        if (armorAmount > 0) player.gainArmor(this.armorAmount);
        if (drawAmount > 0) player.drawCards(this.drawAmount);
    }

    public int getHealAmount() {
        return healAmount;
    }

    public int getDamageAmount() {
        return damageAmount;
    }

    public int getArmorAmount() {
        return armorAmount;
    }

    public int getDrawAmount() {
        return drawAmount;
    }
}
