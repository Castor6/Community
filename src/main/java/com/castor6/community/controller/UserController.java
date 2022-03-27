package com.castor6.community.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.castor6.community.annotation.LoginRequired;
import com.castor6.community.bean.User;
import com.castor6.community.service.UserService;
import com.castor6.community.util.CommunityUtil;
import com.castor6.community.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-27 22:14
 * @Description 处理用户账户设置的控制类
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    //修改密码
    @LoginRequired
    @PostMapping("/changePassword")
    public String changePassword(String oldPassword, String newPassword, @CookieValue("ticket") String ticket,
                                 Model model, RedirectAttributes redirectAttributes){
        User user = hostHolder.getUser();
        if(StringUtils.isBlank(oldPassword)){
            model.addAttribute("oldPasswordMsg", "原密码不能为空");
            return "/site/setting";
        }
        if(StringUtils.isBlank(newPassword)){
            model.addAttribute("newPasswordMsg", "新密码不能为空");
            return "/site/setting";
        }
        if(!user.getPassword().equals(CommunityUtil.md5(oldPassword + user.getSalt()))){
            model.addAttribute("oldPasswordMsg", "原密码不正确");
            return "/site/setting";
        }
        userService.changePassword(user.getId(), newPassword, user.getSalt());
        redirectAttributes.addFlashAttribute("msg", "修改成功，请重新登录");
        redirectAttributes.addFlashAttribute("target", "/login");
        userService.logout(ticket);
        return "redirect:/operate-result";
    }

    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage() {
        return "/site/setting";
    }

    //上传头像
    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "redirect:/user/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确!");
            return "redirect:/user/setting";
        }

        // 生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            log.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }

        // 更新当前用户的头像的路径(web访问路径)
        // http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    //不登录，也可以看别人上传的头像，所以不加拦截的注解
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            log.error("读取头像失败: " + e.getMessage());
        }
    }
}
