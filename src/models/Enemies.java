package models;

public class Enemies {

    public static class SkeletonEnemy extends Enemy {
        public SkeletonEnemy() { 
            super(LanguageManager.getInstance().getText("enemy_1"), 28, "enemy_guard.png.png"); 
            this.moveSequence = new Move[] {
                new Move("Kemik Darbe", 4, 0, 0),
                new Move("Saldırıyor", 6, 0, 0),
                new Move("Zırhlanıyor", 0, 4, 0)
            };
        }
    }

    public static class GoblinEnemy extends Enemy {
        public GoblinEnemy() { 
            super(LanguageManager.getInstance().getText("enemy_2"), 35, "enemy_goblin.png.png"); 
            this.moveSequence = new Move[] {
                new Move("Bıçak", 5, 0, 0),
                new Move("Çift Bıçak", 8, 0, 0),
                new Move("Kaçış", 0, 6, 0),
                new Move("Sinsi Saldırı", 10, 0, 0)
            };
        }
    }

    public static class SlimeEnemy extends Enemy {
        public SlimeEnemy() { 
            super(LanguageManager.getInstance().getText("enemy_3"), 42, "enemy_slime.png.png"); 
            this.moveSequence = new Move[] {
                new Move("Asit", 6, 0, 0),
                new Move("Zırhlanıyor", 0, 5, 0),
                new Move("Asit Patlaması", 12, 0, 0)
            };
        }
    }

    public static class VampireEnemy extends Enemy {
        public VampireEnemy() { 
            super(LanguageManager.getInstance().getText("enemy_4"), 52, "enemy_vampire.png.png"); 
            this.moveSequence = new Move[] {
                new Move("Kan Emme", 8, 0, 4),
                new Move("Pençe", 10, 0, 0),
                new Move("Yarasa Kalkanı", 0, 10, 0),
                new Move("Büyük Isırık", 12, 0, 6)
            };
        }
    }

    public static class GolemEnemy extends Enemy {
        public GolemEnemy() { 
            super(LanguageManager.getInstance().getText("enemy_5"), 65, "enemy_golem.png.png"); 
            this.moveSequence = new Move[] {
                new Move("Kaya Yumruk", 10, 0, 0),
                new Move("Taş Kabuk", 0, 14, 0),
                new Move("Deprem", 15, 0, 0),
                new Move("Taş Kabuk", 0, 8, 0)
            };
        }
    }

    public static class WraithEnemy extends Enemy {
        public WraithEnemy() { 
            super(LanguageManager.getInstance().getText("enemy_6"), 55, "enemy_wraith.png.png"); 
            this.moveSequence = new Move[] {
                new Move("Buz Dokunuş", 9, 0, 0),
                new Move("Ruh Kalkanı", 0, 12, 0),
                new Move("Ruh Emici", 14, 0, 7),
                new Move("Çığlık", 11, 0, 0)
            };
        }
    }

    public static class DemonEnemy extends Enemy {
        public DemonEnemy() { 
            super(LanguageManager.getInstance().getText("enemy_7"), 75, "enemy_demon.png.png"); 
            this.moveSequence = new Move[] {
                new Move("Alev Kılıcı", 14, 0, 0),
                new Move("Şeytan Kalkanı", 0, 15, 0),
                new Move("Cehennem Darbesi", 18, 0, 0),
                new Move("Alev", 12, 0, 0)
            };
        }
    }

    public static class LichEnemy extends Enemy {
        public LichEnemy() { 
            super(LanguageManager.getInstance().getText("enemy_8"), 70, "enemy_lich.png.png"); 
            this.moveSequence = new Move[] {
                new Move("Nekro Patlama", 16, 0, 0),
                new Move("Kemik Kalkan", 0, 16, 0),
                new Move("Ruh Hırsızı", 12, 0, 8),
                new Move("Ölüm Işını", 20, 0, 0)
            };
        }
    }

    public static class HydraEnemy extends Enemy {
        public HydraEnemy() { 
            super(LanguageManager.getInstance().getText("enemy_9"), 90, "enemy_hydra.png.png"); 
            this.moveSequence = new Move[] {
                new Move("Ateş Başı", 12, 0, 0),
                new Move("Asit Başı", 14, 0, 0),
                new Move("Yıldırım", 16, 0, 0),
                new Move("Pul Kalkanı", 0, 18, 0),
                new Move("Üçlü Saldırı", 22, 0, 0)
            };
        }
    }

    public static class DragonBoss extends Enemy {
        public DragonBoss() { 
            super(LanguageManager.getInstance().getText("enemy_10"), 120, "enemy_dragon_boss.png.png"); 
            this.moveSequence = new Move[] {
                new Move("Pençe", 14, 0, 0),
                new Move("Ejderha Pulları", 0, 20, 0),
                new Move("Kuyruk", 18, 0, 0),
                new Move("Ateş Nefesi", 22, 0, 0),
                new Move("CEHENNEM ATEŞİ!", 30, 0, 0)
            };
        }
    }
}
