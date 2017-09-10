package com.jh.base;

import org.springframework.stereotype.Service;

/**
 * 分页操作的实现类
 */
@Service
public class CreatePageSQL implements CreatePageInfoInteface {
    /**
     * 获取分页总数
     *
     * @param OQL 执行的查询语句
     * @return 返回当前数据语句的总数据量SQL语句
     */
    @Override
    public String getPageCountSQL(String OQL) {
        StringBuilder sql = new StringBuilder();
        if (hasSelectAll(OQL)) {
            int len = OQL.toUpperCase().startsWith("SELECT") ? 5 : 7;
            sql.append(OQL.substring(0, len));
            sql.append("COUNT(1) ");
            sql.append(OQL.substring(OQL.toUpperCase().indexOf("FROM")));
            return sql.toString();
        } else {
            sql.append("select count(1) ");
            sql.append(OQL);
            return sql.toString();
        }
    }

    /**
     * 封装分页查询
     *
     * @param OQL      执行的查询语句
     * @param pageInfo 分页信息
     * @return 返回当前数据语句的分页OQL/SQL语句
     */
    @Override
    public String getPageListSQL(String OQL, PageInfo pageInfo) {
        StringBuilder result = new StringBuilder(OQL);
        Integer pageNo = pageInfo.getPageNo();
        Integer pageCount = pageInfo.getPageCount();
        pageNo = pageNo == null ? 1 : pageNo;
        pageCount = pageCount == null ? 10 : pageCount;
        result.append(" limit ").append((pageNo - 1) * pageCount).append(",").append(pageCount);
        return result.toString();
    }

    /**
     * 判断是否查询指定查询字段
     *
     * @param OQL
     * @return
     */
    private boolean hasSelectAll(String OQL) {
        if (OQL.toUpperCase().startsWith("SELECT") || OQL.toUpperCase().startsWith("(SELECT)")) {
            return true;
        } else {
            return false;
        }
    }


}
