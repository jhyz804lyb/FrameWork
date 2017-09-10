package com.jh.base;

/**
 * 分页信息页面
 *
 * @author liyabin
 * @date 2017-08-30下午 8:26
 */
public class PageInfo
{
    public PageInfo()
    {
    }

    /**
     * 当前页
     */
    private Integer pageNo;
    /**
     * 最大页
     */
    private Integer maxPage;
    /**
     * 多少数据
     */
    private Integer maxCount;
    /**
     * 每页多少数据
     */
    private Integer pageCount;
    /**
     * 请求方法的地址 用于拼接URL
     */
    private String url;
    /**
     * 这次请求的参数拼接
     */
    private String parameterUrl;

    public Integer getPageNo()
    {
        return pageNo;
    }

    public void setPageNo(Integer pageNo)
    {
        this.pageNo = pageNo;
    }

    public Integer getMaxPage()
    {
        return maxPage;
    }

    public void setMaxPage(Integer maxPage)
    {
        this.maxPage = maxPage;
    }

    public Integer getPageCount()
    {
        return pageCount;
    }

    public void setPageCount(Integer pageCount)
    {
        this.pageCount = pageCount;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getParameterUrl()
    {
        return parameterUrl;
    }

    public void setParameterUrl(String parameterUrl)
    {
        this.parameterUrl = parameterUrl;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }
}
