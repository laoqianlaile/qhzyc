/***************
   中信组件库
 ***************/
package ces.sdk.util.file;

import java.io.*;
import java.util.*;

import org.jdom.Element;
import org.jdom.Document;
import org.jdom.Attribute;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.Namespace;

/**
 * <b>文件名:</b>XMLProperties.java<br>
 * <b>功能描述:</b>提供处理简单的XML文件的功能，XML属性名称格式为：X.Y.Z,将对付XML文件内容如下：
 * 
 * <pre>
 * &lt;X&gt;
 *     &lt;Y&gt;
 *         &lt;Z&gt;someValue&lt;/Z&gt;
 *     &lt;/Y&gt;
 * &lt;/X&gt;
 * </pre>
 * 
 * 该类将完成XML属性名称的创建、值的设置及属性的删除功能，但XML文件必须是可读可写的。<br>
 * <b>版权所有:</b>上海中信信息发展有限公司(CES)2003
 * 
 * @author 郑国强 顾永朋 钟新华 冀德豪
 * @version 1.0.2003.0910
 * 
 */

public class XMLProperties {

	private File file;
	private Document doc;

	/**
	 * Parsing the XML file every time we need a property is slow. Therefore, we
	 * use a Map to cache property values that are accessed more than once.
	 */
	protected Map propertyCache = new HashMap();

	// added by jdh at 20031226
	// 由于与logger相互制约，commented by steve_gu at 2004-03-11
	// private Logger log = new Logger(XMLProperties.class);

	// private Map propContainer;
	private String attrDeli = "&";
	private String nodeDeli = ".";

	/**
	 * 是否加载过，注意这两个属性都不能为static.
	 */
	private boolean loadFlag = false;

	/**
	 * 是否在构造时就加载。
	 */
	private boolean loadAll;

	// private org.jdom.filter.Filter filter=null;
	/**
	 * encoding for this xml,used for saveproperty
	 */
	private String encoding = "gb2312";

	// end added
	private XMLProperties() {
	}

	private boolean isSave = true; // 是否保存改后的文件

	/**
	 * 创建一个XML属性文件对象.
	 * 
	 * @param file
	 *            文件必须可读可写
	 */
	public XMLProperties(String file) {

		this.file = new File(file);
		try {
			SAXBuilder builder = new SAXBuilder();
			// Strip formatting
			// DataUnformatFilter format = new DataUnformatFilter();
			// builder.setXMLFilter(format);
			doc = builder.build(new File(file));
			// CommonSystemOut.SystemOutFun("doc:"+doc);
		} catch (Exception e) {
			System.err.println("在XMLProperties.java文件中出现XML解析错误！");
			e.printStackTrace();
		}
	}

	// added by jdh at 20031226
	/**
	 * Constructor，可以选择是否一次性全部加载 使用建议：在需要频繁的改动配置文件时，构造时用false,然后调用loadData方法，这样
	 * 文件修改后能反映出来。反之，构造时用true，则只加载一次。
	 * 
	 * @param file
	 *            xml文件
	 * @param loadAll
	 *            是否一次性全部读出
	 * @throws NullPointerException
	 * @author 冀德豪
	 * @version 20031226
	 */
	public XMLProperties(String file, boolean loadAll)
			throws NullPointerException {
		if (file == null) {
			System.err.println("file can't null!");
			throw new NullPointerException();
		}
		this.file = new File(file);
		this.loadAll = loadAll;
		Exception genE = null;
		boolean isIOE=false;
		// hudongping:增加异步处理中错误。因为该文件可能被令一个文件在写，同事没有锁定！
		while (true) {
			isIOE=false;
			long lastDate = this.file.lastModified();
			// CommonSystemOut.SystemOutFun(new Date(lastDate));
			FileInputStream fis = null;
			RandomAccessFile raFile = null;
			try {

				raFile = new RandomAccessFile(file, "rws");
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buff=new byte[1024];
				int readLen=raFile.read(buff);
				while (readLen>0)
				{
					bos.write(buff,0,readLen);
					readLen=raFile.read(buff);
				}	
				fis = new FileInputStream(file);
				SAXBuilder builder = new SAXBuilder();
				// Strip formatting
				// DataUnformatFilter format = new DataUnformatFilter();
				// builder.setXMLFilter(format);
			    bos.flush();
				ByteArrayInputStream bis = new ByteArrayInputStream(bos
						.toByteArray());
				doc = builder.build(bis);
				raFile.close();
				raFile = null;
			    bos.close();
			    bos=null;
				// CommonSystemOut.SystemOutFun("doc:"+doc);
				// CommonSystemOut.SystemOutFun("doctype is " + doc.getDocType());
				break;
			} catch (IOException ioE) {
				ioE.printStackTrace();
				isIOE=true;
			} catch (Exception e) {
				System.err.println("在XMLProperties.java文件中(" + file
						+ ")出现XML解析错误！(配置文件可能处于在写状态和脏数据)");
				genE = e;
				try {
					if (raFile != null) {
						raFile.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (lastDate >= this.file.lastModified()) {
				if (genE != null) {
					genE.printStackTrace();
				}
				break;
			}
		}
		// 加载全部数据，如loadAll为true并且loadFlag为false.
		if (loadAll) {
			if (!loadFlag) { // 避免多次重复加载
				loadData();
			}
		}
	}

	/**
	 * 加载数据
	 * 
	 * @author 冀德豪
	 * @version 20031226
	 */
	public void loadData() {
		if (doc == null)
			throw new NullPointerException();
		// 如果在构造时已经加载，则不重新加载。
		// 为了在修改后仍能够重新加载，用户可以构造时选择不加载，然后调用此方法。
		if (!loadFlag) {
			Element root = doc.getRootElement();
			List propName = new ArrayList();
			recursion(root, propName);
			loadFlag = true;
			// 设为只读
			// propertyCache = Collections.unmodifiableMap(propertyCache);
		}
		// List childs=root.getChildren();

	}

	/**
	 * 递归加载
	 * 
	 * @param parent
	 *            父节点
	 * @param propName
	 *            存储节点层次
	 * @author 冀德豪
	 * @version 20031226
	 */
	private void recursion(Element parent, List propName) {
		propName.add(((parent.getNamespacePrefix().length() > 0) ? (parent
				.getNamespacePrefix() + ":") : "")
				+ parent.getName());
		List attribute = parent.getAttributes();
		Iterator itAttris = attribute.iterator();
		while (itAttris.hasNext()) {
			Object objTmp = itAttris.next();
			Attribute att = (Attribute) objTmp;
			// CommonSystemOut.SystemOutFun("attribute's name is " + att.getName());
			// CommonSystemOut.SystemOutFun("attribute's value is " + att.getValue());
			// CommonSystemOut.SystemOutFun("attribute's namespace is " +
			// att.getNamespacePrefix() + " length is " +
			// att.getNamespacePrefix().length());
			String key = list2Str(propName, nodeDeli)
					+ attrDeli
					+ ((att.getNamespacePrefix().length() > 0) ? (att
							.getNamespacePrefix() + ":") : "") + att.getName();
			addProperty(key, att.getValue());
			// propName.remove(propName.size() - 1);
			// CommonSystemOut.SystemOutFun("test is " + child.getText());
		}

		List list = parent.getChildren();
		if (list.size() > 0) {
			Iterator itChilds = list.iterator();
			while (itChilds.hasNext()) {
				Object objTmp = itChilds.next();
				Element child = (Element) objTmp;
				// propName.add(child.getn);
				recursion(child, propName);
				// CommonSystemOut.SystemOutFun("test is " + child.getText());
			}
			/** 加这句话很重要，当递归结束时要把该递归加到propName的内容全部清理 * */
			propName.remove(propName.size() - 1);
		} else {
			// CommonSystemOut.SystemOutFun("element is " + parent.getName());
			// CommonSystemOut.SystemOutFun("value is " + parent.getText());
			// CommonSystemOut.SystemOutFun("namespace is " + parent.getNamespacePrefix()
			// +
			// " length is " +
			// parent.getNamespacePrefix().length());
			String key = list2Str(propName, nodeDeli);
			addProperty(key, parent.getText());
			propName.remove(propName.size() - 1);
		}
	}

	/**
	 * 将list转为一个字符串，中间用token分割,并且将根节点去掉
	 * 
	 * @param list
	 *            要转换的list
	 * @param token
	 *            用来分隔的字符
	 * @return 转换后的字符串
	 * @author 冀德豪
	 * @version 20031226
	 */
	private String list2Str(List list, String token) {
		String strReturn = "";
		Iterator itChilds = list.iterator();
		int i = 0;
		while (itChilds.hasNext()) {
			if (i == 0) {
				i++;
				itChilds.next();
				continue;
			}
			strReturn += itChilds.next().toString() + token;
			i++;
		}
		if (strReturn.charAt(strReturn.length() - 1) == '.') {
			strReturn = strReturn.substring(0, strReturn.length() - 1);
		}
		return strReturn;
	}

	/**
	 * 在hashmap里添加数据，如果已经存在，并且不是list，就用一个list代替，存储以前的值加上新值，
	 * 如果是list,直接add。如果没有，直接put
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @author 冀德豪
	 * @version 20031226
	 */
	private void addProperty(Object key, Object value) {
		if (propertyCache.containsKey(key)) {
			Object objTmp = propertyCache.get(key);
			if (objTmp instanceof List) {
				((List) objTmp).add(value);
			} else {
				List newList = new ArrayList();
				newList.add(objTmp);
				newList.add(value);
				propertyCache.remove(key);
				propertyCache.put(key, newList);
			}
		} else {
			propertyCache.put(key, value);
		}

	}

	/**
	 * 由于getProperty返回的是String，所以添加一个方法
	 * 用此方法之前必须要执行过loadData方法，否则会调用getProperty方法
	 * 
	 * @param name
	 *            参数名称，规则是：去掉根节点后，节点名+.+节点名，如果是属性，+&+属性名。 例如：<?xml
	 *            version="1.0" encoding="ISO-8859-1" ?> <ces> <student
	 *            xmlns="http://cesgroup.com.cn/students"
	 *            xmlns:teachers="http://cesgroup.com.cn/teachers"> <name>JDH</name>
	 *            <grade>three</grade> <teachers name="wang"> <teachers:docter
	 *            teachers:name="li" sex="1"> <teachers:name>dragon</teachers:name>
	 *            <teachers:name>bruce</teachers:name> </teachers:docter>
	 *            </teachers> </student> </ces> student.grade=three,
	 *            student.teachers.teachers:docter&sex=1,
	 *            student.teachers.teachers:docter.teachers:name返回的是一个list,内容如下：
	 *            [dragon, bruce]
	 *            student.teachers.teachers:docter&teachers:name=li
	 * @return 如果没有，返回null,否则，返回list或者String
	 * @author 冀德豪
	 * @version 20031226
	 */
	public Object getPropertyValue(String name) {
		if (name == null)
			return null;
		if (loadFlag) {
			if (propertyCache.containsKey(name)) {
				return propertyCache.get(name);
			} else {
				return null;
			}
		} else {
			return getProperty(name);
		}
	}

	/**
	 * setProperty 对于指定节点设置值，如果已存在，修改，否则，增加。
	 * 
	 * @param name
	 *            String 规则是：去掉根节点后，节点名+.+节点名，如果是属性，+&+属性名。 要设置属性的全名，包括路径、命名空间。
	 * @param value
	 *            Object 要设置的值，单个值是字符串，多个值就是ArrayList
	 * @author 冀德豪
	 * @version 20040113
	 */
	public void setProperty(String name, Object value) {
		// first ,set name and value into propertyCache
		propertyCache.put(name, value);
		// then added it in xml file.
		// all need 3 steps,1 and 2 maybe need resursion.
		// 1:parse name,
		boolean isNS = false;
		boolean isAtt = false; // 是否是设置属性
		int count = 0;
		boolean isMul = false;
		if (name.indexOf("&") > 0) {
			isAtt = true;
		}
		if ((!isAtt) && (value == null)) {
			throw new IllegalArgumentException("当设置节点时不能传递空值");
		}
		boolean isDelAtt = false;
		if ((isAtt) && (value == null)) {
			isDelAtt = true;
		}
		if (value instanceof List) {
			List list = (List) value;
			if (list.size() > 1) {
				isMul = true;
				count = list.size();
			} else {
				if (list.size() > 0) {
					value = list.get(0);
				} else {
					throw new RuntimeException("1001:不能用一个空的list设置");
				}
			}
		}

		List list = null;

		if (isAtt && isMul) {
			throw new RuntimeException("1000:不能对属性赋多个值");
		}
		if (name.indexOf(":") > 0) {
			isNS = true;
		}
		String[] propName = parsePropertyName(name);
		// for (int i = 0; i < propName.length; i++) {
		// CommonSystemOut.SystemOutFun("propName[" + i + "] is " + propName[i]);
		// }
		if (isAtt) {
			if (propName.length > 0) {
				String last = propName[propName.length - 1];
				String[] tmp = new String[propName.length + 1];
				System.arraycopy(propName, 0, tmp, 0, propName.length);
				tmp[propName.length - 1] = last.substring(0, last.indexOf("&"));
				tmp[tmp.length - 1] = last.substring(last.indexOf("&") + 1);
				propName = tmp;
			}
		}
		Element element = doc.getRootElement();
		for (int i = 0; i < propName.length; i++) {
			// If we don't find this part of the property in the XML heirarchy
			// we add it as a new node
			// first judge if have namespace
			String pName = propName[i];
			String pNs = "";
			boolean blGoon = false;
			if (isNS) { // 是否有命名空间
				int index = propName[i].indexOf(":");
				if (index > 0) {
					pNs = propName[i].substring(0, index);
					pName = propName[i].substring(index + 1);
					Namespace nameSpace = element.getNamespace(pNs);
					if (i == (propName.length - 1)) {
						if (isAtt) {
							// element.getAttribute(pName, nameSpace);
							if (isDelAtt) {
								if (element.getAttribute(pName, nameSpace) != null) {
									element.removeAttribute(pName, nameSpace);
								}
							} else {
								element.setAttribute(pName, value.toString(),
										nameSpace);
							}
							break;
							// }else{
							// element.setAttribute(pName, value.toString(),
							// nameSpace);
							// break;
							// }
						} else {
							if (element.getChild(pName, nameSpace) == null) {
								element
										.addContent(new Element(pName,
												nameSpace));
							}

						}
					}

					if (i == (propName.length - 1)) { // last node
						if (isMul) {
							if (!isAtt) { // is text,not attribute
								List haveList = element.getChildren(pName,
										nameSpace);
								int addCount = 0;
								if (haveList != null) {
									addCount = count - haveList.size();
								} else {
									addCount = count - 1;
								}

								for (int j = 0; j < addCount; j++) {
									element.addContent(new Element(pName,
											nameSpace));
								}
								list = element.getChildren(pName, nameSpace);
							} else {
								// CommonSystemOut.SystemOutFun("ssssssssssssssssss");
								throw new RuntimeException("1000:不能对属性赋多个值");
							}
						} else {

							element = element.getChild(pName, nameSpace);
						}
					} else {
						if (element.getChild(pName, nameSpace) == null) {
							element.addContent(new Element(pName, nameSpace));
						}
						element = element.getChild(pName, nameSpace);
					}
				} else {
					blGoon = true;
				}
			} else {
				blGoon = true;
			}
			if (blGoon) {
				if (i == (propName.length - 1)) {
					if (isAtt) { // 属性
						if (isDelAtt) {
							if (element.getAttribute(pName) != null) {
								element.removeAttribute(pName);
							}
						} else {
							element.setAttribute(pName, value.toString());
						}
						break;
						// }
					} else { // 节点内容
						if (element.getChild(pName) == null) {
							element.addContent(new Element(pName));
						}
						// else{
						// element.removeContent()
						// }
						// element = element.getChild(pName);
					}
					// /////////////added at 0331 for mul set
					if (isMul) {
						if (!isAtt) { // is text,not attribute
							List haveList = element.getChildren(pName);
							int addCount = 0;
							if (haveList != null) {
								addCount = count - haveList.size();
							} else {
								addCount = count - 1;
							}
							for (int j = 0; j < addCount; j++) {
								element.addContent(new Element(pName));
							}
							list = element.getChildren(pName);
						} else {
							// CommonSystemOut.SystemOutFun("ssssssssssssssssss");
							throw new RuntimeException("1000:不能对属性赋多个值");
						}
					}
					// else {
					element = element.getChild(pName);
					// }
					// /////////////////////end added at 0331

				} else {
					if (element.getChild(pName) == null) {
						element.addContent(new Element(pName));
					}
					element = element.getChild(pName);
				}

			}
		}
		if (!isMul) {
			if (!isAtt) {
				// Set the value of the property in this node.
				element.setText(value.toString());
			} else {

			}
		} else {
			if (isAtt) {

			} else {
				Iterator it = list.iterator();
				List valueList = (List) value;
				int valueListSize = valueList.size();
				int index = 0;
				while (it.hasNext()) {
					if (index > valueListSize - 1) {
						break;
					} else {
						element = (Element) it.next();
						element.setText(valueList.get(index).toString());
						index++;
					}
				}
			}
		}
		// write the XML properties to disk
		if (isSave) {
			saveProperties();
		}

		// 2:if node exist,update it,or,add a node
		// 3:save file.
	}

	// end added by jdh
	/**
	 * 返回指定属性的值.
	 * 
	 * @param name
	 *            要得到属性的名称，格式如X.Y.Z
	 * @return 指定属性的值
	 */
	public String getProperty(String name) {
		if (propertyCache.containsKey(name)) {
			Object objTmp = propertyCache.get(name);
			if (objTmp instanceof String) {
				return (String) objTmp;
			} else {
				List list = (List) objTmp;
				if (list.size() > 0) {
					return list.get(0).toString();
				}
			}
		}

		String[] propName = parsePropertyName(name);
		// Search for this property by traversing down the XML heirarchy.
		Element element = doc.getRootElement();
		for (int i = 0; i < propName.length; i++) {
			element = element.getChild(propName[i]);
			if (element == null) {
				// This node doesn't match this part of the property name which
				// indicates this property doesn't exist so return null.
				return null;
			}
		}
		// At this point, we found a matching property, so return its value.
		// Empty strings are returned as null.
		String value = element.getText();
		if ("".equals(value)) {
			return null;
		} else {
			// Add to cache so that getting property next time is fast.
			value = value.trim();
			propertyCache.put(name, value);
			return value;
		}
	}

	/**
	 * 返回指定属性的值列表.
	 * 
	 * @param name
	 *            要得到属性的名称，格式如X.Y.Z
	 * @return 指定属性的值列表
	 */
	public List getPropertyValueList(String name) {
		List newList = new ArrayList();
		String[] propName = parsePropertyName(name);
		// Search for this property by traversing down the XML heirarchy.
		Element element = doc.getRootElement();
		setElementValueList(newList, element, propName, 0);
		return newList;
	}

	private void setElementValueList(List valueList, Element element,
			String[] propName, int index) {
		if (element != null && propName != null && index <= propName.length) {
			if (index == propName.length) {
				String value = element.getText();
				if (value != null && !value.equals("")) {
					valueList.add(value.trim());
				}
			} else {
				List elements = element.getChildren(propName[index]);
				if (elements == null) {
					// This node doesn't match this part of the property name
					// which
					// indicates this property doesn't exist so return null.
				} else {
					Iterator it = elements.iterator();
					while (it.hasNext()) {
						Element element1 = (Element) it.next();
						setElementValueList(valueList, element1, propName,
								index + 1);
					}
				}
			}

		}
	}

	/**
	 * Return all children property names of a parent property as a String
	 * array, or an empty array if the if there are no children. For example,
	 * given the properties <tt>X.Y.A</tt>, <tt>X.Y.B</tt>, and
	 * <tt>X.Y.C</tt>, then the child properties of <tt>X.Y</tt> are
	 * <tt>A</tt>, <tt>B</tt>, and <tt>C</tt>.
	 * 
	 * @param parent
	 *            the name of the parent property.
	 * @return all child property values for the given parent.
	 */
	public String[] getChildrenProperties(String parent) {
		String[] propName = parsePropertyName(parent);
		// Search for this property by traversing down the XML heirarchy.
		Element element = doc.getRootElement();
		for (int i = 0; i < propName.length; i++) {
			element = element.getChild(propName[i]);
			if (element == null) {
				// This node doesn't match this part of the property name which
				// indicates this property doesn't exist so return empty array.
				return new String[] {};
			}
		}
		// We found matching property, return names of children.
		List children = element.getChildren();
		int childCount = children.size();
		String[] childrenNames = new String[childCount];
		for (int i = 0; i < childCount; i++) {
			childrenNames[i] = ((Element) children.get(i)).getName();
		}
		return childrenNames;
	}

	/**
	 * Sets the value of the specified property. If the property doesn't
	 * currently exist, it will be automatically created.
	 * 
	 * @param name
	 *            the name of the property to set.
	 * @param value
	 *            the new value for the property.
	 * @deprecated
	 * @see setProperty(String name,Object obj)
	 */
	public void setProperty(String name, String value) {
		// added by jdh at 20040113
		// 如果有&符号或者命名空间，就用新的方法
		if ((name.indexOf("&") > 0) || (name.indexOf(":") > 0)) {
			Object objValue = value;
			setProperty(name, objValue);
			return;
		}
		// end added
		// Set cache correctly with prop name and value.
		propertyCache.put(name, value);

		String[] propName = parsePropertyName(name);
		// Search for this property by traversing down the XML heirarchy.
		Element element = doc.getRootElement();
		for (int i = 0; i < propName.length; i++) {
			// If we don't find this part of the property in the XML heirarchy
			// we add it as a new node
			if (element.getChild(propName[i]) == null) {
				element.addContent(new Element(propName[i]));
			}
			element = element.getChild(propName[i]);
		}
		// Set the value of the property in this node.
		element.setText(value);
		// write the XML properties to disk
		if (isSave) {
			saveProperties();
		}
	}

	/**
	 * 添加单个节点属性或内容
	 * 
	 * @param name
	 *            String 规则是：去掉根节点后，节点名+.+节点名，如果是属性，+&+属性名。 要设置属性的全名，包括路径、命名空间。
	 * @param value
	 *            要设置的值
	 * @param isSave
	 *            是否保存改后的文件
	 * @return 添加的内容
	 * @author jdh at 0331
	 */
	public String addProperty(String name, String value, boolean isSave) {
		String strReturn = value;
		if (value != null) {
			this.isSave = isSave;
			boolean isAtt = false;
			// if(name.indexOf("&")>0){
			// isAtt=true;
			// throw new IllegalArgumentException("不能对节点的属性设置多个值");
			// }
			// Object objTmp=propertyCache.get(name);
			// if(objTmp!=null){
			// if(objTmp instanceof String){
			//
			// }
			// else if(objTmp instanceof List){
			// return strReturn;
			// ((List)objTmp).add(value);
			// }
			// }
			this.setProperty(name, value);
			// listReturn=value;
			// 同步
			propertyCache.put(name, value);

		}
		// 恢复对象的isSave
		isSave = true;

		return strReturn;
	}

	/**
	 * 更新多个节点内容
	 * 
	 * @param name
	 *            String 规则是：去掉根节点后，节点名+.+节点名， 包括路径、命名空间。不能设置节点属性
	 * @param value
	 *            要设置的值列表
	 * @param isSave
	 *            是否保存改后的文件
	 * @return 添加的内容
	 * @author jdh at 0331
	 */

	public List updateProperty(String name, List value, boolean isSave) {
		List listReturn = value;
		if (value != null) {
			this.isSave = isSave;
			if (name.indexOf("&") > 0) {
				throw new IllegalArgumentException("不能对节点的属性修改多个值");
			}
			Object objTmp = propertyCache.get(name);
			if (objTmp != null) {
				if (objTmp instanceof String) {
					// value.add(objTmp.toString());
				} else if (objTmp instanceof List) {
					int objTmpSize = ((List) objTmp).size();
					int diff = objTmpSize - value.size();
					while (diff > 0) {
						value.add(((List) objTmp).remove(--objTmpSize));
						diff--;
					}
					// value.addAll((List)objTmp);
				}
			}
			this.setProperty(name, value);
			listReturn = value;
			// 同步
			propertyCache.put(name, value);
		}
		// 恢复对象的isSave
		isSave = true;
		return listReturn;

	}

	/**
	 * 添加多个节点内容
	 * 
	 * @param name
	 *            String 规则是：去掉根节点后，节点名+.+节点名， 包括路径、命名空间。不能添加属性值
	 * @param value
	 *            要添加的内容值列表
	 * @param isSave
	 *            是否保存改后的文件
	 * @return 添加的内容
	 * @author jdh at 0331
	 */
	public List addProperty(String name, List value, boolean isSave) {
		List listReturn = value;
		if (value != null) {
			this.isSave = isSave;
			if (name.indexOf("&") > 0) {
				throw new IllegalArgumentException("不能对节点的属性设置多个值");
			}
			Object objTmp = propertyCache.get(name);
			if (objTmp != null) {
				if (objTmp instanceof String) {
					value.add(objTmp.toString());
				} else if (objTmp instanceof List) {
					value.addAll((List) objTmp);
				}
			}
			this.setProperty(name, value);
			listReturn = value;
			// 同步
			propertyCache.put(name, value);
		}
		// 恢复对象的isSave
		isSave = true;
		return listReturn;
	}

	/**
	 * 删除节点属性或节点
	 * 
	 * @param name
	 *            名称 String 规则是：去掉根节点后，节点名+.+节点名，如果是属性，+&+属性名。
	 *            要设置属性的全名，包括路径、命名空间。
	 * @param isSave
	 *            是否保存改后的文件
	 * @return true/false 成功/失败
	 * @author jdh at 0331
	 */
	public boolean deleteProperty(String name, boolean isSave) {
		boolean blReturn = false;
		if (name == null) {
			return blReturn;
		} else {
			this.isSave = isSave;
			boolean isAtt = false; // 是否删除节点属性
			if (name.indexOf("&") > 0) {
				isAtt = true;
			}
			if (isAtt) {
				this.deleteAttribute(name);
			} else {
				this.deleteProperty(name);
			}
			// 同步
			propertyCache.remove(name);
		}
		// 恢复对象的isSave
		isSave = true;
		blReturn = true;
		return blReturn;
	}

	/**
	 * 删除一个节点
	 * 
	 * @param name
	 *            String 规则是：去掉根节点后，节点名+.+节点名，如果是属性，+&+属性名。 要设置属性的全名，包括路径、命名空间。
	 * @author jdh at 0331
	 */
	private void deleteNode(String name) {
		Object objTmp = propertyCache.get(name);
		if (objTmp != null) {
			if (objTmp instanceof String) {

			} else if (objTmp instanceof List) {
				if (((List) objTmp).size() > 0) {
					((List) objTmp).remove(0);
					this.setProperty(name, ((List) objTmp));
				}
			}
		}

	}

	/**
	 * 删除节点属性
	 * 
	 * @param name
	 *            String 规则是：去掉根节点后，节点名+.+节点名，如果是属性，+&+属性名。 要设置属性的全名，包括路径、命名空间。
	 * @author jdh at 0331
	 */
	private void deleteAttribute(String name) {
		if (name == null)
			return;
		this.setProperty(name, null);
		// boolean isAtt = false; //是否删除节点属性
		// if (name.indexOf("&") > 0) {
		// isAtt = true;
		// }

		// String[] propName = parsePropertyName(name);
		// if (isAtt) {
		// if (propName.length > 0) {
		// String last = propName[propName.length - 1];
		// String[] tmp = new String[propName if (propName.length > 0) {
		// String last = propName[propName.length - 1];
		// String[] tmp = new String[propName.length + 1];
		// System.arraycopy(propName, 0, tmp, 0, propName.length);
		// tmp[propName.length - 1] = last.substring(0, last.indexOf("&"));
		// tmp[tmp.length - 1] = last.substring(last.indexOf("&") + 1);
		// propName = tmp;
		// }.length + 1];
		// System.arraycopy(propName, 0, tmp, 0, propName.length);
		// tmp[propName.length - 1] = last.substring(0, last.indexOf("&"));
		// tmp[tmp.length - 1] = last.substring(last.indexOf("&") + 1);
		// propName = tmp;
		// }
		// }

		// Search for this property by traversing down the XML heirarchy.
		// Element element = doc.getRootElement();
		// for (int i = 0; i < propName.length - 1; i++) {
		// element = element.getChild(propName[i]);
		// // Can't find the property so return.
		// if (element == null) {
		// return;
		// }
		// }
		// element.removeAttribute(name);

		// .. then write to disk.
		// if (isSave) {
		// saveProperties();
		// }

	}

	/**
	 * 删除指定节点，如果是对应多个节点，只能删除一个节点， 如果要删除所有节点，可以通过循环方式删除。
	 * 
	 * @param name
	 *            the property to delete.
	 * @deprecated
	 * @see public boolean deleteProperty(String name,boolean isSave)
	 */
	public void deleteProperty(String name) {
		String[] propName = parsePropertyName(name);
		// Search for this property by traversing down the XML heirarchy.
		Element element = doc.getRootElement();
		for (int i = 0; i < propName.length - 1; i++) {
			element = element.getChild(propName[i]);
			// Can't find the property so return.
			if (element == null) {
				return;
			}
		}
		// Found the correct element to remove, so remove it...
		element.removeChild(propName[propName.length - 1]);

		// .. then write to disk.
		if (isSave) {
			saveProperties();
		}
	}

	/**
	 * Saves the properties to disk as an XML document. A temporary file is used
	 * during the writing process for maximum safety.
	 */
	private synchronized void saveProperties() {
		OutputStream out = null;
		boolean error = false;
		// Write data out to a temporary file first.
		File tempFile = null;
		try {
			tempFile = new File(file.getParentFile(), file.getName() + ".tmp");
			// Use JDOM's XMLOutputter to do the writing and formatting. The
			// file should always come out pretty-printed.
			//Format xmlFormat = Format.getCompactFormat();
			//xmlFormat.setEncoding("gb2312");
			//xmlFormat.setIndent("  ");
//			XMLOutputter xmlOut = new XMLOutputter(xmlFormat);
			XMLOutputter xmlOut = new XMLOutputter();
			// "GBK"
			out = new BufferedOutputStream(new FileOutputStream(tempFile));
			xmlOut.output(doc, out);
		} catch (Exception e) {
			e.printStackTrace();
			// There were errors so abort replacing the old property file.
			error = true;
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
				error = true;
			}
		}
		// No errors occured, so we should be safe in replacing the old
		if (!error) {
			// Delete the old file so we can replace it.
			file.delete();
			// Rename the temp file. The delete and rename won't be an
			// automic operation, but we should be pretty safe in general.
			// At the very least, the temp file should remain in some form.
			tempFile.renameTo(file);
		}
	}

	/**
	 * Returns an array representation of the given Jive property. Jive
	 * properties are always in the format "prop.name.is.this" which would be
	 * represented as an array of four Strings.
	 * 
	 * @param name
	 *            the name of the Jive property.
	 * @return an array representation of the given Jive property.
	 */
	private String[] parsePropertyName(String name) {
		// changed by jdh at 20040114,delete codes followes,because not needed.
		// Figure out the number of parts of the name (this becomes the size
		// of the resulting array).
		// int size = 1;
		// for (int i = 0; i < name.length(); i++) {
		// if (name.charAt(i) == '.') {
		// size++;
		// }
		// }
		// CommonSystemOut.SystemOutFun("size is "+size);
		// String[] propName = new String[size];
		// end changed
		// Use a StringTokenizer to tokenize the property name.
		StringTokenizer tokenizer = new StringTokenizer(name, ".");
		// added by jdh at 20040114
		// CommonSystemOut.SystemOutFun("tokenizer.countTokens() is
		// "+tokenizer.countTokens());
		String[] propName = new String[tokenizer.countTokens()];
		// end added
		int i = 0;
		while (tokenizer.hasMoreTokens()) {
			propName[i] = tokenizer.nextToken();
			i++;
		}
		return propName;
	}

	public static void main(String[] args) {
		String file = "test.xml";
		XMLProperties xmlProperties = new XMLProperties(file, true);

		// 1.
		// CommonSystemOut.SystemOutFun(xmlProperties.getProperty("b.c"));

		// 2.
		// CommonSystemOut.SystemOutFun(xmlProperties.getPropertyValue("b.c"));

		// 3.
		// Object listReturn = xmlProperties.getPropertyValue("b.c");
		// CommonSystemOut.SystemOutFun(""+listReturn);
		// Iterator it = listReturn.iterator();
		// while (it.hasNext()) {
		// String element1 = (String) it.next();
		// CommonSystemOut.SystemOutFun(element1);
		// }

		// 4.
		// String[] pools = xmlProperties.getChildrenProperties("database");
		// CommonSystemOut.SystemOutFun(pools.length);
		// for (int i = 0; i < pools.length; i++) {
		// CommonSystemOut.SystemOutFun("pool" + i + "=" + pools[i]);
		// }

		// 5.
		// xmlProperties.deleteProperty("b.w");

	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}