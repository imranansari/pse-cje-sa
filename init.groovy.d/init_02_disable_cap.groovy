import jenkins.model.Jenkins

import java.nio.file.Path
import java.nio.file.Paths

import java.util.logging.Logger

String scriptName = "init_02_disable_cap.groovy"

Logger logger = Logger.getLogger(scriptName)

File disableScript = new File(Jenkins.getInstance().getRootDir(), ".disable-install_plugins-script")
if (disableScript.exists()) {
    logger.info("DISABLE " + scriptName)
    return
}

File cbEnvelopeFolder = new File(Jenkins.getInstance().root, "cb-envelope")

if (!cbEnvelopeFolder.exists()) {
    cbEnvelopeFolder.mkdir()
    logger.info("Created cb-envelope folder")
}

File disableCap = new File(cbEnvelopeFolder, ".envelope.json.disabled")
disableCap.createNewFile()
logger.info("DISABLED CAP")

disableScript.createNewFile()
