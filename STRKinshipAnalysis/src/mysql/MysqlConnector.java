package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import obj.*;
import tools.AlleleFreqCal;
import tools.ObjManager;

public final class MysqlConnector {
	
	private Connection connect = null;

	private static MysqlConnector instance;
	
	private MysqlConnector() {};
	
	public static MysqlConnector getInstance() {
		if (instance == null) {
            synchronized (MysqlConnector.class) {
                if (instance == null) {
                    instance = new MysqlConnector();
                    instance.createConnection();
                }
            }
        }
        return instance;
	}
	
	public Connection getConnection() {
		if(connect == null) {
			if(createConnection()) {
				return connect;
			}else {
				System.out.println("No return Connection");
				return null;
			}
		}else {
			return connect;
		}
	}
	
	private boolean createConnection() {	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect =  DriverManager.getConnection("jdbc:mysql://localhost/strdb" +
					"?user=root&password=aom1234&useSSL=false");

			if(connect != null){
				System.out.println("Database Connected.");
				return true;
			} else {
				System.out.println("Database Connect Failed.");
				return false;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean queryAlleleForCalculateFreq() {
		
		AlleleFreqCal cal = AlleleFreqCal.getInstance();
		String sql = "SELECT region,locus,genotype1,genotype2\r\n" + 
				"FROM strdb.forenseq JOIN strdb.person_information\r\n" + 
				"ON strdb.forenseq.Sample_ID = strdb.person_information.Sample_ID;";
		
		try {
			Statement statement = getConnection().createStatement();
			ResultSet rec = statement.executeQuery(sql);
			
			while((rec!=null) && (rec.next())){
				cal.addData(rec.getString("region"), rec.getString("locus"), rec.getFloat("genotype1"));
				cal.addData(rec.getString("region"), rec.getString("locus"), rec.getFloat("genotype2"));
            }
			return true;
			
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	public void updateFreq(Float freq,String region,String locus,Float allele) {
		String sqlCheck = "SELECT freq FROM strdb.freq where regionname=\""
				+ region + "\" and alleleid=\""+ locus+ "\" and allele="+ allele;
		String sqlInsert = "insert into freq(regionname,alleleid,allele,freq) value(\""+region
				+ "\",\""+locus+ "\" ,"+allele+ " ,"+freq+ ")";
		
		String sqlUpdate ="update strdb.freq set freq = "+freq+ " where regionname=\""+region
			+ "\" and alleleid= \""+locus+ "\"and allele=  "+allele;
	    
		try {
			Statement statement = getConnection().createStatement();
			ResultSet rec = statement.executeQuery(sqlCheck);
			if(!rec.next()) {
				statement.executeUpdate(sqlInsert);
			}else{
				statement.executeUpdate(sqlUpdate);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    System.out.println("failed");
		}
	}
	
	public void queryRegion(String regionName) {
		
		Region region = new Region();
		region.setRegionName(regionName);
		ObjManager.getInstance().insertRegion(region);
		String sql = "SELECT AlleleID,Allele,Freq FROM strdb.freq WHERE RegionName=\""+regionName+"\";";
		
		try {
			Statement statement = connect.createStatement();
			ResultSet rec = statement.executeQuery(sql);
			
			while((rec!=null) && (rec.next())){
				AlleleFreq newFreq = new AlleleFreq();
				newFreq.setRegionName(regionName);
				newFreq.setLocus(rec.getString("AlleleID"));
                newFreq.setAllele(rec.getFloat("Allele"));
                newFreq.setFreq(rec.getFloat("Freq"));
                ObjManager.getInstance().updateRegion(newFreq);
            }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public PersonInfo queryPerson(Connection connect,String sampleID) {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setSampleID(sampleID);
		ObjManager.getInstance().insertPersonInfo(personInfo);
		String sql1 = "SELECT Sample_ID,Region,Name FROM strdb.person_information WHERE Sample_ID=\""+sampleID+"\";";
		String sql2 = "SELECT Sample_ID,locus,genotype1,genotype2 FROM strdb.forenseq WHERE Sample_ID=\""+sampleID+"\";";
		
		try {
			Statement statement = connect.createStatement();
			ResultSet rec = statement.executeQuery(sql1);
			
			while((rec!=null) && (rec.next())){
				String region = rec.getString("Region");
				String name = rec.getString("Name");
				personInfo.setRegion(region);
				personInfo.setName(name);
				queryRegion(region);
            }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Statement statement = connect.createStatement();
			ResultSet rec = statement.executeQuery(sql2);
			
			while((rec!=null) && (rec.next())){
				StrData data = new StrData();
				data.setSampleID(sampleID);
				data.setLocus(rec.getString("locus"));
				data.setGenotype1(Float.valueOf(rec.getString("genotype1")));
				data.setGenotype2(Float.valueOf(rec.getString("genotype2")));
				ObjManager.getInstance().updatePersonInfo(data);
            }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return personInfo;
	}
	
	public void closeConnection(Connection connect) {
		try {
			if(connect != null){
				connect.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
