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
package nl.anwb.online.tfinder;

import net.sourceforge.hippopagefinder.SiteMapTemplate;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Ebrahim Aharpour
 *
 */
public class SiteMapTemplateTest {

	@Test
	public void anyTest() {
		SiteMapTemplate siteMapTemplate = new SiteMapTemplate("test/${1}", "news/_any_");
		Assert.assertEquals(true, siteMapTemplate.matches("test/something"));
		Assert.assertEquals(true, siteMapTemplate.matches("test/something/"));
		Assert.assertEquals("test/", siteMapTemplate.getBasePath());
		Assert.assertEquals("test/", siteMapTemplate.getBasePath());
		Assert.assertEquals("news/something", siteMapTemplate.getUrl("test/something"));
		Assert.assertEquals("news/something", siteMapTemplate.getUrl("test/something/"));
		Assert.assertEquals("news/something/else", siteMapTemplate.getUrl("test/something/else"));
	}
	
	@Test
	public void defaultTest() {
		SiteMapTemplate siteMapTemplate = new SiteMapTemplate("test/${1}", "news/_default_");
		Assert.assertEquals(true, siteMapTemplate.matches("test/something"));
		Assert.assertEquals(true, siteMapTemplate.matches("test/something/"));
		Assert.assertEquals(false, siteMapTemplate.matches("test/something/something/else"));
		Assert.assertEquals(false, siteMapTemplate.matches("test/something/something/else/"));
		Assert.assertEquals("test/", siteMapTemplate.getBasePath());
		Assert.assertEquals("news/something", siteMapTemplate.getUrl("test/something"));
		
		boolean exceptionWasCatched = false;
		try  {
			siteMapTemplate.getUrl("test/something/else");
		} catch (IllegalArgumentException e) {
			exceptionWasCatched = true;
		}
		Assert.assertEquals(true, exceptionWasCatched);
	}
	
	@Test
	public void staticTest() {
		SiteMapTemplate siteMapTemplate = new SiteMapTemplate("test/", "news/_default_");
		Assert.assertEquals(true, siteMapTemplate.matches("test/"));
		Assert.assertEquals(true, siteMapTemplate.matches("test"));
		Assert.assertEquals("test/", siteMapTemplate.getBasePath());
		Assert.assertEquals("news/somevalue", siteMapTemplate.getUrl("test/"));
		
		boolean exceptionWasCatched = false;
		try  {
			siteMapTemplate.getUrl("test/something");
		} catch (IllegalArgumentException e) {
			exceptionWasCatched = true;
		}
		Assert.assertEquals(true, exceptionWasCatched);
	}
	
	@Test
	public void combinationTest() {
		SiteMapTemplate siteMapTemplate = new SiteMapTemplate("test/${2}/${3}", "news/_default_/_default_/_any_");
		Assert.assertEquals(false, siteMapTemplate.matches("test/"));
		Assert.assertEquals(false, siteMapTemplate.matches("test"));
		Assert.assertEquals(false, siteMapTemplate.matches("test/something"));
		Assert.assertEquals(false, siteMapTemplate.matches("test/something/"));
		Assert.assertEquals(true, siteMapTemplate.matches("test/something/else"));
		Assert.assertEquals(true, siteMapTemplate.matches("test/something/else/"));
		Assert.assertEquals(true, siteMapTemplate.matches("test/something/else/plus"));
		Assert.assertEquals(true, siteMapTemplate.matches("test/something/else/plus/"));
		Assert.assertEquals("test/", siteMapTemplate.getBasePath());
		
		Assert.assertEquals("news/somevalue/something/else/plus/extra", siteMapTemplate.getUrl("test/something/else/plus/extra"));
		boolean exceptionWasCatched = false;
		try  {
			siteMapTemplate.getUrl("news/something/else/plus");
		} catch (IllegalArgumentException e) {
			exceptionWasCatched = true;
		}
		Assert.assertEquals(true, exceptionWasCatched);
	}
}
