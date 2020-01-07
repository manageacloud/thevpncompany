<#import "dashboard_template.ftl" as t />
<#import "../parts/_choosedevice.ftl" as cd />

<#assign head>
    <title>Dashboard</title>
</#assign>

<#assign extra_css>
${cd.chooseDeviceCss}
</#assign>


<@t.dashboardTemplate head=head extra_css=extra_css>
    <section class="c-f section-dashboard section-top">
        <div class="c">
            <div class="r">
                <div class="c-l-12">
                    <h1>Choose your device</h1>
                    <p>Thanks for choosing The<b>VPN</b>Company. To configure the VPN, please choose your device below.</p>
                    <@cd.chooseDeviceOptions/>
                </div>
            </div>

        </div>
    </section>
</@t.dashboardTemplate>

