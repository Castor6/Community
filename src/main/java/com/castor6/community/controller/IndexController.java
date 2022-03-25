package com.castor6.community.controller;

import com.castor6.community.bean.DiscussPost;
import com.castor6.community.bean.DiscussPostPageInfo;
import com.castor6.community.service.DiscussPostService;
import com.castor6.community.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-24 22:30
 * @Description
 */
@Controller
public class IndexController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;

    private static final int PAGE_SIZE = 10;         //分页一页的通常记录数大小
    private static final int NAVIGATE_PAGES = 5;    //分页条通常显示的页码数

    @GetMapping({"/", "/index"})
    public String toIndexPage(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model){
        DiscussPostPageInfo page = new DiscussPostPageInfo(discussPostService.pageByUserId(0, pn, PAGE_SIZE, NAVIGATE_PAGES));
        List<Map<String, Object>> list = page.getList();
        PageInfo<DiscussPost> pageInfo = page.getPageInfo();
        for(DiscussPost discussPost : pageInfo.getList()){
            HashMap<String, Object> map = new HashMap<>();
            map.put("post", discussPost);
            map.put("user", userService.findUserById(discussPost.getUserId()));
            list.add(map);
        }
        pageInfo.setList(null);     //不额外存储结果集了
        model.addAttribute("page", page);
        return "index";
    }
}
