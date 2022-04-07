package th.ac.ku.cardgame;

import com.google.gson.annotations.Expose;

public class User {
    @Expose
    String id;
    @Expose
    String key;

    public String getId() {
        return id;
    }
    public String getKey() {
        return key;
    }
}
