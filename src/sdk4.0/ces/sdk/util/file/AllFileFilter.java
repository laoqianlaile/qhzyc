/***************
   中信组件库
 ***************/
package ces.sdk.util.file;


import java.io.*;

/**
 * <b>文件名:</b>AllFileFilter.java<br>
 * <b>功能描述:</b>全部文件过滤器<br>
 * <b>版权所有:</b>上海中信信息发展有限公司(CES)2003
 * @author 钟新华
 * @version 1.0.2003.0910
 */

/**
 * 。
 * <b>此类在0.6版本的时候方式重大变化，通过继承自CombineFileFilter实现了双重功能。</b>
 * @since  0.1
 */

public class AllFileFilter
    extends CombineFileFilter {
  /**
   * 构造一个针对swing的AllFileFilter。
   * @since  0.6
   */
  public AllFileFilter() {
    super(SWING);
  }
  /**
   * 根据指定类型构造一个AllFileFilter。
   * @param type 过滤器类型
   * @since  0.6
   */
  public AllFileFilter(int type) {
    super(type);
  }

  /**
   * 判断指定的文件是否可以被接受。
   * @param file 需要判断的文件
   * @return 在任何情况都返回true。
   * @since  0.6
   */
  protected boolean acceptFile(File file) {
    return true;
  }

  /**
   * 返回过滤器的描述字符串。
   * @return 过滤器的描述字符串“All Files”
   * @since  0.1
   */
  public String getDescription() {
    return "All Files";
  }
}
