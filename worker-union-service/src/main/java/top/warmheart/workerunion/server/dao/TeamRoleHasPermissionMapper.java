package top.warmheart.workerunion.server.dao;

import java.math.BigInteger;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import top.warmheart.workerunion.server.model.TeamRoleHasPermission;

public interface TeamRoleHasPermissionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table team_role_has_permission
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(@Param("teamRoleId") BigInteger teamRoleId, @Param("permissionId") BigInteger permissionId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table team_role_has_permission
     *
     * @mbg.generated
     */
    int insert(TeamRoleHasPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table team_role_has_permission
     *
     * @mbg.generated
     */
    List<TeamRoleHasPermission> selectAll();
}