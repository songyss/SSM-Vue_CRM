package com.csi.domain;
import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;


/**
 * 订单表(Orders)实体类
 *
 * @author admin
 * @since 2025-09-14 15:29:52
 */


@Data

public class Orders implements Serializable {
    private static final long serialVersionUID = 222341483067888608L;
/**
     * 主键ID
     */
    private Integer id;

/**
     * 订单编号
     */
    private String orderNumber;

/**
     * 关联商机ID
     */
    private Integer opportunityId;

/**
     * 客户ID
     */
    private Integer customerId;

/**
     * 订单总金额
     */
    private double totalAmount;

/**
     * 签约日期
     */
    private String signedDate;

/**
     * 订单状态
     */
    private Integer orderStatus;
    private String orderStatusName;

/**
     * 合同文件路径
     */
    private String fileUrl;

/**
     * 订单备注
     */
    private String notes;

/**
     * 创建时间
     */
    private String createTime;


    // 以下字段映射多表联查
    private String customerName;
    private String opportunityName;


}

