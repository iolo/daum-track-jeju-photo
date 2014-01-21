package geophoto.controller;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller("loginController")
public class LoginController {

	private static Gson gson = new Gson();
	private HashSet<String> reg = new HashSet<String>();

	@ResponseBody
	@RequestMapping("/login")
	public String login(ModelMap modelMap, HttpServletRequest req, HttpServletResponse res, @RequestParam String fbid) {
		System.out.println("login user : " + fbid);
		reg.add(fbid);
		return gson.toJson("ok");
	}

	@ResponseBody
	@RequestMapping(value = "/checkreg", method = RequestMethod.GET)
	public String checkReg(@RequestParam String fbid) {
		boolean contains = reg.contains(fbid);
		System.out.println("login : '" + fbid + "' / result : " + contains);
		return gson.toJson(contains);
	}

}
