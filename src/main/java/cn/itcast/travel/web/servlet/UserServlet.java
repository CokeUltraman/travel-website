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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    //声明userService业务对象
    private UserService service=new UserServiceImpl();

    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //输入的验证码
        String check = request.getParameter("check");
        //从session中获取验证码
        HttpSession session=request.getSession();
        String checkcode_server = (String)session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        //比较
        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
            ResultInfo info=new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("checkcode wrong注册失败");

            ObjectMapper mapper=new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;

        }


        //获取数据
        Map<String, String[]> map = request.getParameterMap();
        //封装数据
        User user=new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service完成注册
        //UserService service=new UserServiceImpl();
        boolean flag=service.regist(user);
        ResultInfo info=new ResultInfo();
        //响应结果
        if(flag){
            info.setFlag(true);

        }else {
            info.setFlag(false);
            info.setErrorMsg("user存在，注册失败");
        }
        ObjectMapper mapper=new ObjectMapper();
        String json = mapper.writeValueAsString(info);

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

    }


    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        //UserService service=new UserServiceImpl();
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
    public void findone(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        //将user写回客户端
//        ObjectMapper mapper=new ObjectMapper();
//        response.setContentType("application/json;charset=utf-8");
//        mapper.writeValue(response.getOutputStream(),user);
        writeValue(user,response);

    }
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath()+"/login.html");
    }

    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String code = request.getParameter("code");
        if(code!=null){
            //UserService service=new UserServiceImpl();
            boolean flag=service.active(code);

            String msg=null;
            if(flag){
                msg="激活成功，请<a href='http://localhost:8080/travel/login.html'>登录</a>";

            }else{
                msg="激活失败，请联系管理员";


            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }
}
