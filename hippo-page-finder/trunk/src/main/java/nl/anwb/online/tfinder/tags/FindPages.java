package nl.anwb.online.tfinder.tags;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import nl.anwb.online.tfinder.PageFinder;
import nl.anwb.online.tfinder.utils.FreemarkerUtils;
import nl.anwb.online.tfinder.utils.HstUtils;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.HstRequestUtils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FindPages extends TagSupport {

	private static final long serialVersionUID = 1L;

	private HstRequest hstRequest;
	private HstResponse hstResponse;

	private final static Template TEMPLATE = FreemarkerUtils.getTemplate("/nl/anwb/online/tfinder/tags/find-pages.ftl",
			FindPages.class);
	private final static PageFinder PAGE_FINDER = new PageFinder();

	public FindPages() {
		reset();
	}

	private void reset() {
		hstRequest = null;
		hstResponse = null;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			HstRequest request = getHstRequest();
			HstRequestContext requestContext = request.getRequestContext();
			if (requestContext.isPreview()) {
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("contextPath", HstUtils.getContextPath(request));
				model.put("mountPath", requestContext.getResolvedMount().getResolvedMountPath());
				model.put("items", PAGE_FINDER.find(request));
				TEMPLATE.process(model, out);
			}
		} catch (IOException e) {
			throw new JspException("An IOException was thrown while writing to out.", e);
		} catch (TemplateException e) {
			throw new JspException("An TemplateException was thrown while trying to process the freemarker template.",
					e);
		} finally {
			reset();
		}
		return EVAL_PAGE;
	}

	private HstRequest getHstRequest() {
		if (hstRequest == null) {
			HttpServletRequest servletRequest = (HttpServletRequest) pageContext.getRequest();
			hstRequest = HstRequestUtils.getHstRequest(servletRequest);
		}
		return hstRequest;
	}

	@SuppressWarnings("unused")
	private HstResponse getHstResponse() {
		if (hstResponse == null) {
			HttpServletResponse servletResponse = (HttpServletResponse) pageContext.getResponse();
			hstResponse = HstRequestUtils.getHstResponse(getHstRequest(), servletResponse);
		}
		return hstResponse;
	}

}
