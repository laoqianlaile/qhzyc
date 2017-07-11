<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!--main-->
<div class="main">
    <!--slide left-->
    <div class="slide picScroll-left" id="slide-left">
        <div class="hd">
            <ul></ul>
        </div>
        <div class="bd">
            <ul class="picList">
                <li>
                    <div class="pic">
                        <a href="#">
                            <img src="css/images/slide-sm/1.png" />
                        </a>
                    </div>
                    <div class="title"><a href="#">采收管理</a></div>
                </li>
                <li>
                    <div class="pic">
                        <a href="#">
                            <img src="css/images/slide-sm/2.png" />
                        </a>
                    </div>
                    <div class="title"><a href="#">原料入库</a></div>
                </li>
                <li>
                    <div class="pic">
                        <a href="#">
                            <img src="css/images/slide-sm/3.png" />
                        </a>
                    </div>
                    <div class="title"><a href="#">药材交易</a></div>
                </li>
                <li>
                    <div class="pic">
                        <a href="#">
                            <img src="css/images/slide-sm/4.png" />
                        </a>
                    </div>
                    <div class="title"><a href="#">药材库存</a></div>
                </li>
                <li>
                    <div class="pic">
                        <a href="#">
                            <img src="css/images/slide-sm/5.png" />
                        </a>
                    </div>
                    <div class="title"><a href="#">药材入库</a></div>
                </li>
                <li>
                    <div class="pic">
                        <a href="#">
                            <img src="css/images/slide-sm/6.png" />
                        </a>
                    </div>
                    <div class="title"><a href="#">饮片库存</a></div>
                </li>
                <li>
                    <div class="pic">
                        <a href="#">
                            <img src="css/images/slide-sm/7.png" />
                        </a>
                    </div>
                    <div class="title"><a href="#">种植任务</a></div>
                </li>
            </ul>
        </div>

        <a class="prev" href="javascript:void(0)"></a>
        <a class="next" href="javascript:void(0)"></a>
    </div>
    <!--slide left end-->

    <!--slide-->
    <div class="slide slideBox" id="slideBox">
        <div class="hd">
            <ul></ul>
        </div>
        <div class="bd">
            <ul>
                <li><a href="#"><img src="css/images/slide-lg/1.jpg" /></a></li>
                <li><a href="#"><img src="css/images/slide-lg/2.jpg" /></a></li>
            </ul>
        </div>

        <!-- 下面是前/后按钮代码，如果不需要删除即可 -->
        <a class="prev" href="javascript:void(0)"></a>
        <a class="next" href="javascript:void(0)"></a>
    </div>
    <!--slide end-->

</div>
<!--main end-->

<!--footer-->
<div class="footer">
    <p>COPYRIGHT 2016 上海中信信息发展股份有限公司 ALL RIGHTS RESERVED</p>
</div>
<!--footer end-->

<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/jquery.SuperSlide.2.1.1.js"></script>
<script type="text/javascript">
    $("#slide-left").slide({titCell:".hd ul",mainCell:".bd ul",autoPage:true,effect:"left",vis:6});
    $("#slideBox").slide({titCell:".hd ul",mainCell:".bd ul",autoPage:true,autoPlay:true,effect:"left",vis:1});
</script>