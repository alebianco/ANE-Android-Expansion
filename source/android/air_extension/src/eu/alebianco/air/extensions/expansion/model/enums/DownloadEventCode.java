package eu.alebianco.air.extensions.expansion.model.enums;

public enum DownloadEventCode {

    PROGRESS("progress"),
    COMPLETE("complete"),
    STATUS_CHANGE("status_change");

    private String name;

    private DownloadEventCode(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }
}