package com.ces.component.hjtds.utils;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {
	public static void main(String[] args) throws IOException {
		rotateImg("D:/1.jpg","D:/1.jpg",90);
	}

	/**
	 * 旋转后保存文件
	 * @param src
	 * @param dest
	 * @param degree
	 * @throws IOException
	 */
	public static void rotateImg(String src, String dest, int degree) throws IOException {

		String ext = src.substring(src.lastIndexOf(".") +1);
		BufferedImage image = ImageIO.read(new File(src));
		
		int iw = image.getWidth();//原始图象的宽度 
		int ih = image.getHeight();//原始图象的高度
		int w = 0;
		int h = 0;
		int x = 0;
		int y = 0;
		degree = degree % 360;
		if (degree < 0)
			degree = 360 + degree;//将角度转换到0-360度之间
		double ang = Math.toRadians(degree);//将角度转为弧度

		/**
		 *确定旋转后的图象的高度和宽度
		 */

		if (degree == 180 || degree == 0 || degree == 360) {
			w = iw;
			h = ih;
		} else if (degree == 90 || degree == 270) {
			w = ih;
			h = iw;
		} else {
			int d = iw + ih;
			w = (int) (d * Math.abs(Math.cos(ang)));
			h = (int) (d * Math.abs(Math.sin(ang)));
		}

		x = (w / 2) - (iw / 2);//确定原点坐标
		y = (h / 2) - (ih / 2);
		BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
		AffineTransform at = new AffineTransform();
		at.rotate(ang, w / 2, h / 2);//旋转图象
		at.translate(x, y);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		op.filter(image, rotatedImage);
		
		ImageIO.write(rotatedImage, ext, new File(dest));
		
	}
	
	/**
	 * 旋转后保存到同一文件
	 * @param src
	 * @param degree
	 * @throws IOException
	 */
	public static void rotateImg(String src, int degree) throws IOException {
		
		String ext = src.substring(src.lastIndexOf(".") +1);
		File file = new File(src);
		
		BufferedImage image = ImageIO.read(file);
		
		int iw = image.getWidth();//原始图象的宽度 
		int ih = image.getHeight();//原始图象的高度
		int w = 0;
		int h = 0;
		int x = 0;
		int y = 0;
		degree = degree % 360;
		if (degree < 0)
			degree = 360 + degree;//将角度转换到0-360度之间
		double ang = Math.toRadians(degree);//将角度转为弧度

		/**
		 *确定旋转后的图象的高度和宽度
		 */

		if (degree == 180 || degree == 0 || degree == 360) {
			w = iw;
			h = ih;
		} else if (degree == 90 || degree == 270) {
			w = ih;
			h = iw;
		} else {
			int d = iw + ih;
			w = (int) (d * Math.abs(Math.cos(ang)));
			h = (int) (d * Math.abs(Math.sin(ang)));
		}

		x = (w / 2) - (iw / 2);//确定原点坐标
		y = (h / 2) - (ih / 2);
		BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
		AffineTransform at = new AffineTransform();
		at.rotate(ang, w / 2, h / 2);//旋转图象
		at.translate(x, y);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		op.filter(image, rotatedImage);
		
		ImageIO.write(rotatedImage, ext, file);
		
		
	}
	
	/**
	 * 返回旋转后的图片
	 * @param file
	 * @param degree
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage rotateImg(File file, int degree) throws IOException {

		BufferedImage image = ImageIO.read(file);
		
		int iw = image.getWidth();//原始图象的宽度 
		int ih = image.getHeight();//原始图象的高度
		int w = 0;
		int h = 0;
		int x = 0;
		int y = 0;
		degree = degree % 360;
		if (degree < 0)
			degree = 360 + degree;//将角度转换到0-360度之间
		double ang = Math.toRadians(degree);//将角度转为弧度

		/**
		 *确定旋转后的图象的高度和宽度
		 */

		if (degree == 180 || degree == 0 || degree == 360) {
			w = iw;
			h = ih;
		} else if (degree == 90 || degree == 270) {
			w = ih;
			h = iw;
		} else {
			int d = iw + ih;
			w = (int) (d * Math.abs(Math.cos(ang)));
			h = (int) (d * Math.abs(Math.sin(ang)));
		}

		x = (w / 2) - (iw / 2);//确定原点坐标
		y = (h / 2) - (ih / 2);
		BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
		AffineTransform at = new AffineTransform();
		at.rotate(ang, w / 2, h / 2);//旋转图象
		at.translate(x, y);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		op.filter(image, rotatedImage);
		
		return rotatedImage;
		
	}
	
	/**
	 * 返回旋转后的图片
	 * @param file
	 * @param degree
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage rotateImg(BufferedImage image, int degree) throws IOException {
		
		int iw = image.getWidth();//原始图象的宽度 
		int ih = image.getHeight();//原始图象的高度
		int w = 0;
		int h = 0;
		int x = 0;
		int y = 0;
		degree = degree % 360;
		if (degree < 0)
			degree = 360 + degree;//将角度转换到0-360度之间
		double ang = Math.toRadians(degree);//将角度转为弧度
		
		/**
		 *确定旋转后的图象的高度和宽度
		 */
		
		if (degree == 180 || degree == 0 || degree == 360) {
			w = iw;
			h = ih;
		} else if (degree == 90 || degree == 270) {
			w = ih;
			h = iw;
		} else {
			int d = iw + ih;
			w = (int) (d * Math.abs(Math.cos(ang)));
			h = (int) (d * Math.abs(Math.sin(ang)));
		}
		
		x = (w / 2) - (iw / 2);//确定原点坐标
		y = (h / 2) - (ih / 2);
		BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
		AffineTransform at = new AffineTransform();
		at.rotate(ang, w / 2, h / 2);//旋转图象
		at.translate(x, y);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		op.filter(image, rotatedImage);
		
		return rotatedImage;
		
	}
	
	/**
	 * 压缩图片
	 * 
	 * @author 黄翔宇
	 * @throws IOException 
	 */
	public static void compressPic(File source, File dest,double maxWidth,int rotation) throws IOException {
		BufferedImage src = ImageIO.read(source);
		double rate = ((double) src.getWidth(null)) / maxWidth + 0.1;
		// 根据缩放比率大的进行缩放控制
		int newWidth = (int) (((double) src.getWidth(null)) / rate);
		int newHeight = (int) (((double) src.getHeight(null)) / rate);
		BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < newHeight; y++) {
			for (int x = 0; x < newWidth; x++) {
				newImage.setRGB(x,y,src.getRGB(x * src.getWidth() / newWidth,y * src.getHeight() / newHeight));
			}
		}
		newImage = rotateImg(newImage,rotation);
		ImageIO.write(newImage, "jpg", dest);
	}
}
