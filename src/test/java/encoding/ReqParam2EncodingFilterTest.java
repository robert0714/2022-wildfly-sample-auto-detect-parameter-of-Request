package encoding;
 
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException; 
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse;
 
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mockito.InjectMocks; 
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner; 
 
@RunWith(MockitoJUnitRunner.class)
public class ReqParam2EncodingFilterTest    {
	
	private static Logger logger = LoggerFactory.getLogger(ReqParam2EncodingFilterTest.class);
	
    @InjectMocks
    private ReqParam2EncodingFilter filter;
	
	@Test
    public void testDoFilter_NoWrappered() throws ServletException, IOException {  
        HttpServletRequest mockReq = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResp = Mockito.mock(HttpServletResponse.class);
        FilterChain mockFilterChain = Mockito.mock(FilterChain.class);
        FilterConfig mockFilterConfig = Mockito.mock(FilterConfig.class);
        
  
        filter.init(mockFilterConfig);
        filter.doFilter(mockReq, mockResp, mockFilterChain);
        filter.destroy();
    } 

	@Test
    public void testDoFilter_Wrappered() throws ServletException, IOException {  
        HttpServletRequest mockReq = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResp = Mockito.mock(HttpServletResponse.class);
        FilterChain mockFilterChain = Mockito.mock(FilterChain.class);
        FilterConfig mockFilterConfig = Mockito.mock(FilterConfig.class); 
        final  Map<String, String[]> map  = new HashMap<String, String[]>();        
		Mockito.when(mockReq.getParameterMap()).thenReturn(map); 
		map.put("test", new String[] { "中文"});
		map.put("test2", new String[] { "中文2"});
  
        filter.init(mockFilterConfig);
        filter.doFilter(mockReq, mockResp, mockFilterChain);
        filter.destroy();
    } 
}