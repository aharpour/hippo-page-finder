/*
 *    Copyright 2013 Ebrahim Aharpour
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */
package net.sourceforge.hippopagefinder.utils;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.core.component.HstRequest;

/**
 * @author Ebrahim Aharpour
 *
 */
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
