<#import "template/stationary_template.ftl" as st />

<@st.template title="Thanks for Signing up">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td align="left" style="padding: 20px 0 0 0; font-size: 16px; line-height: 25px; font-family: Helvetica, Arial, sans-serif; color: #666666;" class="padding-copy">Hey ${user_name},</td>
        </tr>
        <tr>
            <td align="left" style="padding: 20px 0 0 0; font-size: 16px; line-height: 25px; font-family: Helvetica, Arial, sans-serif; color: #666666;" class="padding-copy">
                Thanks for signing up in The<b>VPN</b>Company. The following credetials will give you access to your account:
            </td>
        </tr>
        <tr>
            <td align="left" style="padding: 20px 0 0 0; font-size: 16px; line-height: 25px; font-family: Helvetica, Arial, sans-serif; color: #666666;" class="padding-copy">
                Username: ${user_email}<br>
                Password: ${plain_password}
            </td>
        </tr>
        <tr>
            <td align="left" style="padding: 20px 0 0 0; font-size: 16px; line-height: 25px; font-family: Helvetica, Arial, sans-serif; color: #666666;" class="padding-copy">
                The password that you have received has been created automatically.
                We strongly recommend that you change the password.
            </td>
        </tr>
        <tr>
            <td style="padding: 15px;" bgcolor="#ffffff" align="center">
                <table width="100%" cellspacing="0" cellpadding="0" border="0">
                    <tbody>
                    <tr>
                        <td align="center">
                            <!-- BULLETPROOF BUTTON -->
                            <table width="100%" cellspacing="0" cellpadding="0" border="0">
                                <tbody><tr>
                                    <td style="padding-top: 25px;" class="padding" align="center">
                                        <table class="mobile-button-container" cellspacing="0" cellpadding="0" border="0">
                                            <tbody><tr>
                                                <td style="border-radius: 3px;" bgcolor="#256F9C" align="center">
                                                    <a href="${domain}/update-password?token=${recover_token}" target="_blank" style="font-size: 16px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; color: #ffffff; text-decoration: none; border-radius: 3px; padding: 15px 25px; border: 1px solid #256F9C; display: inline-block;" class="mobile-button">Reset Password &rarr;</a>
                                                </td>
                                            </tr>
                                            </tbody></table>
                                    </td>
                                </tr>
                                </tbody></table>
                        </td>
                    </tr>
                    </tbody>
                </table>

            </td>
        </tr>
        <tr>
            <td align="left" style="padding: 20px 0 0 0; font-size: 16px; line-height: 25px; font-family: Helvetica, Arial, sans-serif; color: #666666;" class="padding-copy">Cheers,<br>The The<b>VPN</b>Company Team</td>
        </tr>
    </table>
</@st.template>