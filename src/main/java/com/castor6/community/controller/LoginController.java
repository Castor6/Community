package com.castor6.community.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.castor6.community.annotation.LoginRequired;
import com.castor6.community.bean.User;
import com.castor6.community.service.UserService;
import com.castor6.community.util.CommunityConstant;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-25 23:28
 * @Description 包含注册和登录的控制类
 */
@Slf4j
@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserService userService;

    @Autowired
    private Producer producer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @LoginRequired
    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String toLoginPage(){
        return "site/login";
    }

    @PostMapping("/login")
    public String login(String username, String password, String code, boolean rememberme,
                        Model model, HttpSession session, HttpServletResponse response) {
        // 检查验证码
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确!");
            return "/site/login";
        }
        // 检查账号,密码
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;   //默认登陆凭证存12小时，勾选了记住我存90天
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie); //响应时将cookie回带给浏览器存储
            return "redirect:/index";   //重定向到首页
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);
        // 将验证码存入session
        session.setAttribute("kaptcha", text);
        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            log.error("响应验证码失败:" + e.getMessage());
        }
    }

    @GetMapping("/register")
    public String toRegisterPage(Model model){
        if(model.getAttribute("user") == null){
            model.addAttribute("user", new User());     //因为注册失败回显需要一个对象，一开始进入到注册页面需要一个空对象
        }
        return "site/register";
    }

    @PostMapping("/register")
    public String register(@Validated User user, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            return "redirect:/register";
        }
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()){   //注册成功
            redirectAttributes.addFlashAttribute("msg", "注册成功，我们已向你的邮箱中发送了一封激活邮件，请尽快您的账号");
            redirectAttributes.addFlashAttribute("target", "/index");   //再以游客形式回到首页
            return "redirect:/operate-result";
        }else{
            redirectAttributes.addFlashAttribute("usernameMsg", map.get("usernameMsg"));
            redirectAttributes.addFlashAttribute("emailMsg", map.get("emailMsg"));
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/register";
        }
    }

    @GetMapping("/operate-result")
    public String toRegisterResult(){
        return "site/operate-result";
    }

    // 激活的请求路径：项目路径/activation/101/code
    @GetMapping("/activation/{userId}/{code}")
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功,您的账号已经可以正常使用了!");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作,该账号已经激活过了!");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败,您提供的激活码不正确!");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }
}
