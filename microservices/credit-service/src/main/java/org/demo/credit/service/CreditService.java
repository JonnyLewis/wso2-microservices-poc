/*
 * Copyright (c) 2017, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.demo.credit.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import org.demo.credit.dao.CreditDAO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 0.1-SNAPSHOT
 */
@Api(value = "creditservice")
@SwaggerDefinition(
        info = @Info(
                title = "Credit service Swagger Definition", version = "1.0",
                description = "The endpoint which is used to manage credit of a user",
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0")
        )
)
@Path("/creditservice")
public class CreditService {

    private static final Logger logger = LoggerFactory.getLogger(CreditService.class);

    @GET
    @Path("/{id}")
    @ApiOperation(
            value = "Return Outstanding balance of customer. ",
            notes = "Returns HTTP 500 if any internal error")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "{outstandingbalance:200.00}"),
            @ApiResponse(code = 500, message = "Particular exception message")})
    public Response getOutstandingBalance(@ApiParam(value = "id", required = true)
                                          @PathParam("id") String id) {

        logger.info("getOutstandingBalance invoked.");
        CreditDAO creditDAO = new CreditDAO();
        double totalOutstandingBalance = creditDAO.getCustomerOutstandingBalance(id);

        JSONObject returnObject = new JSONObject();
        logger.info("OutstandingBalance: " + totalOutstandingBalance);
        returnObject.put("outstandingbalance", totalOutstandingBalance);
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}

