package geophoto.dao;

import geophoto.cmd.ArticleCommand;

import java.util.HashMap;
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

	public ArticleCommand getArticle(String postId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("postId", postId);

		return (ArticleCommand) getSqlMapClientTemplate().queryForObject("article.getArticle", map);
	}

	public List<ArticleCommand> getArticleList(Map<String, Integer> map, String orderby) throws Exception {
		return (List<ArticleCommand>) getSqlMapClientTemplate().queryForList("article.getArticleList_".concat(orderby), map);
	}

	public void updateLikecnt(Map<String, String> map) throws Exception {
		getSqlMapClientTemplate().update("article.updateLikeCount", map);
	}

	// getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
	//
	// public Object doInSqlMapClient(SqlMapExecutor executor) throws
	// SQLException {
	//
	// executor.startBatch();
	//
	// Iterator<BookInfoBean> it = list.iterator();
	// while (it.hasNext()) {
	// BookInfoBean bean = it.next();
	// executor.update("kr.lancerme.insertBookInfo", bean);
	// }
	//
	// executor.executeBatch();
	// return null;
	// }
	// });

}
