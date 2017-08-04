package top.warmheart.workerunion.server.dao;

import java.math.BigInteger;
import java.util.List;
import top.warmheart.workerunion.server.model.SubcontractorGrade;

public interface SubcontractorGradeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subcontractor_grade
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(BigInteger id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subcontractor_grade
     *
     * @mbg.generated
     */
    int insert(SubcontractorGrade record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subcontractor_grade
     *
     * @mbg.generated
     */
    SubcontractorGrade selectByPrimaryKey(BigInteger id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subcontractor_grade
     *
     * @mbg.generated
     */
    List<SubcontractorGrade> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subcontractor_grade
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(SubcontractorGrade record);

}