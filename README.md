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

Ensure that you have the necessary permissions set up for your Lambda function to access Athena and other required AWS services.

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
[Your Name]
Feel free to modify and enhance this function to suit your specific requirements.

For more information on AWS Lambda and Amazon Athena, refer to the AWS documentation:

AWS Lambda Documentation
Amazon Athena Documentation


