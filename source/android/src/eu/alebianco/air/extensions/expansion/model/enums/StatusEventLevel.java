package eu.alebianco.air.extensions.expansion.model.enums;

public enum StatusEventLevel {

    DOWNLOAD("download"),
    EXTRACTOR("extractor");

    private String name;

    private StatusEventLevel(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }
}