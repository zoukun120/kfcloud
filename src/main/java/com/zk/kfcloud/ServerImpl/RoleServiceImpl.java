package com.zk.kfcloud.ServerImpl;


import java.util.List;
import javax.annotation.Resource;

import com.zk.kfcloud.Dao.RoleMapper;
import com.zk.kfcloud.Entity.web.Role;
import com.zk.kfcloud.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public List<Role> listAllRoles() {
        return this.roleMapper.listAllRoles();
    }

    public void deleteRoleById(int roleId) {
        this.roleMapper.deleteRoleById(roleId);
    }

    public Role getRoleById(int roleId) {
        return this.roleMapper.getRoleById(roleId);
    }

    public boolean insertRole(Role role) {
        if (this.roleMapper.getCountByName(role) > 0) {
            return false;
        }
        this.roleMapper.insertRole(role);
        return true;
    }

    public boolean updateRoleBaseInfo(Role role) {
        if (this.roleMapper.getCountByName(role) > 0) {
            return false;
        }
        this.roleMapper.updateRoleBaseInfo(role);
        return true;
    }

    public void updateRoleRights(Role role) {
        this.roleMapper.updateRoleRights(role);
    }

    public String getRoleByUserId(Integer userId) {
        return this.roleMapper.getRoleByUserId(userId);
    }

    public Integer getRoleIdByRoleName(String roleName) {
        return this.roleMapper.getRoleIdByRoleName(roleName);
    }
}
