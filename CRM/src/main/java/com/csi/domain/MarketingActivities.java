package com.csi.domain;
import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;


/**
 * 市场活动表(MarketingActivities)实体类
 *
 * @author admin
 * @since 2025-09-14 15:29:51
 */


@Data

public class MarketingActivities implements Serializable {
    private static final long serialVersionUID = -56505199384580754L;
/**
     * 主键ID
     */
    private Integer id;

/**
     * 活动名称
     */
    private String name;

/**
     * 关联的方案ID
     */
    private Integer planId;

    /**
     * 方案内容(HTML格式)
     */
    private String content;

/**
     * 活动地点
     */
    private String location;

/**
     * 活动形式
     */
    private String form;

/**
     * 开始时间
     */
    private String startTime;

/**
     * 结束时间
     */
    private String endTime;

/**
     * 预估成本
     */
    private double estimatedCost;

/**
     * 实际成本
     */
    private double actualCost;

/**
     * 活动负责人ID
     */
    private Integer managerId;
    private String managername;

/**
     * 活动状态
     */
    private Integer activityStatus;
    private String activityStatusName;

/**
     * 创建时间
     */
    private String createTime;

    //活动二维码
    private String qrCodeUrl;

}

