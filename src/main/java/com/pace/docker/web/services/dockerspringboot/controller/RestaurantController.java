package com.pace.docker.web.services.dockerspringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pace.docker.web.services.dockerspringboot.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/rest/docker/restaurant")
public class RestaurantController {
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    ResourceLoader resourceLoader;

    @RequestMapping(path = "/all", method = RequestMethod.GET, name = "getAllRestaurants")
    public Restaurant[] getAllRestaurants() {
        Resource resource = resourceLoader.getResource("classpath:restaurants.json");
        Restaurant[] restaurants = null;
        try {
            restaurants = mapper.readValue(resource.getFile(), Restaurant[].class);
            restaurants = mapper.readValue(this.getAllRestaurants().getClassLoader().getResourceAsStream(), Restaurant[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return restaurants;
    }
}
