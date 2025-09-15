package com.csi.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 二维码生成工具（适配SSM项目，支持普通二维码和带logo二维码）
 */
public class QRCodeUtil {
    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    private static final int QRCODE_SIZE = 300; // 二维码尺寸
    private static final int LOGO_WIDTH = 60;   // LOGO宽度
    private static final int LOGO_HEIGHT = 60;  // LOGO高度

    /**
     * 创建二维码图片（核心方法）
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static BufferedImage createImage(String content, String logoPath, boolean needCompress) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // 高容错
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1); // 边距1像素
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);

        // 创建二维码图片缓冲区
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // 黑色二维码，白色背景
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        // 若需要添加logo，则插入logo
        if (logoPath != null && !logoPath.isEmpty()) {
            insertLogo(image, logoPath, needCompress);
        }
        return image;
    }

    /**
     * 插入logo到二维码中间
     */
    private static void insertLogo(BufferedImage source, String logoPath, boolean needCompress) throws Exception {
        // 从类路径加载logo（适配SSM资源存放方式，logo放在src/main/resources下）
        ClassPathResource resource = new ClassPathResource(logoPath);
        if (!resource.exists()) {
            throw new IOException("logo文件不存在：" + logoPath);
        }

        Image src = ImageIO.read(resource.getInputStream());
        int width = src.getWidth(null);
        int height = src.getHeight(null);

        // 压缩logo到指定尺寸
        if (needCompress) {
            if (width > LOGO_WIDTH) width = LOGO_WIDTH;
            if (height > LOGO_HEIGHT) height = LOGO_HEIGHT;
            src = src.getScaledInstance(width, height, Image.SCALE_SMOOTH); // 平滑缩放
        }

        // 绘制logo到二维码中间
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2; // 居中x坐标
        int y = (QRCODE_SIZE - height) / 2; // 居中y坐标
        graph.drawImage(src, x, y, width, height, null);

        // 给logo加白色边框（可选，增强美观）
        Shape shape = new RoundRectangle2D.Float(x, y, width, height, 6, 6);
        graph.setStroke(new BasicStroke(2f)); // 边框粗细
        graph.setColor(Color.WHITE); // 边框颜色
        graph.draw(shape);

        graph.dispose(); // 释放资源
    }

    /**
     * 生成二维码到输出流（供Controller直接返回给前端）
     * @param content 二维码内容（如活动表单链接）
     * @param logoPath logo在类路径的位置（如"logo.jpg"，即src/main/resources/logo.jpg）
     * @param output 响应输出流
     * @param needCompress 是否压缩logo
     */
    public static void encode(String content, String logoPath, OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = createImage(content, logoPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output); // 写入输出流
    }

    /**
     * 生成不带logo的二维码（简化方法）
     */
    public static void encode(String content, OutputStream output) throws Exception {
        encode(content, null, output, false);
    }

    // 以下方法可选（如果需要生成二维码文件到服务器，保留；否则可删除）
    public static void encode(String content, String logoPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = createImage(content, logoPath, needCompress);
        mkdirs(destPath);
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }

    private static void mkdirs(String destPath) {
        File file = new File(destPath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }
}