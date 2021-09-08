package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavouriteDao {
    public Favorite findByRidAndUid(int rid, int uid);

    public int findCountByRid(int rid);


    //添加收藏
    public void add(int parseInt, int uid);
}
