package RVRC.GEQ1917.G34.android.diningmania;

import java.util.Date;

public class PointTransaction extends TransactionHistory{
    private static int total;

    public PointTransaction(String type, String name, Date date) {
        super(type, name, date);
        total++;
    }

    public static int getTotal() {
        return total;
    }
}
