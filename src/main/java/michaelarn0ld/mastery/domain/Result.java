package michaelarn0ld.mastery.domain;

import java.util.ArrayList;
import java.util.List;

public class Result<T> {

    private ArrayList<String> errors  = new ArrayList<>();
    private T payload;

    public boolean isSuccess() {
        return errors.size() == 0;
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    public void addError(String error) {
        errors.add(error);
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}