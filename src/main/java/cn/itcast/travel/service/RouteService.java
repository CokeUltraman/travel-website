package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

//线路service
public interface RouteService {
    public PageBean<Route> pageQuery(int cid,int currentPage,int pageSize,String rname);

    public Route findOne(String rid);
}
