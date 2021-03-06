package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class BaseServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //完成方法分发
        //获取请求路径
        String uri = req.getRequestURI();
        //获取方法名称
        String methodname = uri.substring(uri.lastIndexOf("/") + 1);
        //获取方法对象Method
        try {
            //忽略访问权限的修修饰符，获取方法
            Method method = this.getClass().getMethod(methodname, HttpServletRequest.class, HttpServletResponse.class);
            //反射
            //暴力反射
            //method.setAccessible(true);
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    //直接将传入的对象序列化为json,并写回客户端
    public void writeValue(Object obj,HttpServletResponse response) throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),obj);
    }

    //将传入的对象序列化为json,返回
    public String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        String s = mapper.writeValueAsString(obj);
        return s;

    }
}
