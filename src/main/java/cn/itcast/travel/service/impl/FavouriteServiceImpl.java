package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavouriteDao;
import cn.itcast.travel.dao.impl.FavouriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavouriteService;

public class FavouriteServiceImpl implements FavouriteService {
    private FavouriteDao favouriteDao=new FavouriteDaoImpl();
    @Override
    public boolean isFavourite(String rid, int uid) {
        Favorite favorite= favouriteDao.findByRidAndUid(Integer.parseInt(rid),uid);
        return favorite!=null;//如果对象有值，返回true,不然false

    }

    @Override
    public void add(String rid, int uid) {
        favouriteDao.add(Integer.parseInt(rid),uid);
    }
}
