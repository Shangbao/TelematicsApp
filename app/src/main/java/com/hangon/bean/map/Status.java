package com.hangon.bean.map;


import java.util.List;

public class Status {
	private String resultcode;
	private String reason;
	private Result result;
	private String error_code;

	public String getResultcode() {
		return resultcode;
	}

	public void setResultcode(String resultcode) {
		this.resultcode = resultcode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}



	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "Status [resultcode=" + resultcode + ", reason=" + reason
				+ ", result=" + result + ", error_code=" + error_code + "]";
	}

}
