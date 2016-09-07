package com.pluralsight.security;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import javax.sql.DataSource;
import java.io.Serializable;

/**
 * Created by kmandawe on 06/09/2016.
 */
public class FitnessPermissionEvaluator implements PermissionEvaluator {

    public DataSource getDatasource() {
        return datasource;
    }

    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    private DataSource datasource;

    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        JdbcTemplate template = new JdbcTemplate(datasource);
        Object[] args = {((User)auth.getPrincipal()).getUsername(), targetDomainObject.getClass().getName(), permission.toString()};

        int count = template.queryForObject("select count(*) from permissions p where p.username = ? and p.target = ? and p.permission = ?",
                args, Integer.class);
        if(count == 1){
            return true;
        } else {
            return false;
        }

    }

    public boolean hasPermission(Authentication authentication, Serializable serializable, String type, Object permission) {
        return false;
    }
}
