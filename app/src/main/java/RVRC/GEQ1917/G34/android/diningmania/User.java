package RVRC.GEQ1917.G34.android.diningmania;

import android.widget.Toast;

import java.util.List;
import java.util.Stack;

/**
 * Very basic design:
 * 1) It does not invalidate expired ticked.
 * 2) It does not limit the tickets can be used daily
 * 3) It does not separate transaction history according to
 *          > breakfast; > dinner; > points
 */

public class User {

    private static final int TOTAL_CREDIT = 100;

    private int bUsedCredit;
    private int bLeftCredit;
    private int dUsedCredit;
    private int dLeftCredit;
    private int usedPoint;
    private int leftPoint;
    private List<String> transactions;

    public User(){
        bLeftCredit = TOTAL_CREDIT;
        dLeftCredit = TOTAL_CREDIT;
        transactions = new Stack<>();
    }

    public int getBUsedCredit() {
        return bUsedCredit;
    }
    public int getBLeftCredit() {
        return bLeftCredit;
    }
    public int getDUsedCredit() {
        return dUsedCredit;
    }
    public int getDLeftCredit() {
        return dLeftCredit;
    }
    public int getUsedPoint() {
        return usedPoint;
    }
    public int getLeftPoint() {
        return leftPoint;
    }
    public List<String> getTransactions() {
        return transactions;
    }

    public void setBUsedCredit(int bUsedCredit) {
        this.bUsedCredit = bUsedCredit;
    }
    public void setBLeftCredit(int bLeftCredit) {
        this.bLeftCredit = bLeftCredit;
    }
    public void setDUsedCredit(int dUsedCredit) {
        this.dUsedCredit = dUsedCredit;
    }
    public void setDLeftCredit(int dLeftCredit) {
        this.dLeftCredit = dLeftCredit;
    }
    public void setUsedPoint(int usedPoint) {
        this.usedPoint = usedPoint;
    }
    public void setLeftPoint(int leftPoint) {
        this.leftPoint = leftPoint;
    }
    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(TransactionHistory transaction){
        transactions.add(transaction.toString());
        if (transaction instanceof BreakfastTransaction){
            bUsedCredit++;
            bLeftCredit--;
        } else {
            dUsedCredit++;
            dLeftCredit--;
        }
    }
    public void addTransaction(TransactionHistory transaction, int point){
        transactions.add(transaction.toString());
        usedPoint += point;
        leftPoint -= point;
    }

    public void earnPoint(int point) {
        leftPoint += point;
    }
}
