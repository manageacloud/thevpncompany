<#import "../dashboard_template.ftl" as t />
<#import "../../parts/_chooseplan.ftl" as p />

<#assign extra_css>

</#assign>

<#assign extra_js>
</#assign>


<#assign head>
    <title>Information updated</title>
</#assign>

<@t.dashboardTemplate head=head extra_css=extra_css extra_js=extra_js>
    <section class="c-f section-dashboard section-top">
        <div class="c">

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="r ">
                <div class="c-l-12">
                    <h2>Information Updated</h2>
                    <p>Your personal information and your credit card information has been updated.</p>
                </div>
            </div>
        </form>
        </div>
    </section>
</@t.dashboardTemplate>

