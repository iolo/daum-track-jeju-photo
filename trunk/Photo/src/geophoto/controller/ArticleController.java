package geophoto.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import geophoto.cmd.ArticleCommand;
import geophoto.dao.ArticleDao;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

@Controller("articleController")
public class ArticleController {

	final static String MY_APP_ID = "1433071126925017";
	final static String MY_APP_SECRET = "1d35095948180a07dc68893f0492017d";
	final static String PAGE_ID = "638274506208023";
	final static String MY_ACCESS_TOKEN = "CAAUXXt91TtkBAFbfWLV8mhjAZBtpX1FpGfe02uYnZCLmz5CqahowJ5uhG1uEBpHkZAZBPXgq9kgYBy5x9NnrvOgJfQfJisZBdEy7GgF47gkjaMJozN5fP14U0xDnoNzCdAn9YwvaNWPU4RrLMQ9OKUnobKNAO42cpanL3p1ZCqO5ZAHEFz38ZB2O";

	private static Gson gson = new Gson();

	@Resource
	private ArticleDao articleDao;

	// http://localhost:8080/writeArticle.photo?content=test1&lat=351234&lng=1281234
	@RequestMapping("/writeArticle")
	public String writeArticle(ModelMap modelMap, HttpServletRequest req, HttpServletResponse res, ArticleCommand cmd) throws Exception {

		System.out.println(cmd.toString());
//		System.out.println(cmd.getAttach().toString());

		// facebook 에 등록
		FacebookClient client = new DefaultFacebookClient(MY_ACCESS_TOKEN);
		FacebookType publishPhotoResponse = client.publish(PAGE_ID + "/photos", FacebookType.class,
				BinaryAttachment.with("cat.png", getClass().getResourceAsStream("/128.png")), Parameter.with("message", cmd.getContent()));

		String id = publishPhotoResponse.getId();
		cmd.setId(id);

		articleDao.insertArticle(cmd);

		modelMap.addAttribute("data", id);
		return "/result";
	}

	@RequestMapping("/getArticleList")
	public String getArticleList(ModelMap modelMap, HttpServletRequest req, HttpServletResponse res, ArticleCommand cmd) throws Exception {

		int RANGE = 20000;

		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("minLat", cmd.getLat() - RANGE);
		paramMap.put("maxLat", cmd.getLat() + RANGE);
		paramMap.put("minLng", cmd.getLng() - RANGE);
		paramMap.put("maxLng", cmd.getLng() + RANGE);

		List<ArticleCommand> articleList = articleDao.getArticleList(paramMap);
		modelMap.addAttribute("data", gson.toJson(articleList));
		return "/result";
	}

}
