package models;

/**
 * Hasar alabilecek her nesne bu arayüzü uygulamalıdır.
 * OOP Prensibi: Interface (Arayüz Sözleşmesi).
 */
public interface IDamageable {
    /**
     * @return Uygulanan toplam hasar miktarı (animasyon amaçlı).
     */
    int takeDamage(int amount);
}
