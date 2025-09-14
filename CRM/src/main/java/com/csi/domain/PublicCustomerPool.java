package com.csi.domain;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;


/**
 * 公共客户池表(PublicCustomerPool)实体类
 *
 * @author admin
 * @since 2025-09-14 15:29:53
 */


@Data

public class PublicCustomerPool implements Serializable {
    private static final long serialVersionUID = -59244226701413868L;
/**
     * 主键ID
     */
    private Integer id;

/**
     * 客户ID
     */
    private Integer customerId;

/**
     * 放入原因
     */
    private String reason;

/**
     * 释放时间
     */
    private Date releaseTime;

/**
     * 锁定截止时间
     */
    private Date lockedUntil;

/**
     * 锁定人ID
     */
    private Integer lockedBy;

/**
     * 创建时间
     */
    private Date createTime;



}

