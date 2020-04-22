package ppj.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "States")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class State {

    @Id
    @GeneratedValue
    @Column(name = "id_state")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    private List<City> cities;

    public State(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public State(String name) {
        this.name = name;
    }

    public State() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
