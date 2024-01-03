package com.obs.bean;

	import java.sql.Timestamp;

	public class TransactionBean {
		
		private int accountNo;
		private int deposit;
		private int withdraw;
		
		
		

		public TransactionBean(int accountNo, int deposit, int withdraw) {
			super();
			this.accountNo = accountNo;
			this.deposit = deposit;
			this.withdraw = withdraw;
			
		}

		public TransactionBean() {
			super();
			// TODO Auto-generated constructor stub
		}

		public int getAccountNo() {
			return accountNo;
		}

		public void setAccountNo(int accountNo) {
			this.accountNo = accountNo;
		}

		public int getDeposit() {
			return deposit;
		}

		public void setDeposit(int deposit) {
			this.deposit = deposit;
		}

		public int getWithdraw() {
			return withdraw;
		}

		public void setWithdraw(int withdraw) {
			this.withdraw = withdraw;
		}
		
		
	}

