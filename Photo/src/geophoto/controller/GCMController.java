
package geophoto.controller;

import geophoto.cmd.TestCommand;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

@Controller("gCMController")
public class GCMController {

	final static String API_KEY = "AIzaSyA4OWdNsmGCkoE4qEG3TugRpf7xumMRWAA";
	
	@RequestMapping("/sendMessage")
	public String sendMessage(ModelMap modelMap, HttpServletRequest req, HttpServletResponse res, TestCommand cmd) throws Exception {

		String regId ="";
		String message = cmd.getTitle();

		List<String> regIdList = new ArrayList<String>();
		regIdList.add(regId);
		
		Sender sender = new Sender(API_KEY);
		Message msg = new Message.Builder()
		.addData("title", message)
		.build();
		
//		MulticastResult result = sender.send(msg, regIdList, 5);
		Result result = sender.send(msg, regId, 5);

		modelMap.addAttribute("data", result.getErrorCodeName());		//푸시 결과
		

		if (result.getMessageId() != null) {
			String canonicalRegId = result.getCanonicalRegistrationId();
			if (canonicalRegId != null) {
				// same device has more than on registration ID: update database
			}
		} else {
			String error = result.getErrorCodeName();
			if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
				// application has been removed from device - unregister database
			}
		}

		
		return "/result";								
	}
	
	
}
