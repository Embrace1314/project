package top.warmheart.workerunion.server.dao;

import java.math.BigInteger;
import java.util.List;

import top.warmheart.workerunion.server.dto.ResourceImplementItemDto;
import top.warmheart.workerunion.server.model.ResourceImplementItem;

public interface ResourceImplementItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_implement_item
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(BigInteger id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_implement_item
     *
     * @mbg.generated
     */
    int insert(ResourceImplementItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_implement_item
     *
     * @mbg.generated
     */
    ResourceImplementItem selectByPrimaryKey(BigInteger id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_implement_item
     *
     * @mbg.generated
     */
    List<ResourceImplementItem> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_implement_item
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ResourceImplementItem record);

	List<ResourceImplementItemDto> selectListByProjectId(BigInteger projectId);

	ResourceImplementItem selectByPrimaryKey4Update(BigInteger resourceImplementItemId);

	List<ResourceImplementItem> selectListByFundPlanItemId(BigInteger fundPlanItemId);

	List<ResourceImplementItem> selectListSimpleByProjectId(BigInteger projectId);
}