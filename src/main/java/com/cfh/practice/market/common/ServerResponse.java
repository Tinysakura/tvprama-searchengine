package com.cfh.practice.market.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 根据请求的状态对用户的请求做通用的封装
 * @author Mr.Chen
 * date: 2018年7月14日 下午2:50:53
 */
//保证序列化json的时候,如果是null的对象,key也会消失
@JsonSerialize(include =  JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T>{
    T data;
    int status;
    String msg;

    private ServerResponse(int status){
        this.status = status;
    }

    private ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status,String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }

    //只需要返回请求成功的响应
    public static<T> ServerResponse<T> createSuccessResponse(){
        return new ServerResponse<>(ResponseCode.SUCCESS);
    }

    //将响应成功的结果返回
    public static<T> ServerResponse<T> createSuccessResponse(T data){
        return new ServerResponse<>(ResponseCode.SUCCESS,data);
    }

    //将响应成功的的描述返回
    public static<T> ServerResponse<T> createSuccessResponse(String msg){
        return new ServerResponse<>(ResponseCode.SUCCESS,msg);
    }

    //将响应成功的结果与描述返回
    public static<T> ServerResponse<T> createSuccessResponse(String msg,T data){
        return new ServerResponse<>(ResponseCode.SUCCESS,msg,data);
    }

    //只需要返回错误的响应
    public static<T> ServerResponse<T> createErrorResponse(){
        return new ServerResponse<>(ResponseCode.ERROR);
    }

    //返回具体错误的类型
    public static<T> ServerResponse<T> createErrorResponse(int status){
        return new ServerResponse<>(status);
    }

    //返回错误的信息
    public static<T> ServerResponse<T> createErrorResponse(String msg){
        return new ServerResponse<>(ResponseCode.ERROR,msg);
    }

    //返回具体的错误类型与错误信息
    public static<T> ServerResponse<T> createErrorResponse(int status,String msg){
        return new ServerResponse<>(status,msg);
    }

    //判断请求是否成功
    @JsonIgnore
    public boolean success(){
        return this.status == 1;
    }

    public T getData(){
        return data;
    }

    public String getMsg(){
        return msg;
    }

    public int getStatus() {
        return status;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class ResponseCode{
        public static final int SUCCESS = 1;
        public static final int ERROR = 2;
        public static final int NEED_LOGING = 10;
        public static final int ILLEGAL_ARGUMENT = 2;
    }
}