package models;

/**
 * Tüm düşmanların atasıdır.
 * Her düşman kendi executeTurn yapay zekasını override eder.
 * OOP Prensibi: Abstraction + Polymorphism.
 */
public abstract class Enemy extends Character {
    public static class Move {
        public String name;
        public int damage;
        public int armor;
        public int heal;

        public Move(String name, int damage, int armor, int heal) {
            this.name = name;
            this.damage = damage;
            this.armor = armor;
            this.heal = heal;
        }
    }

    protected Move[] moveSequence;

    public Enemy(String name, int maxHp, String imagePath) {
        super(name, maxHp, imagePath);
    }

    private Move lastMove = null;

    public Move decideNextMove(int turnNumber) {
        return decideNextMove(null, turnNumber);
    }

    public Move decideNextMove(Player target, int turnNumber) {
        if (moveSequence == null || moveSequence.length == 0) return null;

        double hpPercentage = (double) this.getCurrentHp() / this.getMaxHp();

        // 1. Düşük Can Savunma Önceliği: Can %35'in altındaysa iyileşme veya zırh önceliği
        if (hpPercentage < 0.35) {
            // Önce iyileşme hamlesi ara
            for (Move m : moveSequence) {
                if (m.heal > 0 && m != lastMove) {
                    lastMove = m;
                    return m;
                }
            }
            // İyileşme yoksa zırh hamlesi ara
            for (Move m : moveSequence) {
                if (m.armor > 0 && m != lastMove) {
                    lastMove = m;
                    return m;
                }
            }
        }

        // 2. Bitirici Vuruş Önceliği: Oyuncunun canı eldeki bir atağın hasarından az ise bitirici vuruş yap
        if (target != null) {
            for (Move m : moveSequence) {
                if (m.damage >= target.getCurrentHp()) {
                    lastMove = m;
                    return m;
                }
            }
        }

        // 3. Zırh Koruma Önceliği: Eğer düşmanın zırhı yoksa ve listede zırh hamlesi varsa %50 ihtimalle zırhlan
        if (this.getArmor() == 0) {
            java.util.List<Move> armorMoves = new java.util.ArrayList<>();
            for (Move m : moveSequence) {
                if (m.armor > 0 && m != lastMove) {
                    armorMoves.add(m);
                }
            }
            if (!armorMoves.isEmpty() && Math.random() < 0.5) {
                Move chosen = armorMoves.get(new java.util.Random().nextInt(armorMoves.size()));
                lastMove = chosen;
                return chosen;
            }
        }

        // Varsayılan: Sıralı hamle
        int index = (turnNumber - 1) % moveSequence.length;
        Move chosen = moveSequence[index];
        lastMove = chosen;
        return chosen;
    }

    public void executeTurn(Player target, int turnNumber) {
        Move m = decideNextMove(target, turnNumber);
        if (m == null) return;
        this.resetArmor();
        if (m.armor > 0) this.gainArmor(m.armor);
        if (m.damage > 0) target.takeDamage(m.damage);
        if (m.heal > 0) this.heal(m.heal);
    }
}
