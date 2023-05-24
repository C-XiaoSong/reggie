package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.reggie.pojo.AddressBook;
import com.reggie.service.AddressBookService;
import com.reggie.utis.BaseContext;
import com.reggie.utis.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author： 陈晓松
 * @CLASS_NAME： AddressBookController
 * @date： 2023/5/12
 * @注释： TODO
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
    * @Author: 陈晓松
    * @Description: 查询指定用户的全部地址
    * @Params: [addressBook]
    * @Return com.reggie.utis.R<java.util.List<com.reggie.pojo.AddressBook>>
    */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(addressBook.getUserId() != null,AddressBook::getUserId,addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        return R.success(addressBookService.list(queryWrapper));
    }

    /**
    * @Author: 陈晓松
    * @Description: 新增
    * @Params: [addressBook]
    * @Return com.reggie.utis.R<com.reggie.pojo.AddressBook>
    */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        // 设置用户ID、登录成功之后可以用这种方式获取用户ID：BaseContext.getCurrentId()
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
    * @Author: 陈晓松
    * @Description: 设置默认地址
    * @Params: [addressBook]
    * @Return com.reggie.utis.R<com.reggie.pojo.AddressBook>
    */
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        // 把地址的值设为0
        updateWrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(updateWrapper);

        // 把当前地址设为默认地址 1
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
    * @Author: 陈晓松
    * @Description: 根据ID查询地址
    * @Params: [id]
    * @Return com.reggie.utis.R
    */
    @GetMapping("{id}")
    public R getById(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null){
            return R.success(addressBook);
        }else {
            return R.error("没有找到该对象");
        }
    }

    /**
    * @Author: 陈晓松
    * @Description: 查询默认地址
    * @Params: []
    * @Return com.reggie.utis.R<com.reggie.pojo.AddressBook>
    */
    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (addressBook != null){
            return R.success(addressBook);
        }else {
            return R.error("没有找到该对象");
        }
    }
}
