package com.csi.domain;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;


/**
 * 客户跟进表(CustomerFollows)实体类
 *
 * @author admin
 * @since 2025-09-14 15:29:49
 */


@Data

public class CustomerFollows implements Serializable {
    private static final long serialVersionUID = -20758402112946249L;
/**
     * 主键ID
     */
    private Integer id;

/**
     * 客户ID
     */
    private Integer customerId;

/**
     * 跟进人ID
     */
    private Integer executorId;

/**
     * 跟进方式
     */
    private Integer type;

/**
     * 跟进内容
     */
    private String content;

/**
     * 下次计划
     */
    private String nextPlan;

/**
     * 下次联系时间
     */
    private Date nextContactTime;

/**
     * 创建时间
     */
    private Date createTime;



}

