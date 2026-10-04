package com.bolosdaaxcila.cakemanager.data.model;

public enum OrderStatus {
    PENDENTE("Pendente"),
    CONFIRMADA("Confirmada"),
    EM_PREPARACAO("Em preparação"),
    PRONTA("Pronta"),
    ENTREGUE("Entregue");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static OrderStatus fromString(String value) {
        if (value == null) return PENDENTE;
        for (OrderStatus s : values()) {
            if (s.name().equalsIgnoreCase(value)) return s;
        }
        return PENDENTE;
    }
}
