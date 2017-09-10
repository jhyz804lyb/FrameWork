package com.jh.service;

import com.jh.base.BaseDao;
import com.jh.entity.*;

/**
 * @author liyabin
 * @date 2017-09-06上午 10:44
 */
public interface FaceService extends BaseDao<FaceInfo>
{
    public boolean checkFace(FaceInfo faceInfo);
}