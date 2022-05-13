package com.kpsilent.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kpsilent.reggie.common.R;
import com.kpsilent.reggie.entity.User;
import com.kpsilent.reggie.service.UserService;
import com.kpsilent.reggie.utils.ShortMessageUtils;
import com.kpsilent.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送短信
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpServletRequest request){
        // 获取手机号
        String phone = user.getPhone();
        log.info("phoneNumber:{}", phone);
        // 如果手机号不为空，则调用阿里云短信api发送短信
        if(StringUtils.isNotEmpty(phone)){
            // 生成4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            // 调用api发送短信
            try {
//                ShortMessageUtils.sendMsg(phone, code);
            } catch (Exception e) {
                e.printStackTrace();
                return R.error("短信发送失败");
            }
            // 将验证码存入session中
            request.getSession().setAttribute("code", 1234);
            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpServletRequest request){
        log.info("map:{}", map.toString());
        // 获取手机号
        String phone = map.get("phone").toString();
        // 获取验证码
        String code = map.get("code").toString();
        // 获取Session中的验证码
        String sessionCode = request.getSession().getAttribute("code").toString();
        // 比对验证码
        if(code != null && code.equals(sessionCode)){
            // 查询数据库中是否有当前手机号，如果有返回当前用户信息，没有则插入当前用户信息，并返回
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if(user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            request.getSession().setAttribute("user", user.getId());
            return R.success(user);
        }

        return R.error("登录失败");
    }
}
