package com.lgl.mes.common.util;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.BlowfishCipherService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dreamer，75039960@qq.com
 * @date 2022/06/23
 */
public final class PasswordUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordUtil.class);

    public static  String GenPassword(String username,String password)
    {
        String result = new Md5Hash(password,username , 3).toString();

        return   result;
    }
        //private static final Pattern PATTERN_LONG_ID = Pattern.compile("^([0-9]{15})([0-9a-f]{32})([0-9a-f]{3})$");




    public static void main(String[] args)  throws  Exception {


        String password ="123456";//18665802636
        String base64Encoded = Base64.encodeToString(password.getBytes());

        System.out.println("base64Encoded="+base64Encoded);
        String decodeToString = Base64.decodeToString(base64Encoded);
        System.out.println("decodeToString="+decodeToString);

        String  pwd =GenPassword("admin" ,"123456"); //123456
        System.out.println("pwd="+pwd);
    }

}

