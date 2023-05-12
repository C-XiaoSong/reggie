package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggie.pojo.User;
import com.reggie.service.UserService;
import com.reggie.utis.R;
import com.reggie.utis.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author： 陈晓松
 * @CLASS_NAME： UserController
 * @date： 2023/5/10
 * @注释： 移动端手机验证登录
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
    * @Author: 陈晓松
    * @Description: 发送手机短信验证码
    * @Params: [user]
    * @Return com.reggie.utis.R<java.lang.String>
    */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        // 获取手机号
        String phone = user.getPhone();
        if (StringUtils.hasLength(phone)){
            // 生成随机的4为验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("生成的验证码code：{}",code);
            // 调用阿里云提供的短信服务API完成发送短信
            // SMSUtils.sendMessage("陈","",phone,code);
            // 需要将生成的验证码保存到session中，登录的时候校验使用
            session.setAttribute("phone",code);
            return R.success("验证码已发送");
        }
        return R.error("验证码发送失败");
    }

    /**
    * @Author: 陈晓松
    * @Description: 移动端用户登录
    * @Params: [map, session]
    * @Return com.reggie.utis.R<java.lang.String>
    */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session) {
        log.info("map：{}",map);
        // 获取手机号
        String phone = map.get(("phone")).toString();
        // 获取验证码
        String code = map.get("code").toString();
        // 从session中获取保存的验证码
        Object CodeInSession = session.getAttribute("phone");
        // 进行验证码比对（页面提交的验证码和session中保存的进行比对）
        if (CodeInSession != null && CodeInSession.equals(code)){
            // 比对成功，说明登录成功
            // 判断当前手机号是否为新用户，是新用户就完成自动注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user == null){
                // 完成自动注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }

}
