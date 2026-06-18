package models;

public class AttackCard extends Card {
    private int damage;
    private int hits;
    private int poison;

    public AttackCard(String id, String title, int energyCost, String description, String iconPath, int damage, int hits, int poison) {
        super(id, title, energyCost, description, iconPath);
        this.damage = damage;
        this.hits = hits;
        this.poison = poison;
    }

    public int getDamage() { return damage; }
    public int getHits() { return hits; }
    public int getPoison() { return poison; }

    @Override
    public void applyEffect(Player player, Enemy enemy) {
        int bonusDamage = (player.getHeroClass() == Player.HeroClass.KNIGHT) ? 1 : 0;
        int bonusPoison = (player.getHeroClass() == Player.HeroClass.ROGUE && this.poison > 0) ? 2 : 0;
        
        for (int i = 0; i < hits; i++) {
            enemy.takeDamage(this.damage + bonusDamage);
        }
        if (this.poison > 0) {
            enemy.addPoison(this.poison + bonusPoison);
        }
    }
}
