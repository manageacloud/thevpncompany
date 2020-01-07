<#-- @ftlvariable name=".globals.username" type="String" -->
<#-- @ftlvariable name="subscription" type="com.manageacloud.vpn.model.Subscription" -->
<#assign security = JspTaglibs["http://www.springframework.org/security/tags"]>
<@security.authentication property="principal.name" var="username" />

<#import "dashboard_template.ftl" as t />

<#assign head>
    <title>Dashboard</title>
</#assign>

<#assign extra_css>
    .b-d {
        width: 100%;
        margin-bottom: 15px;
        text-align: center;
    }
    .b-d:hover {
        color: #ffffff;
        background-color: #4582ec;
    }


    @media (min-width: 370px) {
        .b-d {
            width: 200px;
        }
    }

</#assign>


<@t.dashboardTemplate head=head extra_css=extra_css>
    <section class="c-f section-dashboard section-top">
        <div class="c">
            <div class="r">
                <div class="c-l-12">
                    <h1>${.globals.username?capitalize}</h1>
                    <#if subscription??>
                        <p>You are currently subscribed to <i>${subscription.getPlan().getDefaultName()}</i>.</p>
                        <p><a href="/dashboard/account/subscription" class="b b-d">Change Plan</a></p>
                        <#if !subscription.getPlan().getType().isFree() >
                            <p><a href="/dashboard/account/cc" class="b b-d">Update Credit Card</a></p>
                        </#if>
                        <p><a href="/dashboard/account/cancel" class="b b-d">Cancel Subscription</a></p>
                    <#else>
                        <p>You don't have an active subscription.</p>
                        <p><a href="/dashboard/account/subscription/new" class="b b-d">New Subscription</a></p>
                    </#if>
                    <p><a href="/dashboard/account/password" class="b b-d">Change Password</a></p>
                </div>
            </div>

        </div>
    </section>
</@t.dashboardTemplate>

