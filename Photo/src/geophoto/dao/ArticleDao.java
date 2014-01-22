package geophoto.dao;

import geophoto.cmd.ArticleCommand;

import java.util.List;
import java.util.Map;

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

	// public List<ArticleCommand> getArticleList(Map<String, Integer> map)
	// throws Exception {
	// return (List<ArticleCommand>)
	// getSqlMapClientTemplate().queryForList("article.getArticleList", map);
	// }

	public List<ArticleCommand> getArticleList(Map<String, Integer> map, String orderby) throws Exception {
		return (List<ArticleCommand>) getSqlMapClientTemplate().queryForList("article.getArticleList_".concat(orderby), map);
	}

}
