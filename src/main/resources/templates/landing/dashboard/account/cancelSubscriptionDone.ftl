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

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="r ">
                <div class="c-l-12">
                    <h2>We are sorry to see you go !</h2>
                    <p><b>Your subscription has ben cancelled</b></p>
                </div>
            </div>
        </form>
        </div>
    </section>
</@t.dashboardTemplate>

