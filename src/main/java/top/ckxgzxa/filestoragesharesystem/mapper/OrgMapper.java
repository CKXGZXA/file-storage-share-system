package top.ckxgzxa.filestoragesharesystem.mapper;

import org.apache.ibatis.annotations.Select;
import top.ckxgzxa.filestoragesharesystem.domain.po.Organization;

/**
 * @author 赵希奥
 * @date 2023/4/14 13:46
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public interface OrgMapper {
    /**
     * 根据组织id查询组织信息
     * @param orgId
     * @return
     */
    @Select("select * from sys_org where id = #{orgId}")
    Organization selectOrgById(Long orgId);
}
