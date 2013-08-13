package nl.anwb.online.tfinder.utils;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.core.component.HstRequest;

public class HstUtils {

	private HstUtils() {
	}

	public static String getContextPath(HstRequest request) {
		String contextPath = request.getRequestContext().getVirtualHost().getVirtualHosts().getDefaultContextPath();
		boolean contextPathInUrl = request.getRequestContext().getVirtualHost().isContextPathInUrl();
		if (StringUtils.isBlank(contextPath)) {
			contextPath = request.getContextPath();
		}
		return contextPathInUrl ? contextPath : "";
	}
}
