package org.zznode.entity;

import java.math.BigDecimal;

public class CaseReferrer {
   /**
     * 资源编号
     */
    private String dataid;

    /**
     * 资源名称
     */
    private String dataname;

    /**
     * 资源存放路径
     */
    private String datapath;

    /**
     * 场景缩略图存放路径
     */
    private String imgpath;

   /**

     * 评论人数
     */
    private Long acount;

    /**
     * 评分
     */
    private BigDecimal average;



 public String getDataid() {
  return dataid;
 }

 public void setDataid(String dataid) {
  this.dataid = dataid;
 }

 public String getDataname() {
  return dataname;
 }

 public void setDataname(String dataname) {
  this.dataname = dataname;
 }

 public String getDatapath() {
  return datapath;
 }

 public void setDatapath(String datapath) {
  this.datapath = datapath;
 }

 public String getImgpath() {
  return imgpath;
 }

 public void setImgpath(String imgpath) {
  this.imgpath = imgpath;
 }

 public BigDecimal getAverage() {
  return average;
 }

 public void setAverage(BigDecimal average) {
  this.average = average;
 }

 public Long getAcount() {
  return acount;
 }

 public void setAcount(Long acount) {
  this.acount = acount;
 }

 public CaseReferrer(String dataid, String dataname, String datapath, String imgpath, Long acount, BigDecimal average) {
  this.dataid = dataid;
  this.dataname = dataname;
  this.datapath = datapath;
  this.imgpath = imgpath;
  this.acount = acount;
  this.average = average;
 }

 public CaseReferrer() {
 }
}