package com.example.demo.api.user.entity;

import com.example.demo.common.model.KeyValue;

import java.util.Arrays;
import java.util.List;

public class UserConstants {

        public static List<KeyValue> PROFESSIONS = Arrays.asList(new KeyValue(1, "计算机硬件及网络设备"),
                        new KeyValue(2, "计算机软件"), new KeyValue(3, "IT服务（系统/数据/维护）/多领域经营"), new KeyValue(4, "互联网/电子商务"),
                        new KeyValue(5, "网络游戏"), new KeyValue(6, "通讯（设备/运营/增值服务）"), new KeyValue(7, "电子技术/半导体/集成电路"),
                        new KeyValue(8, "仪器仪表及工业自动化"), new KeyValue(9, "金融/银行/投资/基金/证券"), new KeyValue(10, "保险"),
                        new KeyValue(11, "房地产/建筑/建材/工程"), new KeyValue(12, "家居/室内设计/装饰装潢"),
                        new KeyValue(13, "物业管理/商业中心"), new KeyValue(14, "广告/会展/公关/市场推广"),
                        new KeyValue(15, "媒体/出版/影视/文化/艺术"), new KeyValue(16, "印刷/包装/造纸"),
                        new KeyValue(17, "咨询/管理产业/法律/财会"), new KeyValue(18, "教育/培训"), new KeyValue(19, "检验/检测/认证"),
                        new KeyValue(20, "中介服务"), new KeyValue(21, "贸易/进出口"), new KeyValue(22, "零售/批发"),
                        new KeyValue(23, "快速消费品（食品/饮料/烟酒/化妆品"), new KeyValue(24, "耐用消费品（服装服饰/纺织/皮革/家具/家电）"),
                        new KeyValue(25, "办公用品及设备"), new KeyValue(26, "礼品/玩具/工艺美术/收藏品"),
                        new KeyValue(27, "大型设备/机电设备/重工业"), new KeyValue(28, "加工制造（原料加工/模具）"),
                        new KeyValue(29, "汽车/摩托车（制造/维护/配件/销售/服务）"), new KeyValue(30, "交通/运输/物流"),
                        new KeyValue(31, "医药/生物工程"), new KeyValue(32, "医疗/护理/美容/保健"), new KeyValue(33, "医疗设备/器械"),
                        new KeyValue(34, "酒店/餐饮"), new KeyValue(35, "娱乐/体育/休闲"), new KeyValue(36, "旅游/度假"),
                        new KeyValue(37, "石油/石化/化工"), new KeyValue(38, "能源/矿产/采掘/冶炼"), new KeyValue(39, "电气/电力/水利"),
                        new KeyValue(40, "航空/航天"), new KeyValue(41, "学术/科研"), new KeyValue(42, "政府/公共事业/非盈利机构"),
                        new KeyValue(43, "环保"), new KeyValue(44, "农/林/牧/渔"), new KeyValue(45, "跨领域经营"),
                        new KeyValue(46, "其它"));

        public static List<KeyValue> EDUCATIONS = Arrays.asList(new KeyValue(1, "初中"), new KeyValue(2, "高中"),
                        new KeyValue(3, "中专"), new KeyValue(4, "大专"), new KeyValue(5, "本科"), new KeyValue(6, "硕士"),
                        new KeyValue(7, "博士"), new KeyValue(8, "其它"));

        public static byte COLLECT_TYPE_USER = 1;
        public static byte COLLECT_TYPE_ARTICLE = 2;
        public static final Byte ADDRESS_IS_DEFAULT = 1;
        public static final Byte ADDRESS_NOT_DEFAULT = 2;
    
    
        public static List<Byte> COLLECTTYPES = Arrays.asList(COLLECT_TYPE_USER, COLLECT_TYPE_ARTICLE);

}
