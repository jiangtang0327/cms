package com.it.cms.bean.extend;

import com.it.cms.bean.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryExtend extends Category {
    List<Category> cates;
}
