package me.azhar.androidutility.Model;

public class Item {
    private String name;
    private boolean isChecked;

    public Item() {
    }

    public Item(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
