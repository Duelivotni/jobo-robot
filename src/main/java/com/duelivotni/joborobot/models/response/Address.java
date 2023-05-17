package com.duelivotni.joborobot.models.response;
import lombok.Data;

import java.util.List;

@Data
public class Address {
    private String city;
    private String street;
    private String building;
    private String description;
    private Double lat;
    private Double lng;
    private List<MetroStation> metroStations;
}
