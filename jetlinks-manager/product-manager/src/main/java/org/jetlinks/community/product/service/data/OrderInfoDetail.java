package org.jetlinks.community.product.service.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hswebframework.ezorm.rdb.mapping.annotation.ColumnType;
import org.hswebframework.ezorm.rdb.mapping.annotation.DefaultValue;
import org.hswebframework.ezorm.rdb.mapping.annotation.EnumCodec;
import org.hswebframework.ezorm.rdb.mapping.annotation.JsonCodec;
import org.hswebframework.web.crud.generator.Generators;
import org.jetlinks.community.product.entity.ItemEntity;
import org.jetlinks.community.product.enums.OrderStatus;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotBlank;
import java.sql.JDBCType;
import java.util.List;

/**
 * 订单管理具体信息
 *
 * @author wangsheng
 * @since 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderInfoDetail {

    @Schema(description = "订单ID")
    private String id;

    @Schema(description = "订单流水号")
    private String orderNumber;

    @Schema(description = "订单商品集合")
    @JsonCodec
    private List<ItemEntity> itemList;

    @Schema(description = "订单类型")
    private String type;

    @Schema(description = "订单状态")
    @EnumCodec
    @NotBlank
    private OrderStatus status;

    @Schema(
        description = "创建时间(只读)"
        , accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long createTime;

    @Schema(description = "更新时间")
    private Long modifyTime;

    @Schema(
        description = "创建者ID(只读)"
        , accessMode = Schema.AccessMode.READ_ONLY
    )
    private String creatorId;

    @Schema(
        description = "修改人ID"
        , accessMode = Schema.AccessMode.READ_ONLY
    )
    private String modifierId;
}
