package geophoto.velocity;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.ServletUtils;
import org.apache.velocity.tools.view.VelocityView;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;

public class PhotoVelocityToolboxView extends VelocityToolboxView {

	protected Context createVelocityContext(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		VelocityView view = ServletUtils.getVelocityView(getServletContext());
		ViewToolContext velocityContext = view.createContext(request, response);
		velocityContext.putAll(model);
		return velocityContext;
	}
}

