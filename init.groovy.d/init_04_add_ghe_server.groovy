import jenkins.model.*;
import org.jenkinsci.plugins.github_branch_source.*;
import java.util.*;

import java.util.logging.Logger

Logger logger = Logger.getLogger("init.init_04_add_ghe_server.groovy")

logger.info("about to add BeesCloud GHE API endpoint")
GitHubConfiguration gitHubConfig = GlobalConfiguration.all().get(GitHubConfiguration.class)

Endpoint beescloudEndpoint = new Endpoint("https://github.beescloud.com/api/v3/","BeesCloud GHE")
List<Endpoint> endpointList = new ArrayList<Endpoint>()
endpointList.add(beescloudEndpoint)
gitHubConfig.setEndpoints(endpointList)
logger.info("added BeesCloud GHE API endpoint")
