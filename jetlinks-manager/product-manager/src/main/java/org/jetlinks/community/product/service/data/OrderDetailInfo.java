package org.jetlinks.community.product.service.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetlinks.community.product.enums.OrderStatus;

/**
 * @author wangsheng
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailInfo {

    @Schema(description = "订单ID")
    private String id;

    @Schema(description = "订单流水号")
    private String orderNumber;

    @Schema(description = "商品ID")
    private String itemId;

    @Schema(description = "订单类型")
    private String type;

    @Schema(description = "订单状态")
    private OrderStatus status;

    @Schema(
        description = "创建人ID(只读)"
        , accessMode = Schema.AccessMode.READ_ONLY
    )
    private String creatorId;

    @Schema(
        description = "修改人ID"
        , accessMode = Schema.AccessMode.READ_ONLY
    )
    private String modifierId;

    @Schema(
        description = "创建时间(只读)"
        , accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long createTime;

    @Schema(description = "更新时间")
    private Long modifyTime;

    @Schema(description = "商品名称")
    private String itemName;

    @Schema(description = "商品分类")
    private String itemType;
}
