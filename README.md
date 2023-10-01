# AWS Lambda Function - Hello World

This AWS Lambda function is a Java-based serverless function that interacts with Amazon Athena to retrieve title information based on a given title ID.

## Overview

This Lambda function is triggered by an AWS API Gateway and takes a title ID as input. It then performs the following steps:

1. Validates and parses the title ID.
2. Executes a SQL query against Amazon Athena to fetch title information.
3. Processes the query results and constructs a response.
4. Returns the response to the caller.

## Usage

To use this Lambda function, you can invoke it using the AWS API Gateway. The expected input for the function is a JSON object with a "titleid" field, like this:

```json
{
  "titleid": "100"
}

Athena Tables:
![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/88740a54-e6b3-4fd6-b35c-6dc69c320464)

AWS Lamda was assigned this role:
![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/27a6c569-c78f-46b9-bbdf-e26748eb0e7b)

API Gateway Configuraitons:
![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/b33ffe9a-02cd-479e-ab38-4e98d9abe44d)

Request Body Changes
a)Create a model
![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/b5581bb4-aaf5-4b36-9685-968f88259425)
b)Model Json
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "type": "object",
  "properties": {
    "titleid": {
      "type": "string"
    }
  },
  "required": ["titleid"]
}

c)Click on Method Request
![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/25a4e71a-955e-49d8-960a-5a2f7c962457)

d)Add model Under Request Body
![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/ed2eb19c-cfe7-4e7f-b798-fd422d62691c)

e)Deply this API into "test"stage
![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/35ac7560-e95f-4ee4-92c6-e86c7eca6d59)

f)![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/7f7d0ed7-c3e5-4fb5-8d5a-7d371d05b17b)



e)Optional:To enable API key..Chnage it to True
![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/029ea87e-326e-4ddd-94f8-f7e51c10264b)

f)Create new API Key
![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/e2bb523a-898c-4098-9d4e-cd9fbf140389)

g)Create usage plan
![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/993600c4-5f90-43ea-8082-18109252698d)

h)Assign API key to this usage plan
Map PAI key to our "test" stage
![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/1ec44f91-038a-4fff-b4ea-f5df311dc43b)

i)Redeploy the API to stage to refeclt APY key changes
![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/2c1f5b4e-6fcc-491a-9d32-2d0eb6822599)

h)To test using postman ..make sure to pass the API key under header x-api-key
![image](https://github.com/Kriengg/AwslamdaAthena/assets/6264991/d43d69af-b321-40ef-9605-121b265919a4)













Ensure that you have the necessary permissions set up for your Lambda function to access Athena and other required AWS services.

Note:
While testing main function locally ..make sure to set AWS key and ID in your env variables

Dependencies
This Lambda function uses the following libraries and AWS SDKs:

AWS SDK for Java v2
Amazon Athena SDK
Amazon S3 SDK
The function is designed to handle dependencies automatically.

Deployment
This Lambda function is designed to be deployed on AWS. You can deploy it using the AWS Management Console, AWS CLI, or any CI/CD pipeline that supports AWS Lambda deployment.

License
This project is licensed under the MIT License - see the LICENSE file for details.

Author
[Krishn]
Feel free to modify and enhance this function to suit your specific requirements.

For more information on AWS Lambda and Amazon Athena, refer to the AWS documentation:

AWS Lambda Documentation
Amazon Athena Documentation


