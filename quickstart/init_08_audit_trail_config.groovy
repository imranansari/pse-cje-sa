import jenkins.model.*;
import hudson.plugins.audit_trail.AuditTrailPlugin
import hudson.plugins.audit_trail.SyslogAuditLogger

import java.util.logging.Logger

Logger logger = Logger.getLogger("init.init_08_audit_trail_config.groovy")
logger.info("begin audit trail config")
def syslogHostIp = "ec2-52-26-60-228.us-west-2.compute.amazonaws.com"
def syslogPort = 31034
SyslogAuditLogger syslogAuditLogger = new SyslogAuditLogger(syslogHostIp, syslogPort, System.properties.'MASTER_NAME', "", null, null)
Jenkins j = Jenkins.getInstance();
AuditTrailPlugin plugin = j.getPlugin(AuditTrailPlugin.class);
plugin.loggers.clear()
plugin.loggers.add(syslogAuditLogger)
plugin.save()
plugin.start()
logger.info("completed audit trail config and started")
