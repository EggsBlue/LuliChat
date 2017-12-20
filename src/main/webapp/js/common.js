var debug = true;
var port = 5210; //socket 端口
var url = debug ?'localhost' : '106.14.141.88';

var TYPE = {
    REQFRIEND:'1',   //请求加好友
     P2PMSG : "5",    //点对点消息
    GROUP_MSG_REQ:'6', //群组消息
    OLDMSG:'8',        // 未读消息
    FAIL_MESSAGE_RESP:'9',  //失败消息
    SUCCESS_MESSAGE_RESP:'10', //成功消息
    REQFRIENDSUCCESS :'11'  //加好友成功消息
    ,JOIN_GROUP_REQ:'3', //加群成功消息
    ONLINECOUNT:'12'
}