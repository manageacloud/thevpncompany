<#import "../amp_framework.ftl" as amp />
<#--<#assign schema>
<script type="application/ld+json">
    {
        "@context":"http://schema.org",
        "@type":"HowTo",
        "name":"Guide to install OpenVPN Connect for Android",
        "totalTime":"00:05:00",
        "supply":
        "steps":
        [
            {
                "@type":"HowToSection",
                "name":"Download OpenVPN Connect",
                "itemListElement":
                [
                    {
                        "@type":"HowToStep",
                        "itemListElement":
                        [
                            {
                                "@type":"HowToDirection",
                                "@if":"https://play.google.com/store/apps/details?id=net.openvpn.openvpn&hl=en"
                                "description":"Download OpenVPN Connect for Android",
                            }
                        ]
                    }

                ]
            },
            {
                "@type":"HowToSection",
                "name":"Download the Configuration File",
                "itemListElement":
                [
                    {
                        "@type":"HowToStep",
                        "itemListElement":
                        {
                            "@type":"HowToDirection",
                            "description":"Position the jack underneath the car, next to the flat tire."
                        }
                    },
                    {
                        "@type":"HowToStep",
                        "itemListElement":
                        [
                            {
                                "@type":"HowToDirection",
                                "description":"Raise the jack until the flat tire is just barely off of the ground.",
                                "beforeMedia":
                                {
                                    "@type":"ImageObject",
                                    "contentUrl":"car-on-ground.jpg"
                                },
                                "afterMedia":
                                {
                                    "@type":"ImageObject",
                                    "contentUrl":"car-raised.jpg"
                                }
                            },
                            {
                                "@type":"HowToTip",
                                "description":"It doesn't need to be too high."
                            }
                        ]
                    },
                    {
                        "@type":"HowToStep",
                        "itemListElement":
                        {
                            "@type":"HowToDirection",
                            "description":"Remove the hubcap and loosen the lug nuts."
                        }
                    },
                    {
                        "@type":"HowToStep",
                        "itemListElement":
                        {
                            "@type":"HowToDirection",
                            "description":"Remove the flat tire and put the spare tire on the exposed lug bolts.",
                            "duringMedia":
                            {
                                "@type":"VideoObject",
                                "contentUrl":"replacing-tire.mp4"
                            }
                        }
                    },
                    {
                        "@type":"HowToStep",
                        "itemListElement":
                        [
                            {
                                "@type":"HowToDirection",
                                "description":"Tighten the lug nuts by hand."
                            },
                            {
                                "@type":"HowToTip",
                                "description":"Don't use the wrench just yet."
                            }
                        ]
                    }
                ]
            },
            {
                "@type":"HowToSection",
                "name":"Finishing up",
                "itemListElement":
                [
                    {
                        "@type":"HowToStep",
                        "itemListElement":
                        {
                            "@type":"HowToDirection",
                            "description":"Lower the jack and tighten the lug nuts with the wrench."
                        }
                    },
                    {
                        "@type":"HowToStep",
                        "itemListElement":
                        {
                            "@type":"HowToDirection",
                            "description":"Replace the hubcap."
                        }
                    },
                    {
                        "@type":"HowToStep",
                        "itemListElement":
                        {
                            "@type":"HowToDirection",
                            "description":"Put the equipment and the flat tire away."
                        }
                    }
                ]
            }
        ]
    }
</script>
</#assign>-->
<#assign extra_css>
    a.b {
    margin-left: 8px;
    margin-right: 8px;
    }
</#assign>

<#assign extra_js>
    <script async custom-element="amp-anim" src="https://cdn.ampproject.org/v0/amp-anim-0.1.js"></script>
</#assign>

<#assign head>
    <title>Guide to install OpenVPN Connect for Android</title>
</#assign>

<@amp.html head=head extra_css=extra_css extra_js=extra_js>
    <section class="c-f section-main section-blue section-intro">
        <div class="c">
            <div class="r">
                <div class="c-l-12">
                    <h1>Guide to install openVPN Connect for Android</h1>
                </div>
            </div>
        </div>
    </section>
    <section class="c section-main">
        <div class="r ">
            <div class="c-l-12 readdable">
                <h2>Step 1 - Install the Software</h2>
                <p>
                    Install <a class="b b-d" href="https://play.app.goo.gl/?link=https://play.google.com/store/apps/details?id=net.openvpn.openvpn">OpenVPN Connect for Android</a>
                </p>
                <amp-anim width="191" height="339" src="/static/install/android-step1.gif">
                    <amp-img placeholder width="191" height="339"  src="/static/install/android-step1.gif">
                    </amp-img>
                </amp-anim>
                <h2>Step 2 - Configure the Software</h2>
                <p>
                    Access from your mobile to The<b>VPN</b>Company account, <a class="b b-d" href="/dashboard/android/ovpn">download the configuration file</a>
                    and open it with OpenVPN Connect.
                </p>
                <h2>Step 3 - Check that works</h2>
                <p>
                    You should be connected to The<b>VPN</b>Company safely and be able to browser the internet safely.
                    To make sure that everything was set up correctly, please use <a class="b b-d" href="/is-vpn-up">Is Vpn Up ?</a> tool.
                </p>

            </div>
        </div>
    </section>
</@amp.html>

