package com.arcticlord.jkav.db;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import com.arcticlord.jkav.data.AddressData;
import com.arcticlord.jkav.data.ContactData;
import com.arcticlord.jkav.data.CustomerData;
import com.arcticlord.jkav.data.JobData;
import com.arcticlord.jkav.utils.Settings;

public class DBController{
	
	
	// datatypes
	private static final int IGNORE = 	0;
	private static final int STRING =	1;
	private static final int INTEGER =	2;
	private static final int BOOLEAN = 	3;
	private static final int DOUBLE = 	4;
	private static final int DATE = 	5;
	
	private static final String	  DBURL 	 = "jdbc:mysql://localhost:3306/jkav";
	private static final String	  CONTROLURL = "jdbc:mysql://localhost:3306";
	private static final String[] TABLENAMES = {	"customer",
													"address",
													"contactinfo",
													"job"	};
	private Settings settings;
		
	public DBController(Settings settings){
		this.settings = settings;
	}

	private ArrayList<Object[]> runQuery(String cmd, int[] datatypes, boolean control){

		ArrayList<Object[]> result = null;
		ResultSet res = null;
		
		try{
			Statement stmt;	
			Class.forName("com.mysql.jdbc.Driver"); 
			String url;
			if(control) url = CONTROLURL;
			else url = DBURL;
			
			Connection con = DriverManager.getConnection(	url,
															settings.username,
															settings.password );
			
			if(con == null) return null;
			/*
			DatabaseMetaData meta = con.getMetaData();
	       System.out.println(”Driver Name : “+meta.getDriverName());
	       System.out.println(”Driver Version : “+meta.getDriverVersion());
			 */
			
			stmt = con.createStatement();
			res = stmt.executeQuery( cmd );
		
			result = createResult(res, datatypes);
			
			con.close();
			
		} catch(Exception e){e.printStackTrace(); }// todo result mit fehler bauen}
		
		return result;
	}
	
	private ArrayList<Object[]> runControlQuery(String cmd){
		return runQuery(cmd, new int[]{STRING}, true);
	}

	
	private ArrayList<Object[]> runDBQuery(String cmd, int[] datatypes){
		return runQuery(cmd, datatypes, false);
	}
	
	/**
	 * Führt ein SQL INSERT oder UPDATE Query durch
	 * @param cmd		der Aufzurufende SQL Query
	 * @param control	Auf Control- oder User Level ausführen
	 * @return			liefert -1 bei einem Fehler oder
	 * 					die generierte ID des neuen Eintrags
	 */
	private int runUpdate(String cmd, boolean control){
			
		int result = -1;
		try{
			Statement stmt;         
	
			Class.forName("com.mysql.jdbc.Driver"); 
			String url;
			if(control) url = CONTROLURL;
			else url = DBURL;
			Connection con = DriverManager.getConnection(	url,
															settings.username,
															settings.password);
			
			if(con == null) return -1;

			stmt = con.createStatement();
			int affectedRows = stmt.executeUpdate( cmd, Statement.RETURN_GENERATED_KEYS );
	
	 		if (affectedRows == 1){
	 			int lastKey = 0;
	 			ResultSet keys = stmt.getGeneratedKeys();
	 			while (keys.next())
	 				lastKey = keys.getInt(1);
	 			result = lastKey;
	 		}
	 
			con.close();
			
		} catch(Exception e){e.printStackTrace(); return -1;}
		return result;
	}
	
	private int runDBUpdate(String cmd){
		return runUpdate(cmd, false);
	}
	private int runControlUpdate(String cmd){
		return runUpdate(cmd, true);
	}	
	private ArrayList<Object[]> createResult(ResultSet res, int[] datatypes) throws SQLException{
		ArrayList<Object[]> ret = new ArrayList<Object[]>();
	 	while (res.next()) {
	 		int numColumns = res.getMetaData().getColumnCount();
	 		if(numColumns != datatypes.length){System.out.println("falscher aufruf"); return null;}
	 		Object[] entry = new Object[numColumns];

	 		for(int i = 0; i < numColumns; i++){
	 			switch(datatypes[i]){
	 				case IGNORE:
	 					entry[i] = null;
	 					break;
	 				case STRING:
	 					entry[i] = res.getString(i + 1);
	 					break;
	 				case INTEGER:
	 					entry[i] = res.getInt(i + 1);
	 					break;
	 				case BOOLEAN:
	 					entry[i] = res.getBoolean(i + 1);
	 					break;
	 				case DOUBLE:
	 					entry[i] = res.getDouble(i + 1);
	 					break;
	 				case DATE:
	 					entry[i] = res.getDate(i + 1);
	 					break;
	 				default: 
	 					System.out.println("wrong datatype: " + datatypes[i]);
	 					entry[i] = null;		
	 			}
	 		}
	 		ret.add(entry);
	 	}
		return ret;
	}
	
	private MySQLEnum extractEnums(String enum_type){
	    int position = 0;
	    int id_counter = 1;
	    MySQLEnum enums  = new MySQLEnum();
	    
	    while ((position = enum_type.indexOf("'", position)) > 0) {
	    	int secondPosition = enum_type.indexOf("'", position + 1);
	        enums.add(id_counter++, enum_type.substring(position + 1,
	            secondPosition));
	        position = secondPosition + 1;
	    }
	    return enums;
	}
	
	public String getCustomerName(int id){
		String ret = "";
		try{
			ArrayList<Object[]> res = runDBQuery(	"SELECT forename,name " +
													"FROM customer " +
													"WHERE cid = \'"+id+"\'",
													new int[]{STRING,STRING});
			ret = res.get(0)[0] + " " + res.get(0)[1]; 
		} catch( Exception e){}
		return ret;
	}
	
	public ArrayList<Object[]> getCustomerList(){
		return runDBQuery(  "SELECT cid,company,forename,name FROM customer",
							new int[] {INTEGER,STRING,STRING,STRING});
	}
	
	public ArrayList<Object[]> searchCustomerByPersonal(String toSearch){
		return runDBQuery(  "SELECT cid,company,forename,name FROM customer " +
							"WHERE	 company like \"%"+toSearch+"%\" OR " +
									"forename like \"%"+toSearch+"%\" OR " +
									"name like \"%"+toSearch+"%\" OR " +
									"other like \"%"+toSearch+"%\" " +
									"GROUP BY cid",
							new int[] {INTEGER,STRING,STRING,STRING});
	}
	
	public ArrayList<Object[]> searchCustomerByContact(String toSearch){
		// manipulate toSearch String
		char[] orig = toSearch.toCharArray();
		StringBuilder newStr = new StringBuilder();
		newStr.append('%');
		for(char c : orig){
			newStr.append(c);
			newStr.append('%');
		}
		return runDBQuery(  "SELECT cid,company,forename,name FROM customer " +
							"INNER JOIN contactInfo ON customer.cid=contactInfo.customer " +
							"WHERE data like \""+newStr.toString()+"\" " +
							"GROUP BY cid",
							new int[] {INTEGER,STRING,STRING,STRING});
	}

	public ArrayList<Object[]> searchCustomerByAddress(String toSearch){
		return runDBQuery(  "SELECT cid,company,forename,name FROM customer " +
							"INNER JOIN address ON customer.cid=address.customer " +
							"WHERE	 street like \"%"+toSearch+"%\" OR " +
									"housenumber like \"%"+toSearch+"%\" OR " +
									"plz like \"%"+toSearch+"%\" OR " +
									"city like \"%"+toSearch+"%\" " +
									"GROUP BY cid",
							new int[] {INTEGER,STRING,STRING,STRING});
	}
	public ArrayList<Object[]> searchCustomerByJob(String toSearch){
		return runDBQuery(  "SELECT cid,company,forename,name FROM customer " +
							"INNER JOIN job ON customer.cid=job.customer " +
							"WHERE	 job.title like \"%"+toSearch+"%\" OR " +
									"job.description like \"%"+toSearch+"%\" " +
							"GROUP BY cid",
							new int[] {INTEGER,STRING,STRING,STRING});
	}
	
	public MySQLEnum getJobStateEnum(){
		ArrayList<Object[]> res = runDBQuery(  "SHOW COLUMNS FROM job LIKE \"state\"",
				new int[] {IGNORE,STRING,IGNORE,IGNORE,IGNORE,IGNORE});
		return extractEnums((String)res.get(0)[1]);
	}

	public MySQLEnum getTitleEnum(){
		ArrayList<Object[]> res = runDBQuery(  "SHOW COLUMNS FROM customer LIKE \"title\"",
				new int[] {IGNORE,STRING,IGNORE,IGNORE,IGNORE,IGNORE});
		return extractEnums((String)res.get(0)[1]);
	}

	public ArrayList<Object[]> getCustomerJobsList(int customerId){
		return runDBQuery(  "SELECT jid,title,price,state+0 " +
							"FROM job " +
							"WHERE customer = \'"+customerId+"\' " +
							"ORDER BY jid ASC",
							new int[] {INTEGER,STRING,DOUBLE,INTEGER});
	}
	
	public CustomerData getCustomerData(int id){
		Object[] customer = runDBQuery(	"SELECT company,title+0,forename,name,other " +
										"FROM customer " +
										"WHERE cid = \'"+id+"\'",
										new int[]{	STRING,INTEGER,STRING,STRING,STRING}).get(0);
		ArrayList<Object[]> address = runDBQuery(	"SELECT aid,street,housenumber,plz,city " +
													"FROM address " +
													"WHERE customer = \'"+id+"\'",
										new int[]{	INTEGER,STRING,STRING,STRING,STRING});
		ArrayList<Object[]> contact = runDBQuery(	"SELECT iid,type+0,data " +
													"FROM contactinfo " +
													"WHERE customer = \'"+id+"\'",
										new int[]{	INTEGER,INTEGER,STRING});
		
// TODO: möglicherweise null exception abfangen bei entry
		CustomerData res = new CustomerData();
			res.cid = id;
			res.company = (String)customer[0];
			res.title = (Integer)customer[1];
			res.forename = (String)customer[2];
			res.name = (String)customer[3];
			res.other = (String)customer[4];
			
		for(Object[] ol : address){
			AddressData ad = new AddressData();
			ad.aid = (Integer) ol[0];
			ad.street = (String) ol[1];
			ad.housenumber = (String) ol[2];
			ad.plz = (String) ol[3];
			ad.city = (String) ol[4];
			res.address.add(ad);
		}
		
		for(Object[] ol : contact){
			ContactData cd = new ContactData();
			cd.iid = (Integer) ol[0];
			cd.type = (Integer) ol[1];
			cd.data = (String) ol[2];
			res.contact.add(cd);
		}
		return res;
	}
	
/*
 * SELECT table_name, table_rows
    FROM INFORMATION_SCHEMA.TABLES
    WHERE TABLE_SCHEMA = '<your db>';
*/
	public JobData getJobData(int id){
		Object[] entry = runDBQuery(	"SELECT jid," +
												"customer," +
												"title," +
												"price," +
												"state+0," +
												"beginDate," +
												"endDate," +
												"description " +
										"FROM job " +
										"WHERE jid = \'"+id+"\'",
										new int[]{	INTEGER,INTEGER,STRING,DOUBLE,
													INTEGER,DATE,DATE,STRING }).get(0);
		JobData res = new JobData();
			res.jid = (Integer)entry[0];
			res.customerId = (Integer)entry[1];
			res.title = (String)entry[2];
			res.price = (Double)entry[3];
			res.state = (Integer)entry[4];
			res.beginDate = (Date)entry[5];
			res.endDate = (Date)entry[6];
			res.describtion = (String)entry[7];
		return res;
	}
	
	public boolean storeCustomerData(CustomerData data){
		
		// store customer Table Content
		// new customer
		if(data.cid == -1){
			int res = runDBUpdate(	"INSERT INTO CUSTOMER (company,title,forename,name,other) " +
									"VALUES (" +
									"\""+data.company+"\"," +
									"\""+data.title+"\"," +
									"\""+data.forename+"\"," +
									"\""+data.name+"\"," +
									"\""+data.other+"\")");
			if(res  == -1) return false;
			else data.cid = res;
		}
		// existing customer
		else{
			int res = runDBUpdate(	"UPDATE CUSTOMER SET " +
									"company=\"" + data.company + "\"," +
									"title=\"" + data.title + "\"," +
									"forename=\"" + data.forename + "\"," +
									"name=\"" + data.name + "\"," +
									"other=\"" + data.other + "\" " +
									"WHERE  cid = \'" + data.cid + "\'");
			if (res == -1) return false;
		}
		// addreses
		for( Iterator<AddressData> it = data.address.iterator(); it.hasNext();){
			AddressData adr = it.next();
			// valid address entry -> store
			if(adr.valid){
				// new Address
				if(adr.aid == -1){
					int res = runDBUpdate(	"INSERT INTO ADDRESS (customer,street,housenumber,plz,city) " +
											"VALUES (" +
											"\""+data.cid+"\","+
											"\""+adr.street+"\"," +
											"\""+adr.housenumber+"\"," +
											"\""+adr.plz+"\"," +
											"\""+adr.city+"\")");
					if(res  == -1) return false;
					else adr.aid = res;
				}
				// existing address
				else{
					int res = runDBUpdate(	"UPDATE ADDRESS SET " +
											"street=\"" + adr.street + "\"," +
											"housenumber=\"" + adr.housenumber + "\"," +
											"plz=\"" + adr.plz + "\"," +
											"city=\"" + adr.city + "\" " +
											"WHERE  aid = \'" + adr.aid+ "\'");
					if (res == -1) return false;
				}
			}
			// not valid, not new address entry -> delete
			else {
				if(adr.aid != -1){
					int res = runDBUpdate(	"DELETE FROM ADDRESS " +
											"WHERE aid=\'" + adr.aid + "\'");
					if(res == -1) return false;
				}
				// delete non valid entries from customer Data Structure
				it.remove();			
			}
		}
		// contacts
		for( Iterator<ContactData> it = data.contact.iterator(); it.hasNext();){
			ContactData con = it.next();
			// valid contact entry -> store
			if(con.valid){
				// new Contact
				if(con.iid == -1){
					int res = runDBUpdate(	"INSERT INTO CONTACTINFO (customer,type,data) " +
											"VALUES (" +
											"\""+data.cid+"\","+
											"\""+con.type+"\"," +
											"\""+con.data+"\")");
					if(res  == -1) return false;
					else con.iid = res;
				}
				// existing contact
				else{
					int res = runDBUpdate(	"UPDATE CONTACTINFO SET " +
											"data=\"" + con.data + "\" " +
											"WHERE  iid = \'" + con.iid+ "\'");
					if (res == -1) return false;
				}
			}
			// not valid, not new contact entry -> delete
			else {
				if(con.iid != -1){
					int res = runDBUpdate(	"DELETE FROM CONTACTINFO " +
											"WHERE iid=\'" + con.iid + "\'");
					if(res == -1) return false;
				}
				// delete non valid entries from customer Data Structure
				it.remove();
			}
		}
			
		return true;
	}
	
	public boolean storeJobData(JobData data){
		if(data.jid == -1){
			// new Job
			int res = runDBUpdate(	"INSERT INTO JOB (customer,title,beginDate,endDate," +
									"price,state,description) values (" +
									"\""+data.customerId+"\"," +
									"\""+data.title+"\"," +
									"\""+JobData.convertDate(data.beginDate, JobData.DATE_STYLE_SQL)+"\"," +
									"\""+JobData.convertDate(data.endDate, JobData.DATE_STYLE_SQL)+"\"," +
									"\""+data.price+"\"," +
									"\""+data.state+"\"," +
									"\""+data.describtion+"\")");
			if(res  == -1) return false;
			else data.jid = res;
		}
		else {
			int res = runDBUpdate(	"UPDATE JOB SET " +
					"title=\""+data.title+"\"," +
					"beginDate=\""+JobData.convertDate(data.beginDate, JobData.DATE_STYLE_SQL)+"\"," +
					"endDate=\""+JobData.convertDate(data.endDate, JobData.DATE_STYLE_SQL)+"\"," +
					"price=\""+data.price+"\"," +
					"description=\""+data.describtion+"\" " +
					"WHERE  jid = \'" + data.jid + "\'");
			if(res == -1) return false;
		}
		return true;
	}

	public boolean changeJobState(int id, int newState){
		int res = runDBUpdate(	"UPDATE JOB SET " +
								"state=\""+newState+"\"" +
								"WHERE jid=\'" + id + "\'");
		if(res == -1) return false;
		return true;
	}
	
	public boolean removeCustomerData(int id){
		int res = runDBUpdate(	"DELETE FROM CUSTOMER " +
								"WHERE cid=\'" + id + "\'");
		if(res == -1) return false;
		return true;
	}
	
	public boolean removeJobData(int id){
		int res = runDBUpdate(	"DELETE FROM JOB " +
				"WHERE jid=\'" + id + "\'");
		if(res == -1) return false;
		return true;
	}
	
	public boolean checkDatabase(){
		boolean result = false;
		ArrayList<Object[]> res = runControlQuery("SHOW DATABASES");
		if(res == null) return false;
		for(Object[] o: res){
			if(((String)o[0]).compareTo("jkav") == 0) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	// einzelnde tabellen checken
	// show columns from customer;
	
	public boolean checkDatabaseScheme(){
		boolean tableFound = false;
		ArrayList<Object[]> res = runDBQuery("SHOW TABLES", new int[]{STRING});
		for(String s: TABLENAMES){
			tableFound = false;
			for(Object[] o: res){
				if(((String)o[0]).compareTo(s) == 0) {
					tableFound = true;
					break;
				}
			}
			if(tableFound != true ) return false;
		}
		return true;
	}
	
	public void createDatabaseScheme(){
		runControlUpdate("CREATE DATABASE jkav");
		
		runDBUpdate("CREATE TABLE customer ("+
				  "cid INTEGER AUTO_INCREMENT," +
				  "company TEXT,"+
				  "title ENUM('Herr', 'Herr Dr.', 'Herr Prof. Dr.', 'Frau', 'Frau Dr.', 'Frau Prof. Dr.') NOT NULL,"+
				  "forename TEXT,"+
				  "name TEXT,"+
				  "other TEXT,"+
				  "PRIMARY KEY(cid)" +
				  ")");
		runDBUpdate("CREATE TABLE address ("+
				  "aid INTEGER AUTO_INCREMENT," +
				  "customer INTEGER NOT NULL,"+
				  "street TEXT,"+
				  "housenumber TEXT,"+
				  "plz TEXT,"+
				  "city TEXT,"+
				  "PRIMARY KEY(aid)," +
				  "FOREIGN KEY (customer) REFERENCES customer(cid) ON DELETE CASCADE" +
				  ")");
		runDBUpdate("CREATE TABLE contactinfo ("+
				  "iid INTEGER AUTO_INCREMENT," +
				  "customer INTEGER NOT NULL,"+
				  "type ENUM('phone','fax','email') NOT NULL,"+
				  "data TEXT,"+
				  "PRIMARY KEY(iid),"+
				  "FOREIGN KEY (customer) REFERENCES customer(cid) ON DELETE CASCADE" +
				  ")");
		runDBUpdate("CREATE TABLE job ("+
				  "jid INTEGER AUTO_INCREMENT," +
				  "customer INTEGER NOT NULL,"+
				  "title TEXT," +
				  "beginDate DATE,"+
				  "endDate DATE,"+
				  "price DOUBLE,"+
				  "state ENUM('pending','accepted','rejected') NOT NULL,"+
				  "description text,"+
				  "PRIMARY KEY(jid)," +
				  "FOREIGN KEY (customer) REFERENCES customer(cid) ON DELETE CASCADE" +
				  ")");
	}
}