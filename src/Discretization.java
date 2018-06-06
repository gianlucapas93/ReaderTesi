public class Discretization {
    private Boolean on;
    private Integer n_interval;
    private Integer[] interval;
    private String[] name_interval;


    public Discretization() {
    }

    public Discretization(Boolean on, Integer n_interval, Integer[] interval, String[] name_interval) {
        this.on = on;
        this.n_interval = n_interval;
        this.interval = interval;
        this.name_interval = name_interval;
    }

    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    public Integer getN_interval() {
        return n_interval;
    }

    public void setN_interval(Integer n_interval) {
        this.n_interval = n_interval;
    }

    public Integer[] getInterval() {
        return interval;
    }

    public void setInterval(Integer[] interval) {
        this.interval = interval;
    }

    public String[] getName_interval() {
        return name_interval;
    }

    public void setName_interval(String[] name_interval) {
        this.name_interval = name_interval;
    }
}
