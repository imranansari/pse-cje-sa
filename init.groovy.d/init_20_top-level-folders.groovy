import hudson.model.*;
import jenkins.model.*;

import com.cloudbees.hudson.plugins.folder.Folder;
import com.cloudbees.hudson.plugins.folder.properties.SubItemFilterProperty;

import org.jenkinsci.plugins.workflow.job.WorkflowJob;

import org.jenkinsci.plugins.simplebuild.PipelineFolderIcon;

import java.util.logging.Logger

Logger logger = Logger.getLogger("init_20_top-level-folders.groovy")

def j = Jenkins.instance

File disableScript = new File(j.rootDir, ".disable-init_20_top-level-folders.groovy")
if (disableScript.exists()) {
    logger.info("DISABLED init_20_top-level-folders script")
    return
}

def pipelineExamplesFolder = j.createProject(Folder.class, "pipeline-examples")
pipelineExamplesFolder.getProperties().add(new SubItemFilterProperty(WorkflowJob.class))
pipelineExamplesFolder.setDisplayName("Pipeline Examples")
pipelineExamplesFolder.setIcon(new PipelineFolderIcon())

logger.info("created pipeline-examples folder")

//create marker file to disable scripts from running twice
disableScript.createNewFile()
