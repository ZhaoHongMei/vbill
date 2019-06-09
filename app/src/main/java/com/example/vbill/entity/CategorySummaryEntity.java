package com.example.vbill.entity;

import com.example.vbill.bean.Category;

import java.util.List;

public class CategorySummaryEntity {
    /**
     * 此实体类是为了解析“账单种类”，在创建新账单选择种类
     *
     * 字段要和后台给的字段是一样的
     *
     * 写出所有的字段
     *
     * 并进行set（）与get（）的方法，以便调用与显示出数据
     * */

    private List<Category> in;
    private List<Category> out;

    public List<Category> getIn() {
        return in;
    }

    public void setIn(List<Category> in) {
        this.in = in;
    }

    public List<Category> getOut() {
        return out;
    }

    public void setOut(List<Category> out) {
        this.out = out;
    }
}
