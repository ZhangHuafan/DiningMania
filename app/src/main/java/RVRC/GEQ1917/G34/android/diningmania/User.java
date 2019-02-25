package RVRC.GEQ1917.G34.android.diningmania;

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

    private int usedCredit;
    private int remainingCredit;
    private int usedPoint;
    private int remainingPoint;
    private List<String> transactions;

    public User(){
        usedCredit = 0;
        remainingCredit = TOTAL_CREDIT;
        usedPoint = 0;
        remainingPoint = 0;
        transactions = new Stack<>();
    }

    public int getUsedCredit() {
        return usedCredit;
    }

    public int getRemainingCredit() {
        return remainingCredit;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getRemainingPoint() {
        return remainingPoint;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setUsedCredit(int usedCredit) {
        this.usedCredit = usedCredit;
    }

    public void setRemainingCredit(int remainingCredit) {
        this.remainingCredit = remainingCredit;
    }

    public void setUsedPoint(int usedPoint) {
        this.usedPoint = usedPoint;
    }

    public void setRemainingPoint(int remainingPoint) {
        this.remainingPoint = remainingPoint;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(TransactionHistory transaction, int point){
        transactions.add(transaction.toString());
        if(transaction instanceof PointTransaction){
            usedPoint += point;
            remainingPoint -= point;

        }else {
            usedCredit++;
            remainingCredit--;

        }
        int totalTransactions = transactions.size();

    }

    public void earnPoint(int point) {
        remainingPoint += point;
    }

    public void usePoint(int point){

    }

}
