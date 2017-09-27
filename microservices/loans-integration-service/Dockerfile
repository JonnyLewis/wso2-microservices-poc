# ------------------------------------------------------------------------
#
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
#
# ------------------------------------------------------------------------

FROM openjdk:8
MAINTAINER WSO2 Docker Maintainers "dev@wso2.org"
ENV DEBIAN_FRONTEND noninteractive

ARG USER_HOME=/home/root/

COPY files/ballerina-0.93.zip ${USER_HOME}
COPY loanProcessService.balx ${USER_HOME}

WORKDIR ${USER_HOME}

RUN unzip ballerina-0.93.zip \
    && chgrp -R 0 ${USER_HOME} \
    && chmod -R g=u ${USER_HOME} \
    && chown -R root ${USER_HOME} \
    && chmod -R 0774 ${USER_HOME}

CMD ballerina-0.93/bin/ballerina run loanProcessService.balx
