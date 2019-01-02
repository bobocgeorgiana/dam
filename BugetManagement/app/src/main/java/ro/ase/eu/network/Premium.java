package ro.ase.eu.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Premium implements Serializable {

    private Item bronze;
    private Item silver;
    private List<Item> gold;

    public Premium() {
    }

    public Premium(Item bronze, Item silver, List<Item> gold) {
        this.bronze = bronze;
        this.silver = silver;
        this.gold = gold;
    }

    public Item getBronze() {
        return bronze;
    }

    public void setBronze(Item bronze) {
        this.bronze = bronze;
    }

    public Item getSilver() {
        return silver;
    }

    public void setSilver(Item silver) {
        this.silver = silver;
    }

    public List<Item> getGold() {
        return gold;
    }

    public void setGold(Item item){
        if(gold == null){
            gold = new ArrayList<>();
        }

        if(item!=null){
            gold.add(item);
        }

    }

    public void setGold(List<Item> gold) {
        this.gold = gold;
    }

    @Override
    public String toString() {
        return "Premium{" +
                "bronze=" + bronze +
                ", silver=" + silver +
                ", gold=" + gold +
                '}';
    }
}

