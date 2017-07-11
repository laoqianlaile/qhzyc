/*

function oneli() {
        $(".li4").toggle();
        $(".li5").hide();
        $(".li6").hide();
        $(".li1").addClass("sd_bj_border").siblings().removeClass("sd_bj_border");

    $(".sd_submenu dd a").click(function () {
        $(".sd_submenu").hide()
    })
}
function towli() {

        $(".li5").toggle();
        $(".li4").hide();
        $(".li6").hide();
    $(".li2").addClass("sd_bj_border").siblings().removeClass("sd_bj_border");


    $(".sd_submenu dd a").click(function () {
        $(".sd_submenu").hide()
    })
}

function threeli() {

        $(".li6").toggle();
        $(".li5").hide();
        $(".li4").hide();
        $(".li3").addClass("sd_bj_border").siblings().removeClass("sd_bj_border");
    $(".sd_submenu dd a").click(function () {
        $(".sd_submenu").hide()
    })
}






*/

function oneli(){
    $(".li1").mousemove(function () {
        $(".li4").show();
        $(".li1").addClass("sd_bj_border").parent("li").children("a").removeClass("sd_bj_border")
    });
    $(".li1").mouseleave(function () {
        $(".li4").hide();
        $(".li1").removeClass("sd_bj_border")
    });
    $(".li4").mousemove(function () {
        $(".li4").show();
        $(".li1").addClass("sd_bj_border").parent("li").children("a").removeClass("sd_bj_border")
    });
    $(".li4").mouseleave(function () {
        $(".li4").hide();
        $(".li1").removeClass("sd_bj_border")
    });
}
function towli() {
    $(".li2").mousemove(function () {
        $(".li5").show();
        $(".li2").addClass("sd_bj_border").parent("li").children("a").removeClass("sd_bj_border")
    });
    $(".li5").mousemove(function () {
        $(".li5").show();
        $(".li2").addClass("sd_bj_border").parent("li").children("a").removeClass("sd_bj_border")
    });
    $(".li2").mouseleave(function () {
        $(".li5").hide();
        $(".li2").removeClass("sd_bj_border")
    });
    $(".li5").mouseleave(function () {
        $(".li5").hide();
        $(".li2").removeClass("sd_bj_border")
    });
}

function threeli() {
    $(".li3").mousemove(function () {
        $(".li6").show();
        $(".li3").addClass("sd_bj_border").parent("li").children("a").removeClass("sd_bj_border")
    });
    $(".li6").mousemove(function () {
        $(".li6").show();
        $(".li3").addClass("sd_bj_border").parent("li").children("a").removeClass("sd_bj_border")
    });
    $(".li3").mouseleave(function () {
        $(".li6").hide();
        $(".li3").removeClass("sd_bj_border")
    });
    $(".li6").mouseleave(function () {
        $(".li6").hide();
        $(".li3").removeClass("sd_bj_border")
    });
}







