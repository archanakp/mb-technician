package com.battmobile.battmobiletechnician.chat.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ChatListPojo{

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("count")
	private int count;

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ChatListPojo{" + 
			"data = '" + data + '\'' + 
			",count = '" + count + '\'' + 
			",error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}