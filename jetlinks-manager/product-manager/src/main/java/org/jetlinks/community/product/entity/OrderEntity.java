package org.jetlinks.community.product.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hswebframework.ezorm.rdb.mapping.annotation.*;
import org.hswebframework.web.api.crud.entity.*;
import org.hswebframework.web.crud.annotation.EnableEntityEvent;
import org.hswebframework.web.crud.generator.Generators;
import org.hswebframework.web.utils.DigestUtils;
import org.jetlinks.community.product.enums.OrderStatus;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.sql.JDBCType;
import java.util.List;

/**
 * 订单管理实体类
 *
 * @author wangsheng
 * @since 1.0
 */
@Getter
@Setter
@Table(name = "prod_order")
@Comment("订单管理表")
@EnableEntityEvent
public class OrderEntity
    extends GenericEntity<String> implements RecordCreationEntity, RecordModifierEntity {

    @Schema(description = "订单流水号")
    @Column(name = "order_number", nullable = false)
    @GeneratedValue(generator = Generators.DEFAULT_ID_GENERATOR)
    private String orderNumber;

    @Schema(description = "订单商品ID集合")
    @Column(name = "item_ids", updatable = false)
    @JsonCodec
    @ColumnType(jdbcType = JDBCType.LONGVARCHAR, javaType = String.class)
    private List<String> itemIds;

    @Schema(description = "订单类型")
    @Column
    private String type;

    @Schema(description = "订单状态")
    @Column(length = 32, nullable = false)
    @EnumCodec
    @ColumnType(javaType = String.class)
    @NotBlank
    @DefaultValue("pendingPayment")
    private OrderStatus status;

    @Column(updatable = false)
    @DefaultValue(generator = Generators.CURRENT_TIME)
    @Schema(
        description = "创建时间(只读)"
        , accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long createTime;

    @Column
    @DefaultValue(generator = Generators.CURRENT_TIME)
    @Schema(description = "更新时间")
    private Long modifyTime;

    @Column(updatable = false)
    @Schema(
        description = "创建者ID(只读)"
        , accessMode = Schema.AccessMode.READ_ONLY
    )
    private String creatorId;

    @Column(length = 64)
    @Schema(
        description = "修改人ID"
        , accessMode = Schema.AccessMode.READ_ONLY
    )
    private String modifierId;

    @Override
    public String getId() {
        if (super.getId() == null) {
            generateId();
        }
        return super.getId();
    }

    public void generateId() {
        String id = generateHexId(orderNumber, itemIds);
        setId(id);
    }

    public static String generateHexId(String orderNumber, List<String> itemIds) {
        return DigestUtils.md5Hex(String.join(orderNumber, "|", String.join(",", itemIds)));
    }
}