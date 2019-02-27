package RVRC.GEQ1917.G34.android.diningmania;

import java.util.Map;

public class FoodChoice {
    private String choice;
    private Map<String, String> daily_menu;
    private String image;

    public FoodChoice() {
    }

    public FoodChoice(String choice, Map<String, String> daily_menu, String image) {
        this.choice = choice;
        this.daily_menu = daily_menu;
        this.image = image;
    }
    public void setChoice(String choice) {
        this.choice = choice;
    }

    public void setDaily_menu(Map<String, String> daily_menu) {
        this.daily_menu = daily_menu;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getChoice() {
        return choice;
    }

    public Map<String, String> getDaily_menu() {
        return daily_menu;
    }

    public String getImage() {
        return image;
    }
}
