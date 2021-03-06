package me.johnniang.ncov.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Comparator;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author johnniang Lei XinXin
 */
@NoArgsConstructor
@Data
public class TimeLine implements Comparator<TimeLine> {

    /**
     * id : 192
     * pubDate : 1579861199000
     * pubDateStr : 10分钟前
     * title : 云南启动一级响应
     * summary : 根据《云南省突发公共卫生事件应急预案》，云南省新型冠状病毒感染的肺炎疫情防控工作
     * infoSource : 人民日报
     * sourceUrl : https://weibo.com/2803301701/IqZlWaeD3?from=page_1002062803301701_profile&wvr=6&mod=weibotime&type=comment#_rnd1579861293575
     * provinceId : 53
     * provinceName : 云南省
     * createTime : 1579861344000
     * modifyTime :` 1579861344000
     */

    private int id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date pubDate;

    private String pubDateStr;

    private String title;

    private String summary;

    private String infoSource;

    private String sourceUrl;

    private String provinceId;

    private String provinceName;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date createTime;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date modifyTime;

    @Override
    public int compare(TimeLine left, TimeLine right) {
        return left.pubDate.compareTo(right.pubDate) * -1;
    }
}
