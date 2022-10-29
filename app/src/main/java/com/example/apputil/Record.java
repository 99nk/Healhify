package com.example.apputil;

public class Record
{
    String name;
    String date;
    String age;
    String problem;
    String medicines;

    public Record() {
    }

    public Record(String name, String date, String age, String problem, String medicines) {
        this.name = name;
        this.date = date;
        this.age = age;
        this.problem = problem;
        this.medicines = medicines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }
}
