package RVRC.GEQ1917.G34.android.diningmania;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionHistory{
    protected String transactionType;
    protected String transactionName;
    protected Date date;

    public TransactionHistory(){
    }

    public TransactionHistory(String type, String name, Date date){
        this.transactionType = type;
        this.transactionName = name;
        this.date = date;
    }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("E, dd/MMM/yyyy      hh:mm");
        return String.format("%s %s\n%s",transactionType,transactionName,df.format(date));
    }
}