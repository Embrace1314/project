package top.warmheart.workerunion.server.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import top.warmheart.workerunion.server.model.TeamRole;

public interface TeamRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table team_role
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(BigInteger id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table team_role
     *
     * @mbg.generated
     */
    int insert(TeamRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table team_role
     *
     * @mbg.generated
     */
    TeamRole selectByPrimaryKey(BigInteger id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table team_role
     *
     * @mbg.generated
     */
    List<TeamRole> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table team_role
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(TeamRole record);

	TeamRole selectByCompanyType(@Param("companyId")BigInteger companyId,@Param("type") String type);
}