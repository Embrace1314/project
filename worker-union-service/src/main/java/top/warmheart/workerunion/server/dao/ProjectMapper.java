package top.warmheart.workerunion.server.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import top.warmheart.server.util.dto.Page;
import top.warmheart.workerunion.server.model.Project;

public interface ProjectMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table project
	 *
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(BigInteger id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table project
	 *
	 * @mbg.generated
	 */
	int insert(Project record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table project
	 *
	 * @mbg.generated
	 */
	Project selectByPrimaryKey(BigInteger id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table project
	 *
	 * @mbg.generated
	 */
	List<Project> selectAll();

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table project
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKey(Project record);

	/**
	 * 根据项目编号获取项目信息
	 * 
	 * @param companyId
	 *            企业ID
	 * @param num
	 *            项目编号
	 * @return
	 */
	Project selectByNum(@Param("companyId") BigInteger companyId, @Param("num") String num);

	Integer countAll(BigInteger companyId);

	Integer countAllEx(@Param("staffId") BigInteger staffId, @Param("companyId") BigInteger companyId);

	List<Project> pageAll(@Param("companyId") BigInteger companyId, @Param("page") Page<?> page);

	List<Project> pageAllEx(@Param("staffId") BigInteger staffId, @Param("companyId") BigInteger companyId,
			@Param("page") Page<?> page);

	Integer countByStatusAndYear(@Param("companyId") BigInteger companyId, @Param("fileStatus") String fileStatus,
			@Param("yearMin") Date yearMin, @Param("yearMax") Date yearMax);

	Integer countByStatusAndYearEx(@Param("staffId") BigInteger staffId, @Param("companyId") BigInteger companyId,
			@Param("fileStatus") String fileStatus, @Param("yearMin") Date yearMin, @Param("yearMax") Date yearMax);

	List<Project> pageByStatusAndYear(@Param("companyId") BigInteger companyId, @Param("fileStatus") String fileStatus,
			@Param("yearMin") Date yearMin, @Param("yearMax") Date yearMax, @Param("page") Page<?> page);

	List<Project> pageByStatusAndYearEx(@Param("staffId") BigInteger staffId, @Param("companyId") BigInteger companyId,
			@Param("fileStatus") String fileStatus, @Param("yearMin") Date yearMin, @Param("yearMax") Date yearMax,
			@Param("page") Page<?> page);

	Integer countByStatus(@Param("companyId") BigInteger companyId, @Param("fileStatus") String fileStatus);

	Integer countByStatusEx(@Param("staffId") BigInteger staffId, @Param("companyId") BigInteger companyId,
			@Param("fileStatus") String fileStatus);

	List<Project> pageByStatus(@Param("companyId") BigInteger companyId, @Param("fileStatus") String fileStatus,
			@Param("page") Page<?> page);

	List<Project> pageByStatusEx(@Param("staffId") BigInteger staffId, @Param("companyId") BigInteger companyId,
			@Param("fileStatus") String fileStatus, @Param("page") Page<?> page);

	Integer countByYear(@Param("companyId") BigInteger companyId, @Param("yearMin") Date yearMin,
			@Param("yearMax") Date yearMax);

	Integer countByYearEx(@Param("staffId") BigInteger staffId, @Param("companyId") BigInteger companyId,
			@Param("yearMin") Date yearMin, @Param("yearMax") Date yearMax);

	List<Project> pageByYear(@Param("companyId") BigInteger companyId, @Param("yearMin") Date yearMin,
			@Param("yearMax") Date yearMax, @Param("page") Page<?> page);

	List<Project> pageByYearEx(@Param("staffId") BigInteger staffId, @Param("companyId") BigInteger companyId,
			@Param("yearMin") Date yearMin, @Param("yearMax") Date yearMax, @Param("page") Page<?> page);

	Project selectByPrimaryKey4Update(BigInteger id);

	List<Project> selectListSimpleByCompanyId(BigInteger companyId);

	List<Project> selectListSimpleByFileStatus(@Param("companyId")BigInteger companyId,@Param("fileStatus") String fileStatus);
}