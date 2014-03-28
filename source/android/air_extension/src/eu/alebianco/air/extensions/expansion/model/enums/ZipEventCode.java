package eu.alebianco.air.extensions.expansion.model.enums;

public enum ZipEventCode {

    BEGIN("begin"),
    COMPLETE("complete"),
    ERROR("error");

    private String name;

    private ZipEventCode(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }
}