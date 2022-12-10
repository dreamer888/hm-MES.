package com.dream.iot.test.db.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dreamer，75039960@qq.com
 * @date 2022/06/23
 */
public final class ScanCodeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanCodeUtil.class);
    private static  final  Map<String, Object> map1 = new HashMap<>(8);

    //解析产品名称
    public static final Map<String, String> nameMap = new HashMap<String, String>() {
        {
            put("EC", "PCB板");
            put("MS", "鼠标");
            put("key3", null);
        }
    };

    public static  String[] ParseQRCode(String code)
    {
        String result[] =  code.split("#",4);
        if (result.length !=4) {
            return null;
        }

        return   result;
    }

    public static  String GetProuctName(String code)
    {
        String result=  code.substring(0,2);
        return  nameMap.get(result);
    }


    //private static final Pattern PATTERN_LONG_ID = Pattern.compile("^([0-9]{15})([0-9a-f]{32})([0-9a-f]{3})$");

    public static void main(String[] args)  throws  Exception {
        //产品编码#批次#数量#订单号
        String  codeStr ="EC101000035#220614#6000#CG20220516002";

        String strArr[] =ParseQRCode(codeStr) ;
        if (strArr!=null) {
            GetProuctName(strArr[0]);
            System.out.println( "Prouct Name="+GetProuctName(strArr[0])) ;

            for (int i = 0; i < strArr.length; i++) {
                System.out.println(strArr[i]);
            }
        }



    }

}




