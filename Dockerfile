FROM cloudbees/pse-master:1.1.0

USER root
#override CJE war to use newer version
RUN rm /usr/share/jenkins/jenkins.war
RUN curl -fsSL http://jenkins-updates.cloudbees.com/download/je/1.651.3.1/jenkins.war -o /usr/share/jenkins/jenkins.war \
  && echo "515508cd6311cd343f304c6567bc7f6e03c06a0e /usr/share/jenkins/jenkins.war" | sha1sum -c -

#setup base set of Pipeline Global Libs via init groovy script
COPY ./init.groovy.d/* /usr/share/jenkins/home/init.groovy.d/

#use the CloudBees update center
ENV JENKINS_UC https://jenkins-updates.cloudbees.com

#need to overried FROM plugins.sh for use with cloudbees update center, cloubees uses '*latest*' instead of just 'latest'
COPY plugins.sh /usr/local/bin/plugins.sh
#copy list of non-standard plugins to install
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt
COPY ./plugins/* /usr/share/jenkins/ref/plugins/

#change back to jenkins user for RUN/ENTRYPOINT commands
USER jenkins
