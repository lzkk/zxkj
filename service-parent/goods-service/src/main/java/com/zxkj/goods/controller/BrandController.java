package com.zxkj.goods.controller;

import com.zxkj.common.page.PagedList;
import com.zxkj.common.web.RespResult;
import com.zxkj.goods.condition.BrandCondition;
import com.zxkj.goods.entity.Brand;
import com.zxkj.goods.service.BrandService;
import com.zxkj.goods.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /****
     * 增加方法
     */
    @PostMapping
    public RespResult add(@RequestBody Brand brand) {
        brandService.save(brand);
        return RespResult.ok();
    }

    /****
     * 修改方法
     */
    @PutMapping
    public RespResult update(@RequestBody Brand brand) {
        brandService.updateById(brand);
        return RespResult.ok();
    }

    /****
     * 删除方法
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable(value = "id") String id) {
        brandService.removeById(id);
        return RespResult.ok();
    }

    /****
     * 条件查询
     */
    @PostMapping(value = "/search/mybatisPlus")
    public RespResult<PagedList<BrandVo>> queryList(@RequestBody BrandCondition brand) {
        PagedList<BrandVo> pagedList = brandService.queryList(brand);
        return RespResult.ok(pagedList);
    }

    /****
     * 条件查询
     */
    @PostMapping(value = "/search/pageHelper")
    public RespResult<PagedList<BrandVo>> queryPageList(@RequestBody BrandCondition brand) {
        PagedList<BrandVo> pageInfo = brandService.queryPageList(brand);
        return RespResult.ok(pageInfo);
    }

    /****
     * 根据分类ID查询品牌集合
     * http://localhost:9001/brand/category/11159
     * http://192.168.100.130/msitems/1.html
     */
    @GetMapping(value = "/category/{pid}")
    public RespResult<List<BrandVo>> categoryBrands(@PathVariable(value = "pid") Integer pid) throws InterruptedException {
        List<BrandVo> brands = brandService.queryByCategoryId(pid);
        return RespResult.ok(brands);
    }
}
