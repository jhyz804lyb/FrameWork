package com.jh.service.impl;

import com.jh.base.BaseDaoImlp;
import com.jh.entity.FaceInfo;
import com.jh.service.FaceService;
import com.jh.utils.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liyabin
 * @date 2017-09-06上午 10:46
 */
@Service
public class FaceServiceImpl extends BaseDaoImlp<FaceInfo> implements FaceService
{

    @Override
    public boolean checkFace(FaceInfo faceInfo)
    {
        String oql = "select t from FaceInfo t where t.userName =:userName";
        if (faceInfo == null && StringUtils.isNull(faceInfo.getUserName()))
        {
            faceInfo.setSuccess(false);
            faceInfo.setMsg("用户名不能为空");
            return false;
        }
        else
        {
            Query query = this.createQuery(oql);
            List userName = query.setParameter("userName", faceInfo.getUserName()).list();
            if (userName == null || userName.size() == 0)
            {
                faceInfo.setSuccess(true);
                return true;
            }
            else
            {
                faceInfo.setSuccess(false);
                faceInfo.setMsg("用户名已经存在！");
                return false;
            }
        }

    }
}
