package org.jetlinks.community.product.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.jetlinks.community.io.excel.ExcelUtils;
import org.jetlinks.community.io.excel.ImportHelper;
import org.jetlinks.community.io.file.FileInfo;
import org.jetlinks.community.io.file.FileManager;
import org.jetlinks.community.io.file.FileOption;
import org.jetlinks.community.io.utils.FileUtils;
import org.jetlinks.community.product.entity.ItemEntity;
import org.jetlinks.community.product.service.ItemService;
import org.jetlinks.community.product.service.OrderService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


/**
 * 商品管理接口
 *
 * @author wangsheng
 * @since 1.0
 */

@RestController
@RequestMapping("/prod/item")
@AllArgsConstructor
@Getter
@Resource(id = "item", name = "商品管理接口")
@Tag(name = "商品管理") //swagger
public class ItemController implements ReactiveServiceCrudController<ItemEntity, String> {

    private final OrderService orderService;

    private final ItemService service;

    private final FileManager fileManager;

    @GetMapping("/getItemListByOrderInfo")
    @Operation(summary = "查询存在订单的商品列表")
    public Flux<ItemEntity> getItemListByOrderInfo(@Parameter QueryParamEntity queryParam){
        return service
            .createQuery()
            .where()
            .and(ItemEntity::getId, "order_item", queryParam.getTerms())
            .fetch();
    }


    //根据上传的文件来导入数据并将导入结果保存到文件中返回结果文件地址，
    //客户端可以引导用户下载结果文件
    @Operation(summary = "商品批量导入")
    @PostMapping("/_import.{format}")
    public Mono<String> importByFileUpload(@PathVariable String format,
                                           @RequestPart("file") Mono<FilePart> file) {


        return FileUtils
            .dataBufferToInputStream(file.flatMapMany(FilePart::content))
            .flatMap(inputstream -> new ImportHelper<>(
                ItemEntity::new,
                //数据处理逻辑
                flux -> service.save(flux).then())
                //批量处理数量
                .bufferSize(200)
                //当批量处理失败时,是否回退到单条数据处理
                .fallbackSingle(true)
                .doImport(inputstream,
                          format,
                          //忽略导入结果
                          info -> null,
                          //将导入的结果保存为临时文件
                          result -> fileManager
                              .saveFile("import." + format, result, FileOption.tempFile)
                              .map(FileInfo::getAccessUrl))
                .last()
            );
    }

    //导出数据
    @Operation(summary = "商品批量导出")
    @GetMapping("/_export/{name}.{format}")
    public Mono<Void> export(QueryParamEntity param,
                             @PathVariable String name,
                             //文件格式: 支持csv,xlsx
                             @PathVariable String format,
                             ServerWebExchange exchange) {

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //文件名
        exchange.getResponse().getHeaders().setContentDisposition(
            ContentDisposition
                .attachment()
                .filename(name + "." + format, StandardCharsets.UTF_8)
                .build()
        );
        return exchange
            .getResponse()
            .writeWith(
                ExcelUtils.write(ItemEntity.class, service.query(param.noPaging()), format)
            );
    }
}
