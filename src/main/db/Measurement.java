package db;

public class Measurement {
    private String measurementTime;
    private int actualWeight;
    private String origoTime;
    private int origoWeight;
    private String catName;
    private int id;


    public Measurement(String measurementTime, String origoTime, int actualWeight, int origoWeight) {
        this(-1, measurementTime, origoTime, actualWeight, origoWeight);
    }

    public Measurement(int id, String measurementTime, String origoTime, int actualWeight, int origoWeight) {
        this.measurementTime = measurementTime;
        this.origoTime = origoTime;
        this.actualWeight = actualWeight;
        this.origoWeight = origoWeight;
    }

    public String getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
    }

    public int getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(int actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getOrigoTime() {
        return origoTime;
    }

    public void setOrigoTime(String origoTime) {
        this.origoTime = origoTime;
    }

    public int getOrigoWeight() {
        return origoWeight;
    }

    public void setOrigoWeight(int origoWeight) {
        this.origoWeight = origoWeight;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
