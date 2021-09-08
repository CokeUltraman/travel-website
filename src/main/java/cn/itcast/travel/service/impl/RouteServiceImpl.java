package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavouriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellDao;
import cn.itcast.travel.dao.impl.FavouriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routedao=new RouteDaoImpl();
    private RouteImgDao routeImgDao=new RouteImgDaoImpl();
    private SellDao sellDao=new SellDaoImpl();
    private FavouriteDao favouriteDao=new FavouriteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {

        //封装PageBean
        PageBean<Route> pb=new PageBean<Route>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        //设置总记录数
        int  totalCount=routedao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);

        //设置当前页显示的数据集合
        int start=(currentPage-1)*pageSize;
        List<Route> list=routedao.findByPage(cid,start,pageSize,rname);
        pb.setList(list);

        int totalPage=totalCount % pageSize== 0 ? totalCount / pageSize : (totalCount/pageSize)+1  ;
        pb.setTotalPage(totalPage);





        return pb;
    }

    @Override
    public Route findOne(String rid) {
        //根据rid去route表中查询route对象
        Route route=routedao.findOne(Integer.parseInt(rid));
        //根据route的id去查询图片集合信息
        List<RouteImg> list = routeImgDao.findByRid(route.getRid());
        //将集合设置到route对象
        route.setRouteImgList(list);
        //根据route sid查询商家对象
        Seller seller = sellDao.findById(route.getSid());
        route.setSeller(seller);
        //查询收藏次数
        int count=favouriteDao.findCountByRid(Integer.parseInt(rid));
        route.setCount(count);

        return route;



    }
}
