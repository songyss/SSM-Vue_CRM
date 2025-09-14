package com.csi.domain;
import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;


/**
 * 商机表(Opportunities)实体类
 *
 * @author admin
 * @since 2025-09-14 15:29:52
 */


@Data

public class Opportunities implements Serializable {
    private static final long serialVersionUID = 981289747808294574L;
/**
     * 主键ID
     */
    private Integer id;

/**
     * 客户ID
     */
    private Integer customerId;

/**
     * 商机名称
     */
    private String name;

/**
     * 预估金额
     */
    private BigDecimal amount;

/**
     * 商机阶段（'初步接触','需求分析','方案报价','谈判审核','赢单','输单'）
     */
    private Integer stage;

/**
     * 赢率(0-1)
     */
    private BigDecimal probability;

/**
     * 预计成交日期
     */
    private Date expectedCloseDate;

/**
     * 负责人ID
     */
    private Integer assigneeId;

/**
     * 商机描述
     */
    private String description;

/**
     * 创建时间
     */
    private Date createTime;

/**
     * 更新时间
     */
    private Date updateTime;



}

