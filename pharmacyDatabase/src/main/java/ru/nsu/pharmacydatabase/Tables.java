package ru.nsu.pharmacydatabase;

public enum Tables {
    provider(new Table(
            "PROVIDER",
            "/ru/nsu/pharmacydatabase/windows/insert/provider.fxml"));

    private Table table;

    Tables(Table table) {
        this.table = table;
    }

    public String getWindowName() {
        return table.getWindowName();
    }

    String getName() {
        return table.getName();
    }

    public static Tables getTableByName(String name) {
        for (Tables table: values()) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        throw new IllegalArgumentException("No enum found with name: [" + name + "]");
    }
}
