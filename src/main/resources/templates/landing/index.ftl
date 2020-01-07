<#import "amp_framework.ftl" as amp />

<#assign schema>
<script type="application/ld+json">
    {
   "@context":"http://schema.org",
   "@type":"Organization",
   "name":"TheVPNCompany",
   "url":"https://www.thevpncompany.com.au",
   "logo":"https://www.thevpncompany.com.au/static/the-vpn-company-logo.png",
<#--   "sameAs":[
      "https://www.facebook.com/Oracle",
      "https://www.twitter.com/Oracle",
      "https://plus.google.com/+Oracle",
      "https://www.linkedin.com/company/oracle",
      "https://www.instagram.com/oracle/",
      "https://www.youtube.com/user/Oracle"
   ],-->
   "contactPoint":[
      {
         "@type":"ContactPoint",
         "email":"info@manageacloud.com",
         "contactType":"customer service",
         "areaServed":"AU"
      },
      {
         "@type":"ContactPoint",
         "email":"support@manageacloud.com",
         "areaServed":"AU"
      },
      {
         "@type":"ContactPoint",
         "email":"sales@manageacloud.com",
         "contactType":"sales",
         "areaServed":"AU"
      }
   ]
}
</script>
</#assign>

<#assign extra_css>
    .section-intro a {
    margin-top: 30px;
    }
</#assign>

<#assign head>
    <title>Australian VPN</title>
</#assign>

<@amp.html head=head extra_css=extra_css schema=schema>
    <section class="c-f section-main section-blue section-intro">
        <div class="c">
            <div class="r">
                <div class="c-l-12">
<#--                    <div style="float:right" >-->
<#--                    <amp-img width="300" height="288" src="/static/australian-owned-operated-logo.png"/>-->
<#--                    </div>-->
<#--                    <div style="width: 630px;float: left;">-->
                        <h1 class="clear">Worried about your online privacy?</h1>
                        <p class="lead t-b">Secure your online privacy with a VPN</p>
                        <p class="readdable">The<b>VPN</b>Company is an australian owned and operated business that
                            allows Australians to protect their privacy. High speed, ultra secure, and easy to use. Instant setup.</p>
                        <a href="/what-is-vpn" class="b b-p b-l">Learn More</a>
                            <p>30-day money-back guarantee</p>
                    <#--</div>-->
                </div>
            </div>
        </div>
    </section>
    <section class="c section-main">
        <div class="r ">
            <div class="readdable">
                <h1>Australian Data Retention Laws</h1>
                <p class="lead">The<b>VPN</b>Company lets you be anonymous online in Australia.</p>
                <p>
                    Australian Telecommunications Companies and ISPs (Telstra, Optus, Vodafone, etc) are forced to retain
                    their clients communications metadata which includes the date, time and duration of communication or any
                    details identifying a connection to an internet service for a recommended period of 1 year.
                </p>
                <a href="/what-is-vpn" class="b b-p b-l">Learn More</a>
                <p>30-day money-back guarantee</p>
            </div>
        </div>
    </section>
    <section class="c-f section-main section-blue section-intro">
        <div class="c">
            <div class="r">
                <div class="readdable">
                    <h1>Unlock the Internet</h1>
                    <p>Need a fast website unblocker app to access your favorite sites at school, at work, or anywhere in the world?</p>
                    <p>Learn how to unblock websites, access web services, and defeat censorship. Get the content you want—wherever you want it.</p>
                    <a href="/what-is-vpn" class="b b-p b-l">Learn More</a>
                    <p>30-day money-back guarantee</p>
                </div>
            </div>
        </div>
    </section>
    <section class="c section-main">
        <div class="r ">
            <div class="readdable">
                <h1>Blazing-fast VPN speeds</h1>
                <p>
                    Huge network of global VPN servers optimized for fast connections. Unlimited bandwidth, no throttling.
                </p>
                <a href="/what-is-vpn" class="b b-p b-l">Learn More</a>
                <p>30-day money-back guarantee</p>
            </div>
        </div>
    </section>
</@amp.html>

