FROM cloudbees/pse-master:1.3.1

ARG TAG_FROM_TRIGGER=2.19.4.2

#set java opts variable to skip setup wizard; plugins will be installed via license activated script
ENV JAVA_ARGS="-Djenkins.install.runSetupWizard=false"
#JAVA_OPTS don't appear to propagate correctly, so also using JAVA_ARGS above
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
ENV JENKINS_UC http://jenkins-updates.cloudbees.com

USER root
#override CJE war to use newer version
RUN rm /usr/share/jenkins/jenkins.war
RUN curl -fsSL http://jenkins-updates.cloudbees.com/download/je/$TAG_FROM_TRIGGER/jenkins.war -o /usr/share/jenkins/jenkins.war
#DISABLE - need to figure out how to get sha - echo "515508cd6311cd343f304c6567bc7f6e03c06a0e /usr/share/jenkins/jenkins.war" | sha1sum -c -

#setup base set of Pipeline Global Libs via init groovy script
COPY ./init.groovy.d/* /usr/share/jenkins/home/init.groovy.d/
COPY ./license-activated/* /usr/share/jenkins/home/license-activated-or-renewed-after-expiration.groovy.d/
COPY ./quickstart/* /usr/share/jenkins/home/quickstart.groovy.d/
COPY ./scriptApproval.xml /usr/share/jenkins/home/

#install plugins that aren't bundled
RUN /usr/local/bin/install-plugins.sh \
  audit-trail \
  http_request \
  docker-commons:1.4.0 \
  dockerhub-notification \
  pipeline-utility-steps \
  hipchat

#change back to jenkins user for RUN/ENTRYPOINT commands
USER jenkins
