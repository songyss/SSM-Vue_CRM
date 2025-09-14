package com.csi.domain;
import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;


/**
 * 活动报告表(ActivityReports)实体类
 *
 * @author admin
 * @since 2025-09-14 15:38:25
 */


@Data

public class ActivityReports implements Serializable {
    private static final long serialVersionUID = -96221848637823245L;
/**
     * 主键ID
     */
    private Integer id;

/**
     * 关联的活动ID
     */
    private Integer activityId;

/**
     * 报告标题
     */
    private String title;

/**
     * 报告内容(HTML格式)
     */
    private String content;

/**
     * 新增客户数
     */
    private Integer newCustomersCount;

/**
     * 潜在线索数
     */
    private Integer potentialLeadsCount;

/**
     * 预估投资回报率
     */
    private BigDecimal estimatedRoi;

/**
     * 报告创建人ID
     */
    private Integer creatorId;

/**
     * 创建时间
     */
    private Date createTime;



}

