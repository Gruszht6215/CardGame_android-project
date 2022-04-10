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

    public Transaction(String created_at, String task, String value, String detail) {
        this.created_at = created_at;
        this.task = task;
        this.value = value;
        this.detail = detail;
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
}
