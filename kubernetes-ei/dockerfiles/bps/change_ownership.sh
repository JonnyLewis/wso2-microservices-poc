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

echo 'changing ownership of ${server_artifact_location} directory: '
echo "user: ${USER}"
echo "user home: ${USER_HOME}"
echo "carbon server: ${WSO2_SERVER}-${WSO2_SERVER_VERSION}"

/bin/chown -R ${USER} ${server_artifact_location}
/bin/chgrp -R root ${server_artifact_location}
