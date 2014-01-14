package geophoto.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;

import geophoto.cmd.TestCommand;


@Repository(value = "testDao")
public class TestDao extends SqlMapClientDaoSupport {

	@Resource(name = "sqlMapClient")
	public void setEstateSqlMapClient(SqlMapClient sqlMapClient) {
		super.setSqlMapClient(sqlMapClient);
	}
	
	public void insertTest(TestCommand cmd) throws Exception {		
		getSqlMapClientTemplate().insert("test.insertTest", cmd);		
	}
	public List<TestCommand> readSample() throws Exception {		
		return (List<TestCommand>)getSqlMapClientTemplate().queryForList("test.readSample");		
	}
	
}
