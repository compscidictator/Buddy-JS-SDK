using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Net.Mail;
using System.Text;

namespace StartupExplorer.Helper
{
    public class Utility
    {
        public Utility() { }

        /// <summary>
        /// Get Mail From
        /// </summary>
        /// <returns></returns>
        public static string GetMailFrom()
        {
            return Convert.ToString(System.Configuration.ConfigurationManager.AppSettings.Get("MailFrom"));
        }

        /// <summary>
        /// Get Mail Port
        /// </summary>
        /// <returns></returns>
        public static string GetMailPort()
        {
            return Convert.ToString(System.Configuration.ConfigurationManager.AppSettings.Get("MailPort"));
        }

        /// <summary>
        /// Get Mail Server
        /// </summary>
        /// <returns></returns>
        public static string GetMailServer()
        {
            return Convert.ToString(System.Configuration.ConfigurationManager.AppSettings.Get("MailServer"));
        }

        /// <summary>
        /// Get UserName for mail
        /// </summary>
        /// <returns></returns>
        public static string GetMailUserName()
        {
            return Convert.ToString(System.Configuration.ConfigurationManager.AppSettings.Get("MailUserName"));
        }
        /// <summary>
        /// Get Password for mail
        /// </summary>
        /// <returns></returns>
        public static string GetMailPassword()
        {
            return Convert.ToString(System.Configuration.ConfigurationManager.AppSettings.Get("MailPassword"));
        }

        /// <summary>
        /// Send Mail 
        /// </summary>
        /// <param name="mailFrom">mailFrom</param>
        /// <param name="mailTo">mailTo</param>
        /// <param name="mailSubject">mailSubject</param>
        /// <param name="mailBody">mailBody</param>
        /// <param name="mlFormat">mlFormat</param>
        /// <returns></returns>
        public static string SendEmail(string mailTo, string mailSubject, string mailBody, bool mlFormat)
        {
            try
            {
                System.Net.Mail.MailMessage mail = new System.Net.Mail.MailMessage();
                mail.From = new MailAddress(GetMailFrom());
                mail.To.Add(mailTo);
                mail.Subject = mailSubject;
                mail.Body = mailBody;
                mail.IsBodyHtml = mlFormat;
                SmtpClient client = new SmtpClient();
                client.Host = GetMailServer();
                client.Port = Convert.ToInt32(GetMailPort());
                client.UseDefaultCredentials = false;
                client.Credentials = new System.Net.NetworkCredential(GetMailUserName(), GetMailPassword());
                client.EnableSsl = true;

                client.Send(mail);
            }
            catch (Exception ex)
            {
                return ex.ToString();
            }
            return "Message Has Been Sent";
        }

        /// <summary>
        /// Mail Format to send Register Customer
        /// </summary>
        /// <param name="startupName"></param>
        /// <param name="startupCity"></param>
        /// <param name="startupCategory"></param>
        /// <param name="startupStreet"></param>
        /// <param name="startupContactName"></param>
        /// <param name="startupZip"></param>
        /// <param name="startupContactEmail"></param>
        /// <returns></returns>
        public static string MailFormat(string startupName, string startupCity, string startupCategory, string startupStreet, string startupContactName, string startupZip, string startupContactEmail)
        {
            StringBuilder strMailoCustomer = new StringBuilder();
            strMailoCustomer.Append("<div align='left' >");
            strMailoCustomer.Append("<table style='WIDTH: auto; HEIGHT: 150px;' border ='0' align='Left' ID='Table1'>");
            strMailoCustomer.Append("<tr bordercolor='white'>");
            strMailoCustomer.Append("<td align='Left' colspan='3' style='font-family: Times New Roman;font-size: 16;color: #000000;vertical-align:left;' nowrap='true'>Hello " + startupContactName + ",<br/><br/></td>");
            strMailoCustomer.Append("</tr>");
            strMailoCustomer.Append("<tr bordercolor='white'>");
            strMailoCustomer.Append("<td align='Left' colspan='3' style='font-family: Times New Roman;font-size: 16;color: #000000;vertical-align:middle;' nowrap='true'>");
            strMailoCustomer.Append("Startup Name: " + startupName + "<br />Startup City: " + startupCity + "<br />Startup Category: " + startupCategory + "<br />");
            if (!string.IsNullOrEmpty(startupStreet))
            {
                strMailoCustomer.Append("Startup Street: " + startupStreet + "<br />");
            }
            strMailoCustomer.Append("Startup Contact Name: " + startupContactName + "<br />");
            if (!string.IsNullOrEmpty(startupZip))
            {
                strMailoCustomer.Append("Startup Zip: " + startupZip + "<br />");
            }
            strMailoCustomer.Append("Startup Contact Email: " + startupContactEmail + "<br /><br /><br />");
            strMailoCustomer.Append("</td>");
            strMailoCustomer.Append("</tr>");
            strMailoCustomer.Append("<tr bordercolor='white'>");
            strMailoCustomer.Append("<td align='Left' colspan='3' style='font-family: Times New Roman;font-size: 16;color: #000000;vertical-align:left;' nowrap='true'>With Regards,<br />" + "</td>");
            strMailoCustomer.Append("</tr>");
            strMailoCustomer.Append("<tr bordercolor='white'>");
            strMailoCustomer.Append("<td align='Left' colspan='3' style='font-family: Times New Roman;font-size: 16;color: #000000;vertical-align:left;' nowrap='true'>Startup Team</td>");
            strMailoCustomer.Append("</tr>");
            strMailoCustomer.Append("</table>");
            strMailoCustomer.Append("</div>");

            return Convert.ToString(strMailoCustomer);
        }
    }
}