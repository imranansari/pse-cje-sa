import jenkins.model.Jenkins

import java.util.logging.Logger

Logger logger = Logger.getLogger("init_99_save.groovy")
    
logger.info("creating file to disable specified init scripts")
new File(Jenkins.getInstance().getRootDir(), ".disable-init-script").createNewFile()

logger.info("configuration finished")
Jenkins.getInstance().save()