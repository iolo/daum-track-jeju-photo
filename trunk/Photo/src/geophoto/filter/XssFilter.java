package geophoto.filter;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XssFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(XssFilter.class);

	private FilterConfig filterConfig;
	private String[] removeArray = null;

	@Override
	public void destroy() {
		filterConfig = null;
		removeArray = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		chain.doFilter(getFilteredRequest(httpRequest), response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		String removeParameter = this.filterConfig.getInitParameter("removeList");
		if (removeParameter != null) {
			StringTokenizer st = new StringTokenizer(removeParameter, ",");
			int tokenCount = st.countTokens();
			removeArray = new String[tokenCount];
			for (int i = 0; i < tokenCount; i++) {
				removeArray[i] = st.nextToken().trim();
			}
		}
		logger.debug("Removes {} filter loaded successfully.", filterConfig.getFilterName());

	}

	private ServletRequest getFilteredRequest(HttpServletRequest httpRequest) {
		String path = httpRequest.getPathInfo();
		if (path != null && path.indexOf("/api/") >= 0) {
			return new HttpServletRequestWrapper(httpRequest);
		}

		return new RequestParamWrapper(httpRequest, this.removeArray);
	}
}
