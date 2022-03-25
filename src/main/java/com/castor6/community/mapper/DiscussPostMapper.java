package com.castor6.community.mapper;

import com.castor6.community.bean.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-24 20:33
 * @Description
 */
@Mapper
public interface DiscussPostMapper {
    //若只有一个参数，且该参数被使用在动态sql中的if判断中，需要通过@Param取别名
    List<DiscussPost> selectDiscussPosts(@Param("userId") int userId);   //当userId为0时查询的帖子不局限于某个用户，合并到用户个人主页查询其帖子
    int selectDiscussPostRows(@Param("userId") int userId);
}
