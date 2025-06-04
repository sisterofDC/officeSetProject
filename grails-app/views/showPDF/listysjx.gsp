<g:if test="${session.user}">
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>内江市“码”上审预算</title>
    <script src="js/jquery.min.js"></script>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/iphone.css">
</head>

<body>
    <!-- 头部 -->
    <div class="header">
        <a href="list.gsp">
            <img class="pc" src="images/titpc.png" alt="">
            <img class="iphone" src="images/tit.png" alt="">
        </a>
    </div>

    <!-- 内容 -->
    <div class="main">
        <div class="mktit">
            <img src="images/ysjx.jpg" alt="预算绩效">
        </div>
        <div id="cont1" class="mk">
            <div class="mktit2">
                <span>市级部门整体支出绩效目标</span>
            </div>
            <div class="mkcont">
                <ul class="list2">
                    <li><a target="_blank" href="pdf/101-市委2025年度部门整体目标填报表.pdf">市委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/103-市人大2025年度部门整体目标填报表.pdf">市人大2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/104-市政协2025年度部门整体目标填报表.pdf">市政协2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/105-市纪委2025年度部门整体目标填报表.pdf">市纪委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/106-市委组织部2025年度部门整体目标填报表.pdf">市委组织部2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/107-市委宣传部2025年度部门整体目标填报表.pdf">市委宣传部2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/108-市委统战部2025年度部门整体目标填报表.pdf">市委统战部2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/109-市直机关工委2025年度部门整体目标填报表.pdf">市直机关工委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/111-市委社会工作部2025年度部门整体目标填报表.pdf">市委社会工作部2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/112-市委老干局2025年度部门整体目标填报表.pdf">市委老干局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/114-市信访局2025年度部门整体目标填报表.pdf">市信访局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/115-市委党史地方志研究室2025年度部门整体目标填报表.pdf">市委党史地方志研究室2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/117-市委机构编制委员会2025年度部门整体目标填报表.pdf">市委机构编制委员会2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/118-市文联2025年度部门整体目标填报表.pdf">市文联2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/119-市委巡察办2025年度部门整体目标填报表.pdf">市委巡察办2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/120-市委网信办2025年度部门整体目标填报表.pdf">市委网信办2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/121-市委政研室2025年度部门整体目标填报表.pdf">市委政研室2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/122-市政府驻成都办2025年度部门整体目标填报表.pdf">市政府驻成都办2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/201-市政法委2025年度部门整体目标填报表.pdf">市政法委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/202-市公安局2025年度部门整体目标填报表.pdf">市公安局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/203-市交警支队2025年度部门整体目标填报表.pdf">市交警支队2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/204-市检察院2025年度部门整体目标填报表.pdf">市检察院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/205-市中级人民法院2025年度部门整体目标填报表.pdf">市中级人民法院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/206-市司法局2025年度部门整体目标填报表.pdf">市司法局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/211-市中区检察院2025年度部门整体目标填报表.pdf">市中区检察院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/212-市中区法院2025年度部门整体目标填报表.pdf">市中区法院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/213-东兴区检察院2025年度部门整体目标填报表.pdf">东兴区检察院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/214-东兴区法院2025年度部门整体目标填报表.pdf">东兴区法院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/215-资中县检察院2025年度部门整体目标填报表.pdf">资中县检察院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/216-资中县法院2025年度部门整体目标填报表.pdf">资中县法院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/217-威远县检察院2025年度部门整体目标填报表.pdf">威远县检察院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/218-威远县法院2025年度部门整体目标填报表.pdf">威远县法院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/219-隆昌市检察院2025年度部门整体目标填报表.pdf">隆昌市检察院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/220-隆昌市法院2025年度部门整体目标填报表.pdf">隆昌市法院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/305-市教育和体育局2025年度部门整体目标填报表.pdf">市教育和体育局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/307-市审计局2025年度部门整体目标填报表.pdf">市审计局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/308-市体育事业发展中心2025年度部门整体目标填报表.pdf">市体育事业发展中心2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/309-市统计局2025年度部门整体目标填报表.pdf">市统计局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/310-市文广旅局2025年度部门整体目标填报表.pdf">市文广旅局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/311-市科技局2025年度部门整体目标填报表.pdf">市科技局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/312-市自然资源和规划局2025年度部门整体目标填报表.pdf">市自然资源和规划局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/313-市林业局2025年度部门整体目标填报表.pdf">市林业局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/314-市农业农村局2025年度部门整体目标填报表.pdf">市农业农村局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/315-市水利局2025年度部门整体目标填报表.pdf">市水利局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/319-市商务局2025年度部门整体目标填报表.pdf">市商务局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/322-市生态环境局2025年度部门整体目标填报表.pdf">市生态环境局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/323-市经信局2025年度部门整体目标填报表.pdf">市经信局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/324-市住房和城乡建设局2025年度部门整体目标填报表.pdf">市住房和城乡建设局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/326-市发改委2025年度部门整体目标填报表.pdf">市发改委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/327-市交通局2025年度部门整体目标填报表.pdf">市交通局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/328-市城市管理行政执法局2025年度部门整体目标填报表.pdf">市城市管理行政执法局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/329-市人社局2025年度部门整体目标填报表.pdf">市人社局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/330-市民政局2025年度部门整体目标填报表.pdf">市民政局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/331-市卫健委2025年度部门整体目标填报表.pdf">市卫健委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/332-市应急管理局2025年度部门整体目标填报表.pdf">市应急管理局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/333-市人防办2025年度部门整体目标填报表.pdf">市人防办2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/334-市国资委2025年度部门整体目标填报表.pdf">市国资委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/337-市市场监督管理局2025年度部门整体目标填报表.pdf">市市场监督管理局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/338-市退役军人事务局2025年度部门整体目标填报表.pdf">市退役军人事务局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/339-市经济合作局2025年度部门整体目标填报表.pdf">市经济合作局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/340-市医疗保障局2025年度部门整体目标填报表.pdf">市医疗保障局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/341-市农科院2025年度部门整体目标填报表.pdf">市农科院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/402-市消防支队2025年度部门整体目标填报表.pdf">市消防支队2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/501-市妇联2025年度部门整体目标填报表.pdf">市妇联2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/502-市工商联2025年度部门整体目标填报表.pdf">市工商联2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/504-市侨联2025年度部门整体目标填报表.pdf">市侨联2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/505-市团委2025年度部门整体目标填报表.pdf">市团委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/506-市科协2025年度部门整体目标填报表.pdf">市科协2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/507-市残联2025年度部门整体目标填报表.pdf">市残联2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/508-市总工会2025年度部门整体目标填报表.pdf">市总工会2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/509-市红十字会2025年度部门整体目标填报表.pdf">市红十字会2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/510-民革内江市委2025年度部门整体目标填报表.pdf">民革内江市委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/511-民盟内江市委2025年度部门整体目标填报表.pdf">民盟内江市委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/512-民建内江市委2025年度部门整体目标填报表.pdf">民建内江市委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/513-民进内江市委2025年度部门整体目标填报表.pdf">民进内江市委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/514-农工党内江市委2025年度部门整体目标填报表.pdf">农工党内江市委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/515-九三学社内江市委2025年度部门整体目标填报表.pdf">九三学社内江市委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/601-市委党校2025年度部门整体目标填报表.pdf">市委党校2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/602-市档案馆2025年度部门整体目标填报表.pdf">市档案馆2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/605-市融媒体中心2025年度部门整体目标填报表.pdf">市融媒体中心2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/606-市大数据中心2025年度部门整体目标填报表.pdf">市大数据中心2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/609-市公共资源交易服务中心2025年度部门整体目标填报表.pdf">市公共资源交易服务中心2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/611-市职业技术学院2025年度部门整体目标填报表.pdf">市职业技术学院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/612-市政务和公共资源管理办2025年度部门整体目标填报表.pdf">市政务和公共资源管理办2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/613-川南幼师2025年度部门整体目标填报表.pdf">川南幼师2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/614-市气象局2025年度部门整体目标填报表.pdf">市气象局2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/615-内江师院2025年度部门整体目标填报表.pdf">内江师院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/616-市卫健院2025年度部门整体目标填报表.pdf">市卫健院2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/618-市供销社2025年度部门整体目标填报表.pdf">市供销社2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/621-内江物港委2025年度部门整体目标填报表.pdf">内江物港委2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/629-市住房保障和房地产事务中心2025年度部门整体目标填报表.pdf">市住房保障和房地产事务中心2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/631-市住房公积金管理中心2025年度部门整体目标填报表.pdf">市住房公积金管理中心2025年度部门整体目标填报表</a></li>
                    <li><a target="_blank" href="pdf/632-市口岸与物流发展中心2025年度部门整体目标填报表.pdf">市口岸与物流发展中心2025年度部门整体目标填报表</a></li>
                </ul>
            </div>
        </div>
        <div id="cont2" class="mk">
            <div class="mktit2">
                <span>市级项目支出绩效</span>
            </div>
            <div class="mkcont">
                <ul class="list2">
                    <li><a target="_blank" href="pdf/101001-中国共产党内江市委员会办公室.pdf">中国共产党内江市委员会办公室</a></li>
                    <li><a target="_blank" href="pdf/102001-内江市人民政府办公室.pdf">内江市人民政府办公室</a></li>
                    <li><a target="_blank" href="pdf/102604-内江市人民政府驻北京联络处.pdf">内江市人民政府驻北京联络处</a></li>
                    <li><a target="_blank" href="pdf/103001-内江市人民代表大会常务委员会办公室.pdf">内江市人民代表大会常务委员会办公室</a></li>
                    <li><a target="_blank" href="pdf/104001-中国人民政治协商会议四川省内江市委员会办公室.pdf">中国人民政治协商会议四川省内江市委员会办公室</a></li>
                    <li><a target="_blank" href="pdf/105001-中国共产党内江市纪律检查委员会.pdf">中国共产党内江市纪律检查委员会</a></li>
                    <li><a target="_blank" href="pdf/106001-中国共产党内江市委员会组织部.pdf">中国共产党内江市委员会组织部</a></li>
                    <li><a target="_blank" href="pdf/107001-中国共产党内江市委员会宣传部.pdf">中国共产党内江市委员会宣传部</a></li>
                    <li><a target="_blank" href="pdf/108001-中国共产党内江市委员会统一战线工作部.pdf">中国共产党内江市委员会统一战线工作部</a></li>
                    <li><a target="_blank" href="pdf/109001-中共内江市委市直属机关工作委员会.pdf">中共内江市委市直属机关工作委员会</a></li>
                    <li><a target="_blank" href="pdf/110001-中共内江市委保密机要局.pdf">中共内江市委保密机要局</a></li>
                    <li><a target="_blank" href="pdf/111001-中共内江市委社会工作部.pdf">中共内江市委社会工作部</a></li>
                    <li><a target="_blank" href="pdf/112001-中国共产党内江市委员会老干部局.pdf">中国共产党内江市委员会老干部局</a></li>
                    <li><a target="_blank" href="pdf/112901-内江市离退休干部和关心下一代工作服务中心.pdf">内江市离退休干部和关心下一代工作服务中心</a></li>
                    <li><a target="_blank" href="pdf/114001-内江市信访局.pdf">内江市信访局</a></li>
                    <li><a target="_blank" href="pdf/115001-中共内江市委党史地方志研究室.pdf">中共内江市委党史地方志研究室</a></li>
                    <li><a target="_blank" href="pdf/117001-中共内江市委机构编制委员会办公室.pdf">中共内江市委机构编制委员会办公室</a></li>
                    <li><a target="_blank" href="pdf/118001-内江市文学艺术界联合会.pdf">内江市文学艺术界联合会</a></li>
                    <li><a target="_blank" href="pdf/119001-中共内江市委巡察工作领导小组办公室.pdf">中共内江市委巡察工作领导小组办公室</a></li>
                    <li><a target="_blank" href="pdf/120001-中共内江市委网络安全和信息化委员会办公室.pdf">中共内江市委网络安全和信息化委员会办公室</a></li>
                    <li><a target="_blank" href="pdf/122001-内江市人民政府驻成都办事处.pdf">内江市人民政府驻成都办事处</a></li>
                    <li><a target="_blank" href="pdf/201001-中国共产党内江市委员会政法委员会.pdf">中国共产党内江市委员会政法委员会</a></li>
                    <li><a target="_blank" href="pdf/202001-内江市公安局.pdf">内江市公安局</a></li>
                    <li><a target="_blank" href="pdf/202902-内江市特殊人员康复救治中心.pdf">内江市特殊人员康复救治中心</a></li>
                    <li><a target="_blank" href="pdf/203001-内江市公安局交通警察支队.pdf">内江市公安局交通警察支队</a></li>
                    <li><a target="_blank" href="pdf/204001-四川省内江市人民检察院.pdf">四川省内江市人民检察院</a></li>
                    <li><a target="_blank" href="pdf/205001-四川省内江市中级人民法院.pdf">四川省内江市中级人民法院</a></li>
                    <li><a target="_blank" href="pdf/206001-内江市司法局.pdf">内江市司法局</a></li>
                    <li><a target="_blank" href="pdf/206901-内江市法律援助中心.pdf">内江市法律援助中心</a></li>
                    <li><a target="_blank" href="pdf/206903-内江市医疗纠纷调解中心.pdf">内江市医疗纠纷调解中心</a></li>
                    <li><a target="_blank" href="pdf/211001-内江市市中区人民检察院.pdf">内江市市中区人民检察院</a></li>
                    <li><a target="_blank" href="pdf/212001-内江市市中区人民法院.pdf">内江市市中区人民法院</a></li>
                    <li><a target="_blank" href="pdf/213001-内江市东兴区人民检察院.pdf">内江市东兴区人民检察院</a></li>
                    <li><a target="_blank" href="pdf/214001-内江市东兴区人民法院.pdf">内江市东兴区人民法院</a></li>
                    <li><a target="_blank" href="pdf/215001-资中县人民检察院.pdf">资中县人民检察院</a></li>
                    <li><a target="_blank" href="pdf/216001-资中县人民法院.pdf">资中县人民法院</a></li>
                    <li><a target="_blank" href="pdf/217001-威远县人民检察院.pdf">威远县人民检察院</a></li>
                    <li><a target="_blank" href="pdf/218001-威远县人民法院.pdf">威远县人民法院</a></li>
                    <li><a target="_blank" href="pdf/219001-隆昌市人民检察院.pdf">隆昌市人民检察院</a></li>
                    <li><a target="_blank" href="pdf/220001-隆昌市人民法院.pdf">隆昌市人民法院</a></li>
                    <li><a target="_blank" href="pdf/301001-内江市财政局.pdf">内江市财政局</a></li>
                    <li><a target="_blank" href="pdf/301902-内江市财政信息管理中心.pdf">内江市财政信息管理中心</a></li>
                    <li><a target="_blank" href="pdf/301903-内江市财政投资评审中心.pdf">内江市财政投资评审中心</a></li>
                    <li><a target="_blank" href="pdf/301906-内江市金融服务中心.pdf">内江市金融服务中心</a></li>
                    <li><a target="_blank" href="pdf/305001-内江市教育和体育局.pdf">内江市教育和体育局</a></li>
                    <li><a target="_blank" href="pdf/305905-内江市教育科学研究所.pdf">内江市教育科学研究所</a></li>
                    <li><a target="_blank" href="pdf/305906-内江市学校后勤工作指导中心.pdf">内江市学校后勤工作指导中心</a></li>
                    <li><a target="_blank" href="pdf/305908-内江市教育考试院.pdf">内江市教育考试院</a></li>
                    <li><a target="_blank" href="pdf/305909-四川省内江市第一中学.pdf">四川省内江市第一中学</a></li>
                    <li><a target="_blank" href="pdf/305910-四川省内江市第二中学.pdf">四川省内江市第二中学</a></li>
                    <li><a target="_blank" href="pdf/305911-四川省内江市第四中学.pdf">四川省内江市第四中学</a></li>
                    <li><a target="_blank" href="pdf/305912-四川省内江市第六中学.pdf">四川省内江市第六中学</a></li>
                    <li><a target="_blank" href="pdf/305913-四川省内江市第七中学.pdf">四川省内江市第七中学</a></li>
                    <li><a target="_blank" href="pdf/305915-四川省内江市第二职业中学校.pdf">四川省内江市第二职业中学校</a></li>
                    <li><a target="_blank" href="pdf/305916-内江开放大学.pdf">内江开放大学</a></li>
                    <li><a target="_blank" href="pdf/305917-内江市桐梓坝小学校.pdf">内江市桐梓坝小学校</a></li>
                    <li><a target="_blank" href="pdf/305922-内江铁路中学.pdf">内江铁路中学</a></li>
                    <li><a target="_blank" href="pdf/307001-内江市审计局.pdf">内江市审计局</a></li>
                    <li><a target="_blank" href="pdf/308001-内江市体育事业发展中心.pdf">内江市体育事业发展中心</a></li>
                    <li><a target="_blank" href="pdf/308901-内江市少年儿童业余体校.pdf">内江市少年儿童业余体校</a></li>
                    <li><a target="_blank" href="pdf/308902-内江市体育中心.pdf">内江市体育中心</a></li>
                    <li><a target="_blank" href="pdf/309001-内江市统计局.pdf">内江市统计局</a></li>
                    <li><a target="_blank" href="pdf/310001-内江市文化广播电视和旅游局.pdf">内江市文化广播电视和旅游局</a></li>
                    <li><a target="_blank" href="pdf/310903-内江市图书馆.pdf">内江市图书馆</a></li>
                    <li><a target="_blank" href="pdf/310904-内江市张大千纪念馆.pdf">内江市张大千纪念馆</a></li>
                    <li><a target="_blank" href="pdf/310905-内江市艺术交流发展中心.pdf">内江市艺术交流发展中心</a></li>
                    <li><a target="_blank" href="pdf/310906-内江市川剧团.pdf">内江市川剧团</a></li>
                    <li><a target="_blank" href="pdf/311001-内江市科学技术局.pdf">内江市科学技术局</a></li>
                    <li><a target="_blank" href="pdf/312001-内江市自然资源和规划局.pdf">内江市自然资源和规划局</a></li>
                    <li><a target="_blank" href="pdf/312907-内江市不动产登记中心.pdf">内江市不动产登记中心</a></li>
                    <li><a target="_blank" href="pdf/313001-内江市林业局.pdf">内江市林业局</a></li>
                    <li><a target="_blank" href="pdf/314001-内江市农业农村局.pdf">内江市农业农村局</a></li>
                    <li><a target="_blank" href="pdf/314917-内江市动植物疫病防控和农产品质量检测中心.pdf">内江市动植物疫病防控和农产品质量检测中心</a></li>
                    <li><a target="_blank" href="pdf/314920-内江市畜牧业发展中心.pdf">内江市畜牧业发展中心</a></li>
                    <li><a target="_blank" href="pdf/314923-内江市渔业发展中心.pdf">内江市渔业发展中心</a></li>
                    <li><a target="_blank" href="pdf/314924-内江市水产技术推广站.pdf">内江市水产技术推广站</a></li>
                    <li><a target="_blank" href="pdf/315001-内江市水利局.pdf">内江市水利局</a></li>
                    <li><a target="_blank" href="pdf/315902-内江市水土保持中心.pdf">内江市水土保持中心</a></li>
                    <li><a target="_blank" href="pdf/315908-内江市水旱灾害防御和河湖中心.pdf">内江市水旱灾害防御和河湖中心</a></li>
                    <li><a target="_blank" href="pdf/319001-内江市商务局.pdf">内江市商务局</a></li>
                    <li><a target="_blank" href="pdf/322001-内江市生态环境局.pdf">内江市生态环境局</a></li>
                    <li><a target="_blank" href="pdf/322902-内江市生态环境保护综合行政执法支队.pdf">内江市生态环境保护综合行政执法支队</a></li>
                    <li><a target="_blank" href="pdf/322903-内江市环境信息中心.pdf">内江市环境信息中心</a></li>
                    <li><a target="_blank" href="pdf/322904-内江市环境安全应急服务中心.pdf">内江市环境安全应急服务中心</a></li>
                    <li><a target="_blank" href="pdf/323001-内江市经济和信息化局.pdf">内江市经济和信息化局</a></li>
                    <li><a target="_blank" href="pdf/324001-内江市住房和城乡建设局.pdf">内江市住房和城乡建设局</a></li>
                    <li><a target="_blank" href="pdf/324903-内江市城市园林绿化建设管护中心.pdf">内江市城市园林绿化建设管护中心</a></li>
                    <li><a target="_blank" href="pdf/324904-内江市市政设施建设管护中心.pdf">内江市市政设施建设管护中心</a></li>
                    <li><a target="_blank" href="pdf/324921-内江市城市公园管护中心.pdf">内江市城市公园管护中心</a></li>
                    <li><a target="_blank" href="pdf/324922-内江市城市建设服务中心.pdf">内江市城市建设服务中心</a></li>
                    <li><a target="_blank" href="pdf/326001-内江市发展和改革委员会.pdf">内江市发展和改革委员会</a></li>
                    <li><a target="_blank" href="pdf/326902-内江市粮油质量检测和救灾物资储备中心.pdf">内江市粮油质量检测和救灾物资储备中心</a></li>
                    <li><a target="_blank" href="pdf/327001-内江市交通运输局.pdf">内江市交通运输局</a></li>
                    <li><a target="_blank" href="pdf/327601-内江市水路交通发展中心.pdf">内江市水路交通发展中心</a></li>
                    <li><a target="_blank" href="pdf/327603-内江市交通建设服务中心.pdf">内江市交通建设服务中心</a></li>
                    <li><a target="_blank" href="pdf/327604-内江市公路建设服务中心.pdf">内江市公路建设服务中心</a></li>
                    <li><a target="_blank" href="pdf/327605-内江市交通建设工程质量安全站.pdf">内江市交通建设工程质量安全站</a></li>
                    <li><a target="_blank" href="pdf/327607-内江市道路运输发展中心.pdf">内江市道路运输发展中心</a></li>
                    <li><a target="_blank" href="pdf/327608-内江市公路建设服务中心内江段.pdf">内江市公路建设服务中心内江段</a></li>
                    <li><a target="_blank" href="pdf/327609-内江市公路建设服务中心资中段.pdf">内江市公路建设服务中心资中段</a></li>
                    <li><a target="_blank" href="pdf/327610-内江市公路机械化养护中心.pdf">内江市公路机械化养护中心</a></li>
                    <li><a target="_blank" href="pdf/328001-内江市城市管理行政执法局.pdf">内江市城市管理行政执法局</a></li>
                    <li><a target="_blank" href="pdf/328901-内江市环境卫生管理处.pdf">内江市环境卫生管理处</a></li>
                    <li><a target="_blank" href="pdf/328903-内江市数字化城市管理中心.pdf">内江市数字化城市管理中心</a></li>
                    <li><a target="_blank" href="pdf/329001-内江市人力资源和社会保障局.pdf">内江市人力资源和社会保障局</a></li>
                    <li><a target="_blank" href="pdf/329903-内江市人力资源社会保障数据网络中心.pdf">内江市人力资源社会保障数据网络中心</a></li>
                    <li><a target="_blank" href="pdf/329904-内江市人才交流中心.pdf">内江市人才交流中心</a></li>
                    <li><a target="_blank" href="pdf/329906-内江市高级技工学校.pdf">内江市高级技工学校</a></li>
                    <li><a target="_blank" href="pdf/329907-内江市人事考试中心.pdf">内江市人事考试中心</a></li>
                    <li><a target="_blank" href="pdf/329908-内江市劳动人事争议仲裁院.pdf">内江市劳动人事争议仲裁院</a></li>
                    <li><a target="_blank" href="pdf/329911-内江市机关事业单位社会保险事务中心.pdf">内江市机关事业单位社会保险事务中心</a></li>
                    <li><a target="_blank" href="pdf/329912-内江市就业创业促进中心.pdf">内江市就业创业促进中心</a></li>
                    <li><a target="_blank" href="pdf/329913-内江市人力资源服务中心.pdf">内江市人力资源服务中心</a></li>
                    <li><a target="_blank" href="pdf/329914-内江市社会保险事务中心.pdf">内江市社会保险事务中心</a></li>
                    <li><a target="_blank" href="pdf/329917-内江市农民工服务中心.pdf">内江市农民工服务中心</a></li>
                    <li><a target="_blank" href="pdf/330001-内江市民政局.pdf">内江市民政局</a></li>
                    <li><a target="_blank" href="pdf/330903-内江市儿童福利院.pdf">内江市儿童福利院</a></li>
                    <li><a target="_blank" href="pdf/330904-内江市第一社会福利院.pdf">内江市第一社会福利院</a></li>
                    <li><a target="_blank" href="pdf/330905-内江市第二社会福利院.pdf">内江市第二社会福利院</a></li>
                    <li><a target="_blank" href="pdf/330906-内江市救助站.pdf">内江市救助站</a></li>
                    <li><a target="_blank" href="pdf/330907-内江市未成年人救助保护中心.pdf">内江市未成年人救助保护中心</a></li>
                    <li><a target="_blank" href="pdf/331001-内江市卫生健康委员会.pdf">内江市卫生健康委员会</a></li>
                    <li><a target="_blank" href="pdf/331902-内江市中医药发展服务中心（内江市卫生健康信息中心）.pdf">内江市中医药发展服务中心（内江市卫生健康信息中心）</a></li>
                    <li><a target="_blank" href="pdf/331905-内江市第一人民医院.pdf">内江市第一人民医院</a></li>
                    <li><a target="_blank" href="pdf/331906-内江市第二人民医院.pdf">内江市第二人民医院</a></li>
                    <li><a target="_blank" href="pdf/331907-内江市中医医院.pdf">内江市中医医院</a></li>
                    <li><a target="_blank" href="pdf/331908-内江市中心血站.pdf">内江市中心血站</a></li>
                    <li><a target="_blank" href="pdf/331909-内江市疾病预防控制中心.pdf">内江市疾病预防控制中心</a></li>
                    <li><a target="_blank" href="pdf/331910-四川省内江医科学校.pdf">四川省内江医科学校</a></li>
                    <li><a target="_blank" href="pdf/331916-内江市第六人民医院.pdf">内江市第六人民医院</a></li>
                    <li><a target="_blank" href="pdf/331917-内江市妇幼保健院.pdf">内江市妇幼保健院</a></li>
                    <li><a target="_blank" href="pdf/331919-内江市计划生育协会.pdf">内江市计划生育协会</a></li>
                    <li><a target="_blank" href="pdf/332001-内江市应急管理局.pdf">内江市应急管理局</a></li>
                    <li><a target="_blank" href="pdf/332902-内江市安全生产应急救援支队.pdf">内江市安全生产应急救援支队</a></li>
                    <li><a target="_blank" href="pdf/333001-内江市人民政府国防动员办公室.pdf">内江市人民政府国防动员办公室</a></li>
                    <li><a target="_blank" href="pdf/334001-内江市国有资产监督管理委员会.pdf">内江市国有资产监督管理委员会</a></li>
                    <li><a target="_blank" href="pdf/334901-内江市国资国企服务中心.pdf">内江市国资国企服务中心</a></li>
                    <li><a target="_blank" href="pdf/337001-内江市市场监督管理局.pdf">内江市市场监督管理局</a></li>
                    <li><a target="_blank" href="pdf/337902-内江市检验检测中心.pdf">内江市检验检测中心</a></li>
                    <li><a target="_blank" href="pdf/337905-内江市食品药品检验检测中心.pdf">内江市食品药品检验检测中心</a></li>
                    <li><a target="_blank" href="pdf/337907-内江市保护消费者权益委员会秘书科.pdf">内江市保护消费者权益委员会秘书科</a></li>
                    <li><a target="_blank" href="pdf/338001-内江市退役军人事务局.pdf">内江市退役军人事务局</a></li>
                    <li><a target="_blank" href="pdf/338902-内江市军队离休退休干部休养所.pdf">内江市军队离休退休干部休养所</a></li>
                    <li><a target="_blank" href="pdf/338903-内江军供站.pdf">内江军供站</a></li>
                    <li><a target="_blank" href="pdf/338904-内江市革命烈士陵园管理所.pdf">内江市革命烈士陵园管理所</a></li>
                    <li><a target="_blank" href="pdf/339001-内江市经济合作局.pdf">内江市经济合作局</a></li>
                    <li><a target="_blank" href="pdf/340001-内江市医疗保障局.pdf">内江市医疗保障局</a></li>
                    <li><a target="_blank" href="pdf/340901-内江市医疗保障事务中心.pdf">内江市医疗保障事务中心</a></li>
                    <li><a target="_blank" href="pdf/341001-四川省内江市农业科学院.pdf">四川省内江市农业科学院</a></li>
                    <li><a target="_blank" href="pdf/402001-内江市消防救援支队.pdf">内江市消防救援支队</a></li>
                    <li><a target="_blank" href="pdf/501001-内江市妇女联合会.pdf">内江市妇女联合会</a></li>
                    <li><a target="_blank" href="pdf/502001-四川省内江市工商业联合会.pdf">四川省内江市工商业联合会</a></li>
                    <li><a target="_blank" href="pdf/504001-内江市归国华侨联合会.pdf">内江市归国华侨联合会</a></li>
                    <li><a target="_blank" href="pdf/505001-中国共产主义青年团内江市委员会.pdf">中国共产主义青年团内江市委员会</a></li>
                    <li><a target="_blank" href="pdf/506001-内江市科学技术协会.pdf">内江市科学技术协会</a></li>
                    <li><a target="_blank" href="pdf/507001-内江市残疾人联合会.pdf">内江市残疾人联合会</a></li>
                    <li><a target="_blank" href="pdf/508001-内江市总工会.pdf">内江市总工会</a></li>
                    <li><a target="_blank" href="pdf/510001-中国国民党革命委员会内江市委员会.pdf">中国国民党革命委员会内江市委员会</a></li>
                    <li><a target="_blank" href="pdf/511001-中国民主同盟内江市委员会.pdf">中国民主同盟内江市委员会</a></li>
                    <li><a target="_blank" href="pdf/512001-中国民主建国会内江市委员会.pdf">中国民主建国会内江市委员会</a></li>
                    <li><a target="_blank" href="pdf/513001-中国民主促进会内江市委员会.pdf">中国民主促进会内江市委员会</a></li>
                    <li><a target="_blank" href="pdf/514001-中国农工民主党内江市委员会.pdf">中国农工民主党内江市委员会</a></li>
                    <li><a target="_blank" href="pdf/515001-九三学社内江市委员会.pdf">九三学社内江市委员会</a></li>
                    <li><a target="_blank" href="pdf/601001-中国共产党内江市委员会党校.pdf">中国共产党内江市委员会党校</a></li>
                    <li><a target="_blank" href="pdf/602001-内江市档案馆.pdf">内江市档案馆</a></li>
                    <li><a target="_blank" href="pdf/605001-内江市融媒体中心.pdf">内江市融媒体中心</a></li>
                    <li><a target="_blank" href="pdf/606001-内江市大数据中心.pdf">内江市大数据中心</a></li>
                    <li><a target="_blank" href="pdf/609001-内江市公共资源交易服务中心.pdf">内江市公共资源交易服务中心</a></li>
                    <li><a target="_blank" href="pdf/611001-内江职业技术学院.pdf">内江职业技术学院</a></li>
                    <li><a target="_blank" href="pdf/612001-内江市政务服务和公共资源交易管理办公室.pdf">内江市政务服务和公共资源交易管理办公室</a></li>
                    <li><a target="_blank" href="pdf/613001-川南幼儿师范高等专科学校.pdf">川南幼儿师范高等专科学校</a></li>
                    <li><a target="_blank" href="pdf/614001-内江市气象局.pdf">内江市气象局</a></li>
                    <li><a target="_blank" href="pdf/615001-内江师范学院.pdf">内江师范学院</a></li>
                    <li><a target="_blank" href="pdf/616001-内江卫生与健康职业学院.pdf">内江卫生与健康职业学院</a></li>
                    <li><a target="_blank" href="pdf/618001-内江市供销合作社联合社.pdf">内江市供销合作社联合社</a></li>
                    <li><a target="_blank" href="pdf/621001-内江国际物流港管理委员会.pdf">内江国际物流港管理委员会</a></li>
                    <li><a target="_blank" href="pdf/629001-内江市住房保障和房地产事务中心.pdf">内江市住房保障和房地产事务中心</a></li>
                    <li><a target="_blank" href="pdf/631001-内江市住房公积金管理中心.pdf">内江市住房公积金管理中心</a></li>
                </ul>
            </div>
        </div>
    </div>

    <script src="js/foot.js"></script>

    <style>
        .mk {
            display: none;
        }
    </style>
    <script src="js/list.js"></script>
    <script>
        $(function () {
            var name = getUrlParam("name");
            $('#cont' + name).css('display', 'block');
        });
    </script>
</body>

</html>
</g:if>
<g:else>
    <%@ page contentType="text/html;charset=UTF-8" %>
    <html>
    <head>
    </head>
    <body>
    <script>
        alert("无访问权限，请输入查看密码");
        location.replace("index.gsp")
    </script>
    </body>
    </html>
</g:else>