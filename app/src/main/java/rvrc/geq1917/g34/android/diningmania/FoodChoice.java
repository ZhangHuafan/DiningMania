package rvrc.geq1917.g34.android.diningmania;

import java.util.Map;

public class FoodChoice {
    private String choice;
    private Map<String, String> daily_menu;

    public FoodChoice() {
    }

    public FoodChoice(String choice, Map<String, String> daily_menu) {
        this.choice = choice;
        this.daily_menu = daily_menu;
    }
    public void setChoice(String choice) {
        this.choice = choice;
    }

    public void setDaily_menu(Map<String, String> daily_menu) {
        this.daily_menu = daily_menu;
    }


    public String getChoice() {
        return choice;
    }

    public Map<String, String> getDaily_menu() {
        return daily_menu;
    }

}
