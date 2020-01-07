<#assign chooseDeviceCss>
    .device {
        width: 100%;
        margin-bottom: 15px;
        text-align: center;
    }
    .b-d:hover {
        color: #ffffff;
        background-color: #4582ec;
    }


    @media (min-width: 370px) {
        .device {
            width: 25%;
            margin-bottom: 15px;
            text-align: center;
        }
        .b-d:hover {
            color: #ffffff;
            background-color: #4582ec;
        }
    }
</#assign>

<#macro chooseDeviceOptions>
    <#if os?? && os != "UNKNOWN">
        <h2>How to Configure ${os?capitalize}</h2>
        <a href="/install/${os?lower_case}" class="device b b-d ">View Instructions</a>
        <a href="/dashboard/${os?lower_case}/ovpn" class="device b b-d ">Download Configuration</a>
        <h2>Other Configurations</h2>
    <#else >
        <h2>Configurations</h2>
    </#if>

    <a href="/install/windows" class="device b b-d ">Windows</a>
    <a href="/install/mac" class="device b b-d ">Mac</a>
    <a href="/install/linux" class="device b b-d ">Linux</a>

    <a href="/install/android" class="device b b-d ">Android</a>
    <a href="/install/ios" class="device b b-d ">IPad</a>
    <a href="/install/ios" class="device b b-d ">IPhone</a>

    <p>
        <a href="/support/chat">Would you like to request the configuration for another device ?</a>
    </p>

</#macro>