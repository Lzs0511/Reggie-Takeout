package com.kpsilent.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kpsilent.reggie.common.R;
import com.kpsilent.reggie.entity.Employee;
import com.kpsilent.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @RequestMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        // 1. 将页面提交的密码password进行MD5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2. 根据页面提交的username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 3. 如果没有查询到则返回登陆失败结果
        if(emp == null){
            return R.error("登录失败");
        }

        // 4. 密码比对，如果不一致则返回登陆失败
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        // 5. 查看员工状态, 如果账号已经禁用，则返回员工禁用状态
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        // 6. 登录成功，将员工id存入Session 并返回登陆成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

    }

    /**
     * 员工退出功能
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工, 员工信息：{}", employee.toString());
        // 1. 设置新增员工初始化密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        // 2. 更新新用户的创建时间和最后更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 3. 获得当前登录用户的ID
        Long empId = (Long) request.getSession().getAttribute("employee");

        // 4. 更新新用户的创始人和最新更新人的id
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        // 5. 保存新用户到数据库中
        employeeService.save(employee);
        return R.success("添加成功！");
    }
}
