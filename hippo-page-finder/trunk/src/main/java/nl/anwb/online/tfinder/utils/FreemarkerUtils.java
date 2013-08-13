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
 *   Partially sponsored by Smile B.V
 */
package nl.anwb.online.tfinder.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Ebrahim Aharpour
 * 
 */
public class FreemarkerUtils {

	private FreemarkerUtils() {
	}

	public static Template getTemplate(String path, Class<?> classLoaderOfClass) {
		try {
			Template result;
			Configuration configuration = new Configuration();
			ClassTemplateLoader templateLoader = new ClassTemplateLoader(classLoaderOfClass, "/");
			configuration.setTemplateLoader(templateLoader);
			result = configuration.getTemplate(path);
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String renderTemplate(String templatePath, Map<String, Object> model, Class<?> classLoaderOfClass) throws TemplateException,
			IOException {
		if (StringUtils.isBlank(templatePath) || model == null) {
			throw new IllegalArgumentException("both templatePath and model are required.");
		}
		Template tempalte = FreemarkerUtils.getTemplate(templatePath, classLoaderOfClass);
		StringWriter stringWriter = new StringWriter();
		tempalte.process(model, stringWriter);
		return stringWriter.toString();
	}
}