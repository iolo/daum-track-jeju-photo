package geophoto.filter;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.ArrayUtils;


public class RequestParamWrapper extends HttpServletRequestWrapper {

	private Hashtable<String, String[]> parameters = new Hashtable<String, String[]>();

	@SuppressWarnings("unchecked")
	public RequestParamWrapper(HttpServletRequest request, String[] removeArray) {
		super(request);
		Enumeration<String> parameterNames = super.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameterName = parameterNames.nextElement();
			String[] parameterValues = super.getParameterValues(parameterName);

			for (int i = 0; i < removeArray.length; i++) {
				// parameterName filtering
				if (parameterName.indexOf(removeArray[i]) >= 0) {
					parameterName = "";
					parameterValues = ArrayUtils.EMPTY_STRING_ARRAY;
				}
				// parameterValue filtering
				else if ("GET".equalsIgnoreCase(request.getMethod())) {
					for (int j = 0; j < parameterValues.length; j++) {
						parameterValues[j] = parameterValues[j].indexOf(removeArray[i]) >= 0 ? "" : parameterValues[j];
					}
				}
			}
			parameters.put(parameterName, parameterValues);
		}
	}

	@Override
	public String getParameter(String key) {
		return parameters.containsKey(key) ? ((String[]) parameters.get(key))[0] : null;
	}

	@Override
	public String[] getParameterValues(String key) {
		return parameters.containsKey(key) ? (String[]) parameters.get(key) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map getParameterMap() {
		return parameters;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration getParameterNames() {
		return parameters.keys();
	}

}
