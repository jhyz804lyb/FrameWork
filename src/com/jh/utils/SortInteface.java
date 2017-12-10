package com.jh.utils;

public interface SortInteface
{
    /**
     * 排序中的比较大小，如果 obj1 先排到 obj2 前面那么在ASC 的情况下返回 true ,desc false
     * @param obj1
     * @param obj2
     * @return
     */
    public boolean compare( Object obj1,Object obj2) ;
}
