package com.hangon.bean.map;


import java.util.List;

public class Status {

	private Result result;

	public Status() {
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Status{" +
				"result=" + result +
				'}';
	}
}
