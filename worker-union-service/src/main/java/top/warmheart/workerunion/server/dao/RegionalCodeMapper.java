package top.warmheart.workerunion.server.dao;

import java.util.List;

import top.warmheart.workerunion.server.model.RegionalCode;

public interface RegionalCodeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table regional_code
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String code);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table regional_code
     *
     * @mbg.generated
     */
    int insert(RegionalCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table regional_code
     *
     * @mbg.generated
     */
    RegionalCode selectByPrimaryKey(String code);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table regional_code
     *
     * @mbg.generated
     */
    List<RegionalCode> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table regional_code
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(RegionalCode record);

	RegionalCode selectByCode(String code);
}