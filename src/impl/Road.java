package impl;

public class Road {

    private String fromCity;
    private int fromCityIndex;
    private String toCity;
    private int toCityIndex;
    private int cost;

    public Road(String fromCity, String toCity, int fromCityIndex, int toCityIndex, int cost) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.fromCityIndex = fromCityIndex;
        this.toCityIndex = toCityIndex;
        this.cost = cost;
    }

    public int getFromCityIndex() {
        return fromCityIndex;
    }

    public int getToCityIndex() {
        return toCityIndex;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public int getCost() {
        return cost;
    }

    public String getNeighbourCity(String neighbourCity) {
        if ( this.fromCity.equals(neighbourCity)) {
            return this.toCity;
        } else {
            return  this.fromCity;
        }
    }

    @Override
    public String toString() {
        return "Road{" +
                "fromCity='" + fromCity + '\'' +
                ", toCity='" + toCity + '\'' +
                ", cost=" + cost +
                '}';
    }
}
