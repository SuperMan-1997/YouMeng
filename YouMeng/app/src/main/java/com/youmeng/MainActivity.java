package com.youmeng;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
public class MainActivity extends AppCompatActivity {

    private CheckBox cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化
        PlatformConfig.setQQZone("1109692281", "C5IcYneFlWEUKhNj");
        PlatformConfig.setWeixin("wx6569554d1d002d04", "dc96ab3a64c8822a8433cf29677c4d07");
//        PlatformConfig.setSinaWeibo("","","http://sns.whalecloud.com");//新浪微博
        setContentView(R.layout.activity_main);
        cb = (CheckBox) findViewById(R.id.check_close_editor);
        //监听CheckBox
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Config.OpenEditor=isChecked;
            }
        });
        /**
         * 初始化common库
         * 参数1:上下文，必须的参数，不能为空
         * 参数2:友盟 app key，非必须参数，如果Manifest文件中已配置app key，该参数可以传空，则使用Manifest中配置的app key，否则该参数必须传入
         * 参数3:友盟 channel，非必须参数，如果Manifest文件中已配置channel，该参数可以传空，则使用Manifest中配置的channel，否则该参数必须传入，channel命名请详见channel渠道命名规范
         * 参数4:设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机
         * 参数5:Push推送业务的secret，需要集成Push功能时必须传入Push的secret，否则传空
         */
        //如果AndroidManifest.xml清单配置中没有设置appkey和channel，则可以在这里设置
        // UMConfigure.init(this, "58edcfeb310c93091c000be2", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "1fe6a20054bcef865eeb0991ee84525b");
//        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE,"");
        UMConfigure.init(this,"","", UMConfigure.DEVICE_TYPE_PHONE,"");
    }

    public void onClick(View view) {
        final UMImage image=new UMImage(this,R.drawable.pic);
        final UMImage imageMusic=new UMImage(this,R.drawable.umeng_socialize_share_music);
        final UMImage imageVideo=new UMImage(this,R.drawable.umeng_socialize_share_video);
        //设置图片压缩的方式
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
        //压缩格式设置
        image.compressFormat = Bitmap.CompressFormat.PNG;//用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色

        //设置音频的网页在线地址
        UMusic music=new UMusic("https://t3.kugou.com/song.html?id=6CncN54uMV2");
        music.setTitle("再见吧我最爱的你");//音乐标题
        music.setThumb(imageMusic);//音乐的专辑图
        music.setDescription("演唱:东方晴儿");//音乐的描述
        //视频网页地址
        UMVideo video=new UMVideo("https://www.iqiyi.com/w_19s4bz7q49.html");
        video.setTitle("如果你觉得累了坚持不下去了...");
        video.setThumb(imageVideo);//视频的缩略图

        switch (view.getId()) {
            case R.id.app_open_share:
               //系统默认分享面板
                new ShareAction(this).setDisplayList(
                        SHARE_MEDIA.QQ,
                        SHARE_MEDIA.QZONE,
                        SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.SINA,
                        SHARE_MEDIA.MORE
                        ).setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                      new ShareAction(MainActivity.this).setPlatform(share_media).
                              setCallback(umShareListener).withText("多平台分享").withMedia(image).share();
                    }
                }).open();//弹出面板
                break;
            case R.id.app_share_qq:
//                new ShareAction(MainActivity.this).setPlatform(SHARE_MEDIA.QQ).
//                        setCallback(umShareListener).withText("QQ分享").withMedia(image).share();
                new ShareAction(MainActivity.this).setPlatform(SHARE_MEDIA.QQ).
                        setCallback(umShareListener).withText("QQ分享").withMedia(imageVideo).share();

                break;
            case R.id.app_share_qzone:
                new ShareAction(MainActivity.this).setPlatform(SHARE_MEDIA.QZONE).
                        setCallback(umShareListener).withText("QQ空间分享").withMedia(image).share();

                //QQ空间多图分享
//                new ShareAction(MainActivity.this).withMedias(image)
//                        .setPlatform(SHARE_MEDIA.QZONE)
//                        .withText("QQ空间分享")
//                        .setCallback(umShareListener).share();
                break;
            case R.id.app_share_wx:
                new ShareAction(MainActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).
                        setCallback(umShareListener).withText("微信分享").withMedia(music).share();
                break;
            case R.id.app_share_wx_circle:
                new ShareAction(MainActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).
                        setCallback(umShareListener).withText("微信朋友圈分享").withMedia(video).share();
                break;
            case R.id.app_share_sina:
                new ShareAction(MainActivity.this).setPlatform(SHARE_MEDIA.SINA).
                        setCallback(umShareListener).withText("新浪微博分享").withMedia(image).share();
                break;
        }
    }
    //回调函数
    private UMShareListener umShareListener=new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(MainActivity.this, "分享成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable t) {
            Toast.makeText(MainActivity.this,"分享失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(MainActivity.this, "取消分享！", Toast.LENGTH_SHORT).show();

        }
    };
    //重写onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
}
