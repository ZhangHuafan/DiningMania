package RVRC.GEQ1917.G34.android.diningmania;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionHistory{
    protected String transactionName;
    protected Date date;

    public TransactionHistory(){
    }

    public TransactionHistory(String name, Date date){
        this.transactionName = name;
        this.date = date;
    }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy (E) hh:mm");
        return String.format("%-25s %s",transactionName, df.format(date));
    }
}