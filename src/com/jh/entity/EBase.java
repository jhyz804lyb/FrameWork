package com.jh.entity;

/**
 * @author liyabin
 * @date 2017-09-06上午 10:37
 */
public class EBase
{
    private Boolean success;
    private String msg;
    private Object data;

    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }
}
