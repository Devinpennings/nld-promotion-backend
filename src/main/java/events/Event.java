package events;

public class Event {

    private Object value;

    public Event(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

}
