<#import "dashboard/dashboard_template.ftl" as t />

<#assign extra_css>

    .message {
        margin-top: 150px
    }

</#assign>

<#assign head>
    <title>VPN Support</title>
</#assign>

<@t.dashboardTemplate head=head extra_css=extra_css is_amp=false>
    <section class="c-f section-dashboard section-top">
        <div class="c">
            <div class="message">
                The Chat is now available in the bottom right corner of the screen.
            </div>
        </div>
    </section>

    <script type="text/javascript">
        var $zoho=$zoho || {};$zoho.salesiq = $zoho.salesiq || {widgetcode:"5536fbe62dec1141ec6e826f7843c1d1c6829348e8ecf98233d4fa00883c12c84031671d15ebaf79927307fc6a3a1111", values:{},ready:function(){}};var d=document;s=d.createElement("script");s.type="text/javascript";s.id="zsiqscript";s.defer=true;s.src="https://salesiq.zoho.com/widget";t=d.getElementsByTagName("script")[0];t.parentNode.insertBefore(s,t);d.write("<div id='zsiqwidget'></div>");
    </script>


</@t.dashboardTemplate>


