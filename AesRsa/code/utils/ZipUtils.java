package utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils{
    public static String zip(String zipFileName, File[] files) {
        ZipOutputStream out = null;
        BufferedOutputStream bo = null;
        String resPath = "";
        try {
            resPath = createDir(zipFileName);
            out = new ZipOutputStream(new FileOutputStream(zipFileName));
            for (int i = 0; i < files.length; i++) {
                if (null != files[i]) {
                    zip(out, files[i], files[i].getName());
                    out.flush();
                }
            }
            if(out!=null){
                out.flush();
                out.close(); // 输出流关闭
            }
            return resPath;
        } catch (Exception e) {
            resPath = null; //异常则返回NULL，可用于判断
            e.printStackTrace();
        }
        return resPath;
    }
    public static void zip(ZipOutputStream out, File f, String base) { // 方法重载
        try {
            if (f.isDirectory()) {//压缩目录
                try {
                    File[] fl = f.listFiles();
                    if (fl.length == 0) {
                        out.putNextEntry(new ZipEntry(base + "/"));  // 创建zip实体
                        out.flush();
                    }
                    for (int i = 0; i < fl.length; i++) {
                        zip(out, fl[i], base + "/" + fl[i].getName()); // 递归遍历子文件夹
                    }
                    //System.out.println("第" + k + "次递归");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{ //压缩单个文件

                out.putNextEntry(new ZipEntry(base)); // 创建zip实体
                FileInputStream in = new FileInputStream(f);
				/*BufferedInputStream bi = new BufferedInputStream(in);*/
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b); // 将字节流写入当前zip目录
                }
                out.flush();
				/*bi.close();*/
                in.close(); // 输入流关闭
                out.closeEntry(); //关闭zip实体
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 目录不存在时，先创建目录
     * @param zipFileName
     */
    public static String createDir(String zipFileName){
        String filePath = StringUtils.substringBeforeLast(zipFileName, "/");
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {//目录不存在时，先创建目录
            targetFile.mkdirs();
        }
        return filePath;
    }


    /**
     *
     * @Description (解压)
     * @param zipPath zip路径
     * @param charset 编码
     * @param outPutPath 输出路径
     */
    public static int unZip(String zipPath, String charset, String outPutPath) {
        int i =0;
        try {
            ZipInputStream Zin=new ZipInputStream(new FileInputStream(zipPath), Charset.forName(charset));//输入源zip路径
            BufferedInputStream Bin=new BufferedInputStream(Zin);
            String Parent = outPutPath; //输出路径（文件夹目录）
            File Fout=null;
            ZipEntry entry;
            try {
                while((entry = Zin.getNextEntry())!=null && !entry.isDirectory()){
                    i+=1;
                    Fout=new File(Parent,entry.getName());
                    if(!Fout.exists()){
                        (new File(Fout.getParent())).mkdirs();
                    }
                    FileOutputStream out=new FileOutputStream(Fout);
                    BufferedOutputStream Bout=new BufferedOutputStream(out);
                    int b;
                    while((b=Bin.read())!=-1){
                        Bout.write(b);
                    }
                    Bout.close();
                    out.close();
                }
                Bin.close();
                Zin.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return i;
    }

    public static void main(String[] args) {
        try {
            List<File> fileList =new ArrayList<>();
            fileList.add(new File("d:/a.xls"));
            fileList.add(new File("d:/b.xls"));
            File[] files=fileList.toArray(new File[fileList.size()]);
            zip("d:/asdf.zip",files);
            unZip("d:/asdf.zip","utf-8","E:/wps");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}