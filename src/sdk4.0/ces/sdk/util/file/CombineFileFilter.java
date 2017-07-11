/***************
   中信组件库
 ***************/
package ces.sdk.util.file;

/**
 * Copyright: Copyright (c) 2002-2003
 * <b>版权所有:</b>上海中信信息发展有限公司(CES)2003
 * @author Cherami
 */
import java.io.*;

/**
 * 复合文件过滤器，实现java.io.FileFilter，同时也扩展了javax.swing.filechooser.FileFilter。
 * 但是由于这两类过滤器的特性并不完全一样，java.io.FileFilter一般只是针对一个目录下的文件，
 * 不对子目录中的内容处理，而javax.swing.filechooser.FileFilter一般是包括子目录的，
 * 因此定义了一些方法以及构造方法进行这方面的设置。
 * 一般情况下，如果在构造的时候定义过滤器实例是针对IOLIST的将不包括目录，如果是针对SWING的则包括目录。
 * 子类必须定义的方法就是acceptFile，其内部定义就是等同于java.io.FileFilter的accept方法。
 * 由于要实现swing的FileFilter方法，因此子类也应该定义getDescription方法。
 * 另外如果子类要重新定义此类的特性可以通过覆盖includeDirectoryForSwing使之返回false即可。
 * 当然也可以通过直接覆盖accept方法来重新定义此类的特性。
 * @since  0.6
 */

public abstract class CombineFileFilter
    extends javax.swing.filechooser.FileFilter
    implements FileFilter{
  /**
   * 常量，定义过滤器的类型为针对java.io包中的FileFilter接口。
   * @since  0.6
   */
  public static final int IOLIST=0;
  /**
   * 常量，定义过滤器的类型为针对javax.swing包中的FileFilter接口。
   * @since  0.6
   */
  public static final int SWING=1;
  /**
   * 过滤器的类型。
   * @since  0.6
   */
  protected int type;
  /**
   * 构造一个针对swing的CombineFileFilter。
   * @since  0.6
   */
  public CombineFileFilter() {
    this(SWING);
  }
  /**
   * 根据指定类型构造一个CombineFileFilter。
   * @param type 过滤器类型
   * @since  0.6
   */
  public CombineFileFilter(int type) {
    this.type=type;
  }


  /**
   * 判断指定的文件是否可以被接受。
   * 如果指定的类型错误则按照swing进行处理。
   * @param file 需要判断的文件
   * @return 如果。
   * @since  0.6
   */
  public boolean accept(File file) {
    switch (type) {
      case IOLIST:
        return acceptFile(file);
      case SWING:
        if (includeDirectoryForSwing()&&file.isDirectory()) {
          return true;
        }
        return acceptFile(file);
      default:
        if (includeDirectoryForSwing()&&file.isDirectory()) {
          return true;
        }
        return acceptFile(file);
    }
  }

  /**
   * 判断指定目录下的指定的文件名是否可以包含在文件列表里面。
   * @param dir 查找文件的目录
   * @param name 文件名
   * @return 在任何情况都返回true。
   * @since  0.6
   */
  public boolean accept(File dir, String name) {
    return acceptFile(new File(name));
  }

  /**
   * 判断指定的文件是否可以被接受。
   * @param file 需要判断的文件
   * @return 在任何情况都返回true。
   * @since  0.6
   */
  protected abstract boolean acceptFile(File file);
  /**
   * 定义在针对swing时是否包括子目录。
   * @return true
   * @since  0.6
   */
  protected boolean includeDirectoryForSwing() {
    return true;
  }

}
