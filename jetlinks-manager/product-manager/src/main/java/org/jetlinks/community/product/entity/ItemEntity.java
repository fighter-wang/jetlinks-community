package org.jetlinks.community.product.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.h2.util.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hswebframework.ezorm.rdb.mapping.annotation.Comment;
import org.hswebframework.ezorm.rdb.mapping.annotation.DefaultValue;
import org.hswebframework.reactor.excel.CellDataType;
import org.hswebframework.web.api.crud.entity.*;
import org.hswebframework.web.crud.annotation.EnableEntityEvent;
import org.hswebframework.web.crud.generator.Generators;
import org.hswebframework.web.utils.DigestUtils;
import org.hswebframework.web.validator.CreateGroup;
import org.jetlinks.community.io.excel.annotation.ExcelHeader;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

/**
 * 商品管理实体类
 *
 * @author wangsheng
 * @since 1.0
 */
@Getter
@Setter
@Table(name = "prod_item")
@Schema(description = "商品管理表")
@Comment("商品管理表")
@EnableEntityEvent
public class ItemEntity
    extends GenericEntity<String> implements RecordCreationEntity, RecordModifierEntity {

    @Schema(description = "商品名称")
    @Column(length = 32, nullable = false)
    @Length(max = 32, min = 1, groups = CreateGroup.class)
    @ExcelHeader
    private String name;

    @Schema(description = "商品分类 ")
    @Column
    @ExcelHeader
    private String type;

    @Schema(description = "商品批次")
    @Pattern(regexp = "[A-Za-z]{3}@[0-9]{2}", message = "batch限制格式为：3位字母编码@2位数字编码，例如abc@12", groups = CreateGroup.class)
    @Column(length = 6, nullable = false)
    @ExcelHeader
    private String batch;

    @Schema(description = "上架时间")
    @Column(name = "shelf_time", updatable = false)
    @GeneratedValue(generator = Generators.CURRENT_TIME)
    @ExcelHeader(dataType = CellDataType.DATE_TIME)
    private Long shelfTime;

    @Schema(description = "下架时间")
    @Column(name = "removal_time", updatable = false)
    @ExcelHeader(dataType = CellDataType.DATE_TIME)
    private Long removalTime;

    @Column(updatable = false)
    @Schema(
        description = "创建者ID(只读)"
        , accessMode = Schema.AccessMode.READ_ONLY
    )
    @ExcelHeader
    private String creatorId;

    @Column(length = 64)
    @Schema(
        description = "修改人ID"
        , accessMode = Schema.AccessMode.READ_ONLY
    )
    @ExcelHeader
    private String modifierId;

    @Column(updatable = false)
    @DefaultValue(generator = Generators.CURRENT_TIME)
    @Schema(
        description = "创建时间(只读)"
        , accessMode = Schema.AccessMode.READ_ONLY
    )
    @ExcelHeader(dataType = CellDataType.DATE_TIME)
    private Long createTime;

    @Column
    @DefaultValue(generator = Generators.CURRENT_TIME)
    @Schema(description = "更新时间")
    @ExcelHeader(dataType = CellDataType.DATE_TIME)
    private Long modifyTime;


    @Override
    public String getId() {
        if (StringUtils.isNullOrEmpty(super.getId())) {
            generateId();
        }
        return super.getId();
    }

    public void generateId() {
        String id = generateHexId(name, creatorId);
        setId(id);
    }

    public static String generateHexId(String itemName, String creatorId) {
        return DigestUtils.md5Hex(String.join(itemName, "|", creatorId));
    }}
