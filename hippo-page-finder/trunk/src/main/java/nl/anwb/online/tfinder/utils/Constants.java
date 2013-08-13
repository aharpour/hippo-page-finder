package nl.anwb.online.tfinder.utils;

import java.util.regex.Pattern;

public class Constants {
	
	public final static Pattern SITEMAP_WILD_CART = Pattern.compile("_any_|_default_");
	public final static Pattern RELATIVE_PATH_WILD_CART = Pattern.compile("\\$\\{(\\d*)\\}");

}
