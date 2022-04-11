package th.ac.ku.cardgame.UserModel;

import com.google.gson.annotations.Expose;

public class Transaction {
    @Expose
    protected int id;
    @Expose
    protected String created_at;
    @Expose
    protected String task;
    @Expose
    protected String value;
    @Expose
    protected String detail;
    @Expose
    protected int wallet_id;

    public Transaction(String task, String value, String detail, int wallet_id) {
        this.task = task;
        this.value = value;
        this.detail = detail;
        this.wallet_id = wallet_id;
    }

    public Transaction(String created_at, String task, String value, String detail, int wallet_id) {
        this.created_at = created_at;
        this.task = task;
        this.value = value;
        this.detail = detail;
        this.wallet_id = wallet_id;
    }

    public int getId() {
        return id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getTask() {
        return task;
    }

    public String getValue() {
        return value;
    }

    public String getDetail() {
        return detail;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", created_at='" + created_at + '\'' +
                ", task='" + task + '\'' +
                ", value='" + value + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
