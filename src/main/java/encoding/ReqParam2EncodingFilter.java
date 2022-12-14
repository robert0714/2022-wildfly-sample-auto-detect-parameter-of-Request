package encoding;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class ReqParam2EncodingFilter implements Filter {
	
	private static Logger logger = LoggerFactory.getLogger(ReqParam2EncodingFilter.class);
	@Override
	public void doFilter(final ServletRequest request,final  ServletResponse response,final  FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest httpReq = (HttpServletRequest) request;
		final Set<Entry<String, String[]>> entrySet = httpReq.getParameterMap().entrySet();
		final CharsetDetector detector = new CharsetDetector();
		final EncodingRequestWrapper parameterRequestWrapper = new EncodingRequestWrapper(httpReq);
		boolean useWrappered=false;
		for (final Entry<String, String[]> unit : entrySet) {
			final String key = unit.getKey();
			final String[] value = unit.getValue();
 
			final byte[] data = value[0].getBytes("iso-8859-1");
			detector.setText(data);
			final CharsetMatch cm = detector.detect();
			if (cm != null) {
				final int confidence = cm.getConfidence();
				logger.debug("Encoding: " + cm.getName() + " - Confidence: " + confidence + "%");
				// Here you have the encode name and the confidence
				// In my case if the confidence is > 50 I return the encode, else I return the
				// default value
				if (confidence > 50) {
					final String charset = cm.getName();
					
					logger.debug("prepare convert charset to :" + charset);
					
					final String str = new String(data, charset);
					parameterRequestWrapper.addParameter(key, str);
					useWrappered = true;
				}
			} 
		}
		
		if (useWrappered) {
			chain.doFilter(parameterRequestWrapper, response);
		} else {
			chain.doFilter(request, response);
		}
		
	}
}

class EncodingRequestWrapper extends HttpServletRequestWrapper {
	private Map<String, String[]> params = new HashMap<String, String[]>();

	public EncodingRequestWrapper(HttpServletRequest request) {
		// ???request???????????????????????????????????????????????????????????????????????????????????????????????????????????????new???????????????
		super(request);
		// ?????????????????????????????????Map???????????????request????????????
		this.params.putAll(request.getParameterMap());
	}

	// ????????????????????????
	public EncodingRequestWrapper(HttpServletRequest request, Map<String, Object> extendParams) {
		this(request);
		addAllParameters(extendParams);// ????????????????????????????????????
	}

	@Override
	public String getParameter(String name) {// ??????getParameter?????????????????????????????????map??????
		String[] values = params.get(name);
		if (values == null || values.length == 0) {
			return null;
		}
		return values[0];
	}

	public String[] getParameterValues(String name) {// ??????
		return params.get(name);
	}

	public void addAllParameters(Map<String, Object> otherParams) {// ??????????????????
		for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
			addParameter(entry.getKey(), entry.getValue());
		}
	}

	public void addParameter(String name, Object value) {// ????????????
		if (value != null) {
			if (value instanceof ArrayList) {
				String value1 = String.valueOf(value).substring(1, String.valueOf(value).length());
				value = value1.substring(0, value1.length() - 1);
				params.put(name, new String[] { (String) value });
			} else if (value instanceof String[]) {
				params.put(name, (String[]) value);
			} else if (value instanceof String) {
				params.put(name, new String[] { (String) value });
			} else {
				params.put(name, new String[] { String.valueOf(value) });
			}
		}
	}
}