package com.dream.iot.test.db.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dream.iot.test.db.product.entity.SpProduct;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lgl
 * @since 2022-06-27
 */
public interface ISpProductService extends IService<SpProduct> {

    public  Boolean  AddProdut(SpProduct record);

    public int updateSpProductById (SpProduct record);

    public String  GetCurrentOrderCode(String  line);



}
