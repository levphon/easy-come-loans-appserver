package cn.com.payu.app.modules.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.OutputStream;

/**
 * 图片处理工具
 */
@Slf4j
public class ThumbnailatorUtil {

    /**
     * 将图片压缩到1.2M以下
     *
     * @param sourcePath
     * @param targetPath
     */
    public static boolean compressImage(String sourcePath, String targetPath) {
        try {
            double outputQuality = 1.0d;
            File file = new File(sourcePath);
            long fileSize = file.length();
            if (fileSize > 1.2 * 1024 * 1024) {
                if (fileSize >= 10 * 1024 * 1024) {
                    outputQuality = 0.1d;
                } else if (8 * 1024 * 1024 <= fileSize && fileSize < 10 * 1024 * 1024) {
                    outputQuality = 0.20d;
                } else if (7 * 1024 * 1024 <= fileSize && fileSize < 8 * 1024 * 1024) {
                    outputQuality = 0.25d;
                } else if (6 * 1024 * 1024 <= fileSize && fileSize < 7 * 1024 * 1024) {
                    outputQuality = 0.50d;
                } else if (5 * 1024 * 1024 <= fileSize && fileSize < 6 * 1024 * 1024) {
                    outputQuality = 0.60d;
                } else if (4 * 1024 * 1024 <= fileSize && fileSize < 5 * 1024 * 1024) {
                    outputQuality = 0.35d;
                } else if (3 * 1024 * 1024 <= fileSize && fileSize < 4 * 1024 * 1024) {
                    outputQuality = 0.65d;
                } else if (2 * 1024 * 1024 <= fileSize && fileSize < 3 * 1024 * 1024) {
                    outputQuality = 0.65d;
                } else if (1.2 * 1024 * 1024 <= fileSize && fileSize < 2 * 1024 * 1024) {
                    outputQuality = 0.75d;
                }
                Thumbnails.of(file)
                        .scale(1.0d)
                        .outputQuality(outputQuality)
                        .outputFormat("jpg")//png本身就是一种无损的图片格式，而jpg是一种压缩的图片格式
                        .toFile(new File(targetPath));
                return true;
            }
        } catch (Exception e) {
            log.error("图片压缩异常" + e.getMessage(), e);
        }
        return false;
    }

    /**
     * 将图片压缩到1.2M以下
     *
     * @param sourceFile
     * @param targetFile
     */
    public static boolean compressImage(File sourceFile, File targetFile) {
        try {
            double outputQuality = 1.0d;
            long fileSize = sourceFile.length();
            if (fileSize > 1.2 * 1024 * 1024) {
                if (fileSize >= 10 * 1024 * 1024) {
                    outputQuality = 0.1d;
                } else if (8 * 1024 * 1024 <= fileSize && fileSize < 10 * 1024 * 1024) {
                    outputQuality = 0.20d;
                } else if (7 * 1024 * 1024 <= fileSize && fileSize < 8 * 1024 * 1024) {
                    outputQuality = 0.25d;
                } else if (6 * 1024 * 1024 <= fileSize && fileSize < 7 * 1024 * 1024) {
                    outputQuality = 0.50d;
                } else if (5 * 1024 * 1024 <= fileSize && fileSize < 6 * 1024 * 1024) {
                    outputQuality = 0.60d;
                } else if (4 * 1024 * 1024 <= fileSize && fileSize < 5 * 1024 * 1024) {
                    outputQuality = 0.35d;
                } else if (3 * 1024 * 1024 <= fileSize && fileSize < 4 * 1024 * 1024) {
                    outputQuality = 0.65d;
                } else if (2 * 1024 * 1024 <= fileSize && fileSize < 3 * 1024 * 1024) {
                    outputQuality = 0.65d;
                } else if (1.2 * 1024 * 1024 <= fileSize && fileSize < 2 * 1024 * 1024) {
                    outputQuality = 0.75d;
                }
                Thumbnails.of(sourceFile)
                        .scale(1.0d)
                        .outputQuality(outputQuality)
                        .outputFormat("jpg")//png本身就是一种无损的图片格式，而jpg是一种压缩的图片格式
                        .toFile(targetFile);
                return true;
            }
        } catch (Exception e) {
            log.error("图片压缩异常" + e.getMessage(), e);
        }
        return false;
    }

    /**
     * 将图片压缩到1.2M以下
     *
     * @param sourceFile
     * @param os
     */
    public static boolean compressImage(File sourceFile, OutputStream os) {
        try {
            double outputQuality = 1.0d;
            long fileSize = sourceFile.length();
            if (fileSize > 1.2 * 1024 * 1024) {
                if (fileSize >= 10 * 1024 * 1024) {
                    outputQuality = 0.1d;
                } else if (8 * 1024 * 1024 <= fileSize && fileSize < 10 * 1024 * 1024) {
                    outputQuality = 0.20d;
                } else if (7 * 1024 * 1024 <= fileSize && fileSize < 8 * 1024 * 1024) {
                    outputQuality = 0.25d;
                } else if (6 * 1024 * 1024 <= fileSize && fileSize < 7 * 1024 * 1024) {
                    outputQuality = 0.50d;
                } else if (5 * 1024 * 1024 <= fileSize && fileSize < 6 * 1024 * 1024) {
                    outputQuality = 0.60d;
                } else if (4 * 1024 * 1024 <= fileSize && fileSize < 5 * 1024 * 1024) {
                    outputQuality = 0.35d;
                } else if (3 * 1024 * 1024 <= fileSize && fileSize < 4 * 1024 * 1024) {
                    outputQuality = 0.65d;
                } else if (2 * 1024 * 1024 <= fileSize && fileSize < 3 * 1024 * 1024) {
                    outputQuality = 0.65d;
                } else if (1.2 * 1024 * 1024 <= fileSize && fileSize < 2 * 1024 * 1024) {
                    outputQuality = 0.75d;
                }
                Thumbnails.of(sourceFile)
                        .scale(1.0d)
                        .outputQuality(outputQuality)
                        .outputFormat("jpg")//png本身就是一种无损的图片格式，而jpg是一种压缩的图片格式
                        .toOutputStream(os);
                return true;
            }
        } catch (Exception e) {
            log.error("图片压缩异常" + e.getMessage(), e);
        }
        return false;
    }

    public static void main(String[] args) {
        File sourceFile = new File("C:\\Users\\liuyu\\Pictures\\aaa.jpg");
        File targetFile = new File("C:\\Users\\liuyu\\Pictures\\aaa1.jpg");
        ThumbnailatorUtil.compressImage(sourceFile, targetFile);
    }

}
