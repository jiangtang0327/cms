package com.it.cms.bean.extend;

import com.it.cms.bean.Role;
import com.it.cms.bean.User;
import lombok.Data;

@Data
public class UserExtend extends User {
    private Role role;
}