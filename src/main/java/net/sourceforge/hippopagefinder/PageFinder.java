package net.sourceforge.hippopagefinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jcr.RepositoryException;

import net.sourceforge.hippopagefinder.utils.Constants;

import org.hippoecm.hst.configuration.site.HstSite;
import org.hippoecm.hst.configuration.sitemap.HstSiteMap;
import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryManager;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.jaxrs.services.AbstractResource;

public class PageFinder extends AbstractResource {

	public List<String> find(HstRequest request) {
		HstRequestContext requestContext = request.getRequestContext();
		String pageName = getPageName(requestContext);
		HstSiteMap siteMap = getHstSiteMap(requestContext);
		List<HstSiteMapItem> siteMapItems = getAllSiteMapsWhichMatch(pageName, siteMap.getSiteMapItems());
		return getLinks(siteMapItems, request);
	};

	private List<String> getLinks(List<HstSiteMapItem> siteMapItems, HstRequest request) {
		List<String> result = new ArrayList<String>();
		HstRequestContext requestContext = request.getRequestContext();
		HstQueryManager hstQueryManager = getHstQueryManager(requestContext);
		HippoBean siteContentBean = getSiteContentBean(requestContext);
		for (HstSiteMapItem siteMapItem : siteMapItems) {
			String siteMpatId = siteMapItem.getId();
			if (Constants.SITEMAP_WILD_CART.matcher(siteMpatId).find()) {
				result.addAll(resolveWildcards(siteMapItem, hstQueryManager, siteContentBean));
			} else {
				result.add(siteMpatId);
			}
		}
		return result;
	}

	private HippoBean getSiteContentBean(HstRequestContext requestContext) {
		try {
			String contentPath = requestContext.getResolvedMount().getMount().getContentPath();
			return (HippoBean) getObjectConverter(requestContext).getObject(requestContext.getSession(), contentPath);
		} catch (ObjectBeanManagerException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (RepositoryException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private Collection<? extends String> resolveWildcards(HstSiteMapItem siteMapItem, HstQueryManager hstQueryManager,
			HippoBean siteContentBean) {
		try {
			List<String> result = new ArrayList<String>();
			SiteMapTemplate siteMapTemplate = new SiteMapTemplate(siteMapItem);
			HippoBean scope = siteContentBean.getBean(siteMapTemplate.getBasePath());
			if (scope != null) {
				HstQuery query = hstQueryManager.createQuery(scope);
				HippoBeanIterator beanIterator = query.execute().getHippoBeans();
				while (beanIterator.hasNext()) {
					HippoBean item = beanIterator.next();
					if (item != null) {
						String relativePath = absolutePathToRelativePath(item.getPath(), siteContentBean);
						if (siteMapTemplate.matches(relativePath)) {
							result.add(siteMapTemplate.getUrl(relativePath));
						}
					}
				}

			}

			return result;
		} catch (QueryException e) {
			throw new RuntimeException();
		}
	}

	private String absolutePathToRelativePath(String path, HippoBean siteContentBean) {
		String result = null;
		String siteContentPath = siteContentBean.getPath();
		if (path != null && path.startsWith(siteContentPath)) {
			result = path.substring(siteContentPath.length());
			if (result.startsWith("/")) {
				result = result.substring(1);
			}
		}
		return result;
	}

	private HstSiteMap getHstSiteMap(HstRequestContext requestContext) {
		HstSite hstSite = requestContext.getResolvedMount().getMount().getHstSite();
		HstSiteMap siteMap = hstSite.getSiteMap();
		return siteMap;
	}

	private String getPageName(HstRequestContext requestContext) {
		return requestContext.getResolvedSiteMapItem().getHstComponentConfiguration().getName();
	}

	private List<HstSiteMapItem> getAllSiteMapsWhichMatch(String pageName, List<HstSiteMapItem> siteMapItems) {
		List<HstSiteMapItem> result = new ArrayList<HstSiteMapItem>();
		for (HstSiteMapItem hstSiteMapItem : siteMapItems) {
			result.addAll(getAllChildrenOfThisPageType(pageName, hstSiteMapItem));
		}
		return result;
	}

	private List<HstSiteMapItem> getAllChildrenOfThisPageType(String pageName, HstSiteMapItem hstSiteMapItem) {
		List<HstSiteMapItem> result = new ArrayList<HstSiteMapItem>();
		if (("hst:pages/" + pageName).equals(hstSiteMapItem.getComponentConfigurationId())) {
			result.add(hstSiteMapItem);
		}
		List<HstSiteMapItem> children = hstSiteMapItem.getChildren();
		if (children != null) {
			for (HstSiteMapItem child : children) {
				result.addAll(getAllChildrenOfThisPageType(pageName, child));
			}
		}
		return result;
	}
}
