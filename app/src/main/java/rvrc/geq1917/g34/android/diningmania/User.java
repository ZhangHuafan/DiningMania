package rvrc.geq1917.g34.android.diningmania;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Very basic design:
 * 1) It does not invalidate expired ticked.
 * 2) It does not limit the tickets can be used daily
 * 3) It does not separate transaction history according to
 *          > breakfast; > dinner; > points
 */

public class User implements Serializable{

    private static final int TOTAL_CREDIT = 5;

    private String studentId;
    private int bUsedCredit;
    private int bLeftCredit;
    private int dUsedCredit;
    private int dLeftCredit;
    private int usedPoint;
    private int leftPoint;
    private List<String> transactions = new LinkedList<>();
    private Map<String, String> breakfastIndications = new HashMap<>();
    private Map<String, String> dinnerIndications = new HashMap<>();;

    public User() {
    }

    public User(String studentId){
        this.studentId = studentId;
        this.bLeftCredit = TOTAL_CREDIT;
        this.dLeftCredit = TOTAL_CREDIT;
    }

    public String getStudentId() {
        return studentId;
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
    public Map<String, String> getBreakfastIndications() {
        return breakfastIndications;
    }
    public Map<String, String> getDinnerIndications() {
        return dinnerIndications;
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
    public void setBreakfastIndications(Map<String, String> breakfastIndications) {
        this.breakfastIndications = breakfastIndications;
    }
    public void setDinnerIndications(Map<String, String> dinnerIndications) {
        this.dinnerIndications = dinnerIndications;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public void addBreakfastIndication(String date, String indication) {
        breakfastIndications.put(date,indication);
    }
    public void addDinnerIndication(String date, String indication) {
        dinnerIndications.put(date,indication);
    }
}
