package com.csi.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class Role implements Serializable {
    private Integer id;
    private String roleName;
    private String createTime;
    private String updateTime;
    private Integer isDelete;
}
