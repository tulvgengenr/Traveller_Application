package com.example.test3;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * 为OneNet平台操作提供的java方法
 *
 * @author hnu根根儿
 *
 */
public class API extends Thread {
    API_EDP api_edp = new API_EDP();
    //测试部分
    //查询设备信息
    //System.out.println(main.getDevice("729910889", "version=2018-10-31&res=products%2F435187&et=1656209269&method=md5&sign=hpts5sU5mUSYttG5hRpeBA%3D%3D"));
    //查询数据流(失败)
    //System.out.println(main.getDataStreams("729910889", "version=2018-10-31&res=products%2F435187&et=1656209269&method=md5&sign=hpts5sU5mUSYttG5hRpeBA%3D%3D", "tempatature_zheng"));
    //查询数据点
    //下发信息(待测试)
    //String body="01 01 01";
    //System.out.println(main.postCommand("729910889","version=2018-10-31&res=products%2F435187&et=1656209269&method=md5&sign=hpts5sU5mUSYttG5hRpeBA%3D%3D",30,body));
    //温度
    int temperature_zheng;
    int temperature_xiao;
    //光
    int guang_zheng;
    int guang_xiao;
    //湿度
    int shi_du_zheng;
    int shi_du_xiao;
    //位置(纬度、经度)
    int wz;
    int wx;
    int jz;
    int jx;
    //x,y,z三轴
    int acce_x;
    int acce_y;
    int acce_z;
    //表示打开或关闭
    int is_open;
   /**
     * 向OneNet查询设备信息
     *
     * @param apiKey
     * @param deviceId:设备ID
     * @return
     */
    public String getDevice(String deviceId,String apiKey) {
        String result="";
        BufferedReader in =null;
        try {
            String url = "http://api.heclouds.com/devices/"+deviceId;
            //String url = "http://api.heclouds.com/mqtt/v1/devices/"+deviceId;
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            HttpURLConnection connection=(HttpURLConnection)realUrl.openConnection();
            //设置通用的请求属性
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization",apiKey);
            //建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询设备信息出现异常！ ");
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                System.out.println("IO close error !");
            }
        }
        return result;

    }
    /**
     * 获取数据流（失败）
     *
     * @param apiKey
     * @param datastreamId:数据流id
     * @param deviceId:设备ID
     * @return
     */
    public String getDataStreams(String deviceId,String apiKey,String datastreamId) {
        String result="";
        BufferedReader in =null;
        try {
            String url = "http://api.heclouds.com/devices/"+deviceId+"/datastreams/"+datastreamId;
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            HttpURLConnection connection=(HttpURLConnection)realUrl.openConnection();
            //设置通用的请求属性
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization",apiKey);
            //建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("获取数据流出现异常！ ");
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                System.out.println("IO close error !");
            }
        }
        return result;
    }
    /**
     * 查询设备数据点
     * @param deviceId:设备ID
     * @param apiKey
     * @param datastreamId:数据流id
     * @param start:提取数据点的开始时间
     * @param limit:限定本次请求最多返回的数据点数，默认为100
     * @return
     */
    public String getDataPoints(String deviceId,String apiKey,String datastreamId,String start,int limit) {
        String result="";
        BufferedReader in =null;
        try {
            String url = "http://api.heclouds.com/devices/"+deviceId+"/datapoints?datastream_id="+datastreamId+"&limit="+limit;//"&start="+start
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            HttpURLConnection connection=(HttpURLConnection)realUrl.openConnection();
            //设置通用的请求属性
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization",apiKey);
            //建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取数据流出现异常！ ");
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                System.out.println("IO close error !");
            }
        }
        return result;
    }
    /**
     * 下发命令(待完善)
     * @param deviceId:设备ID
     * @param apiKey:token
     * @param timeout:响应最长等待时间
     * @param body:当报头为1时的命令命令内容，用不到时默认为""
     * @param flag:标识符，与报头一致
     * @param en:1打开，0关闭 ,用不到时默认为1
     * @return
     */
    public String postCommand(String deviceId,String apiKey,int timeout,String body,int flag,int en)
    {
        String result="";
        BufferedReader in =null;
        DataOutputStream out=null;
        try {
            String url = "http://api.heclouds.com/v1/synccmds?device_id="+deviceId+"&timeout="+timeout;
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            HttpURLConnection connection=(HttpURLConnection)realUrl.openConnection();
            //设置通用的请求属性
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization",apiKey);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //建立实际的连接
            connection.connect();
            //下发命令
            out=new DataOutputStream(connection.getOutputStream());
            switch (flag){
                case 1: //10行每行20个字符，目前只实现了一页
                    //以“字节数组”形式发送给服务器信息、
                    byte[] data=body.getBytes();
                    out.writeByte(1);//报头
                    out.writeByte(1);//当前页数
                    out.writeByte(1);//总页数
                    out.write(data);
                    out.writeByte(0);
                    break;
                case 2:
                    out.writeByte(2);//报头
                    if(en==1) out.writeByte(1);
                    else out.writeByte(0);
                    out.writeByte(0);
                    break;
                case 3:
                    out.writeByte(3);//报头
                    out.writeByte(0);
                    out.writeByte(0);
                    break;
                case 4:
                    out.writeByte(11);
                    if(en==1) out.writeByte(1);
                    else out.writeByte(0);
                    out.writeByte(0);
                    break;
                case 5:
                    out.writeByte(5);
                    if(en==1) out.writeByte(1);
                    else out.writeByte(0);
                    out.writeByte(0);
                    break;
                case 6:
                    out.writeByte(6);
                    if(en==1) out.writeByte(1);
                    else out.writeByte(0);
                    out.writeByte(0);
                    break;
                case 7:
                    out.writeByte(7);
                    if(en==1) out.writeByte(1);
                    else out.writeByte(0);
                    out.writeByte(0);
                    break;
                case 8:
                    out.writeByte(8);
                    if(en==1) out.writeByte(1);
                    else out.writeByte(0);
                    out.writeByte(0);
                    break;
                case 10:
                    out.writeByte(10);
                    if(en==1) out.writeByte(1);
                    else out.writeByte(0);
                    out.writeByte(0);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + flag);
            }


            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;}
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("命令下发异常！ ");
        }

        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                System.out.println("IO close error !");
            }
        }
        System.out.println(result);
        return result;
    }
    /**
     * 辅助函数1
     * json数据转int型
     * @param data:json数据
     * @return
     */
    public int json2int(String data)
    {
        int result=0;
        //定位value值的首末位置
        int start=data.indexOf("value");
        int end=data.indexOf("}]");
        //获取value值的String类型
        String temp=data.substring(start+7, end);
        //System.out.println(temp);//测试
        //转int
        result=Integer.parseInt(temp);
        return result;
    }
    //中间变量
    private String t_zheng;
    private String t_xiao;
    private String g_zheng;
    private String g_xiao;
    private String s_zheng;
    private String s_xiao;
    private String w_z;
    private String w_x;
    private String j_z;
    private String j_x;
    private String x_temp;
    private String y_temp;
    private String z_temp;
    /**
     * 功能函数1
     * 实时更新温度
     * @param deviceId:设备ID
     * @param apiKey
     * @return
     */
    public void refresh_temperature(String deviceId,String apiKey)
    {
        //实时获取数据
        //字符串格式json数据
        t_zheng=getDataPoints(deviceId, apiKey, "wen_du_zheng", "", 1);
        t_xiao=getDataPoints(deviceId, apiKey, "wen_du_xiao", "", 1);
        //json2int
        temperature_zheng=json2int(t_zheng);
        temperature_xiao=json2int(t_xiao);
        //打印测试
        //System.out.println("温度为:"+temperature_zheng+"."+temperature_xiao);
        //System.out.println("光强为:"+guang_zheng+"."+guang_xiao);
        //System.out.println("湿度为:"+shi_du_zheng+"."+shi_du_xiao);
        return;
    }
    /**
     * 功能函数2
     * 实时更新光强
     * @param deviceId:设备ID
     * @param apiKey
     * @return
     */
    public void refresh_guang(String deviceId,String apiKey)
    {
        //实时获取数据
        g_zheng=getDataPoints(deviceId,apiKey, "guang_qiang_zheng", "", 1);
        g_xiao=getDataPoints(deviceId, apiKey, "guang_qiang_xiao", "", 1);
        guang_zheng=json2int(g_zheng);
        guang_xiao=json2int(g_xiao);
        return;
    }
    /**
     * 功能函数3
     * 实时更新湿度
     * @param deviceId:设备ID
     * @param apiKey
     * @return
     */
    public void refresh_shidu(String deviceId,String apiKey)
    {
        //实时获取数据
        s_zheng=getDataPoints(deviceId,apiKey, "shi_du_zheng", "", 1);
        s_xiao=getDataPoints(deviceId,apiKey, "shi_du_xiao", "", 1);
        shi_du_zheng=json2int(s_zheng);
        shi_du_xiao=json2int(s_xiao);
        return;
    }
    /**
     * 功能函数4
     * 实时更新经纬度
     * @param deviceId:设备ID
     * @param apiKey
     * @return
     */
    public void refresh_location(String deviceId,String apiKey)
    {
        //实时获取数据
        w_z=getDataPoints(deviceId,apiKey, "wz", "", 1);
        w_x=getDataPoints(deviceId,apiKey, "wx", "", 1);
        j_z=getDataPoints(deviceId,apiKey,"jz","",1);
        j_x=getDataPoints(deviceId,apiKey,"jx","",1);
        wz=json2int(w_z);
        wx=json2int(w_x);
        jz=json2int(j_z);
        jx=json2int(j_x);
        return;
    }
    /**
     * 功能函数5
     * 实时更新静态状态下的x,y,z坐标，x^2+y^2+z^2=1000000,球坐标
     * @param deviceId:设备ID
     * @param apiKey
     * @return
     */
    public void refresh_acce(String deviceId,String apiKey)
    {
        //实时获取数据
        x_temp=getDataPoints(deviceId,apiKey,"acce_x","",1);
        y_temp=getDataPoints(deviceId,apiKey,"acce_y","",1);
        z_temp=getDataPoints(deviceId,apiKey,"acce_z",",",1);
        acce_x=json2int(x_temp);
        acce_y=json2int(y_temp);
        acce_z=json2int(z_temp);
        return;
    }
    /**
     * 功能函数6
     * 实时更新箱子开闭状态
     * @param deviceId:设备ID
     * @param apiKey
     * @return
     */
    public void refresh_isopen(String deviceId,String apiKey)
    {
//        is_open=json2int(api_edp.getDataPoints(Device.deviceId_Camera,Device.apiKey_Camera,"box_open","",1));
        is_open = json2int(api_edp.getDataPoints(deviceId,apiKey,"open","",1));
    }
}