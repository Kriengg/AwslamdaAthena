package org.example.lamda.models;

import java.util.List;

public class Response {

    private String message;
    private List<Title> completeTitles;

    public List<Title> getCompleteTitles() {
		return completeTitles;
	}

	public void setCompleteTitles(List<Title> completeTitles) {
		this.completeTitles = completeTitles;
	}

	public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Response{");
        sb.append("message='").append(message).append('\'');
        sb.append(", completeTitles=").append(completeTitles);
        sb.append('}');
        return sb.toString();
    }
}