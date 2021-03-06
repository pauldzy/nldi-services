package gov.usgs.owi.nldi.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LookupDao extends BaseDao {
	private static final String NS = "lookup.";

	@Autowired
	public LookupDao(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}

	public Map<String, Object> getComid(String objectType, Map<String, Object> parameterMap) {
		return getSqlSession().selectOne(NS + objectType, parameterMap);
	}

	public List<Map<String, Object>> getList(String objectType, Map<String, Object> parameterMap) {
		return getSqlSession().selectList(NS + objectType, parameterMap);
	}

}
