import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.lib.StoredConfig;

import jenkins.model.Jenkins

import java.util.logging.Logger

Logger logger = Logger.getLogger("init.init_02_pull_remote_pipeline_global_libs.groovy")

Jenkins j = Jenkins.getInstance();
logger.info(j.getRootPath().toString())
File workflowLibGitDir = new File(j.getRootPath().toString(), "workflow-libs/.git")
File workflowLibDir = new File(j.getRootPath().toString(), "workflow-libs")
println"git init".execute(null, workflowLibDir).text
println "git status".execute(null, workflowLibDir).text
Repository localRepo = new FileRepositoryBuilder().setGitDir(workflowLibGitDir).build();
logger.info("Having repository: " + localRepo.getDirectory());
Git git = new Git(localRepo);
StoredConfig config = git.getRepository().getConfig();
config.setString("remote", "upstream", "url", "https://github.com/beedemo-sa/workflowLibs");
config.save();

println "git remote -v".execute(null, workflowLibDir).text
println "git pull upstream master".execute(null, workflowLibDir).text
println "ls -la".execute(null, workflowLibDir).text
