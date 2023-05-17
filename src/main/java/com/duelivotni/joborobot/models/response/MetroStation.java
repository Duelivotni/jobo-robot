package com.duelivotni.joborobot.models.response;
import lombok.Data;

@Data
public class MetroStation {
    private String stationId;
    private String stationName;
    private String lineId;
    private String lineName;
    private Double lat;
    private Double lng;
}
