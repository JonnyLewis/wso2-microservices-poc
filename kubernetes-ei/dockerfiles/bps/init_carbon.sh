#!/bin/bash

# ------------------------------------------------------------------------
# Copyright 2017 WSO2, Inc. (http://wso2.com)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License
# ------------------------------------------------------------------------

set -e
carbon_home=${HOME}/${WSO2_SERVER}-${WSO2_SERVER_VERSION}
server_artifact_location=${carbon_home}/wso2/business-process/repository/deployment/server

# Change the user of repository/deployment/server to wso2user. 
# this is done to avoid permission issues arising with volume mounts 
sudo /bin/change_ownership.sh

# Copy the backed up artifacts from ${HOME}/tmp/server/. Copying the initial artifacts to ${HOME}/tmp/server/ is done in the 
# Dockerfile. This is to preserve the initial artifacts in a volume mount (the mounted directory can be empty initially). 
# The artifacts will be copied to the CARBON_HOME/wso2/business-process/repository/deployment/server location before the server is started.
if [[ -d ${HOME}/tmp/server/ ]]; then
   if [[ ! "$(ls -A ${server_artifact_location}/)" ]]; then
      # There are no artifacts under CARBON_HOME/wso2/business-process/repository/deployment/server/; copy them.
      echo "copying artifacts from ${HOME}/tmp/server/ to ${server_artifact_location}/ .."
      cp -rf ${HOME}/tmp/server/* ${server_artifact_location}/
   fi
   rm -rf ${HOME}/tmp/server/
fi

# Copy customizations done by user do the CARBON_HOME location. 
if [[ -d ${HOME}/tmp/carbon/ ]]; then
   echo "copying custom configurations and artifacts from ${HOME}/tmp/carbon/ to ${carbon_home}/ .."
   cp -rf ${HOME}/tmp/carbon/* ${carbon_home}/
   rm -rf ${HOME}/tmp/carbon/
fi

# Copy configuration maps
if [ -e ${carbon_home}-conf/bps/conf ]
 then cp ${carbon_home}-conf/bps/conf/* ${carbon_home}/wso2/business-process/conf/
fi

if [ -e ${carbon_home}-conf/bps/conf-axis2 ]
 then cp ${carbon_home}-conf/bps/conf-axis2/* ${carbon_home}/wso2/business-process/conf/axis2/
fi

if [ -e ${carbon_home}-conf/bps/conf-datasources ]
 then cp ${carbon_home}-conf/bps/conf-datasources/* ${carbon_home}/wso2/business-process/conf/datasources/
fi

if [ -e ${carbon_home}-conf/bps/conf-epr ]
 then cp ${carbon_home}-conf/bps/conf-epr/* ${carbon_home}/wso2/business-process/repository/conf/epr/
fi

# overwrite localMemberHost element value in axis2.xml with container ip
export local_docker_ip=$(ip route get 1 | awk '{print $NF;exit}')
axi2_xml_location=${carbon_home}/wso2/business-process/conf/axis2/axis2.xml
if [[ ! -z ${local_docker_ip} ]]; then
   sed -i "s#<parameter\ name=\"localMemberHost\".*#<parameter\ name=\"localMemberHost\">${local_docker_ip}<\/parameter>#" "${axi2_xml_location}"
   if [[ $? == 0 ]]; then
      echo "Successfully updated localMemberHost with ${local_docker_ip}"
   else
      echo "Error occurred while updating localMemberHost with ${local_docker_ip}"
   fi
fi

# Start the bps server.
${carbon_home}/bin/business-process.sh
