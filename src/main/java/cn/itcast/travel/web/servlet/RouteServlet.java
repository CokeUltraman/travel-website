package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavouriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavouriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService=new RouteServiceImpl();
    private FavouriteService favouriteService=new FavouriteServiceImpl();
    //分页查询
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String rname = request.getParameter("rname");
        rname=new String(rname.getBytes("iso-8859-1"),"utf-8");

        int cid=0;
        //处理参数
        if(cidStr!=null && cidStr.length()>0 && !"null".equals(cidStr)){
            cid= Integer.parseInt(cidStr);

        }
        if("null".equals(rname)){
            rname="";

        }
        int pageSize=0;
        if(pageSizeStr!=null &&pageSizeStr.length()>0){
            pageSize= Integer.parseInt(pageSizeStr);

        }
        else{
            pageSize=5;
        }
        int currentPage=0;
        if(currentPageStr!=null && currentPageStr.length()>0){
            currentPage= Integer.parseInt(currentPageStr);

        }else{
            currentPage=1;
        }

        //调用service
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize,rname);
        writeValue(pb,response);

    }
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //根据id查询一个旅游线路的详细信息
        //接收id
        String rid = request.getParameter("rid");
        //调用service 查询一个route对象
        Route route=routeService.findOne(rid);

        //转为json写回客户端
        writeValue(route,response);
    }


    public void isFavourite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //判断当前登录用户是否收藏该线路
       //获取线路id
        String rid  = request.getParameter("rid");
        //获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if(user==null){
            //用户尚未登录
            //uid=0;
            return;
        }else {
            //用户已经登录
            uid= user.getUid();

        }
        //调用favouriteservice查询是否收藏
        boolean flag = favouriteService.isFavourite(rid, uid);

        writeValue(flag,response);



    }
    public void addFavourite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //添加收藏
        String rid = request.getParameter("rid");
        //获取用户对象
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if(user==null){
            //用户尚未登录
            return;
        }else {
            //用户已经登录
            uid= user.getUid();

        }
        //调用service添加
        favouriteService.add(rid,uid);



    }
    

}
