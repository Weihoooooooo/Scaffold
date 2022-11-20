package com.weiho.scaffold.common.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 文件工具类,扩展hutool包
 *
 * @author <a href="https://gitee.com/guchengwuyue/yshopmall">参考链接</a>
 */
@UtilityClass
@SuppressWarnings("all")
public class FileUtils extends cn.hutool.core.io.FileUtil {
    /**
     * 定义GB的计算常量
     */
    private static final int GB = 1024 * 1024 * 1024;

    /**
     * 定义MB的计算常量
     */
    private static final int MB = 1024 * 1024;

    /**
     * 定义KB的计算常量
     */
    private static final int KB = 1024;

    /**
     * 格式化小数
     */
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    /**
     * MultipartFile转File,文件上传(用于保存在远程服务器SMMS图床等)
     *
     * @param multipartFile Spring的MultipartFile文件
     * @return File
     */
    public File toFile(MultipartFile multipartFile) {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = "." + getExtensionName(fileName);
        File file = null;
        try {
            // 用uuid作为文件名，防止生成的临时文件重复
            file = File.createTempFile(IdUtil.simpleUUID(), prefix);
            // MultipartFile to File
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 获取文件扩展名，不带 .
     *
     * @param filename 文件名
     * @return String
     */
    public String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 根据文件后缀获取文件类型
     *
     * @param type 文件后缀
     * @return String
     */
    public String getFileType(String type) {
        String documents = "txt doc pdf ppt pps xlsx xls docx";
        String music = "mp3 wav wma mpa ram ra aac aif m4a";
        String video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";
        String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
        if (image.contains(type)) {
            return "pic";
        } else if (documents.contains(type)) {
            return "txt";
        } else if (music.contains(type)) {
            return "music";
        } else if (video.contains(type)) {
            return "vedio";
        } else {
            return "other";
        }
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     *
     * @param filename 文件名
     * @return String
     */
    public String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 文件大小转换
     *
     * @param size 文件大小
     * @return XXG XXM
     */
    public String getSize(long size) {
        String resultSize;
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = DF.format(size / (float) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = DF.format(size / (float) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = DF.format(size / (float) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }

    /**
     * 文件上传(保存本地)
     *
     * @param multipartFile 文件
     * @param filePath      上传的地址(本地路径)
     * @return File
     */
    public File upload(MultipartFile multipartFile, String filePath) {
        String suffix = getExtensionName(multipartFile.getOriginalFilename());
        StringBuffer nowStr = fileRename();
        try {
            String fileName = nowStr + "." + suffix;
            String path = filePath + fileName;
            // getCanonicalFile 可解析正确各种路径
            File dest = new File(path).getCanonicalFile();
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            // 文件写入
            multipartFile.transferTo(dest);
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传文件重命名(以时间命名)
     *
     * @return 新的文件名
     */
    public StringBuffer fileRename() {
        String time = DateUtils.getNowDateFormat(DateUtils.FormatEnum.YYYYMMDD);
        StringBuffer buf = new StringBuffer(time);
        buf.append("_").append(MD5Utils.getMd5(IdUtil.simpleUUID()));
        return buf;
    }

    /**
     * 导出excel
     *
     * @param list     标题与每一列的值
     * @param response 响应
     * @throws IOException /
     */
    public void downloadExcel(List<Map<String, Object>> list, HttpServletResponse response) throws IOException {
        String tempPath = System.getProperty("java.io.tmpdir") + IdUtil.fastSimpleUUID() + ".xlsx";
        File file = new File(tempPath);
        BigExcelWriter writer = ExcelUtil.getBigWriter(file);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //file.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        ServletOutputStream out = response.getOutputStream();
        // 终止后删除临时文件
        file.deleteOnExit();
        writer.flush(out, true);
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    /**
     * 文件下载
     *
     * @param fileLocalPath 文件本地路径
     * @param response      响应参数
     */
    public void downloadFile(String fileLocalPath, HttpServletResponse response) throws IOException {
        if (StringUtils.isBlank(fileLocalPath)) {
            throw new BadRequestException(I18nMessagesUtils.get("file.path.null"));
        }
        ServletOutputStream out = response.getOutputStream();
        byte[] bytes = readBytes(fileLocalPath);
        response.setContentType("application/octet-stream");
        String fileName = fileLocalPath.substring(fileLocalPath.lastIndexOf("\\") + 1);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        out.write(bytes);
        IoUtil.close(out);
    }

    /**
     * 检查文件大小是否超过规定大小
     *
     * @param maxSize 最大的文件大小
     * @param size    文件大小
     */
    public void checkSize(long maxSize, long size) {
        // 1M
        int len = 1024 * 1024;
        if (size > (maxSize * len)) {
            throw new BadRequestException(I18nMessagesUtils.get("file.size.exception"));
        }
    }

    /**
     * 获取文件的byte数组
     *
     * @param file 文件
     * @return byte[]
     */
    private byte[] getByte(File file) {
        // 得到文件长度
        byte[] b = new byte[(int) file.length()];
        try {
            InputStream in = new FileInputStream(file);
            try {
                in.read(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            IoUtil.close(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return b;
    }

    /**
     * 获取byte数组的加密字符串
     *
     * @param bytes byte数组
     * @return String
     */
    private String getMd5(byte[] bytes) {
        // 16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(bytes);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            // 移位 输出字符串
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件的字节数组的md5加密字符串
     *
     * @param file 文件对象
     * @return String
     */
    public String getMd5(File file) {
        return getMd5(getByte(file));
    }

}
