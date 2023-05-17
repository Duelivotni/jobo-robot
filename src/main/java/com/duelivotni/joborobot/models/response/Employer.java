package com.duelivotni.joborobot.models.response;
import lombok.Data;

@Data
public class Employer {
    private LogoUrls logoUrls;
    private String name;
    private String url;
    private String alternateUrl;
    private String id;
    private Boolean trusted;
    private Boolean accreditedItEmployer;
}
