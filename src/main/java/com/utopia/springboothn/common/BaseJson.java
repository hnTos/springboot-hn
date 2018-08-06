package com.utopia.springboothn.common;

/**
 * @author hn
 * @date 2018/08/02
 */
public class BaseJson {
    /**
     * 是否有错
     */
    private Boolean hasError = false;
    /**
     * 四个0 表示正常
     */

    private String code = "0000";

    /**
     * 默认成功提示信息suc
     */
    private String message = "suc";

    public BaseJson() {
    }
    public void BaseJson(Boolean hasError,String message){
        this.message = message;
        this.hasError = hasError;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setErrorMessage(String message){
        this.message = message;
        this.hasError = Boolean.TRUE;
    }
}
