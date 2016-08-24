package files;
import java.io.*;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import util.LineInfo;

public class FiletoIndexE {
	File file;
	String name;
	String content;
	SuffixE suffix;
	
	public FiletoIndexE(File f) throws Exception{
		this.file=f;		
		this.name=f.getName();
		//set suffix
		if (this.name.endsWith(".txt")){
			this.suffix=SuffixE.TXT;
		}else if(this.name.endsWith(".doc")){
			this.suffix=SuffixE.DOC;
		}else if(this.name.endsWith(".docx")){
			this.suffix=SuffixE.DOCX;
		}else if(this.name.endsWith(".pdf")||this.name.endsWith(".PDF")){
			this.suffix=SuffixE.PDF;
		}else if(this.name.endsWith(".xls")){
			System.out.println(this.name);
			this.suffix=SuffixE.XLS;
		}else if(this.name.endsWith(".xlsx")){
			this.suffix=SuffixE.XLSX;
		}else{
			System.err.println(LineInfo.getLineInfo()+" : "+"无法识别的文件后缀！");
		}
		//set content
		switch(this.suffix){
		case DOC:
			this.content=extractFromDoc();
			break;
		case TXT:
			this.content=extractFromTxt();
			break;
		case DOCX:
			this.content=extractFromDocx();
			break;
		case PDF:
			this.content=extractFromPdf();
			break;
		case XLS:
			this.content=extractFromXls();
			break;
		case XLSX:
			this.content=extractFromXlsx();
			break;
		}
		
	}
	
	private String extractFromXlsx() throws Exception{
		String str="";
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(this.file));
		XSSFSheet sheet;
		for(int i=0;i<wb.getNumberOfSheets();i++){
			sheet=wb.getSheetAt(i);
			String tstr=readXlsxSheet(sheet);
			str+="\n"+tstr;
			System.out.println(tstr);
		}		
		wb.close();
		return str;
	}

	private String readXlsxSheet(XSSFSheet sheet) {
		String str="";
		XSSFRow row = null;
		XSSFCell cell = null;
		Object val = null;		
		for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			if (row == null) {continue;}
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					val = null;
					continue;
				}
				switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_STRING:
					val = cell.getStringCellValue();
					str+=" "+val;
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					break;
				default:
					val = cell.toString();
					str+=" "+val;
					break;
				}				
			}
			str+="\n";
		}
		return str;
	}

	private String extractFromXls() throws Exception{
		String str="";
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(this.file));
		HSSFSheet sheet;
		for(int i=0;i<wb.getNumberOfSheets();i++){
			sheet=wb.getSheetAt(i);
			str+="\n"+readXlsSheet(sheet);
		}
		wb.close();
		return str;
	}
	private String readXlsSheet(HSSFSheet sheet) throws Exception{
		String str="";
		HSSFRow row = null;
		HSSFCell cell = null;
		Object val = null;		
		for (int i = sheet.getFirstRowNum(); i <= sheet
				.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			String rowstr = "";
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					val = null;
					rowstr+=" ";
					continue;
				}
				switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_STRING:
					val = cell.getStringCellValue();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					break;
				default:
					val = cell.toString();
					break;
				}
				rowstr+=" "+val;
			}
			str+="\n"+rowstr;
		}
		return str;
	}
	
	private String extractFromPdf() throws Exception {
		if(!this.toTextFile(this.file,true)) return "";
		File textfile=new File(this.file.getAbsolutePath()+".txt");
		String str="";	
		InputStreamReader read = new InputStreamReader(new FileInputStream(textfile),"gbk");
		BufferedReader reader=new BufferedReader(read);
		char[] buffer=new char[8*1024];
		while(reader.read(buffer)!=-1){
			str+="\n"+String.valueOf(buffer);
		}
		reader.close();	
		String txtfilename=textfile.getAbsolutePath();
		Process prc =Runtime.getRuntime().exec("cmd /c del \""+txtfilename+"\"");
		prc.waitFor();
		if(prc.exitValue()!=0)	System.err.println(LineInfo.getLineInfo());
		return str;
	}

	private boolean toTextFile(File targetfile, boolean isLayout)throws Exception {
		String[] cmd = getCmd(targetfile, isLayout);
		Process prc =Runtime.getRuntime().exec(cmd);
		prc.waitFor();
		if(prc.exitValue()!=0){
			System.out.println(LineInfo.getLineInfo());
			return false;
		}
		return true;
	}
	
	private String[] getCmd(File pdffile, boolean isLayout) {
		String CONVERTOR_STORED_PATH = "C:\\xpdftest\\xpdfbin-win-3.04\\bin32\\";
		// 转换器的名称，默认为pdftotext
		String CONVERTOR_NAME = "pdftotext";
		// 命令字符
		String command = CONVERTOR_STORED_PATH + CONVERTOR_NAME;
		// PDF文件的绝对路径
		String source_absolutePath = pdffile.getAbsolutePath();
		// 输出文本文件的绝对路径
		String target_absolutePath = pdffile.getAbsolutePath()+".txt";
		// 保持原来的layout
		String layout = "-layout";
		// 设置编码方式
		String encoding = "-enc";
		String character = "GBK";
		// 设置不打印任何消息和错误
		String mistake = "-q";
		// 页面之间不加入分页
		String nopagebrk = "-nopgbrk";
		// 如果isLayout为false，则设置不保持原来的layout
		if (!isLayout)
			layout = "";
		return new String[] { command, layout, encoding, character, mistake, 
				nopagebrk, source_absolutePath, target_absolutePath };
	}
	
	private String extractFromDocx() throws Exception {
		FileInputStream fis = new FileInputStream(this.file);
		XWPFDocument doc = new XWPFDocument(fis);
		XWPFWordExtractor wordExtractor = new XWPFWordExtractor(doc);
		String str=wordExtractor.getText();
		fis.close();
		return str;
	}

	private String extractFromTxt() throws Exception{
		String str="";
		BufferedReader reader=new BufferedReader(new FileReader(this.file));
		String line="";
		while((line=reader.readLine())!=null){
			str+="\n"+line;
		}
		reader.close();
		return str;
	}

	private String extractFromDoc() throws Exception {
		FileInputStream fis = new FileInputStream(this.file);
		HWPFDocument doc = new HWPFDocument(fis);
		WordExtractor wordExtractor = new WordExtractor(doc);
		String str=wordExtractor.getText();
		fis.close();
		return str;
	}

	public String getName(){
		return this.name;
	}
	
	public static void main(String[] args) throws Exception{
		//File f=new File("C:\\xpdftest\\xpdfbin-win-3.04\\bin32\\c.PDF");
		//new FiletoIndexE(f);
		File f=new File("D:\\《活法》笔记.xlsx");
		new FiletoIndexE(f);
	}
	
}
