package com.lgl.mes.product.service;

import com.lgl.mes.product.entity.SpProduct;
import com.baomidou.mybatisplus.extension.service.IService;

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

    public String  GetCurrentOrderCode();

}
