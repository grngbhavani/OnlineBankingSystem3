package com.obs.exception;


	

	public class AccountException extends Exception {

	    // Default constructor
	    public AccountException() {
	        super();
	    }

	    // Constructor with a custom error message
	    public AccountException(String message) {
	        super(message);
	    }
	}

