package cn.twt.open.dao;

import cn.twt.open.pojo.Access;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AccessControlDao {
    List<Access> getAccessInfo(String site);
}
