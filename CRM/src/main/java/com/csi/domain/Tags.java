package com.csi.domain;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;


/**
 * 标签表(Tags)实体类
 *
 * @author admin
 * @since 2025-09-14 15:29:53
 */


@Data

public class Tags implements Serializable {
    private static final long serialVersionUID = 774751191618362498L;
/**
     * 主键ID
     */
    private Integer id;

/**
     * 标签名称
     */
    private String name;

/**
     * 标签颜色
     */
    private String color;

/**
     * 创建时间
     */
    private Date createTime;



}

