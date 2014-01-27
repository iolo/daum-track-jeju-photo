package geophoto.controller;

import geophoto.cmd.ArticleCommand;
import geophoto.dao.ArticleDao;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.Post;

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
	public String writeArticle(ModelMap modelMap, HttpServletRequest req, HttpServletResponse res, ArticleCommand cmd, String accesstoken) throws Exception {
		// facebook 에 등록
		cmd.setContent(URLDecoder.decode(cmd.getContent(),"UTF-8"));
		FacebookClient client = new DefaultFacebookClient(accesstoken);
		FacebookType publishPhotoResponse = client.publish(PAGE_ID + "/photos", FacebookType.class,
				BinaryAttachment.with(cmd.getAttach().getName(), cmd.getAttach().getInputStream()), Parameter.with("message", cmd.getContent()));

		cmd.setAvgcolor(getAverageColor(cmd));

		String id = publishPhotoResponse.getId();
		cmd.setId(id);

		articleDao.insertArticle(cmd);

		modelMap.addAttribute("data", id);
		return "/result";
	}

	//01-27 14:12:49.861: W/RestTemplate(4973): GET request for "http://113.198.161.96:8080/getArticle?postId=567405800012957" resulted in 404 (Not Found); invoking error handler
	@RequestMapping("/getArticle")
	public String getArticle(ModelMap modelMap, HttpServletRequest req, HttpServletResponse res, @RequestParam String postId) throws Exception {
		modelMap.addAttribute("data", gson.toJson(articleDao.getArticle(postId)));
		return "/result";
	}

	@RequestMapping("/getArticleList/{orderby}")
	public String getArticleListOrderBy(ModelMap modelMap, HttpServletRequest req, HttpServletResponse res, @PathVariable String orderby, ArticleCommand cmd,
			@RequestParam int limit) throws Exception {

		System.out.println(cmd.toString());
		Map<String, Integer> paramMap = new HashMap<String, Integer>();

		if (orderby.equals("recent")) {
			paramMap.put("from", cmd.getNo());
			paramMap.put("limit", limit);

		} else if (orderby.equals("around")) {
			int RANGE = 2000000;

			paramMap.put("minLat", cmd.getLat() - RANGE);
			paramMap.put("maxLat", cmd.getLat() + RANGE);
			paramMap.put("minLng", cmd.getLng() - RANGE);
			paramMap.put("maxLng", cmd.getLng() + RANGE);
		}

		final List<ArticleCommand> articleList = articleDao.getArticleList(paramMap, orderby);

		likeBatchProcessor.addPostIds(articleList);

		modelMap.addAttribute("data", gson.toJson(articleList));
		return "/result";
	}

	private LikeBatchProcessor likeBatchProcessor = new LikeBatchProcessor(MY_ACCESS_TOKEN);

	class LikeBatchProcessor {
		private Timer timer;
		private HashMap<String, Integer> postIdsCache;
		private HashMap<String, Integer> postIds;
		private int schedulePeriod = 10000; // 10 Seconds
		private String accessToken;

		public LikeBatchProcessor(String accessToken) {
			this.accessToken = accessToken;
			timer = new Timer();
			postIds = new HashMap<String, Integer>();
			postIdsCache = new HashMap<String, Integer>();
			startBatch();
		}

		public void addPostIds(List<ArticleCommand> articles) {
			for (ArticleCommand cmd : articles)
				addPostId(cmd.getId());
		}

		public void addPostId(String postId) {
			synchronized (postIds) {
				postIds.put(postId, 0);
				postIdsCache.put(postId, 0);
			}
		}

		public void clear() {
			postIds.clear();
		}

		public void clearCache() {
			postIdsCache.clear();
		}

		public void startBatch() {
			timer.schedule(new TimerTask() {
				FacebookClient client = new DefaultFacebookClient(accessToken);// ?summary=true
				Connection<Post.Likes> likes;

				@Override
				public void run() {
					synchronized (postIds) {
						if (postIds.size() == 0) return;

						System.err.println("Like Batch Task is Running");
						List<String> keys = new ArrayList<String>(postIds.keySet());

						for (String postId : keys)
							resolveRedundancy(postId, fecthLikeCount(postId));
						System.err.println("Like Batch Task : added postid count : " + postIds.size());
						try {
							updateDb(postIds);
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.err.println("Like Batch Task Complete : updated " + postIds.size() + "rows");
						clear();
					}
				}

				private void resolveRedundancy(String postId, Integer count) {
					if (count != 0 && postIdsCache.get(postId) != count) {
						postIds.put(postId, count);
						postIdsCache.put(postId, count);
					} else postIds.remove(postId);
				}

				private Integer fecthLikeCount(String postId) {
					likes = client.fetchConnection(postId + "/likes", Post.Likes.class, Parameter.with("summary", true));
					if (likes.getData() == null) return 0;
					return likes.getData().size();
				}

				private void updateDb(HashMap<String, Integer> postIds) throws Exception {
					Map<String, String> paramMap = new HashMap<String, String>();
					for (String postId : postIds.keySet()) {
						paramMap.put("id", postId);
						paramMap.put("likecnt", postIds.get(postId).toString());
						articleDao.updateLikecnt(paramMap);
					}
				}
			}, 0, schedulePeriod);
		}
	}

	private int getAverageColor(ArticleCommand cmd) throws IOException {
		BufferedImage read = ImageIO.read(cmd.getAttach().getInputStream());
		BufferedImage newImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);

		Graphics g = newImage.createGraphics();
		g.drawImage(read, 0, 0, 16, 16, null);
		g.dispose();

		int rgb, rsum = 0, gsum = 0, bsum = 0;

		for (int x = 0; x < 16; x++)
			for (int y = 0; y < 16; y++) {
				rgb = newImage.getRGB(x, y);
				rsum += (rgb >> 16) & 0xff;
				gsum += (rgb >> 8) & 0xff;
				bsum += rgb & 0xff;
			}

		rsum /= 256;
		gsum /= 256;
		bsum /= 256;
		rgb = rsum << 16 | gsum << 8 | bsum;

		return rsum << 16 | gsum << 8 | bsum;
	}

}
