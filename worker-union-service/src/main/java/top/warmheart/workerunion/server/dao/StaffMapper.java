package top.warmheart.workerunion.server.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import top.warmheart.server.util.dto.Page;
import top.warmheart.workerunion.server.dto.StaffDto;
import top.warmheart.workerunion.server.model.Staff;

public interface StaffMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table staff
	 *
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(BigInteger id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table staff
	 *
	 * @mbg.generated
	 */
	int insert(Staff record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table staff
	 *
	 * @mbg.generated
	 */
	Staff selectByPrimaryKey(BigInteger id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table staff
	 *
	 * @mbg.generated
	 */
	List<Staff> selectAll();

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table staff
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKey(Staff record);

	Staff selectByJobNo(@Param("companyId") BigInteger companyId, @Param("jobNo") String jobNo);

	int deleteById(BigInteger id);

	StaffDto selectDetailById(@Param("id")BigInteger id, @Param("collapseStatusGoing") String collapseStatusGoing);

	Integer countByFuzzy(@Param("staffDto") StaffDto staffDto, @Param("collapseStatusGoing") String collapseStatusGoing);

	List<StaffDto> pageByFuzzy(@Param("staffDto") StaffDto staffDto,
			@Param("collapseStatusGoing") String collapseStatusGoing, @Param("page") Page<?> page);
}