package com.jh.base;

/**
 * 主要由于不同数据库分页操作不一致这里希望外部去实现它获取具体分页的sqll语句 一句传入的OQL或者sql语句
 */
public interface CreatePageInfoInteface {
    /**
     * 获取分页总数
     *
     * @param OQL 执行的查询语句
     * @return 返回当前数据语句的总数据量SQL语句
     */
    public String getPageCountSQL(String OQL);

    /**
     * 封装分页查询
     *
     * @param OQL 执行的查询语句
     * @param pageInfo 分页信息
     * @return 返回当前数据语句的分页OQL/SQL语句
     */
    public String getPageListSQL(String OQL, PageInfo pageInfo);

}
