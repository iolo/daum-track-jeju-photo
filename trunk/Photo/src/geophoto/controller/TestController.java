
package geophoto.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.bcel.internal.generic.NEW;

import geophoto.cmd.TestCommand;
import geophoto.dao.TestDao;

@Controller("testController")
public class TestController {

	private static Gson gson = new Gson();
	
	@Resource
	private TestDao testDao;
	
	@RequestMapping("/test")
	public String test(ModelMap modelMap, HttpServletRequest req, HttpServletResponse res, TestCommand cmd) throws Exception {

//		testDao.insertTest(cmd);		
		modelMap.addAttribute("data", cmd.getTitle()+","+cmd.getArticleid());

		return "/result";								
	}
	
	@RequestMapping("/read")
	public String read(ModelMap modelMap, HttpServletRequest req, HttpServletResponse res) throws Exception {

		List<TestCommand> sampleList = testDao.readSample();		
		modelMap.addAttribute("sampleList", sampleList);
		
		String jsonData = gson.toJson(sampleList);
		modelMap.addAttribute("jsonData", jsonData);
		
		String result = "[{\"articleid\":0,\"title\":\"nike\"},{\"articleid\":10,\"title\":\"banana\"},{\"articleid\":10,\"title\":\"apple\"}]";
		List<TestCommand> sampleList2 = gson.fromJson(result, new TypeToken<List<TestCommand>>(){}.getType());
		
		for (TestCommand cmd:sampleList2) {
			System.out.println(cmd.getTitle());
		}
		
		return "/readSample";								
	}
	
}
