/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.service;

import com.manageacloud.vpn.utils.CoreUtils;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@Service
public final class SitemapService {

	public String createSitemap() throws MalformedURLException {
		WebSitemapGenerator sitemap = new WebSitemapGenerator(CoreUtils.getDomain());

		sitemap.addUrl(CoreUtils.getDomain() + "/what-is-vpn");
		sitemap.addUrl(CoreUtils.getDomain() + "/products");
		sitemap.addUrl(CoreUtils.getDomain() + "/support");
		sitemap.addUrl(CoreUtils.getDomain() + "/get-started");

		return String.join("", sitemap.writeAsStrings());
	}
}