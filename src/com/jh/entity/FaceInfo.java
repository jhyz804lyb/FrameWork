package com.jh.entity;

import com.jh.Interceptor.*;

import javax.persistence.*;

/**
 * @author liyabin
 * @date 2017-09-06上午 10:36
 */
@Entity(name = "FaceInfo")
@Table(name = "faceInfo")
@Verify(classPath = "com.jh.vilidata.Vilidata",MethodName = "checkFaceInfo")
public class FaceInfo extends EBase
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userName")
    @NotNull
    private String userName;

    @Column(name = "faceImg")
    @NotNull
    private String faceImg;

    @Column(name = "faceIco")
    @NotNull
    private String faceIco;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getFaceImg()
    {
        return faceImg;
    }

    public void setFaceImg(String faceImg)
    {
        this.faceImg = faceImg;
    }

    public String getFaceIco()
    {
        return faceIco;
    }

    public void setFaceIco(String faceIco)
    {
        this.faceIco = faceIco;
    }
}
