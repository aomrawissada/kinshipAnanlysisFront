package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import analysis.NoParentTest;
import mysql.MysqlConnector;
import tools.AlleleFreqCal;
import tools.ObjManager;
import obj.*;
import result.OneParentTestResult;
import server.Server;

public class Main {

	public static void main(String[] args) {
		updateFreq();
		getMode();
	}
	
	private static void getMode() {
		System.out.print("1 : Insert filename\n");
		System.out.print("2 : Insert Input by keyboard\n");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		int mode=0;
		try{
			mode = Integer.parseInt(input);
		}catch (NumberFormatException e) {
			System.out.println("INVALID TYPE");
			System.out.println("-------------");
			getMode();
		}
		if(mode==1) {
			System.out.println("FILE NAME: ");
			//C:\fileProject\test.txt
			String filename = scanner.next();
			ArrayList<String> records = new ArrayList<String>();
				try{
					BufferedReader reader = new BufferedReader(new FileReader(filename));
					String line;
					while ((line = reader.readLine()) != null){
						records.add(line);
					}
					reader.close();
				}catch (Exception e){
					System.out.println("INVALID FILENAME");
					getMode();
				}
			startMode1(records);
		}else if(mode==2) {
			getInput();
			startMode2();
		}else{
			System.out.println("INVALID TYPE");
			System.out.println("-------------");
			getMode();
		}
	}
	
	private static void updateFreq() {
		System.out.println("Updating allele frequency...");
		AlleleFreqCal.getInstance().updateFreq();
	}

	private static void getInput() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("TYPE \n1 One Parent\n2 Two Parent\n");
		System.out.println("TYPE : ");
		String input = scanner.next();
		int type=0;
		try{
			type = Integer.parseInt(input);
		}catch (NumberFormatException e) {
			System.out.println("INVALID TYPE");
			System.out.println("-------------");
			getInput();
		}
		if(type==1) {
			ObjManager.getInstance().setType(type);
			getType1(scanner);
		}else if(type==2) {
			ObjManager.getInstance().setType(type);
			getType2(scanner);
		}else{
			System.out.println("INVALID TYPE");
			System.out.println("-------------");
			getInput();
		}
	}
	
	private static void getType1(Scanner scanner) {
		ObjManager om = ObjManager.getInstance();
		System.out.println("Parent's sample ID >> ");
		String input = scanner.next();
		om.setParentID(input);
		System.out.println("Child's sample ID >> ");
		String input2 = scanner.next();
		om.setChildID(input2);
		System.out.println("Type 1 | Parent "+om.getParentID()+" | Child "+om.getChildID());
		System.out.println(" 1 Edit type");
		System.out.println(" 2 Edit ID");
		System.out.println(" 3 Confirm");
		int confirm = scanner.nextInt();
		if(confirm==1) {
			getInput();
		}else if(confirm==2) {
			getType1(scanner);
		}else {
			
		}
	}
	
	private static void getType2(Scanner scanner) {
		ObjManager om = ObjManager.getInstance();
		System.out.println("First parent's sample ID >> ");
		String input = scanner.next();
		om.setParentID(input);
		System.out.println("Second parent's sample ID >> ");
		String input2 = scanner.next();
		om.setParentID2(input2);
		System.out.println("Child's sample ID >> ");
		String input3 = scanner.next();
		om.setChildID(input3);
		System.out.println("Type 2 | Parent 1 "+om.getParentID()+" | Parent 2 "+om.getParentID2()+" | Child "+om.getChildID());
		System.out.println(" 1 Edit type");
		System.out.println(" 2 Edit ID");
		System.out.println(" 3 Confirm");
		int confirm = scanner.nextInt();
		if(confirm==1) {
			getInput();
		}else if(confirm==2) {
			getType2(scanner);
		}else {
			
		}
	}

	private static void startMode1(ArrayList<String> records) {
		ObjManager manager = ObjManager.getInstance();
		String[] head = records.get(0).split("-");
		int type = 1;//Integer.parseInt(head[0]);
		String parent , child , parent2 , region;
		parent = head[1];
		child = head[2];
		parent2 = head[3];
		region = head[head.length-1];
		MysqlConnector.getInstance().queryRegion(region);
		records.remove(0);
		if(type==1) {
			Map<String, Float> piList = NoParentTest.getInstance().getPiList(records,region);
			OneParentTestResult result = new OneParentTestResult(parent,child,piList);
			System.out.println(result.getString());
		}else if (type==2) {
			
		}
		
	}
	
	private static void startMode2() {
		ObjManager manager = ObjManager.getInstance();
		MysqlConnector connector = MysqlConnector.getInstance();
		Connection connect = connector.getConnection();
		PersonInfo parent,child,parent2;
		
		if(connect==null) {
			System.out.println("No Connection");
		}else {
			if(manager.getType()==1) {
				parent = connector.queryPerson(connect, manager.getParentID());
				child = connector.queryPerson(connect, manager.getChildID());
				
				printAllData();
				
				NoParentTest test = NoParentTest.getInstance();
				Map<String, Float> piList = test.getPiList(parent, child);
				
				OneParentTestResult result = new OneParentTestResult(parent.getName(),child.getName(),piList);
				System.out.println(result.getString());
				
			}else if(manager.getType()==2) {
				parent = connector.queryPerson(connect, manager.getParentID());
				parent2 = connector.queryPerson(connect, manager.getParentID2());
				child = connector.queryPerson(connect, manager.getChildID());
				
				printAllData();
				
				
			}
		}
	}
	
	private static void printAllData() {
		ObjManager manager = ObjManager.getInstance();
		
		System.out.println("--- PERSON INFORMATION ---");
		for(PersonInfo p : manager.getPersons()) {
			for(StrData s : p.getStrList()) {
				System.out.println(p.getSampleID()+" "+s.getLocus()+" "+
						s.getGenotype1()+" "+s.getGenotype2());
			}
		}
		
		System.out.println("--- ALLELE FREQ INFORMATION ---");
		for(Region re : manager.getRegions()) {
			for(AlleleFreq af : re.getData()) {
				System.out.println(re.getRegionName()+" "+af.getRegionName()+" "+af.getLocus()
				+" "+af.getAllele()+" "+af.getFreq());
			}
		}
		System.out.println("--------------------------------");
	}
	

}
