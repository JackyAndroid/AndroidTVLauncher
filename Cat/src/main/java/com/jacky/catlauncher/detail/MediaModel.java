
package com.jacky.catlauncher.detail;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class MediaModel implements Parcelable {

    private int id;
    private String title;
    private String content;
    private String imageUrl;
    private String videoUrl;

    private MediaModel(
            final int id,
            final String title,
            final String content,
            final String imageUrl,
            final String videoUrl) {

        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    protected MediaModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        imageUrl = in.readString();
        videoUrl = in.readString();
    }

    public static final Creator<MediaModel> CREATOR = new Creator<MediaModel>() {
        @Override
        public MediaModel createFromParcel(Parcel in) {
            return new MediaModel(in);
        }

        @Override
        public MediaModel[] newArray(int size) {
            return new MediaModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public static List<MediaModel> getPhotoModels() {
        List<MediaModel> mediaModels = new ArrayList<>();

        String titles[] = {
                "北京",
                "上海",
                "西藏",
                "范冰冰",
                "高圆圆",
                "迈克尔.杰克逊",
        };

        String contents[] = {
                "北京，简称“京”，中华人民共和国首都、直辖市、国家中心城市、超大城市，全国政治中心、文化中心、国际交往中心、科技创新中心，是中国共产党中央委员会、中华人民共和国中央人民政府和全国人民代表大会的办公所在地。\n" +
                        "北京位于华北平原北部，背靠燕山，毗邻天津市和河北省。北京的气候为典型的北温带半湿润大陆性季风气候。\n" +
                        "北京是首批国家历史文化名城和世界上拥有世界文化遗产数最多的城市，三千多年的历史孕育了故宫、天坛、八达岭长城、颐和园等众多名胜古迹。早在七十万年前，北京周口店地区就出现了原始人群部落“北京人”。公元前1045年，北京成为蓟、燕等诸侯国的都城。公元938年以来，北京先后成为辽陪都、金中都、元大都、明清国都。1949年10月1日成为中华人民共和国首都。\n" +
                        "2015年末，北京全市常住人口2170.5万人，比2014年末增加18.9万人。其中，常住外来人口822.6万人，占常住人口的比重为37.9%。2015年北京市实现地区生产总值22968.6亿元，比2014年增长6.9%。\n" +
                        "2015年7月31日，国际奥委会主席巴赫宣布北京携手张家口获得2022年冬季奥林匹克运动会的举办权。北京由此成为全球首个既举办过夏季奥运会又即将举办冬季奥运会的城市。",
                "上海，简称“沪”或“申”，中华人民共和国直辖市，国家中心城市，超大城市，中国的经济、金融、贸易、航运中心，首批沿海开放城市。地处长江入海口，隔东中国海与日本九州岛相望，南濒杭州湾，西与江苏、浙江两省相接。[1-3] \n" +
                        "上海是一座国家历史文化名城，拥有深厚的近代城市文化底蕴和众多历史古迹。江南传统吴越文化与西方传入的工业文化相融合形成上海特有的海派文化，[4]  上海人多属江浙民系使用吴语。[5]  早在宋代就有了“上海”之名，1843年后上海成为对外开放的商埠并迅速发展成为远东第一大城市，今日的上海已经成功举办了2010年世界博览会、中国上海国际艺术节、上海国际电影节等大型国际活动。[6-7] \n" +
                        "上海辖16个市辖区，总面积6340平方公里，属亚热带湿润季风气候，四季分明，日照充分，雨量充沛。上海气候温和湿润，春秋较短，冬暖夏凉。1月份最冷，平均气温约4℃，通常7月份最热，平均气温约28℃。[8] \n" +
                        "上海是中国重要的的经济、交通、科技、工业、金融、会展和航运中心，是世界上规模和面积最大的都会区之一。2014年上海GDP总量居中国城市第一，亚洲第二。\n" +
                        "上海港货物吞吐量和集装箱吞吐量均居世界第一，是一个良好的滨江滨海国际性港口。上海也是中国大陆首个自贸区“中国（上海）自由贸易试验区”所在地。\n" +
                        "上海与江苏、浙江、安徽共同构成的长江三角洲城市群已成为国际6大世界级城市群之一。",
                "西藏自治区（藏文：བོད་རང་སྐྱོང་ལྗོངས།，藏语拼音：Poi Ranggyong Jong，威利转写：Bod rang skyong ljongs），简称“藏”，通称西藏，位于中国西南边陲，首府拉萨，是中国五个少数民族自治区之一。[1] \n" +
                        "西藏自治区位于青藏高原西南部，地处北纬26°50′至36°53′，东经78°25′至99°06′之间，平均海拔在4000米以上，素有“世界屋脊”之称。全区面积120.223万平方公里，约占全国总面积的1/8，在全国各省、市、自治区中仅次于新疆。2014年末常住人口约317.55万人。\n" +
                        "西藏北邻新疆，东接四川，东北紧靠青海，东南连接云南；周边与缅甸、印度、不丹、尼泊尔、克什米尔等国家及地区接壤，陆地国界线4000多公里，是中国西南边陲的重要门户。\n" +
                        "西藏是中国不可分割的一部分[2]  ，中央政权始终对西藏行使着有效管辖。藏族人民是中华民族大家庭中的重要一员。西藏唐宋时期称为“吐蕃”，元明时期称为“乌斯藏”，清代称为“唐古特”、“图伯特”等。清朝康熙年间起称“西藏”至今。1951年5月23日，西藏和平解放。[3]  1965年9月9日，西藏自治区正式宣告成立。[3-4] \n" +
                        "西藏以其雄伟壮观、神奇瑰丽的自然风光闻名。它地域辽阔，地貌壮观、资源丰富。自古以来，这片土地上的人们创造了丰富灿烂的民族文化。",
                "范冰冰，1981年9月16日生于山东青岛，电影演员、歌手，毕业于上海师范大学谢晋影视艺术学院。\n" +
                        "1996年参演电视剧《女强人》。1998年主演电视剧《还珠格格》系列成名，2001年起投身大银幕。[1] \n" +
                        "2004年凭借电影《手机》获得第27届大众电影百花奖最佳女主角奖。[2]  2005年发行首张个人专辑《刚刚开始》；[3]  同年主演电影《墨攻》。2007年，参演电影《心中有鬼》获得第44届台湾电影金马奖最佳女配角奖；[4]  同年凭借电影《苹果》获得第4届欧亚国际电影节最佳女演员奖。[5] \n" +
                        "2009年获得第12届中国电影表演艺术学会金凤凰奖。2010年登上纽约时代广场户外大屏幕播放的中国国家形象片；2011年主演李玉执导的电影《观音山》荣获第23届东京国际电影节最佳女演员奖；[6]  入选《环球时报》“最受全球关注文化人物”前五强，[7]  同年主演陈凯歌执导的古装电影《赵氏孤儿》。2011年担任东京国际电影节评委。[8] \n" +
                        "2013、2014、2015连续三年登上福布斯中国名人榜榜首，成为首位蝉联三届福布斯中国名人榜第一名的中国女星。[9-10] \n" +
                        "2013年凭借电影《二次曝光》获得第9届华鼎奖中国电影最佳女主角奖，[11]  凭借同名电影歌曲《一夜惊喜》获得首届伦敦国际华语电影节最佳原创电影歌曲奖，同年戛纳国际电影节好莱坞报道授予范冰冰年度最佳国际艺人奖。[12]  2014年参演的美国动作片《X战警：逆转未来》全球上映，同年登上美国《Vogue》封面；[13]  主演电视剧《武媚娘传奇》成为中国首部网络点击量超过100亿的电视剧。\n" +
                        "2015年范冰冰与成龙主演的好莱坞电影《跨境追捕》即将上映。",
                "高圆圆，1979年10月5日出生于北京市，中国内地影视女演员、模特。\n" +
                        "1996年，高圆圆被广告公司发掘，随后拍摄大量的商业广告，在广告圈中崭露头[1]  ；1997年主演个人的大银幕处女作《爱情麻辣烫》，从此开始了她的演艺生涯[2]  ；2003年，凭借古装武侠爱情剧《倚天屠龙记》受到广泛关注[3]  ；2005年，因在剧情片《青红》中饰演女主人公青红入围戛纳电影节最佳女主角奖[4]  ；2006年凭借喜剧动作片《宝贝计划》入围百花奖最佳女演员提名[5]  ；2007年因在爱情电影《男才女貌》中饰演聋哑幼师秦小悠获得第11届电影表演艺术学会奖新人奖[6]  。\n" +
                        "2010年，凭借都市爱情电影《单身男女》提名第31届香港电影金像奖最佳女主角、华语电影传媒大奖观众票选最受瞩目女演员[7-8]  ；2013年，高圆圆主演的当代都市情感剧《咱们结婚吧》取得了同时段电视剧收视冠军[9]  ，而其亦获得第十八届北京影视春燕奖最佳女主角奖[10]  ；2014年，高圆圆自创圆漾Ondul'鞋履品牌并兼任设计师[11]  ，随后，凭借爱情电影《一生一世》入围第6届澳门国际电影节最佳女主角[12]  ；2015年获得第52届韩国电影大钟奖最佳海外女演员奖[13]  。",
                "迈克尔·杰克逊（Michael Jackson），1958年8月29日在美国印第安纳州加里市出生，美国歌手、词曲创作人、舞蹈家、表演家、慈善家、音乐家、人道主义者、和平主义者、慈善机构创办人。\n" +
                        "杰克逊是家族的第七个孩子，他在1964年作为杰克逊五人组的成员和他的兄弟一起在职业音乐舞台上初次登台，1968年乐队与当地的一家唱片公司合作出版了第一张唱片《Big Boy》。1971年12月，发行了个人首支单曲《Got to be there》，标志着其个人独唱生涯的开始。\n" +
                        "1982年12月，杰克逊音乐生涯最畅销的专辑《Thriller》发行。1987年9月，杰克逊展开个人首次全球巡演。通过舞台和视频的表演，杰克逊普及了一些像机械舞和太空步等舞蹈技术。杰克逊一生中获得了13个格莱美奖和26个全美音乐奖。在他单飞生涯中，他拥有13支美国冠军单曲。2000年吉尼斯世界纪录大全里认证他资助过39个慈善机构。\n" +
                        "2009年5月，杰克逊宣布将在伦敦举行系列音乐会；6月25日，他因为急性丙泊酚和苯二氮平类药物中毒导致心脏骤停逝世。洛杉矶法医裁定这是一宗凶杀案，他的私人医生康拉德·莫里被判定过失杀人罪。2010年，迈克尔·杰克逊被授予格莱美终生成就奖。\n" +
                        "2009年6月25日，杰克逊辞世，享年50岁。2011年11月7日下午，杰克逊的私人医师莫里 “过失杀人罪”罪名成立，服刑四年监禁。",
        };

        String urls[] = {
                "http://tupian.enterdesk.com/2012/0528/gha/9/120523112107-15.jpg",
                "http://imgstore.cdn.sogou.com/app/a/100540002/541762.jpg",
                "http://www.bz55.com/uploads/allimg/140729/138-140H9144A7.jpg",
                "http://imgstore.cdn.sogou.com/app/a/100540002/717240.jpg",
                "http://e.hiphotos.baidu.com/zhidao/pic/item/5ab5c9ea15ce36d3418e754838f33a87e850b1c4.jpg",
                "http://www.bz55.com/uploads/allimg/150402/139-150402152530.jpg",
        };

        for (int i = 0; i < titles.length; i++) {
            MediaModel mediaModel = new MediaModel(0, titles[i], contents[i], urls[i], "");
            mediaModels.add(mediaModel);
        }

        return mediaModels;
    }

    public static List<MediaModel> getVideoModels() {
        List<MediaModel> mediaModels = new ArrayList<>();

        String titles[] = {
                "CCTV1",
                "湖南卫视",
        };

        String contents[] = {
                "中央电视台综合频道（频道呼号：CCTV-1综合）是以新闻为主的综合类电视频道，是中央电视台第一套节目，于1958年9月2日开播。\n" +
                        "1958年5月1日，CCTV-1开始试验播出，呼号“北京电视台”。1978年5月1日，“北京电视台”正式更名为中央电视台。1995年4月3日，CCTV-1正式更名为“新闻·综合频道”。 " +
                        " 2003年5月1日，CCTV-1由新闻·综合频道改为综合频道。 2009年9月27日，CCTV-1开播高清信号，实现高、标清同播。2011年3月，CCTV-1分版为境内版和香港版（香港版起初由亚洲电视转播，后改为NOW TV转播）",

                "湖南卫视，全称湖南广播电视台卫星频道，昵称芒果台，是湖南广播电视台和芒果传媒有限公司旗下的卫星电视频道。\n" +
                        "1997年1月，湖南电视台第一套节目上星更名为湖南电视台卫星频道。\n" +
                        "2010年1月，湖南广电整合后，呼号更名为“湖南广播电视台卫星频道”。\n" +
                        "2016年4月，湖南卫视推出了“越新鲜越青春”的频道口号。",
        };

        String urls[] = {
                "http://hiphotos.baidu.com/%B1%A1%B1%CC%C0%B6%C9%C0/pic/item/e0d2dc358b514685d1a2d3fa.jpg",
                "http://imgsrc.baidu.com/forum/pic/item/410e5aafa40f4bfbd8a9a68a034f78f0f63618fa.jpg",
        };

        String videoUrls[] = {
                "http://eshare.live.otvcloud.com/otv/nyz/live/channel01/index.m3u8",
                "http://111.39.226.103:8112/120000001001/wlds:8080/ysten-business/live/hdhunanstv/.m3u8",
        };

        for (int i = 0; i < titles.length; i++) {
            MediaModel mediaModel = new MediaModel(
                    0, titles[i], contents[i], urls[i], videoUrls[i]);
            mediaModels.add(mediaModel);
        }

        return mediaModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(imageUrl);
        dest.writeString(videoUrl);
    }
}
