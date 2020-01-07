<#import "template/stationary_template.ftl" as st />

<@st.template title="">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td align="left" style="padding: 20px 0 0 0; font-size: 16px; line-height: 25px; font-family: Helvetica, Arial, sans-serif; color: #666666;" class="padding-copy">Hey,</td>
        </tr>
        <tr>
            <td align="left" style="padding: 20px 0 0 0; font-size: 16px; line-height: 25px; font-family: Helvetica, Arial, sans-serif; color: #666666;" class="padding-copy">
                The<b>VPN</b>Company has received a request to recover your password.
                If you have not requested to change your password, you can
                ignore this requests. If you want to change your password,
                please click on "Reset Password".
            </td>
        </tr>
        <tr>
            <td align="left" style="padding: 20px 0 0 0; font-size: 16px; line-height: 25px; font-family: Helvetica, Arial, sans-serif; color: #666666;" class="padding-copy">
                <table border="0" cellspacing="0" cellpadding="0" class="mobile-button-container">
                    <tr>
                        <td align="center" style="border-radius: 3px;" bgcolor="#256F9C">
                            <a href="${domain}/update-password?token=${recover_token}" target="_blank" style="font-size: 16px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; color: #ffffff; text-decoration: none; border-radius: 3px; padding: 15px 25px; border: 1px solid #256F9C; display: inline-block;" class="mobile-button">Reset Password &rarr;</a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td align="left" style="padding: 20px 0 0 0; font-size: 16px; line-height: 25px; font-family: Helvetica, Arial, sans-serif; color: #666666;" class="padding-copy">Cheers,<br>The The<b>VPN</b>Company Team</td>
        </tr>
    </table>
</@st.template>