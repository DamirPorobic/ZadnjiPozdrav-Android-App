package info.zadnjipozdrav.zadnjipozdrav;

public enum NotifyInterval {
    HalfHour(R.string.notify_interval_HalfHour, 1000 * 60),
    OneHour(R.string.notify_interval_OneHour, 1000 * 60 * 2),
    TwoHour(R.string.notify_interval_TwoHour, 1000 * 60 * 3),
    OneDay(R.string.notify_interval_OneDay, 1000 * 60 * 4);

    private int resourceId;
    private long value;

    NotifyInterval(int id, long value) {
        resourceId = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return App.getContext().getString(resourceId);
    }

    public long getValue() {
        return this.value;
    }
}
