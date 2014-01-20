package geophoto.dao;

import java.util.List;
import java.util.Map;

import geophoto.cmd.ArticleCommand;
import geophoto.cmd.TestCommand;

import javax.annotation.Resource;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;


@Repository(value = "articleDao")
public class ArticleDao extends SqlMapClientDaoSupport {

	@Resource(name = "sqlMapClient")
	public void setEstateSqlMapClient(SqlMapClient sqlMapClient) {
		super.setSqlMapClient(sqlMapClient);
	}
	
	public void insertArticle(ArticleCommand cmd) throws Exception {		
		getSqlMapClientTemplate().insert("article.insertArticle", cmd);		
	}
	
	public List<ArticleCommand> getArticleList(Map<String, Integer> map) throws Exception {		
		return (List<ArticleCommand>)getSqlMapClientTemplate().queryForList("article.getArticleList", map);		
	}
	
	
}
