<#import "../amp_framework.ftl" as amp />

<#macro dashboardTemplate head extra_css="" extra_js="" is_amp=true>
    <#assign extra_css_dashboard>
        body {
            color: #676767;
        }
        .section-top {
            border-top: 1px solid #f1f1f1;
            box-shadow: 1px 2px 4px 0 rgba(0,0,0,.03);
        }
        .section-dashboard {
            min-height:600px;
        }


        ${extra_css}
    </#assign>

    <#assign extra_js_dashboard>

        ${extra_js}
    </#assign>


    <#assign head_dashboard>
        ${head}
    </#assign>

    <@amp.html head=head_dashboard extra_css=extra_css_dashboard extra_js=extra_js_dashboard is_amp=is_amp>
        <#nested />
    </@amp.html>
</#macro>
