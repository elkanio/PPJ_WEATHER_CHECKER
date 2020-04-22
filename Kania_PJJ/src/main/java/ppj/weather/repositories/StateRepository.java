package ppj.weather.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ppj.weather.model.State;

@Repository
public interface StateRepository extends CrudRepository<State, Integer> {
    State findByName(String name);
}
