package org.example.lamda;

import java.util.ArrayList;
import java.util.List;

import org.example.lamda.models.Request;
import org.example.lamda.models.Response;
import org.example.lamda.models.Title;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.athena.AthenaClient;
import software.amazon.awssdk.services.athena.model.AthenaException;
import software.amazon.awssdk.services.athena.model.ColumnInfo;
import software.amazon.awssdk.services.athena.model.Datum;
import software.amazon.awssdk.services.athena.model.GetQueryExecutionRequest;
import software.amazon.awssdk.services.athena.model.GetQueryExecutionResponse;
import software.amazon.awssdk.services.athena.model.GetQueryResultsRequest;
import software.amazon.awssdk.services.athena.model.GetQueryResultsResponse;
import software.amazon.awssdk.services.athena.model.QueryExecutionState;
import software.amazon.awssdk.services.athena.model.ResultConfiguration;
import software.amazon.awssdk.services.athena.model.Row;
import software.amazon.awssdk.services.athena.model.StartQueryExecutionRequest;
import software.amazon.awssdk.services.athena.model.StartQueryExecutionResponse;
import software.amazon.awssdk.services.athena.paginators.GetQueryResultsIterable;



public class HelloWorldLambda implements RequestHandler<Request, Response> {

  
    private static final long SLEEP_AMOUNT_IN_MS = 1000;

    
    public static void main (String args[]) {
    	
    	 try {
			Response response = getTitle(100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Override
    public Response handleRequest(Request request, Context context) {
       // return "Hello, " + titleId + "!";
    	int  intTitleValue = 0;
    	Response response= new Response();
    	if (request.getTitleid() != null && !request.getTitleid().isEmpty()) {
    	    try {
    	        intTitleValue = Integer.parseInt(request.getTitleid());
    	         response = getTitle(intTitleValue);
    	    } catch (NumberFormatException e) {
    	        // Handle the case where the string is not a valid integer
    	        e.printStackTrace(); // You can log the error or handle it as needed
    	    }
    	}
       
        response.setMessage(String.format("Hello %s to AWS Lambda World!", request.getTitleid()));
        return response;
    }

    private static boolean waitForQueryToComplete(AthenaClient athenaClient, String queryExecutionId) throws InterruptedException {

        GetQueryExecutionRequest getQueryExecutionRequest = GetQueryExecutionRequest.builder()
                .queryExecutionId(queryExecutionId).build();

        GetQueryExecutionResponse getQueryExecutionResponse;

        boolean isQueryStillRunning = true;

        while (isQueryStillRunning) {
            getQueryExecutionResponse = athenaClient.getQueryExecution(getQueryExecutionRequest);
            String queryState = getQueryExecutionResponse.queryExecution().status().state().toString();

            if (queryState.equals(QueryExecutionState.FAILED.toString())) {
                throw new RuntimeException("Query Failed to run with Error Message: " + getQueryExecutionResponse
                        .queryExecution().status().stateChangeReason());
            } else if (queryState.equals(QueryExecutionState.CANCELLED.toString())) {
                throw new RuntimeException("Query was cancelled.");
            } else if (queryState.equals(QueryExecutionState.SUCCEEDED.toString())) {
                isQueryStillRunning = false;
            } else {
                Thread.sleep(SLEEP_AMOUNT_IN_MS);
            }

           // logger.info("Current Status is: " + queryState);
        }
        return isQueryStillRunning;
    }

    public static Response getTitle(int titleId) {
        AthenaClient athenaClient = AthenaClient.builder()
                .region(Region.CA_CENTRAL_1) // Specify the appropriate AWS region
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        
     //   String query = "SELECT * FROM mydb.title WHERE title_Id = " + titleId ;
        
        
        String query = "SELECT\n" +
                "    a.title_id,\n" +
                "    a.title_name,\n" +
                "    b.contact_id,\n" +
                "    b.first_name_pen,\n" +
                "    b.last_name_pen,\n" +
                "    c.contributor_sequence,\n" +
                "    c.contributor_role\n" +
                "FROM\n" +
                "    mydb.title a\n" +
                "INNER JOIN\n" +
                "    mydb.contributor c ON a.title_id = c.title_id\n" +
                "INNER JOIN\n" +
                "    mydb.contact b ON b.contact_id = c.contact_id\n" +
                "WHERE\n" +
                "    a.title_id ="+titleId;


        StartQueryExecutionRequest queryExecutionRequest = StartQueryExecutionRequest.builder()
                .queryString(query)
                .resultConfiguration(ResultConfiguration.builder()
                        .outputLocation("s3://my-athena-output-bucket/") //TODO
                        .build())
                .build();

        try {
            // Execute the query
            StartQueryExecutionResponse queryExecutionResponse = athenaClient.startQueryExecution(queryExecutionRequest);
            String queryExecutionId = queryExecutionResponse.queryExecutionId();

            // Wait for the query to complete
            waitForQueryToComplete(athenaClient,queryExecutionId);

            Response response= processResultRows(athenaClient,queryExecutionId);

            response.setMessage("Query successful. Add your response here.");
            return response;
        } catch (AthenaException e) {
            // Handle Athena query execution errors
            e.printStackTrace();
            Response response = new Response();
            response.setMessage("Query failed. Check Lambda logs for details.");
            return response;
        } catch (InterruptedException e) {
            // Handle Athena query execution errors
            e.printStackTrace();
            Response response = new Response();
            response.setMessage("Query failed. Check Lambda logs for details.");
            return response;
        } finally {
            // Close the Athena client
            athenaClient.close();
        }
    }

    private static Response processResultRows(AthenaClient athenaClient, String queryExecutionId) {
  Response response = new Response();
  
        GetQueryResultsRequest getQueryResultsRequest = GetQueryResultsRequest.builder()
                .queryExecutionId(queryExecutionId).build();

        GetQueryResultsIterable getQueryResultsResults = athenaClient.getQueryResultsPaginator(getQueryResultsRequest);
        List<Title> completeTitles=new ArrayList<>();
        for (GetQueryResultsResponse Resultresult : getQueryResultsResults) {
            List<ColumnInfo> columnInfoList = Resultresult.resultSet().resultSetMetadata().columnInfo();

            int resultSize = Resultresult.resultSet().rows().size();
           System.out.println("Result size: " + resultSize);

            List<Row> results = Resultresult.resultSet().rows();
            List<Title> titles= processRow(results, columnInfoList);
            completeTitles.addAll(titles);
        }
        
        response.setCompleteTitles(completeTitles);
        response.setMessage("success");
        System.out.println("response: " + response);
        return response;
    }

    private static List<Title> processRow(List<Row> rowList, List<ColumnInfo> columnInfoList) {

    	List<Title> titles = new ArrayList<>();
    	
        List<String> columns = new ArrayList<>();

        for (ColumnInfo columnInfo : columnInfoList) {
            columns.add(columnInfo.name());
        }

        for (Row row: rowList) {
        	
        	Title titleinfo = new Title();

            int index = 0;

            for (Datum datum : row.data()) {
                // Assuming columnInfoList order matches the POJO field order
                ColumnInfo columnInfo = columnInfoList.get(index);

                // Map and set the data to the corresponding field in the POJO
                if (columnInfo.name().equals("title_id")) {
                	
                	 
                	 if(!"title_id".equals(datum.varCharValue())) {
                	titleinfo.setTitleid(datum.varCharValue());
                	System.out.println("title: "+datum.varCharValue());
                	 }
                } else if (columnInfo.name().equals("title_name")) {
                	
                	titleinfo.setTitlename(datum.varCharValue());
                	 System.out.println("title name: "+datum.varCharValue());
                	 
                }else if (columnInfo.name().equals("contact_id")) {
                	
                	titleinfo.setContactId(datum.varCharValue());
                	 System.out.println("contact_id : "+datum.varCharValue());
                	 
                }else if (columnInfo.name().equals("first_name_pen")) {
                	
                	titleinfo.setFirstNamePen(datum.varCharValue());
                	 System.out.println("first_name_pen : "+datum.varCharValue());
                	 
                }else if (columnInfo.name().equals("last_name_pen")) {
                	
                	titleinfo.setLastNamePen(datum.varCharValue());
                	 System.out.println("last_name_pen : "+datum.varCharValue());
                	 
                }else if (columnInfo.name().equals("contributor_sequence")) {
                	
                	titleinfo.setContributorSequence(datum.varCharValue());
                	 System.out.println("contributor_sequence : "+datum.varCharValue());
                	 
                }else if (columnInfo.name().equals("contributor_role")) {
                	
                	titleinfo.setContributorRole(datum.varCharValue());
                	 System.out.println("contributor_role : "+datum.varCharValue());
                	 
                }
                 
               
                // Add more conditions for other columns
                index++;
            }
          
           // System.out.println("index "+index);
            
         // Add the titleinfo to the list only if at least one field is set
            if (titleinfo.getTitleid() != null) {
                titles.add(titleinfo);
            }
          
            //}

         //   logger.info("===================================");
        }
        return titles;
    }

}
