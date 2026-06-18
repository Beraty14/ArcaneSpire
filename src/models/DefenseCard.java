package models;

/**
 * Savunma kartı. Oyuncuya zırh kazandırır.
 */
public class DefenseCard extends Card {
    private int armorAmount;

    public DefenseCard(String id, String title, int energyCost, String description, String iconPath, int armorAmount) {
        super(id, title, energyCost, description, iconPath);
        this.armorAmount = armorAmount;
    }

    public int getArmorAmount() { return armorAmount; }

    @Override
    public void applyEffect(Player player, Enemy enemy) {
        player.gainArmor(this.armorAmount);
    }
}
