package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.Location;
import com.br.luggycar.api.repositories.LocationRepository;
import com.br.luggycar.api.requests.LocationRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Location save(LocationRequest locationRequest) {
        Location location = new Location();
        BeanUtils.copyProperties(locationRequest, location);

        return locationRepository.save(location);

    }

}
