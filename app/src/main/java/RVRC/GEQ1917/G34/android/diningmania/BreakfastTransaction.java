package RVRC.GEQ1917.G34.android.diningmania;

import java.util.Date;

public class BreakfastTransaction extends TransactionHistory{
    private static int total;

    BreakfastTransaction(String name, Date date){
        super(name, date);
        total++;
    }

    static int getTotal() {
        return total;
    }
}
