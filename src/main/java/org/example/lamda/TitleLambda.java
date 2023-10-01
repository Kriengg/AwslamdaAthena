package org.example.lamda;


import java.util.Map;

import org.example.lamda.models.Response;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class TitleLambda implements RequestHandler<Map<String, Object>, Response> {
    @Override
    public Response handleRequest(Map<String, Object> input, Context context) {
        String titleId = (String) input.get("titleId");
        if (titleId != null) {
            Response response = new Response();
            response.setMessage(String.format("Hello %s to AWS Lambda World!", titleId));
            return response;
        } else {
            // Handle the case when 'titleId' is not provided in the query parameters
            Response response = new Response();
            response.setMessage("Hello AWS Lambda World!");
            return response;
        }
    }
}

