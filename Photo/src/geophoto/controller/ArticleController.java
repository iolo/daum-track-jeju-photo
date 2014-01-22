package geophoto.controller;

import geophoto.cmd.ArticleCommand;
import geophoto.dao.ArticleDao;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
		// facebook 에 등록
		FacebookClient client = new DefaultFacebookClient(MY_ACCESS_TOKEN);
		FacebookType publishPhotoResponse = client.publish(PAGE_ID + "/photos", FacebookType.class,
				BinaryAttachment.with(cmd.getAttach().getName(), cmd.getAttach().getInputStream()), Parameter.with("message", cmd.getContent()));

		cmd.setAvgcolor(getAverageColor(cmd));

		String id = publishPhotoResponse.getId();
		cmd.setId(id);

		articleDao.insertArticle(cmd);

		modelMap.addAttribute("data", id);
		return "/result";
	}

	@RequestMapping("/getArticleList/{orderby}")
	public String getArticleListOrderBy(ModelMap modelMap, HttpServletRequest req, HttpServletResponse res, @PathVariable String orderby, ArticleCommand cmd, @RequestParam int limit)
			throws Exception {
		
		System.out.println(cmd.toString());
		List<ArticleCommand> articleList = null; 
		Map<String, Integer> paramMap = new HashMap<String, Integer>();

		if(orderby.equals("recent")){
			paramMap.put("from", cmd.getNo());
			paramMap.put("limit", limit);
			
		}else if (orderby.equals("around")) {
			int RANGE = 20000;
			
			paramMap.put("minLat", cmd.getLat() - RANGE);
			paramMap.put("maxLat", cmd.getLat() + RANGE);
			paramMap.put("minLng", cmd.getLng() - RANGE);
			paramMap.put("maxLng", cmd.getLng() + RANGE);
		}

		articleList = articleDao.getArticleList(paramMap, orderby);
		modelMap.addAttribute("data", gson.toJson(articleList));
		return "/result";
	}

	private int getAverageColor(ArticleCommand cmd) throws IOException {
		BufferedImage read = ImageIO.read(cmd.getAttach().getInputStream());
		BufferedImage newImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

		Graphics g = newImage.createGraphics();
		g.drawImage(read, 0, 0, 10, 10, null);
		g.dispose();

		int rgb, rsum = 0, gsum = 0, bsum = 0;

		for (int x = 0; x < 10; x++)
			for (int y = 0; y < 10; y++) {
				rgb = newImage.getRGB(x, y);
				rsum += (rgb >> 16) & 0xff;
				gsum += (rgb >> 8) & 0xff;
				bsum += rgb & 0xff;
			}

		rsum /= 100;
		gsum /= 100;
		bsum /= 100;
		rgb = rsum << 16 | gsum << 8 | bsum;

		return rsum << 16 | gsum << 8 | bsum;
	}

}
