package com.pakho.cms.bean.extend;

import com.pakho.cms.bean.Role;
import com.pakho.cms.bean.User;
import lombok.Data;

@Data
public class UserExtend extends User {
    private Role role;
}