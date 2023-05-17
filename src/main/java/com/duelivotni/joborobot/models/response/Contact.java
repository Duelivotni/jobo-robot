package com.duelivotni.joborobot.models.response;
import lombok.Data;

import java.util.List;

@Data
public class Contact {
    private String name;
    private String email;
    private List<Phone> phones;
}
