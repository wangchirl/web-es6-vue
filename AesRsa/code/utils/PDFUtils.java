package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 王钦 on 2018/10/26.
 * 使用itext插件
 * 1 <dependency>
 * 2     <groupId>com.itextpdf</groupId>
 * 3     <artifactId>itextpdf</artifactId>
 * 4     <version>5.5.10</version>
 * 5 </dependency>
 * 中文支持
 * 1 <dependency>
 * 2     <groupId>com.itextpdf</groupId>
 * 3     <artifactId>itext-asian</artifactId>
 * 4     <version>5.2.0</version>
 * 5 </dependency>
 * 加密
 * 1 <dependency>
 * 2     <groupId>org.bouncycastle</groupId>
 * 3     <artifactId>bcprov-jdk15on</artifactId>
 * 4     <version>1.54</version>
 * 5 </dependency>
 */
public class PDFUtils {

    /**
     * 介绍一下itext的一些属性
     */
    public static void itextAttrs() throws FileNotFoundException, DocumentException {
        //创建文件
        //A4纸，上下左右间距40
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        //创建书写器 name: pdf文件生成路径
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("E:/itext.pdf"));
        //打开文件
        document.open();
        //写入内容
        document.add(new Paragraph("this is a itext to create pdf doc"));
        //设置属性
        //标题
        document.addTitle("this is a title");
        //作者
        document.addAuthor("王钦");
        //主题
        document.addSubject("this is subject");
        //关键字
        document.addKeywords("Keywords");
        //创建时间
        document.addCreationDate();
        //应用程序
        document.addCreator("itext.com");
        //关闭文档
        document.close();
        //关闭书写器
        writer.close();

    }


    /**
     * 根据传入的内容生成PDF文件
     *
     * @param contents  内容
     * @param imagePath 图片路径
     * @param pdfPath   PDF文件路径
     * @return
     */
    public static boolean createPDFDocument(HttpServletRequest request, Map<String, Object> contents, String imagePath, String pdfPath) throws IOException, DocumentException {
        //创建文件
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        //创建写入器
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
        //打开文档
        document.open();

        //创建字体 支持中文  -自定义字体
        Font GB_2312_BOLD = createFont(request, Font.BOLD); //粗体
        Font GB_2312_NORMAL = createFont(request, Font.NORMAL);//常规

        //创建换行段落
        Paragraph next = new Paragraph("\n");

        //创建标题
        Paragraph title = new Paragraph(contents.get("title").toString(), GB_2312_BOLD);
        //设置居中
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(next);

        //创建正文
        Paragraph content = new Paragraph(contents.get("content").toString(), GB_2312_NORMAL);

        //插入图片
        Image image = Image.getInstance(imagePath);
        float width = image.getWidth();
        float height = image.getHeight();
        image.scalePercent(10);//比列缩放50%
        //设置固定高度 - - 一般使用缩放
//        image.scaleAbsolute(200,200);
        //设置图片位置 x轴  y轴
//        image.setAbsolutePosition(400f,440f);
        image.setAlignment(Element.ALIGN_CENTER);//设置图片居中
        //加入文档
        document.add(image);
        document.add(next);

        //图片与文字一行写入
        Paragraph paragraph_1 = new Paragraph();
        Chunk chunk = new Chunk(contents.get("content").toString(), GB_2312_NORMAL);
        paragraph_1.add(chunk);
        chunk = new Chunk(image, 10, -8, true);
        paragraph_1.add(chunk);
        document.add(paragraph_1);

        //关闭文档
        document.close();
        //关闭书写器
        writer.close();
        return true;
    }


    public static void createTab() throws IOException, DocumentException {
        //创建文档
        Document document = new Document();
        //创建写入器
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("E:/TAB.pdf"));

        //设置密码  必须在打开open()之前设置密码
        //用户密码
//        String userPassword = "123456";
//        //拥有者密码
//        String ownerPassword = "hd";
//        //设置密码及权限  -- 只读
//        writer.setEncryption(userPassword.getBytes(), ownerPassword.getBytes(), PdfWriter.ALLOW_PRINTING,
//                PdfWriter.ENCRYPTION_AES_128);
        //打开文档
        document.open();
        //段落
        Paragraph p = null;
        //表格格子
        PdfPCell cell = null;
        //表格高度
        int height = 25;
        //创建字体
        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(baseFont);
        font.setColor(BaseColor.BLUE);//字体颜色

        //创建表格 12 列的表格
        PdfPTable table = new PdfPTable(12);
        //设置表格宽度
        table.setWidthPercentage(100);
        //定义表格每列所占百分比
        float[] ev_width = new float[]{0.18f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
        table.setWidths(ev_width);

        //第一行
        p = new Paragraph("项目\r\n类别", font);
        cell = new PdfPCell(p);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setRowspan(2);//占2行
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        cell.setLeading(0.0f, 1.5f);
        table.addCell(cell);

        p = new Paragraph("前期\n藏品总数", font);
        cell = new PdfPCell(p);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setRowspan(2);//占2行
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        cell.setLeading(0.0f, 1.5f);
        table.addCell(cell);

        p = new Paragraph("本期增加量", font);
        cell = new PdfPCell(p);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setColspan(6);//占6列
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        cell.setLeading(0.0f, 1.5f);
        table.addCell(cell);

        p = new Paragraph("本期减少量", font);
        cell = new PdfPCell(p);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setColspan(4);//占4列
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        cell.setLeading(0.0f, 1.5f);
        table.addCell(cell);

        //第二行
        p = new Paragraph("移交", font);
        cell = new PdfPCell(p);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        table.addCell(cell);

        p = new Paragraph("调入", font);
        cell = new PdfPCell(p);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        table.addCell(cell);

        p = new Paragraph("捐赠", font);
        cell = new PdfPCell(p);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        table.addCell(cell);

        p = new Paragraph("交换", font);
        cell = new PdfPCell(p);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        table.addCell(cell);

        p = new Paragraph("其他", font);
        cell = new PdfPCell(p);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        table.addCell(cell);

        p = new Paragraph("小计", font);
        cell = new PdfPCell(p);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        table.addCell(cell);

        p = new Paragraph("损毁", font);
        cell = new PdfPCell(p);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        table.addCell(cell);

        p = new Paragraph("调出", font);
        cell = new PdfPCell(p);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        table.addCell(cell);

        p = new Paragraph("其他", font);
        cell = new PdfPCell(p);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        table.addCell(cell);

        p = new Paragraph();
        //图片
        Image image = Image.getInstance("E:/oracle.jpg");
        image.scaleAbsolute(20, 20);
        image.setAlignment(Image.ALIGN_CENTER);//居右

        cell = new PdfPCell(p);
        cell.addElement(image);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);
        table.addCell(cell);

        document.add(table);


        //创建列表
        com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.ORDERED);//有序
        list.add(new ListItem("this is a list one", font));
        list.add(new ListItem("this is a list two", font));
        list.add(new ListItem("this is a list three", font));

        document.add(list);

        document.close();
        writer.close();
    }


    public static Font createFont(HttpServletRequest request, int fontType) throws IOException, DocumentException {
        String basePath = request.getServletContext().getRealPath("");
        String GB_2312 = basePath + File.separator + "WEN-INF" + File.separator + "classes" + File.separator + "font" + File.separator + "fangsong_GB2312.ttf";
        BaseFont GB2312 = BaseFont.createFont(GB_2312, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        //font 二号  粗体 // Font.NORMAL 常规
        Font font = new Font(GB2312, 22, fontType);
        return font;
    }


    /**
     * 解析PDF文档
     *
     * @param pdfPath 文件路径
     */
    public static void decodePDFdocument(String pdfPath) throws IOException, DocumentException {
        //读取pdf文件
        PdfReader pdfReader = new PdfReader(pdfPath);

        //修改器
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("e:/decodepdf.pdf"));

        Image image = Image.getInstance("e:/taylor.JPG");
        image.scaleAbsolute(150, 150);
        image.setAbsolutePosition(100, 700);

        for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
            PdfContentByte content = pdfStamper.getUnderContent(i);
            content.addImage(image);
        }

        pdfStamper.close();
    }


    public static void main(String[] args) throws IOException, DocumentException {
        itextAttrs();
        Map contents = new HashMap();
        contents.put("title", "标题");
        contents.put("content", "这是内容");
        String imgPath = "E:/oracle.jpg";
        createTab();
        decodePDFdocument("e:/TAB.pdf");
    }

}
