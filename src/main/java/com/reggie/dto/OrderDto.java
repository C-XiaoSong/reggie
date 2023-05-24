package com.reggie.dto;

import com.reggie.pojo.Orders;
import com.reggie.pojo.OrderDetail;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
