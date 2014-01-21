package geophoto.controller;

import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;


public class FBTest2 {

//http://www.testically.org/2011/09/27/5-steps-to-automatically-write-on-your-facebook-page-wall-using-the-graph-api-without-a-logged-in-user/
//https://graph.facebook.com/oauth/authorize?client_id=1433071126925017&scope=offline_access,publish_stream,manage_pages&redirect_uri=http://localhost:8080/
//https://graph.facebook.com/oauth/access_token?client_id=1433071126925017&redirect_uri=http://localhost:8080/&client_secret=1d35095948180a07dc68893f0492017d&code=AQDrZKhR2bm7TyT4L0FmEiVN1C6HTlAvv3577t-kXIfWhrHYZjBPjVP7rz8w729vTw9sw54C0qlz7oFQ-SmcC-xyTqGgATv8R-ThAwV5KD6h2mI7JOW3Yxug_6qqIRNP5wuFvWi2UFnBO2oCoVEa5KeIG-ByNZG2Kw4wMzI6jVxq2HbuDZeAnW9CI9l4jzFBhWXwLG54KhwZm_TSw0OphAq5IhmfE1045vwH8xEvbpz-soszhW-j4F29vqp3v_rowkzOf7WNFvvV0y0BcqqD4HNK5AMJ2Pe5Bs7nAj37xiale908HCTRbRed3aSwxAPdKV4#_=_
	
	final static String MY_APP_ID = "1433071126925017";
	final static String MY_APP_SECRET = "1d35095948180a07dc68893f0492017d";
	
	final static String PAGE_ID = "638274506208023";

	final static String MY_ACCESS_TOKEN = "CAAUXXt91TtkBAFbfWLV8mhjAZBtpX1FpGfe02uYnZCLmz5CqahowJ5uhG1uEBpHkZAZBPXgq9kgYBy5x9NnrvOgJfQfJisZBdEy7GgF47gkjaMJozN5fP14U0xDnoNzCdAn9YwvaNWPU4RrLMQ9OKUnobKNAO42cpanL3p1ZCqO5ZAHEFz38ZB2O";
	
	static FacebookClient facebookClientRabbit = new DefaultFacebookClient(MY_ACCESS_TOKEN);		
	
	public static void main(String[] args) {
		FBTest2 test =  new FBTest2();

		test.publishPhoto(facebookClientRabbit, PAGE_ID);
	}
	
	private void publishPhoto(FacebookClient client, String id) {
		FacebookType publishPhotoResponse = client.publish(id+"/photo", FacebookType.class,
				BinaryAttachment.with("cat.png", getClass().getResourceAsStream("/128.png")),
				Parameter.with("message", "test message 3 by jude"));

		System.out.println("Published photo ID: " + publishPhotoResponse.getId());
	}
}
