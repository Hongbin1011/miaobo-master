package com.anzhuo.video.app.dongtu.config;

/**
 * Created by wbb on 2016/4/27.
 */
public abstract class DongtuAppServerUrl extends DongtuBaseAppServerUrl {

    public final static String BASEURL = getAppServerUrl();

    //点赞或者点踩接口
    public final static String THUMB_UP = getAppServerUrl() + "Jokes.CreateGood";

    //评论点赞接口
    public final static String COMMENT_GOOD = getAppServerUrl() + "Comment.Goods";

    //评论列表接口
    public final static String COMMENTS_LIST = getAppServerUrl() + "Comment.Get";

    //发表评论
    public final static String ADD_COMMENT = getAppServerUrl() + "Comment.Issue";
    //短信验证 接口
    public final static String Note_test = getAppServerUserUrl() + "User.SendMark";
    //用户注册
    public final static String USER_AUTHREG = getAppServerUserUrl() + "User.AuthReg";
    //退出登录
    public final static String User_Logout = getAppServerUserUrl() + "User.Logout";

    //用户登录
    public final static String User_login = getAppServerUserUrl() + "User.Login";
    //绑定
    public final static String Bind_login = getAppServerUserUrl() + "User.UserBind";
    //找回密码
    public final static String Find_pwd = getAppServerUserUrl() + "User.TokenPassword";
    //新密码
    public final static String New_pwd = getAppServerUserUrl() + "User.NewPassword";
    //检查是否登录
    public final static String User_Islogin = getAppServerUserUrl() + "User.IsLogin";
    // 获取绑定状态
    public final static String User_BindStat = getAppServerUserUrl() + "Userinfo.Bstat";
    //获取用户个人资料
    public final static String User_info = getAppServerUserUrl() + "Userinfo.Info";
    //修改用户个人资料
    public final static String User_Editinfo = getAppServerUserUrl() + "Userinfo.Editinfo";
    //获取用户个人信息的资料
    public final static String User_EUserInfo = getAppServerUserUrl() + "User.GetUserInfo";

    //评论接口
    public final static String comments = getAppServerUrl() + "Jokes.CreateGood";
    //分享创建连接
    public final static String Share_Create = getAppServerUrl() + "Jokes.CreateShare";

    //笑话列表接口
    public final static String JOKE_LIST = getAppServerUrl() + "Jokes.Index";
    //关注人的笑话列表接口
    public final static String ATTENTION_JOKE_LIST = getAppServerUrl() + "Follow.MyFollow";

    //笑话详情接口
    public final static String JOKE_DETAILS = getAppServerUrl() + "Jokes.Detail";

    //文件上传接口
    public final static String UPLOAD_UP = getAppServerUrl() + "Upload.Up";
    //笑话创建接口
    public final static String JOKES_CREATE = getAppServerUrl() + "Jokes.Create";

    //他的关注
    public final static String FOLLOW_TOP = getAppServerUrl() + "Follow.Top";
    //他的fans
    public final static String FOLLOW_UNDER = getAppServerUrl() + "Follow.Under";
    //推荐关注
    public final static String FOLLOW_RECOMMEND = getAppServerUrl() + "Follow.Recommend";
    //关注
    public final static String FOLLOW_UP = getAppServerUrl() + "Follow.Up";
    //取消关注
    public final static String FOLLOW_DOWN = getAppServerUrl() + "Follow.Down";
    //获取任务号
    public final static String UPLOAD_GETBIZNO = getAppServerUrl() + "Upload.GetBizNo";


    public final static String NEWS_SCOREINDEX = getAppServerUrl() + "News.Scoreindex";

    //我的积分
    public final static String NEWS_MYSCORE = getAppServerUrl() + "News.Myscore";
    //我的总积分和经验接口
    public final static String NEWS_TOTALSCORE = getAppServerUrl() + "News.Totalscore";

    //意见反馈
    public final static String ADVICE_CREATE = getAppServerUrl() + "Advice.Create";

    //版本升级
    public final static String VERSION_INDEX = getAppServerUrl() + "Version.Index";

    //消息中心接口
    public final static String MESSAGE_CENTER_LIST = getAppServerUrl() + "News.Index";

    //获取用户操作的记录信息
    public final static String USER_GETUSERRECORD = getAppServerUrl() + "User.GetUserRecord";

    //获取用户拥有的帖子接口
    public final static String USER_GETJOKES = getAppServerUrl() + "User.GetJokes";

    //获取用户拥有的分享帖子接口
    public final static String USER_GETSHAREJOKES = getAppServerUrl() + "User.GetShareJokes";

    //获取用户收藏的集合
    public final static String USER_COLLECT_LIST = getAppServerUrl() + "Favourite.Top";

    //用户评论过的帖子接口
    public final static String USER_Comment = getAppServerUrl() + "Comment.MyComment";

    //删除笑话
    public final static String REMOVE_jOKE = getAppServerUrl() + "Jokes.RemoveJoke";
    //删除笑话
    public final static String REMOVE_JOKE_COMMENT = getAppServerUrl() + "Comment.Delete";

    //统计第一次安装
    public final static String Firstlogin_Newindex = getStatisticsUrl() + "Firstlogin.Newindex";

    //统计APP打开次数
    public final static String Openapp_Newindex = getStatisticsUrl() + "Openapp.Newindex";

    //关于我们
    public final static String ABOUT_WE = getAppServerUrl() + "Advice.Aboutus";

    //审核列表
    public final static String AUDIT_LIST = getAppServerUrl() + "Audit.Index";

    //审核动作：好笑不好笑好跳过
    public final static String CHECKACTION = getAppServerUrl() + "Audit.Creategoods";

    //收藏或者取消收藏
    public final static String COLLECT_OR_CANCEL_1 = getAppServerUrl() + "Favourite.Up";
    public final static String COLLECT_OR_CANCEL_2 = getAppServerUrl() + "Favourite.Down";

    //举报
    public final static String REPORT = getAppServerUrl() + "Report.Index";
    //激光推送  每次重新登录时都要访问
    public final static String NOTIFICATION = getAppServerUrl() + "Message.Index";

    //根据笑话ID获取内容
    public final static String JOKE_DETAIL = getAppServerUrl() + "Jokes.Detail";

    //等级规则
    public final static String USER_CLASS = getAppServerUrl() + "Exprule.Index";

    //用户评论的 列表显示
    public final static String USER_COMMENTLIST_COMMENT = getAppServerUrl() + "Comment.Comment";//Comment.Comment 评论段子 改版后的接口


    /*===========================动图==================*/
   /* //动图 栏目分类
    public final static String DontuTitleType = getDongtuServerUrl() + "Dongtu.Category";
    //动图 数据列表
    public final static String DontuContent = getDongtuServerUrl() + "Dongtu.Get";
    //评论
    public final static String DontuComment = getDongtuServerUrl() + "Comment.Get";  //评论
    //点赞 分享 踩上报
    public final static String DontuUserClick = getDongtuServerUrl() + "Dongtu.Up";
    //评论赞
    public final static String DontuCommentUserClick = getDongtuServerUrl() + "Comment.Up";//评论赞
    //举报
    public final static String DontuCommentReport = getDongtuServerUrl() + "Comment.Report";
    //发表评论
    public final static String DontuSendComment = getDongtuServerUrl() + "Comment.Issue";*/

    //动图 栏目分类
    public final static String DontuTitleType = getDongtuServerUrl() + "DongtuMongo.Category";
    //动图 数据列表
    public final static String DontuContent = getDongtuServerUrl() + "DongtuMongo.Get";
    //评论
    public final static String DontuComment = getDongtuServerUrl() + "Comment.Get";  //评论
    //点赞 分享 踩上报
    public final static String DontuUserClick = getDongtuServerUrl() + "DongtuMongo.Up";
    //评论赞
    public final static String DontuCommentUserClick = getDongtuServerUrl() + "Comment.Up";//评论赞
    //举报
    public final static String DontuCommentReport = getDongtuServerUrl() + "Comment.Report";
    //发表评论
    public final static String DontuSendComment = getDongtuServerUrl() + "Comment.Issue";

    //开屏广告控制
    public final static String Version_Start = getDongtuServerUrl() + "Version.Start";


   /* DongtuMongo.Category
    DongtuMongo.Get
	DongtuMongo.Up*/

}
