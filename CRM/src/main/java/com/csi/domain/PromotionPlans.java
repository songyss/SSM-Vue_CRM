package com.csi.domain;
import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;


/**
 * 推广方案表(PromotionPlans)实体类
 *
 * @author admin
 * @since 2025-09-14 15:29:53
 */


@Data

public class PromotionPlans implements Serializable {
    private static final long serialVersionUID = 939810734544587574L;
/**
     * 主键ID
     */
    private Integer id;

/**
     * 方案标题
     */
    private String title;

/**
     * 方案内容(HTML格式)
     */
    private String content;

/**
     * 预算金额
     */
    private double budget;

/**
     * 提交人ID
     */
    private Integer submitterId;

/**
     * 审批人ID
     */
    private Integer approverId;

/**
     * 状态: 0待审批 1通过 2驳回
     */
    private Integer status;
    private String planStatusName;

/**
     * 审批反馈意见
     */
    private String feedback;

/**
     * 创建时间
     */
    private String createTime;

/**
     * 更新时间
     */
    private String updateTime;



}

