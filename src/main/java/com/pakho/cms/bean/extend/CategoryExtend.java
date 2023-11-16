package com.pakho.cms.bean.extend;

import com.pakho.cms.bean.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryExtend extends Category {
    List<Category> cates;
}
