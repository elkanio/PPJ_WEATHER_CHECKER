package ppj.weather.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ppj.weather.model.City;
import ppj.weather.repositories.CityRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public List<City> get() {
        return StreamSupport.stream(cityRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public City create(City city) {
        return cityRepository.save(city);
    }

    public City save(City city) {
        return cityRepository.save(city);
    }

    public void deleteCities() {
        cityRepository.deleteAll();
    }

    public Optional<City> get(int id) {
        Optional<City> city = cityRepository.findById(id);

        return city;
    }

    public void delete(int id) {
        cityRepository.deleteById(id);
    }

    public void delete(City city) {
        cityRepository.delete(city);
    }

    public boolean exists(Integer id) {
        return cityRepository.existsById(id);
    }

    public int getCitiesCount() {
        return (int) cityRepository.count();
    }

    public List<City> getAll() {
        return StreamSupport.stream(cityRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
