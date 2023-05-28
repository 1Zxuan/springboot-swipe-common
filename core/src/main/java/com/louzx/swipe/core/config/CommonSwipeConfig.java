package com.louzx.swipe.core.config;

import com.louzx.swipe.core.jdbc.SqlBuilder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author louzx
 * @date 2021/10/26
 */
public class CommonSwipeConfig {

    private static String taskGroup;

    public static String getTaskGroup() {
        return taskGroup;
    }

    @Value("${task.group:}")
    public void setTaskGroup(String taskGroup) {
        CommonSwipeConfig.taskGroup = taskGroup;
    }

    @Getter
    private static String[] loadUsers;

    @Value("${load.users:}")
    public void setLoadUsers(String[] loadUsers) {
        CommonSwipeConfig.loadUsers = loadUsers;
    }

    @Getter
    private static String[] unLoadUsers;

    @Value("${unload.users:}")
    public void setUnLoadUsers(String[] unLoadUsers) {
        CommonSwipeConfig.unLoadUsers = unLoadUsers;
    }

    public static final Set<String> LOAD_USERS = new HashSet<>();

    public static String conditionSql() {
        StringBuilder sql = new StringBuilder();
        String[] loadUsers = null;
        String[] users = CommonSwipeConfig.getLoadUsers();
        if ((null != users && users.length > 0) || !LOAD_USERS.isEmpty()) {
            loadUsers = new String[(null != users ? users.length : 0) + LOAD_USERS.size()];
            int index = 0;
            if (null != users) {
                for (String user : users) {
                    loadUsers[index++] = user;
                }
            }
            for (String loadUser : LOAD_USERS) {
                loadUsers[index++] = loadUser;
            }
            sql.append(" and username in ( ");
        } else if (null != CommonSwipeConfig.getUnLoadUsers() && CommonSwipeConfig.getUnLoadUsers().length > 0) {
            loadUsers = CommonSwipeConfig.getUnLoadUsers();
            sql.append(" and username not in ( ");
        }
        if (null != loadUsers && loadUsers.length > 0) {
            for (String loadUser : loadUsers) {
                sql.append("'").append(loadUser).append("', ");
            }
            if (sql.indexOf(",") > 0) {
                sql.deleteCharAt(sql.lastIndexOf(","));
            }
            sql.append(")");
        }
        return sql.toString();
    }

    public static SqlBuilder conditionSqlBuilder (SqlBuilder sqlBuilder) {
        if (null != sqlBuilder) {
            if (null != CommonSwipeConfig.getLoadUsers() && CommonSwipeConfig.getLoadUsers().length > 0) {
                sqlBuilder.in("username", Arrays.asList(CommonSwipeConfig.getLoadUsers()));
            } else if (null != CommonSwipeConfig.getUnLoadUsers() && CommonSwipeConfig.getUnLoadUsers().length > 0) {
                sqlBuilder.notIn("username", Arrays.asList(CommonSwipeConfig.getUnLoadUsers()));
            }
        }
        return sqlBuilder;
    }


    public static String getTaskGroupCondition() {
        return StringUtils.isBlank(CommonSwipeConfig.getTaskGroup()) ? "" : String.format(" and task_group = '%s' ", CommonSwipeConfig.getTaskGroup());
    }
}
