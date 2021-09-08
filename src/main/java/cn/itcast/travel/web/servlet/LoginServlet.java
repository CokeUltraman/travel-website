package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户名密码，封装User对象
        Map<String, String[]> map = request.getParameterMap();
        User user=new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service查询
        UserService service=new UserServiceImpl();
        User u=service.login(user);

        ResultInfo info=new ResultInfo();
        //判断用户是否为null
        if(u==null){
            info.setFlag(false);
            info.setErrorMsg("用户名密码错误");

        }else{
            if(u!=null&& !"y".equals(u.getStatus())){
                info.setFlag(false);
                info.setErrorMsg("尚未激活，请登录邮箱激活");

            }
            else{
                request.getSession().setAttribute("user",u);
                info.setFlag(true);


            }
        }
        response.setContentType("application/json;charset=utf-8");

        ObjectMapper mapper=new ObjectMapper();
        mapper.writeValue(response.getOutputStream(),info);





    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
