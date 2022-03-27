package com.castor6.community.mapper;

import com.castor6.community.bean.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-27 15:43
 * @Description
 */
@Mapper
public interface LoginTicketMapper {

    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket} "
    })
    int updateStatusByTicket(String ticket, int status);

}
