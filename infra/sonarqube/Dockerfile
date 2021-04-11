FROM sonarqube:community

LABEL maintainer="mOuliNeX"

ENV PLUGIN_PR_VERSION="1.7.0"
ADD https://github.com/mc1arke/sonarqube-community-branch-plugin/releases/download/$PLUGIN_PR_VERSION/sonarqube-community-branch-plugin-$PLUGIN_PR_VERSION.jar /opt/sonarqube/extensions/plugins/
ADD https://github.com/mc1arke/sonarqube-community-branch-plugin/releases/download/$PLUGIN_PR_VERSION/sonarqube-community-branch-plugin-$PLUGIN_PR_VERSION.jar /opt/sonarqube/lib/common/