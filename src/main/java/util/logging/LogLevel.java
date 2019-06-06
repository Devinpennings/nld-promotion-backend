package util.logging;

public enum LogLevel {
    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3);

    private int value;

    public int value()
    {
        return this.value;
    }

    private LogLevel(int value)
    {
        this.value = value;
    }

}
