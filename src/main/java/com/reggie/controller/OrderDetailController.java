package com.reggie.controller;

import com.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author： 陈晓松
 * @CLASS_NAME： OrderDetailController
 * @date： 2023/5/24
 * @注释： TODO
 */
@Slf4j
@RestController
@RequestMapping
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;
}
