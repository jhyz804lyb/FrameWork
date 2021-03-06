package com.jh.entity;

import com.jh.Interceptor.Verify;
import org.hibernate.Query;
import org.hibernate.*;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "User")
@Table(name = "USER")
@Verify(classPath = "com.jh.vilidata.Vilidata", MethodName = "checkUserInfo")
public class User implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "U_NAME")
    private String username;
    @Column(name = "U_PWD")
    private String pwd;
    @Column(name = "U_PHONE")
    private String phone;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPwd()
    {
        return pwd;
    }

    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public boolean savecheck()
    {
//	WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
//	ServletContext application = wac.getServletContext();
        ApplicationContext act = ContextLoader.getCurrentWebApplicationContext();
        SessionFactory userService = (SessionFactory) act.getBean("sessionFactory");
        Session s = userService.openSession();
        String ds = "select u from com.jh.entity.User u where 1=1";
        Query query = s.createQuery(ds);
        List<User> list = query.list();
        return true;
    }
}
