package com.csi.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Model implements Serializable {

    private Integer id;

    private String modelName;

}
