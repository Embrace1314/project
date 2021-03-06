package top.warmheart.workerunion.server.dao;

import java.math.BigInteger;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import top.warmheart.workerunion.server.model.SubcontractorHasSubcontractorQualification;

public interface SubcontractorHasSubcontractorQualificationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subcontractor_has_subcontractor_qualification
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(@Param("subcontractorId") BigInteger subcontractorId, @Param("subcontractorQualificationId") BigInteger subcontractorQualificationId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subcontractor_has_subcontractor_qualification
     *
     * @mbg.generated
     */
    int insert(SubcontractorHasSubcontractorQualification record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subcontractor_has_subcontractor_qualification
     *
     * @mbg.generated
     */
    List<SubcontractorHasSubcontractorQualification> selectAll();

	int deleteBySubcontractorId(BigInteger id);
}