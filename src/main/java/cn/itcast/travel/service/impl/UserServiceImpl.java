package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao userdao=new UserDaoImpl();



    @Override
    public boolean regist(User user) {
        User u= userdao.findByUsername(user.getUsername());
        if(u != null){
            return false;
        }else{
            //设置激活码和激活状态
            user.setCode(UuidUtil.getUuid());
            user.setStatus("n");

            //2 保护用户信息
            userdao.save(user);
            //3 发送激活邮件
            String content="<a href='http://localhost:8080/travel/user/active?code="+user.getCode()+"'>点击激活 旅游网</a>";
            MailUtils.sendMail(user.getEmail(),content,"注册验证");


            return true;
        }




    }

    @Override
    public boolean active(String code) {
        User user=userdao.findByUserCode(code);
        if(user!=null){
            userdao.updateStatus(user);
            return true;
        }else {
            return false;
        }



    }

    @Override
    public User login(User user) {

        return userdao.findByUsernameandPassword(user.getUsername(),user.getPassword());
    }
}
