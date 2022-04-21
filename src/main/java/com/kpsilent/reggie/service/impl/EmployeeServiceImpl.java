package com.kpsilent.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kpsilent.reggie.entity.Employee;
import com.kpsilent.reggie.mapper.EmployeeMapper;
import com.kpsilent.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
