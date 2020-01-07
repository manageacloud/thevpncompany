<#-- @ftlvariable name="subscription" type="com.manageacloud.vpn.model.Subscription" -->
<#import "../dashboard_template.ftl" as t />
<#import "../../parts/_chooseplan.ftl" as p />

<#assign extra_css>

</#assign>

<#assign extra_js>
</#assign>


<#assign head>
    <title>Cancel Confirmation</title>
</#assign>

<@t.dashboardTemplate head=head extra_css=extra_css extra_js=extra_js>
    <section class="c-f section-dashboard section-top">
        <div class="c">
            <div class="r ">
                <div class="c-l-12">
                    <h2>Your subscription has been updated</h2>
                    <p>
                        Your new subscription has been updated to <i>${subscription.getPlan().getDefaultName()}</i>.
                    </p>
                    <p>
                        We will contact you for any refunds. If you have questions, you can always contact <a href="/dashboard/support">support</a>.
                    </p>

                </div>
            </div>
        </form>
        </div>
    </section>
</@t.dashboardTemplate>

