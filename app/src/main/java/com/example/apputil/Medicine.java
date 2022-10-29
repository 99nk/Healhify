package com.example.apputil;

public class Medicine {

    String medicineId;
    String name;
    String quantity;
    String expiryDate;

    public Medicine() {
    }

    public Medicine(String medicineId, String name, String quantity, String expiryDate) {
        this.medicineId = medicineId;
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
