package cn.zhengsigen.mybatis.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * user：少
 * dateTime: 2019/8/7 12:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResData {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 描述
     */
    private String desc;

    /**
     * 数据
     */
    private Object data;


    //状态码与对应描述 transien
    private static final Map<Integer,String > CODE_DESC = new HashMap<>();

    {
        CODE_DESC.put(0,"成功");
        CODE_DESC.put(1000,"参数验证不通过，存在不合理的数据");
        CODE_DESC.put(1001,"参数缺失");
        CODE_DESC.put(1002,"参数类型异常");
        CODE_DESC.put(1003,"不支持的Http媒体类型异常");
        CODE_DESC.put(1004,"信息格式不匹配");
        CODE_DESC.put(1005,"Http请求方法不支持");
        CODE_DESC.put(2000,"业务处理失败，请重试");
    }


    public static ResData of(Integer code, String desc, Object data) {
        ResData resData = new ResData();
        resData.code = code;
        resData.desc = desc;
        resData.data = data;
        return resData;
    }


    /**
     * 访问成功 有状态码、描述和数据
     * @param code
     * @param desc
     * @param data
     * @return
     */
    public static ResData ofSuccess(Integer code, String desc, Object data) {

        return of(code, desc, data);
    }

    /**
     * 访问成功 有状态码和描述
     * @param code
     * @param desc
     * @return
     */
    public static ResData ofSuccess(Integer code, String desc) {

        return of(code, desc, null);
    }

    /**
     * 访问成功 只有状态码
     * @param code
     * @return
     */
    public static ResData ofSuccess(Integer code) {

        return ofSuccess(code, null);
    }

    /**
     * 失败 有状态码和描述
     * @param code
     * @param data
     * @return
     */
    public static ResData ofFail(Integer code, Object data) {

        return of(code, null, data);
    }

    /**
     * 失败 只有状态码
     * @param code
     * @return
     */
    public static ResData ofFail(Integer code) {

        return ofFail(code, null);
    }

}



