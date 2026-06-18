package models;

public abstract class Card implements ICardEffect {
    protected String id;
    protected String title;
    protected int energyCost;
    protected String description;
    protected String iconPath;

    public Card(String id, String title, int energyCost, String description, String iconPath) {
        this.id = id;
        this.title = title;
        this.energyCost = energyCost;
        this.description = description;
        this.iconPath = iconPath;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return models.LanguageManager.getInstance().getText("card_" + id);
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public String getDescription() {
        return models.LanguageManager.getInstance().getText("card_" + id + "_desc");
    }

    public String getIconPath() {
        return iconPath;
    }

    public boolean canPlay(Player p) {
        return p.getCurrentEnergy() >= this.energyCost;
    }
}
