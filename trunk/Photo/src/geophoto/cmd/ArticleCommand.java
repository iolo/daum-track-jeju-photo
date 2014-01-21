package geophoto.cmd;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

// 글 등록시 argument 를 받아오는 command

public class ArticleCommand {			
			
	private String id;			// facebook 에 등록후 받아온 게시글 ID. client 에서 전송할 필요 없음
	private int likecnt;		// db 에서 읽어오는 값
	private String regdttm;		// db 에서 읽어오는 값
	
	private String fbid;
	
	private int lat;	
	private int lng;			
	
	private String content;	
	
	private CommonsMultipartFile attach;

	
	public int getLikecnt() {
		return likecnt;
	}

	public void setLikecnt(int likecnt) {
		this.likecnt = likecnt;
	}

	public String getRegdttm() {
		return regdttm;
	}

	public void setRegdttm(String regdttm) {
		this.regdttm = regdttm;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFbid() {
		return fbid;
	}

	public void setFbid(String fbid) {
		this.fbid = fbid;
	}

	public int getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public int getLng() {
		return lng;
	}

	public void setLng(int lng) {
		this.lng = lng;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public CommonsMultipartFile getAttach() {
		return attach;
	}

	public void setAttach(CommonsMultipartFile attach) {
		this.attach = attach;
	}

	@Override
	public String toString() {
		return "ArticleCommand [id=" + id + ", likecnt=" + likecnt + ", regdttm=" + regdttm + ", fbid=" + fbid + ", lat=" + lat + ", lng=" + lng + ", content="
				+ content + ", attach=" + attach + "]";
	}
	
	


}
