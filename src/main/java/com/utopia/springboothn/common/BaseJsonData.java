package com.utopia.springboothn.common;

/**
 * @author hn
 * @date 2018/08/02
 */
public class BaseJsonData extends BaseJson {

    protected  Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
