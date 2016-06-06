import jenkins.model.*
import java.util.logging.Logger

def configName = "init_05_mail_server_config.groovy"
Logger logger = Logger.getLogger("$configName")

def j = Jenkins.getInstance()

File disableScript = new File(j.rootDir, ".disable-$configName")
if (disableScript.exists()) {
    logger.info("DISABLED $configName")
    return
}

logger.info("begin mailer config")

def desc = j.getDescriptor("hudson.tasks.Mailer")

def env = System.getenv()
def smtpAuthPasswordSecret = env['GMAIL_SMTP_PASSWORD']

desc.setSmtpAuth("beedemo.sa@gmail.com", "$smtpAuthPasswordSecret")
desc.setReplyToAddress("beedemo.sa@gmail.com")
desc.setSmtpHost("smtp.gmail.com")
desc.setUseSsl(true)
desc.setSmtpPort("465")
desc.setCharset("UTF-8")

desc.save()

logger.info("configured mailer")

//create marker file to disable scripts from running twice
disableScript.createNewFile()