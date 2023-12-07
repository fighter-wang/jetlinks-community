package org.jetlinks.community.product.service;

import org.hswebframework.ezorm.core.param.Term;
import org.hswebframework.ezorm.rdb.metadata.RDBColumnMetadata;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.SqlFragments;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.term.AbstractTermFragmentBuilder;
import org.springframework.stereotype.Component;

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
public class ExtendedTermBuilder extends AbstractTermFragmentBuilder {
    public static final String termType = "ext-name";

    public ExtendedTermBuilder() {
        super(termType, "示例拓展关联条件");
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
            .addSql("exists(select 1 from", getTableName("prod_item", column), "_ext where _ext.example_id = ")
            .addSql(columnFullName)
            .addSql(" and ")
            .addSql("_ext.name = ?)")
            .addParameter(term.getValue());

        System.out.println("防火等级");
        System.out.println(fragments);
        return fragments;
    }
}
