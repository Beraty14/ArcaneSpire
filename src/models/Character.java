package models;

/**
 * Oyundaki tüm karakterlerin (Oyuncu ve Düşman) ortak atasıdır.
 * OOP Prensibi: Abstraction (Soyutlama) ve Encapsulation (Kapsülleme).
 */
public abstract class Character implements IDamageable {
    private String name;
    private int maxHp;
    private int currentHp;
    private int armor;
    private String imagePath;

    private int poison;

    public Character(String name, int maxHp, String imagePath) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.armor = 0;
        this.poison = 0;
        this.imagePath = imagePath;
    }

    // --- Getters (Encapsulation) ---
    public String getName() { return name; }
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getArmor() { return armor; }
    public int getPoison() { return poison; }
    public String getImagePath() { return imagePath; }

    public void setCurrentHp(int hp) { this.currentHp = Math.min(Math.max(hp, 0), this.maxHp); }

    public void addPoison(int amount) {
        if (amount > 0) this.poison += amount;
    }

    public void takePoisonDamage() {
        if (this.poison > 0) {
            this.currentHp = Math.max(0, this.currentHp - this.poison);
            this.poison = Math.max(0, this.poison - 1);
        }
    }

    /**
     * Zırh kazandırır.
     */
    public void gainArmor(int amount) {
        if (amount > 0) {
            this.armor += amount;
        }
    }

    /**
     * İyileştirir (maxHp'yi geçemez).
     */
    public void heal(int amount) {
        if (amount > 0) {
            this.currentHp = Math.min(this.currentHp + amount, this.maxHp);
        }
    }

    /**
     * Hasar uygular. Önce zırhtan düşer, kalanı candan düşer.
     * @return Gerçekte cana verilen net hasar (animasyon amaçlı)
     */
    @Override
    public int takeDamage(int amount) {
        if (amount <= 0) return 0;

        int originalAmount = amount;

        // Önce zırhtan düş
        if (this.armor > 0) {
            if (amount >= this.armor) {
                amount -= this.armor;
                this.armor = 0;
            } else {
                this.armor -= amount;
                amount = 0;
            }
        }

        // Kalan hasarı candan düş
        if (amount > 0) {
            this.currentHp = Math.max(this.currentHp - amount, 0);
        }
        return originalAmount;
    }

    /**
     * Tur başında zırhı sıfırla (Slay the Spire tarzı)
     */
    public void resetArmor() {
        this.armor = 0;
    }

    public boolean isDead() {
        return this.currentHp <= 0;
    }

    @Override
    public String toString() {
        return name + " [HP:" + currentHp + "/" + maxHp + " Zırh:" + armor + "]";
    }
}
