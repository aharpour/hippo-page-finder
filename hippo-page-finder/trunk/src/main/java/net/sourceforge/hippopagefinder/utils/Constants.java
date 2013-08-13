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

import java.util.regex.Pattern;

/**
 * @author Ebrahim Aharpour
 *
 */
public class Constants {
	
	public final static Pattern SITEMAP_WILD_CART = Pattern.compile("_any_|_default_");
	public final static Pattern RELATIVE_PATH_WILD_CART = Pattern.compile("\\$\\{(\\d*)\\}");

}
