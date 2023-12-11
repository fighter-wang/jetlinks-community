package org.jetlinks.community.product.service.builder;

import org.hswebframework.ezorm.core.param.Term;
import org.hswebframework.ezorm.rdb.metadata.RDBColumnMetadata;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.SqlFragments;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.term.AbstractTermFragmentBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.jetlinks.community.utils.ConverterUtils.convertTerms;

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
 * <p>
 * 后端可以使用以下方式构造
 * <pre>{@code
 *
 *  createQuery()
 *   .where()
 *   .and(ExampleEntity::getId,ExtendedTermBuilder.termType,"extName")
 *   .fetch()
 *
 *  }</pre>
 */
@Component
public class ItemTermBuilder extends AbstractTermFragmentBuilder {
    public static final String termType = "order_item";

    public ItemTermBuilder() {
        super(termType, "商品查询拓展条件");
    }

    @Override
    public SqlFragments createFragments(String columnFullName,
                                        RDBColumnMetadata column,
                                        Term term) {

        List<Term> terms = convertTerms(term.getValue());

        PrepareSqlFragments fragments = PrepareSqlFragments.of();
        if (term.getOptions().contains("not")) {
            fragments.addSql("not");
        }
        fragments
            .addSql("exists(select 1 from", getTableName("prod_item", column), "_item where ")
            .addSql(
                terms
                    .stream()
                    .map(t -> "_item." + t.getColumn() + "= ?" + " and ")
                    .collect(Collectors.joining())
            )
            .addParameter(terms.stream().map(ter -> ter.getValue()).collect(Collectors.toList()))
            .addSql("not exists(select 1 from prod_order _order where _order.item_id = prod_item.id)");
        fragments.addSql(")");

        return fragments;
    }
}
