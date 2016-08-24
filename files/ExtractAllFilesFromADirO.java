package files;
import java.io.*;
import java.util.*;

import util.LineInfo;
/*
 * extract all files from a dir
 * a list of FiletoIndexE
 */
public class ExtractAllFilesFromADirO {
	File dir;
	List<FiletoIndexE> files;
	
	public ExtractAllFilesFromADirO(String dirstr) throws Exception{
		this.dir=new File(dirstr);
		this.files = new ArrayList<FiletoIndexE>();
		if(!this.dir.isDirectory()) throw new Exception(LineInfo.getLineInfo());
		File[] fs=this.dir.listFiles(new DirFilter());
		for (int i=0;i<fs.length;i++){
			System.out.println(fs[i].getName());
			FiletoIndexE ff=new FiletoIndexE(fs[i]);
			this.files.add(ff);
		}
	}
    
	public List<FiletoIndexE> getFiletoIndex(){
		return this.files;
	}
	
	public String listAllNames(){
		String allnames="";
		ListIterator<FiletoIndexE> iter=this.files.listIterator();
		while(iter.hasNext()){
			FiletoIndexE f=iter.next();
			allnames+="\n"+f.getName();
		}
		return allnames;
	}
	
	public static void main(String[] args) throws Exception{
		ExtractAllFilesFromADirO e=new ExtractAllFilesFromADirO("D:\\Users\\niuzhen\\Downloads");
		System.out.println(e.listAllNames());
	}
	
	class DirFilter implements FilenameFilter{
		public DirFilter(){			
		}
		@Override
		public boolean accept(File dir, String name) {
			if(name.endsWith(".txt")||name.endsWith(".doc")
					||name.endsWith(".docx")||name.endsWith(".xls")
					||name.endsWith(".xlsx")||name.endsWith(".pdf")
			)
				return true;
			return false;
		}
		
	}
 
}
