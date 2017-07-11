package com.ces.config.utils;

/**
 * <p>描述: 常量集合</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * 
 * @author qiucs
 * @date 2013-6-27 下午9:55:02
 */
public interface ConstantVar {
    /** 自定义查询过滤条件在session中的存储属性名. */
    public String QUERY_FILTER = "__QUERY_FILTER__";

    /** 自定义查询排序条件在session中的存储属性名. */
    public String QUERY_SORT = "__QUERY_SORT__";

    /** 字段名称常量集 */
    interface ColumnName {
        /** ID字段名称 */
        String ID = "ID";

        /** CREATE_TIME字段名称 */
        String CREATE_TIME = "CREATE_TIME";

        /** CREATE_USER字段名称 */
        String CREATE_USER = "CREATE_USER";

        /** UPDATE_TIME字段名称 */
        String UPDATE_TIME = "UPDATE_TIME";

        /** UPDATE_USER字段名称 */
        String UPDATE_USER = "UPDATE_USER";

        /** DELETE_TIME字段名称 */
        String DELETE_TIME = "DELETE_TIME";

        /** UPDATE_USER字段名称 */
        String DELETE_USER = "DELETE_USER";

        /** IS_DELETE字段名称 */
        String IS_DELETE = "IS_DELETE";
    }

    /** 动态表名前缀 */
    interface TablePrefix {
        /** 档案表名前缀 */
        String PRE_AR = "T_AR_";

        /** 预置表名前缀 */
        String PRE_PS = "T_PS_";

        /** 自定义表名前缀 */
        String PRE_DF = "T_DF_";
    }

    /** 表分类 */
    interface TableClassification {
        /** 档案表 */
        String ARCHIVE = "A";

        /** 模板表 */
        String TEMPLATE = "T";

        /** 预置表 */
        String PRESET = "P";

        /** 自定义表 */
        String DEFINE = "D";

        /** 系统视图 */
        String VIEW = "V";

        /** 层次模版 */
        String HIERARCHY = "H";

        /** 库类型 */
        String STOREKIND = "K";

    }

    /** 自定义数据库字段数据类型【未转换的】 */
    interface DataType {
        /** 字符类型 */
        String CHAR = "c";

        /** 数字类型 */
        String NUMBER = "n";

        /** 日期类型 */
        String DATE = "d";

        /** 枚举类型或编码类型 */
        String ENUM = "e";

        /** 用户类型 */
        String USER = "u";

        /** 部门类型 */
        String PART = "p";
    }

    /** DHTMLX 表单元素类型 **/
    interface DhtmlxFormItemType {
        /** 隐藏框 */
        String HIDDEN = "hidden";

        /** 文本框 */
        String INPUT = "input";

        /** 文本域 */
        String TEXTAREA = "textarea";

        /** 下拉框 */
        String COMBO = "combo";

        /** 日期 */
        String CALENDAR = "calendar";
    }

    /** 报表定义. */
    interface Report {
        /** cll文件在项目中相对路径 **/
        String PATH = "/views/config/appmanage/report/cll/";
    }

    /** 应用自定义. */
    interface AppDefine {
        /** 是否使用默认配置. */
        boolean USE_DAFAULT = true;
    }

    /** 索引前缀. */
    interface IndexPrefix {
        /** 表关系索引前缀. */
        String RELATION = "RLT";

        /** 树定义字段类型节点的索引前缀. */
        String TREE = "TRE";
    }

    /** 表标签 */
    interface Labels {
        /** 电子全文 */
        interface Document {
            String CODE = "DOCUMENT";

            String NAME = "电子全文";

            /** 电子全文字段标签 */
            interface Columns {
                /** 字段标签编码 */
                interface Code {
                    /** 原名称 */
                    String ORIGIN_NAME = "origin_name";

                    /** 新名称 */
                    String REAL_NAME = "real_name";

                    /** 文件大小 */
                    String FILE_SIZE = "file_size";

                    /** 文件格式 */
                    String FILE_FORMAT = "file_format";

                    /** 存储位置 */
                    String LOCATION = "location";

                    /** 相对路径 */
                    String PATH = "path";

                    /** 上传人 */
                    String CREATE_USER = "create_user";

                    /** 上传时间 */
                    String CREATE_TIME = "create_time";

                    /** 所属数据Id */
                    String OWNER_ID = "owner_id";

                    /** 所属业务表Id */
                    String OWNER_TABLE_ID = "owner_table_id";

                    /** 电子全文绝对路径 */
                    String PAHT_ALL = "path_all";
                }

                /** 字段标签名称 */
                interface Name {
                    /** 原名称 */
                    String ORIGIN_NAME = "原名称";

                    /** 新名称 */
                    String REAL_NAME = "新名称";

                    /** 文件大小 */
                    String FILE_SIZE = "文件大小";

                    /** 文件格式 */
                    String FILE_FORMAT = "文件格式";

                    /** 存储位置 */
                    String LOCATION = "存储位置";

                    /** 相对路径 */
                    String PATH = "相对路径";

                    /** 上传人 */
                    String CREATE_USER = "上传人";

                    /** 上传时间 */
                    String CREATE_TIME = "上传时间";

                    /** 所属数据Id */
                    String OWNER_ID = "所属数据ID";

                    /** 所属业务表Id" */
                    String OWNER_TABLE_ID = "所属业务表ID";
                }
            }
        }

        /** 卷内 */
        interface File {
            String CODE = "FILE";

            String NAME = "卷内";

            interface Columns {
                /** 字段标签编码 */
                interface Code {

                }

                /** 字段标签名称 */
                interface Name {

                }
            }
        }

        /** 案卷 */
        interface Folder {
            String CODE = "FOLDER";

            String NAME = "案卷";

            interface Columns {
                /** 字段标签编码 */
                interface Code {

                }

                /** 字段标签名称 */
                interface Name {

                }
            }
        }

        /** 项目 */
        interface Project {

            String CODE = "FILE";

            String NAME = "项目";

            interface Columns {
                /** 字段标签编码 */
                interface Code {

                }

                /** 字段标签名称 */
                interface Name {

                }
            }
        }

        /** 审批意见 */
        interface ConfirmOpinion {

            String CODE = "CONFIRM_OPINION";

            String NAME = "审批意见";

            interface Columns {
                /** 字段标签编码 */
                interface Code {

                }

                /** 字段标签名称 */
                interface Name {

                }
            }
        }

        /** 辅助意见 */
        interface AssistOpinion {

            String CODE = "ASSIST_OPINION";

            String NAME = "辅助意见";

            interface Columns {
                /** 字段标签编码 */
                interface Code {

                }

                /** 字段标签名称 */
                interface Name {

                }
            }
        }
    }

    /** 树节点图标. */
    interface IconTreeNode {
        /** 打开节点图标. */
        String FOLDER_OPEN = "folderOpen.gif";

        /** 关闭节点图标. */
        String FOLDER_CLOSE = "folderClosed.gif";

        /** 叶子节点图标. */
        String LEAF = "leaf.gif";
    }

    /** 构件 */
    interface Component {
        /** 构件类型 */
        interface Type {
            /** 公用构件 */
            String COMMON = "0";

            /** 页面构件 */
            String PAGE = "1";

            /** 逻辑构件 */
            String LOGIC = "2";

            /** 树构件 */
            String TREE = "3";

            /** 物理表构件 */
            String PHYSICAL_TABLE = "4";

            /** 逻辑表构件 */
            String LOGIC_TABLE = "5";

            /** 通用表构件 */
            String NO_TABLE = "6";

            /** 标签页构件 */
            String TAB = "7";

            /** 中转器构件 */
            String TRANSFER_DEVICE = "8";

            /** 组合构件 */
            String ASSEMBLY = "9";

            /** 开发的构件 */
            String DEVELOP = "0,1,2,8";

            /** 自定义构件 */
            String SELF_DEFINE = "3,4,5,6";
        }

        /** 构件前台类型 */
        interface View {
            /** dhtmlx */
            String DHTMLX = "dhtmlx";

            /** CORAL40 */
            String CORAL40 = "coral40";
        }

        /** 构件打包 */
        interface Packaged {
            /** 未打包 */
            String NO = "0";

            /** 打包 */
            String YES = "1";
        }

        /** 绑定系统参数 */
        interface SystemParamConfig {
            /** 未绑定 */
            String NOT_FINISHED = "0";

            /** 绑定完成 */
            String FINISHED = "1";

            /** 无需绑定 */
            String NOT_NEED = "2";
        }

        /** 是否应用到本系统 */
        interface SystemUsed {
            /** 否 */
            String NO = "0";

            /** 是 */
            String YES = "1";
        }

        /** 预留区类型 */
        interface ReserveZoneType {
            /** 工具条 */
            String TOOLBAR = "0";

            /** 列表超链接 */
            String GRID_LINK = "1";

            /** 按钮预留区 */
            String BUTTON = "2";

            /** 树节点预留区 */
            String TREE_NODE = "3";

            /** 标签页预留区 */
            String TAB_PAGE = "4";

            /** 中转器预留区 */
            String TRANSFER_DEVICE = "5";

            /** 树预留区 */
            String TREE = "TREE";
        }
    }

    /** 菜单 */
    interface Menu {
        /** 绑定类型 */
        interface BindingType {
            /** 未绑定 */
            String NOT_BINDING = "";

            /** 绑定URL */
            String URL = "0";

            /** 绑定构件 */
            String COMPONENT = "1";
        }
    }

    /** 是否被删除字段名 **/
    public String DELETE_COLUMN_NAME = "DELETE_FLAG";

    /** 是否被删除 **/
    interface IS_DELETE {
        /** 没有被删除 **/
        String NO_DELETE = "0";

        /** 已经被删除 **/
        String YES_DELETE = "1";
    }

    /** 模块号(临时库/正式库) **/
    public String MODULE_COLUMN_NAME = "status";

    /** 模块类型 **/
    interface MODULE_TYPE {
        /** 临时库 */
        String TEMP = "60";

        /** 正式库 */
        String FORMAL = "80";
    }

    /** 通用主键字段 **/
    public String USER_COLUMN_ID = "id";

    /** 通用用户名字段 **/
    public String USER_COLUMN_NAME = "username";

    /** 当前时间字段 **/
    public String DATE_COLUMN_NAME = "date";

    interface UI {
        /** 组件库4.0路径. **/
        String CUI_FOLDER = "/cfg-resource/coral40";

        /** DHTMLX路径. **/
        String DHX_FOLDER = "/cfg-resource/dhtmlx";
    }

    /** 当前登录用户信息 **/
    interface CurrentValue {
        /** 当前登录人员标记符 */
        String USER = "_CURRENT_USER_";

        /** 当前登录部门标记符 */
        String DEPT = "_CURRENT_DEPT_";

        /** 当前登录部门标记符 */
        String DATE = "_CURRENT_DATE_";

        /** 当前登录组织标记符 */
        String ORG = "_CURRENT_ORG_";
    }

    /** 判断是非 **/
    interface Judgment {
        /** 是 **/
        String YES = "1";

        /** 否 **/
        String NO = "0";
    }

    /** 校验规则 **/
    interface Validation {
        /** 设置最大日期 **/
        String MAXDATE = "max";

        /** 设置最小日期 **/
        String MINDATE = "min";

        /** 正则表达式 **/
        String PATTERN = "pattern";
    }

    /** 权限审批 **/
    interface AuthorityApprove {
        /** 待审批的权限类型 1-树权限 2-数据权限 3-编码权限 */
        interface Type {
            String TREE = "1";

            String DATA = "2";

            String CODE = "3";
        }

        /** 状态 0-待审批 1-审批通过 2-审批退回 */
        interface Status {
            String APPROVING = "0";

            String APPROVED_SUCCESS = "1";

            String APPROVED_BACK = "2";
        }

        /** 操作类型 1-新增 2-修改 3-删除 */
        interface Operate {
            String NEW = "1";

            String UPDATE = "2";

            String DELETE = "3";
        }
    }
}
