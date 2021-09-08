package cn.itcast.travel.service;

public interface FavouriteService {
    public boolean isFavourite(String rid,int uid);


    //添加收藏
    public void add(String rid, int uid);
}
