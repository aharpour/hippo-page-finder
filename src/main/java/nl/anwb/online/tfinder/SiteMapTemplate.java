package nl.anwb.online.tfinder;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.anwb.online.tfinder.utils.Constants;
import nl.anwb.online.tfinder.utils.ReplacementUtil;
import nl.anwb.online.tfinder.utils.ReplacementUtil.ReplacemnetItem;

import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;

public class SiteMapTemplate {
	private final String relativePath;
	private final String siteMapId;
	private final String basePath;
	private final Pattern pattern;
	private final Map<Integer, Integer> reorderingMap;

	public SiteMapTemplate(HstSiteMapItem siteMapItem) {
		this(siteMapItem.getRelativeContentPath(), siteMapItem.getId());
	}

	public SiteMapTemplate(String relativePath, String siteMapId) {
		relativePath = normalize(relativePath);
		reorderingMap = getReorderingMap(relativePath);
		this.relativePath = relativePath;
		String regexPattern = relativePath;
		ReplacementUtil amendedSiteMapId = new ReplacementUtil(siteMapId);
		Matcher matcher = Constants.SITEMAP_WILD_CART.matcher(siteMapId);
		for (int i = 1; matcher.find(); i++) {
			amendedSiteMapId.addReplacemnetItem(new ReplacemnetItem(matcher.start(), matcher.end(), "${" + i + "}"));
			String group = matcher.group();
			if ("_any_".equals(group)) {
				regexPattern = regexPattern.replace("${" + i + "}", "(.*)");
			} else {
				regexPattern = regexPattern.replace("${" + i + "}", "([^/]*)");
			}
		}
		this.pattern = Pattern.compile(regexPattern);
		this.siteMapId = amendedSiteMapId.replace();
		int indexOfFirstWildcard = this.relativePath.indexOf("$");
		if (indexOfFirstWildcard >= 0) {
			this.basePath = this.relativePath.substring(0, indexOfFirstWildcard);
		} else {
			this.basePath = this.relativePath;
		}
	}

	public String getUrl(String relativePath) {
		relativePath = normalize(relativePath);
		String result;
		Matcher matcher = pattern.matcher(relativePath);
		boolean matches = matcher.matches();
		if (!matches) {
			throw new IllegalArgumentException("The given relative path does not match this site map item.");
		} else {
			Map<Integer, String> values = getValues(matcher);
			ReplacementUtil replacementUtil = new ReplacementUtil(siteMapId);
			Matcher siteMapIdMatcher = Constants.RELATIVE_PATH_WILD_CART.matcher(siteMapId);
			while (siteMapIdMatcher.find()) {
				int index = new Integer(siteMapIdMatcher.group(1));
				if (values.containsKey(index)) {
					replacementUtil.addReplacemnetItem(new ReplacemnetItem(siteMapIdMatcher.start(), siteMapIdMatcher
							.end(), values.get(index)));
				} else {
					replacementUtil.addReplacemnetItem(new ReplacemnetItem(siteMapIdMatcher.start(), siteMapIdMatcher
							.end(), "somevalue"));
				}
			}
			result = replacementUtil.replace();
		}
		return result;
	}

	private Map<Integer, String> getValues(Matcher matcher) {
		Map<Integer, String> values = new HashMap<Integer, String>();
		for (int i = 1; i <= matcher.groupCount(); i++) {
			values.put(reorderingMap.get(i), matcher.group(i));
		}
		return values;
	}

	private Map<Integer, Integer> getReorderingMap(String relativePath) {
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		Matcher matcher = Constants.RELATIVE_PATH_WILD_CART.matcher(relativePath);
		for (int i = 1; matcher.find(); i++) {
			result.put(i, new Integer(matcher.group(1)));
		}
		return result;
	}

	public String getBasePath() {
		return basePath;
	}

	public boolean matches(String path) {
		path = normalize(path);
		return pattern.matcher(path).matches();
	}

	private String normalize(String path) {
		if (!path.endsWith("/")) {
			path = path + "/";
		}
		return path;
	}

}