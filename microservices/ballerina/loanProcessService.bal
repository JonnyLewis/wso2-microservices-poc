import ballerina.net.http;
import ballerina.lang.messages;
import ballerina.lang.system;

@http:configuration {basePath:"/loanservice"}
service<http> loanProcessService {


   @http:resourceConfig {
        methods:["GET"],
        path:"/"
    }
    resource getAllResource (message m) {

        system:println("Get All Loan application resource invoked");
        http:ClientConnector loanserviceEP = create http:ClientConnector ("http://fintechdemo-loanservice.wso2apps.com/loanservice");
        message response = {};
        response = loanserviceEP.get("/", m);
        reply response;
    }	

    @http:resourceConfig {
        methods:["GET"],
        path:"/status/{referencenumber}"
    }
    resource statusResource (message m, @http:PathParam {value:"referencenumber"} string referencenumber) {

        system:println("Loan application status resource invoked: " + referencenumber);
        http:ClientConnector loanserviceEP = create http:ClientConnector ("http://fintechdemo-loanservice.wso2apps.com/loanservice");
        message response = {};
        response = loanserviceEP.get("/status/" + referencenumber, m);
        reply response;
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/create"
    }
    resource createResource (message m) {
        system:println("Loan application create resource invoked.");
        http:ClientConnector customerserviceEP = create http:ClientConnector ("http://fintechdemo-customerservice.wso2apps.com/customerservice");
        http:ClientConnector creditserviceEP = create http:ClientConnector ("http://fintechdemo-creditservice.wso2apps.com/creditservice");
        http:ClientConnector loanserviceEP = create http:ClientConnector ("http://fintechdemo-loanservice.wso2apps.com/loanservice");


        json jsonMsg = messages:getJsonPayload(m);
        string customerId;
        float income;
        float percentage = 40;
        float loanamount;
        float amounteligible;
        float outstandingbalance;

        customerId, _ = (string)jsonMsg["customerId"];
        income, _ = (float)jsonMsg["income"];
        loanamount, _ = (float)jsonMsg["amount"];

        //system:println("Income :" + income);
        //system:println("Loan Amount :" + loanamount);

        message response = {};
        response = customerserviceEP.get("/" + customerId, m);
        int statuscode = http:getStatusCode(response);
        if(statuscode != 200){
            system:println("Invalid user.");
            reply response;
        }
        system:println("Valid customer found: " + customerId);

        response = creditserviceEP.get("/82781212", m);
        statuscode = http:getStatusCode(response);
        if(statuscode != 200){
            system:println("Error occurred while checking credit balance " + statuscode);
            reply response;
        }
        system:println("Valid Credit records found.");

        json jsonResponse = messages:getJsonPayload(response);
        outstandingbalance, _ = (float)jsonResponse["outstandingbalance"];
        amounteligible = ((income-outstandingbalance) * percentage) / 100;

        system:println("Outstanding Balance :" + outstandingbalance);
        system:println("Amount elligible for the loan :" + amounteligible);
        if(loanamount  > amounteligible){
            messages:setStringPayload(response, "You are not elligible to get that amount.");
            http:setStatusCode(response, 403);
            system:println("=================================================");
            reply response;
        }
        system:println("Customer elligible for the loan.");
        response = loanserviceEP.post("/", m);
        system:println("Loan application successfully.");
        system:println("=========================================");
        reply response;

    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/approve/{referencenumber}"
    }
    resource approveResource (message m, @http:PathParam {value:"referencenumber"} string referencenumber) {

        system:println("Loan application approve resource invoked: " + referencenumber);
        http:ClientConnector loanserviceEP = create http:ClientConnector ("http://fintechdemo-loanservice.wso2apps.com/loanservice");

        message response = {};
        response = loanserviceEP.post("/approve/" + referencenumber, m);
        reply response;
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/reject/{referencenumber}"
    }
    resource rejectResource (message m, @http:PathParam {value:"referencenumber"} string referencenumber) {

	system:println("Loan application reject resource invoked: " + referencenumber);
        http:ClientConnector loanserviceEP = create http:ClientConnector ("http://fintechdemo-loanservice.wso2apps.com/loanservice");

        message response = {};
        response = loanserviceEP.post("/reject/" + referencenumber, m);
        reply response;
    }
}

