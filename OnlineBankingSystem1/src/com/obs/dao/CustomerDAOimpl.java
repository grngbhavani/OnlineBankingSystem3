package com.obs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.obs.bean.CustomerBean;
import com.obs.bean.TransactionBean;
import com.obs.exception.CustomerException;
import com.obs.utility.DBUtil;

public class CustomerDAOimpl implements CustomerDAO {

	@Override
	public CustomerBean LoginCustomer(String username, String password, int accountno) throws CustomerException {
		
		CustomerBean cus = null;
		
		try(Connection conn = DBUtil.provideConnection()) {
			
		PreparedStatement ps= conn.prepareStatement("select * from InfoCustomer i inner join Account a on i.cid=a.cid where cname = ? AND cpass = ? AND cACno=?;" );			
			
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setInt(3, accountno);
			
			ResultSet rs= ps.executeQuery();
			
			
				if(rs.next()) {
				
				int ac=rs.getInt("cACno");	
					
				String n=rs.getString("cname");
				
				int b=rs.getInt("cbal");
				
				String e= rs.getString("cmail");
				
				String p= rs.getString("cpass");
				
				String m=rs.getString("cmob");
				
				String ad=rs.getString("cadd");
				
			cus=new CustomerBean(ac,n,b,e,p,m,ad);	
				
				
			}else {
				throw new CustomerException("Invalid Username or password....Try Again!");
			}
			
		} catch (SQLException e) {
			throw new CustomerException(e.getMessage());
		}
		
		
		return cus;
	}

	
	@Override
	public int viewBalance(int cACno) throws CustomerException {
		int b=-1;
		
		try(Connection conn = DBUtil.provideConnection()) {
			
			PreparedStatement ps= conn.prepareStatement("Select cbal from Account where cACno = ?;" );			
				
				ps.setInt(1, cACno);
				
				ResultSet rs= ps.executeQuery();
				
				if(rs.next()) {
					b=rs.getInt("cbal");
				}
			
					
			} catch (SQLException e) {
				throw new CustomerException(e.getMessage());
			}
		return b;
	}
	
	
	
	
	@Override
	public int Deposit(int cACno, int amount) throws CustomerException {
	    int b = -1;

	    try (Connection conn = DBUtil.provideConnection()) {
	        // Update account balance
	        PreparedStatement ps = conn.prepareStatement("update Account set cbal = cbal + ? where cACno = ?;");
	        ps.setInt(1, amount);
	        ps.setInt(2, cACno);
	        int rs = ps.executeUpdate();

	        if (rs > 0) {
	            // Insert a new transaction record
	            PreparedStatement ps2 = conn.prepareStatement("insert into Transaction (cACno, deposit, withdraw) values (?, ?, 0);");
	            ps2.setInt(1, cACno);
	            ps2.setInt(2, amount);
	            int rs2 = ps2.executeUpdate();

	            if (rs2 > 0) {
	                b = 1; // Success
	            } else {
	                throw new CustomerException("Error inserting transaction record");
	            }
	        } else {
	            throw new CustomerException("Account not found");
	        }

	    } catch (SQLException e) {
	        // Handle SQLException
	        throw new CustomerException(e.getMessage());
	    }

	    return b;
	}


	@Override
	public int Withdraw(int cACno, int amount) throws CustomerException {
	    int vb = viewBalance(cACno);

	    if (vb >= amount) {
	        try (Connection conn = DBUtil.provideConnection()) {
	            // Update account balance
	            PreparedStatement ps = conn.prepareStatement("update Account set cbal = cbal - ? where cACno = ?;");
	            ps.setInt(1, amount);
	            ps.setInt(2, cACno);
	            int rs = ps.executeUpdate();

	            if (rs > 0) {
	                // Insert a new transaction record for withdrawal
	                PreparedStatement ps2 = conn.prepareStatement("insert into Transaction (cACno, deposit, withdraw) values (?, 0, ?);");
	                ps2.setInt(1, cACno);
	                ps2.setInt(2, amount);
	                int rs2 = ps2.executeUpdate();

	                if (rs2 > 0) {
	                    System.out.println("Withdrawal successful");
	                } else {
	                    throw new CustomerException("Error inserting withdrawal transaction record");
	                }
	            } else {
	                System.out.println("Account not found");
	            }
	        } catch (SQLException e) {
	            // Handle SQLException
	            throw new CustomerException(e.getMessage());
	        }
	    } else {
	        System.out.println("Insufficient Balance");
	    }

	    return vb - amount;
	}

	@Override
	public int Transfer(int cACno, int amount, int cACno2) throws CustomerException {
		
		int vb=viewBalance(cACno);
		
		if(vb>amount && checkAccount(cACno2)) {
			
			int wid=Withdraw(cACno, amount);
			int dep=Deposit(cACno2, amount);
			
			
		}else {
			System.out.println("Insufficient Balance");
		}
		
		return 0;
	}
	
	private boolean checkAccount(int cACno) {
		
		try(Connection conn = DBUtil.provideConnection()) {
			PreparedStatement ps=conn.prepareStatement("select * from Account where cACno=?;");
			
			ps.setInt(1, cACno);
			
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()) {
				return true;
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
		
	}


	@Override
	public List<TransactionBean> viewTransaction(int cACno) throws CustomerException {
	    List<TransactionBean> li = new ArrayList<>();

	    try (Connection conn = DBUtil.provideConnection()) {

	        PreparedStatement ps = conn.prepareStatement("select * from transaction where cACno=?;");
	        ps.setInt(1, cACno);

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            TransactionBean transaction = new TransactionBean();
	            transaction.setAccountNo(rs.getInt("cACno"));
	            transaction.setDeposit(rs.getInt("deposit"));
	            transaction.setWithdraw(rs.getInt("withdraw"));

	            li.add(transaction);
	        }

	        if (li.size() == 0) {
	            System.out.println("No Transaction Found");
	        } else {
	        	System.out.println("------TRANSACTION HISTORY-------");
				System.out.println("Account No.: " + li.get(0).getAccountNo());
				
				li.forEach(v->{
					System.out.println("----------------------------------------------------");
					if(v.getDeposit()!=0)
						System.out.println("Amount Credit: "+ v.getDeposit());
					if(v.getWithdraw()!=0)
						System.out.println("Amount Debit : "+ v.getWithdraw());
					
				});
	        }

	    } catch (SQLException e) {
	  
	    }

	    return li;
	}

	


	

}