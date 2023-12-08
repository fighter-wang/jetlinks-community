package org.jetlinks.community.product.service.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.h2.util.StringUtils;
import org.hswebframework.ezorm.rdb.mapping.annotation.ColumnType;
import org.hswebframework.ezorm.rdb.mapping.annotation.DefaultValue;
import org.hswebframework.ezorm.rdb.mapping.annotation.EnumCodec;
import org.hswebframework.ezorm.rdb.mapping.annotation.JsonCodec;
import org.hswebframework.web.api.crud.entity.GenericEntity;
import org.hswebframework.web.api.crud.entity.RecordCreationEntity;
import org.hswebframework.web.api.crud.entity.RecordModifierEntity;
import org.hswebframework.web.crud.annotation.EnableEntityEvent;
import org.hswebframework.web.crud.generator.Generators;
import org.hswebframework.web.utils.DigestUtils;
import org.jetlinks.community.product.entity.ItemEntity;
import org.jetlinks.community.product.enums.OrderStatus;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotBlank;
import java.sql.JDBCType;
import java.util.List;
import java.util.stream.Collectors;

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
public class OrderInfoDetail
    extends GenericEntity<String> implements RecordCreationEntity, RecordModifierEntity {

    @Schema(description = "订单流水号")
    @GeneratedValue(generator = Generators.DEFAULT_ID_GENERATOR)
    private String orderNumber;

    @Schema(description = "订单商品集合")
    @JsonCodec
    private List<String> itemIds;

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

    @Override
    public String getId() {
        if (StringUtils.isNullOrEmpty(super.getId())) {
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
