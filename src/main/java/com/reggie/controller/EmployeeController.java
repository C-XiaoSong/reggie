package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.pojo.Employee;
import com.reggie.service.EmployeeService;
import com.reggie.utis.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/*
 * @author：陈晓松
 * @CLASS_NAME：EmployeeController
 * @date：2023/4/18 19:09
 * @注释：员工管理
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 1、将页面提交的password进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 3、Employee实体类中的username属性，比较的值是employee对象中的username属性值
        // 它用于查询Employee表中username等于指定employee对象的username属性值
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        // 4、没有查到返回失败提示
        if (emp == null) {
            return R.error("账号不正确");
        }
        // 5、如果密码不对返回失败提示
        if (!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        // 6、查看员工状态，如果禁用返回失败提示 0：表示禁用
        if (emp.getStatus() == 0){
            return R.error("用户已被禁用");
        }
        // 6、登录成功，将员工id存到session中并返回结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工的信息：{}",employee.toString());
        // 设置初始密码123456，进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        // 创建时间
        // employee.setCreateTime(LocalDateTime.now());
        // 更新时间
        // employee.setUpdateTime(LocalDateTime.now());
        // 创建人
        // Long empId = (Long) request.getSession().getAttribute("employee");
        // employee.setCreateUser(empId);
        // 更新人时间
        // employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }
    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);
        // 分页构造器
        Page pageInfo = new Page(page,pageSize);
        // 条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        // 添加过滤条件
        queryWrapper.like(StringUtils.hasLength(name),Employee::getName,name);
        // 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        // 执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    /**
     * 根据ID修改员工信息
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        // System.out.println(employee.toString());
        // 获取当前修改时间
        // employee.setUpdateTime(LocalDateTime.now());
        // 获取修改人
        // Long empId = (Long) request.getSession().getAttribute("employee");
        // employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功 ");
    }
    /**
     * ··根据ID查询员工信息··
     * @GetMapping("/{id}")用于将HTTP GET请求映射到指定的控制器方法上
     * 其中{id}是一个占位符，表示该方法可以接受一个名为id的路径参数
     * 具体来说，当用户向服务器发送一个GET请求，请求路径为"/{id}"时，该请求会被Spring MVC框架映射到使用@GetMapping注解标记的控制器方法上
     * 并将请求路径中的{id}占位符的值绑定到方法的id参数上，然后执行该方法
     * 当用户向服务器发送一个GET请求，请求路径为"/123"时，该请求会被映射到方法上，并将123绑定到方法的id参数上，然后执行方法
     * 在方法中，我们可以根据id参数的值查询用户信息，并返回用户信息页面
     * @PathVariable是一个Spring MVC注解，用于将请求路径中的占位符绑定到方法的参数上，以便在控制器方法中访问这些参数的值。
     * 当用户向服务器发送一个GET请求，请求路径为"/users/123"时，Spring MVC会将123绑定到方法的id参数上，然后执行方法
     * @PathVariable 注解只能用于绑定请求路径中的占位符，如果需要绑定请求参数或请求体中的参数，可以使用其他注解，
     * 例如@RequestParam和@RequestBody。
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return R.success(employee);
        }
        return R.error("查询员工信息失败");
    }
    /**
     * 用户退出
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

}
