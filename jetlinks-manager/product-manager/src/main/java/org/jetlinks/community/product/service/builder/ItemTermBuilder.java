package org.jetlinks.community.product.service.builder;

import org.hswebframework.ezorm.core.param.Term;
import org.hswebframework.ezorm.rdb.metadata.RDBColumnMetadata;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.SqlFragments;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.term.AbstractTermFragmentBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 前端可使用以下方式传参:
 * <pre>{@code
 * {
 * "terms":[
 *      {
 *        "column":"id",
 *        "termType":"ext-name",
 *        "value":"extName"
 *      }
 *    ]
 *  }
 * }</pre>
 *
 *  后端可以使用以下方式构造
 *  <pre>{@code
 *
 *  createQuery()
 *   .where()
 *   .and(ExampleEntity::getId,ExtendedTermBuilder.termType,"extName")
 *   .fetch()
 *
 *  }</pre>
 *
 */
@Component
public class ItemTermBuilder extends AbstractTermFragmentBuilder {
    public static final String termType = "order-item";

    public ItemTermBuilder() {
        super(termType, "商品查询拓展条件");
    }

    @Override
    public SqlFragments createFragments(String columnFullName,
                                        RDBColumnMetadata column,
                                        Term term) {
        PrepareSqlFragments fragments = PrepareSqlFragments.of();

        if (term.getOptions().contains("not")) {
            fragments.addSql("not");
        }

        fragments
            .addSql("exists(select 1 from", getTableName("prod_order", column), "_order where _order.id = ")
            .addSql(columnFullName)
            .addSql(" and _item.id = ?")
            .addParameter(term.getValue());
        fragments.addSql(")");

        return fragments;
    }
}
