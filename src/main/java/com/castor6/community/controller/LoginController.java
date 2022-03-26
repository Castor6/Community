package com.castor6.community.controller;

import com.castor6.community.bean.User;
import com.castor6.community.service.UserService;
import com.castor6.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-25 23:28
 * @Description 包含注册和登录的控制类
 */
@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String toLoginPage(){
        return "site/login";
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
