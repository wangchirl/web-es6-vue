package utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Strings;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;


public class Base64Utils{
    private static Logger logger			= (Logger) LoggerFactory.getLogger(Base64Utils.class);
    public static final String	IMG_TYPE_JPG	= ".jpg";

    /**
     * 转换base64字符串为图片
     *
     * @param base64Str
     *            base64字符串 包含(data:image/jpeg;base64)
     * @param saveFilePath
     *            保存路径
     */
    public static String convertBase64ToPic(String base64Str, String saveFilePath, String fileName) {
        String generateFileName = fileName;
        if (!Strings.isNullOrEmpty(base64Str)) {
            File file = new File(saveFilePath);
            if (file.exists() || (!file.exists() && file.mkdirs())) {
                OutputStream out = null;
                try {
                    byte[] b = Base64.decodeBase64(base64Str.replace("data:image/jpeg;base64,", ""));
                    for (int i = 0; i < b.length; ++i) {
                        if (b[i] < 0) {
                            b[i] += 256;
                        }
                    }
                    if (Strings.isNullOrEmpty(fileName)) {
                        generateFileName = getDateFileName() + IMG_TYPE_JPG;
                        out = new FileOutputStream(saveFilePath + File.separator + generateFileName);
                    } else {
                        out = new FileOutputStream(saveFilePath + File.separator + fileName);
                    }
                    out.write(b);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return generateFileName;
    }
    public static String convertBase64ToPic(String base64Str, String last,String saveFilePath, String fileName) {
        String ext = "data:image/"+last+";base64,";
        String generateFileName = fileName;
        if (!Strings.isNullOrEmpty(base64Str)) {
            File file = new File(saveFilePath);
            if (file.exists() || (!file.exists() && file.mkdirs())) {
                OutputStream out = null;
                try {
                    byte[] b = Base64.decodeBase64(base64Str.replace(ext, ""));
                    for (int i = 0; i < b.length; ++i) {
                        if (b[i] < 0) {
                            b[i] += 256;
                        }
                    }
                    if (Strings.isNullOrEmpty(fileName)) {
                        generateFileName = getDateFileName() +"."+last;
                        out = new FileOutputStream(saveFilePath + File.separator + generateFileName);
                    } else {
                        out = new FileOutputStream(saveFilePath + File.separator + fileName);
                    }
                    out.write(b);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    }
                }
            }
        }
        return generateFileName;
    }

    public static String getDateFileName() {
        return DateFormatUtils.format(new Date(), "yyyyMMddHHss");
    }
}