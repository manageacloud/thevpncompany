<#-- @ftlvariable name="subscription" type="com.manageacloud.vpn.model.Subscription" -->
<#import "../dashboard_template.ftl" as t />
<#import "../../parts/_chooseplan.ftl" as p />

<#assign extra_css>

</#assign>

<#assign extra_js>
</#assign>


<#assign head>
    <title>Subscription Confirmation | TheVPNCompany</title>
</#assign>

<@t.dashboardTemplate head=head extra_css=extra_css extra_js=extra_js>
    <section class="c-f section-dashboard section-top">
        <div class="c">
            <div class="r ">
                <div class="c-l-12">
                    <h2>Your subscription has been created</h2>
                    <p>
                        Your new subscription is <i>${subscription.getPlan().getDefaultName()}</i>.
                    </p>
                    <p>
                        Please proceed to <a class="b b-d" href="/dashboard">configure your device</a>.
                    </p>

                </div>
            </div>
        </form>
        </div>
    </section>
</@t.dashboardTemplate>

