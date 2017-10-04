/*
 * Copyright 2016 EPAM Systems
 *
 *
 * This file is part of EPAM Report Portal.
 * https://github.com/reportportal/client
 *
 * Report Portal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Report Portal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Report Portal.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epam.reportportal.utils.markdown;

import com.epam.reportportal.utils.markdown.MarkdownUtils;
import org.junit.Assert;
import org.junit.Test;

import static com.epam.reportportal.utils.markdown.MarkdownUtils.asCode;

/**
 *
 * @author Andrei Varabyeu
 *
 */
public class MarkdownUtilsTest {
	@Test
	public void asMarkdown() throws Exception {
		Assert.assertEquals("Incorrect markdown prefix", "!!!MARKDOWN_MODE!!!hello", MarkdownUtils.asMarkdown("hello"));
	}

	@Test
	public void toMarkdownScript() throws Exception {
		Assert.assertEquals("Incorrect markdown prefix", "!!!MARKDOWN_MODE!!!```groovy\nhello\n```",
				asCode("groovy", "hello"));
	}

}