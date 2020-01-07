<#import "../amp_framework.ftl" as amp />

<#assign extra_css>
    a.b {
    margin-left: 8px;
    margin-right: 8px;
    }
    pre {
    background: #f4f4f4;
    border: 1px solid #ddd;
    border-left: 3px solid #f36d33;
    color: #666;
    page-break-inside: avoid;
    font-family: monospace;
    font-size: 15px;
    line-height: 1.6;
    margin-bottom: 1.6em;
    max-width: 100%;
    overflow: auto;
    padding: 1em 1.5em;
    display: block;
    word-wrap: break-word;
    }
</#assign>

<#assign head>
    <title>Guide to install TheVPNCompany for Linux</title>
</#assign>

<@amp.html head=head extra_css=extra_css>
    <section class="c-f section-main section-blue section-intro">
        <div class="c">
            <div class="r">
                <div class="c-l-12">
                    <h1>Guide to install VPN for Linux (Ubuntu)</h1>
                </div>
            </div>
        </div>
    </section>
    <section class="c section-main">
        <div class="r ">
            <div class="c-l-12 readdable">
                <h2>Step 1 - Install the software</h2>
                <p>
                    Open a terminal and execute the following commands to install OpenVPN client:
                    <pre>sudo apt-get install -y openvpn</pre>
                </p>
                <h2>Step 2 - Configure the software</h2>
                    <p>Access to your The<b>VPN</b>Company account</a> and <a class="b b-d" href="/dashboard/linux/ovpn">download configuration file</a></p>
                    <p>You will need to copy this file /etc/openvpn/thevpncompany.conf
                        <pre>sudo cp ~/Downloads/client-101.linux.ovpn /etc/openvpn/thevpncompany.conf</pre>
                    </p>
                    <p>Start the VPN connection with the following command:</p>
                    <pre>sudo systemctl start openvpn@thevpncompany</pre>
                    <p>If you want this service to start every time the computer starts you need to enable it:</p>
                    <pre>sudo systemctl enable openvpn@thevpncompany</pre>

                <h2>Step 3 - Check that works</h2>
                <p>
                    You should be connected to The<b>VPN</b>Company safely and be able to browser the internet safely.
                    To make sure that everything was set up correctly, please use <a class="b b-d" href="/is-vpn-up">Is Vpn Up ?</a> tool.
                </p>
            </div>
        </div>
    </section>
</@amp.html>

