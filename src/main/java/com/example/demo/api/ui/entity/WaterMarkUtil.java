package com.example.demo.api.ui.entity;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.example.demo.common.resources.R;

import jodd.util.StringUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
public class WaterMarkUtil {
    public static void pressImage(File file, String remark) throws Exception {

        Metadata metadata = ImageMetadataReader.readMetadata(file);
        Directory directory = metadata.getFirstDirectoryOfType(ExifDirectoryBase.class);
        int orientation = 0;
        // Exif信息中有保存方向,把信息复制到缩略图
        // 原图片的方向信息
        if (directory != null && directory.containsTag(ExifDirectoryBase.TAG_ORIENTATION)) {
            orientation = directory.getInt(ExifDirectoryBase.TAG_ORIENTATION);
            System.out.println(orientation);
        }
        int angle = 0;
        if (6 == orientation) {
            //6旋转90
            angle = 90;
        } else if (3 == orientation) {
            //3旋转180
            angle = 180;
        } else if (8 == orientation) {
            //8旋转90
            angle = 270;
        }

        BufferedImage src = ImageIO.read(file);
        BufferedImage des = rotate(src, angle);

        int width = des.getWidth(null);
        int height = des.getHeight(null);
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.drawImage(des, 0, 0, width, height, null);


        Image watermark = ImageIO.read(R.getStream("image/logo.png"));
        int widthWK = watermark.getWidth(null);
        int heightWK = watermark.getHeight(null);

        boolean reduce = width < 1000;
        if (reduce) {
            widthWK = 200;
            heightWK = 48;
            g.drawImage(watermark, width - widthWK - 50, height - heightWK - 80, widthWK, heightWK, null);
        } else {
            g.drawImage(watermark, width - widthWK - 120, height - heightWK - 180, widthWK, heightWK, null);
        }

        if (StringUtil.isNotEmpty(remark)) {

            g.setColor(new Color(51, 51, 51));
            g.setFont(new Font("宋体", Font.PLAIN, reduce ? 28 : 80));

            int x = width - widthWK - (reduce ? 50 : 120);
            int y = height - (reduce ? 50 : 90);
            g.drawString(remark, x, y);
        }

        g.dispose();

        ImageIO.write(image, "jpg", file);

    }

    private static BufferedImage rotate(Image src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        // calculate the new image size
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(
                src_width, src_height)), angel);

        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // transform
        g2.translate((rect_des.width - src_width) / 2,
                (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

        g2.drawImage(src, null, null);
        return res;
    }

    private static Rectangle calcRotatedSize(Rectangle src, int angel) {
        // if angel is greater than 90 degree, we need to do some conversion
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }
}
