package com.csi.domain;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;


/**
 * 客户-标签关联表(CustomerTags)实体类
 *
 * @author admin
 * @since 2025-09-14 15:29:50
 */


@Data

public class CustomerTags implements Serializable {
    private static final long serialVersionUID = -45662710792949612L;
/**
     * 主键ID
     */
    private Integer id;

/**
     * 客户ID
     */
    private Integer customerId;

/**
     * 标签ID
     */
    private Integer tagId;

/**
     * 创建时间
     */
    private Date createTime;



}

