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
		// 将request交给父类，以便于调用对应方法的时候，将其输出，其实父亲类的实现方式和第一种new的方式类似
		super(request);
		// 将参数表，赋予给当前的Map以便于持有request中的参数
		this.params.putAll(request.getParameterMap());
	}

	// 重载一个构造方法
	public EncodingRequestWrapper(HttpServletRequest request, Map<String, Object> extendParams) {
		this(request);
		addAllParameters(extendParams);// 这里将扩展参数写入参数表
	}

	@Override
	public String getParameter(String name) {// 重写getParameter，代表参数从当前类中的map获取
		String[] values = params.get(name);
		if (values == null || values.length == 0) {
			return null;
		}
		return values[0];
	}

	public String[] getParameterValues(String name) {// 同上
		return params.get(name);
	}

	public void addAllParameters(Map<String, Object> otherParams) {// 增加多个参数
		for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
			addParameter(entry.getKey(), entry.getValue());
		}
	}

	public void addParameter(String name, Object value) {// 增加参数
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