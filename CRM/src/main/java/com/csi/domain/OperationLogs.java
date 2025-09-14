package com.csi.domain;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;


/**
 * 系统操作日志表(OperationLogs)实体类
 *
 * @author admin
 * @since 2025-09-14 15:29:51
 */


@Data

public class OperationLogs implements Serializable {
    private static final long serialVersionUID = -50775137267533664L;
/**
     * 主键ID
     */
    private Integer id;

/**
     * 操作人ID
     */
    private Integer operatorId;

/**
     * 操作内容
     */
    private String operation;

/**
     * 操作对象类型
     */
    private String targetType;

/**
     * 操作对象ID
     */
    private Integer targetId;

/**
     * 旧值
     */
    private String oldValue;

/**
     * 新值
     */
    private String newValue;

/**
     * IP地址
     */
    private String ipAddress;

/**
     * 创建时间
     */
    private Date createTime;



}

