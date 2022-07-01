package com.example.test3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 为OneNet平台的多协议接入的EDP协议调用API提供的java方法
 *
 * @author hnu根根儿
 *
 */
public class API_EDP extends Thread implements Device{
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
     * 获取文件(无需deviceid)
     * @param apiKey
     * @param box_id:开箱id(第几次开箱)
     * @param zhen:第几帧
     * @return
     */
    public byte[] getFile(String apiKey,int box_id,int zhen) {
        BufferedReader in =null;
        byte []datas=null;
        try {
            String url = "http://api.heclouds.com"+"/bindata"+"/"+index_front+box_id+"-"+zhen+".jpg";
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            HttpURLConnection connection=(HttpURLConnection)realUrl.openConnection();
            //设置通用的请求属性
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization",apiKey);
            //建立实际的连接
            connection.connect();
            //定义 BufferedReader输入流来读取URL的响应
            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 操作（分段读取）
            byte[] flush = new byte[1024];// 缓冲容器
            int len = -1;// 接收长度
            while ((len = is.read(flush)) != -1) {
                baos.write(flush, 0, len);
            }
            baos.flush();
            // 获取数据
            datas = baos.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取文件出现异常！ ");
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
        return datas;
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
    public String postCommand(String deviceId,String apiKey,int timeout,String body,int flag,int en) {
        String result = "";
        BufferedReader in = null;
        DataOutputStream out = null;
        try {
            String url = "http://api.heclouds.com/cmds?device_id=" + deviceId + "&timeout=" + timeout;
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            //设置通用的请求属性
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", apiKey);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //建立实际的连接
            connection.connect();
            //下发命令
            out = new DataOutputStream(connection.getOutputStream());
            switch (flag) {
                case 9:
                    out.writeByte(9);
                    if (en == 1) out.writeByte(1);
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
     * 功能函数1：查找该数据流是否存在
     * @param deviceId:设备ID
     * @param apiKey
     * @param box_id:开箱id
     * @param zhen:第几帧
     * @return 如果没有该数据流则返回0，有则返回1
     */
    public int Lookup(String deviceId,String apiKey,int box_id,int zhen)
    {
        String datastreamId=box_id+"-"+zhen+".jpg";//数据流id
        String result=getDataPoints(deviceId, apiKey, datastreamId, "", 1);//原始json数据
        //定位count值的首末位置
        int start=result.indexOf("count");
        int end=result.indexOf(",\"datastreams");
        //获取count值的String类型
        result=result.substring(start+7, end);
        if(result.equals("0"))
            return 0;
        else {
            return 1;
        }
    }
    /**
     * 功能函数2：返回最新的开箱id
     * @param deviceId:设备ID
     * @param apiKey
     * @return
     */
    public int getBox_id(String deviceId,String apiKey)
    {
        String temp = this.getDataPoints(deviceId,apiKey,"lastest_id","",1);
        return json2int(temp);
    }
    /**
     * 功能函数3：返回第num次开箱id的总帧数
     * @param deviceId:设备ID
     * @param apiKey
     * @return
     */
    public int getBox_sum(String deviceId,String apiKey,int num)
    {
        //数据流名称
        String datastream = "opid:"+num;
        //json数据字符串
        String temp = this.getDataPoints(deviceId,apiKey,datastream,"",1);
        //获取value的String类型
        String value = this.json2string(temp);
        //定位总帧数的首末位置
        int start = 0;
        int end = value.indexOf(" ");
        //字符串截取再转int
        String result = value.substring(start,end);
        //System.out.println(result);
        return Integer.parseInt(result);
    }
    /**
     * 功能函数4：返回第num次开箱id的时间字符串
     * @param deviceId:设备ID
     * @param apiKey
     * @return 格式示例：2021-08-10--17:04:04
     */
    public String getBox_time(String deviceId,String apiKey,int num)
    {
        //数据流名称
        String datastream = "opid:"+num;
        //json数据字符串
        String temp = this.getDataPoints(deviceId,apiKey,datastream,"",1);
        //获取value的String类型
        String value = this.json2string(temp);
        //截取字符串,并将逗号变为冒号
        int index1 = value.indexOf(" ");
        int index2 = value.indexOf(",");
        String result = value.substring(index1+1,index2);
        result += ":";
        result += value.substring(index2+1,index2+3);
        result += ":";
        result += value.substring(index2+4,index2+6);
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
        //System.out.println("data:"+data);
        String temp=data.substring(start+7, end);
        //System.out.println(temp);//测试
        //转int
        result=Integer.parseInt(temp);
        return result;
    }
    /**
     * 辅助函数2
     * json数据转string型
     * @param data:json数据
     * @return
     */
    public String json2string(String data)
    {
        String result = "";
        //定位value值的首末位置
        int start = data.indexOf("value");
        int end = data.indexOf("}]");
        //获取value的String类型
        result = data.substring(start+8,end-1);
        return  result;
    }
}