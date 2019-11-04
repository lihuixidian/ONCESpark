import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import scala.collection.mutable.ArrayBuffer
import org.apache.spark.rdd.RDD

import scala.io.Source
/**
  * Created by pengsizhe on 2017/4/10.
  */
class Item
{
  var logo=0
  var Item_Time=ArrayBuffer[Int]()//
//var Item_Len=Item_Time.length
  var Largest_time=0
  var Item_Find=false
  def init(args: Int*): Unit =
  {
    for(arg <-args)
    {
      Item_Time +=arg
    }
    print(Item_Time)
  }
}
object Hello {
  var freq_Sum=0
  def clear(items:ArrayBuffer[Item]): Unit =
  {
    for (item<-items)
    {
      item.Item_Time.clear()
    }
  }
  def init(args: Int*): ArrayBuffer[Item] =
  {
    var ab=new ArrayBuffer[Item]()
    for(arg<-args)
    {
      var item =new Item
      item.logo=arg
      ab +=item
    }
    ab
  }
  def initItem_Find(items:ArrayBuffer[Item]): Unit =
  {
    for(item<-items)
    {
      item.Item_Find=false
    }
  }
  def ValidateAndEliminate(items:ArrayBuffer[Item],constrainedTime:Int): List[Int] =
  {

    var returnnum=0
    var Tk=items(items.length-1).Item_Time(0)
    items(items.length-1).Largest_time=Tk
    for(i<- 0 to items(0).Item_Time.size-1)
    {
      if(((Tk-items(0).Item_Time(i))<=constrainedTime)&&(items(0).Item_Find==false))
      {
        items(0).Largest_time=items(0).Item_Time(i)
        items(0).Item_Find=true
      }
    }
    var Time_Up=items(0).Largest_time
    var num=1
    for(i<-0 to items.length-3)
    {
      var Ti=0
      for(j<-0 to items(num).Item_Time.size-1)
      {
        if((items(num).Item_Time(j)>Time_Up)&&(items(num).Item_Find==false))
        {
          Ti=items(num).Item_Time(j)
          items(num).Item_Find=true
        }
      }
      items(num).Largest_time=Ti
      Time_Up=Ti
      num+=1
    }
    var truenum=0
    for(item<-items)
    {
      if(item.Item_Find==true)
      {
        truenum+=1
      }
    }
    var firstitem=(-100)
    var isSure=true
    for(item<-items)
    {
      if(item.Largest_time<=firstitem)
      {
        isSure=false
      }
      firstitem=item.Largest_time
    }
    if(truenum==(items.size-1)&&(isSure))
    {
      for (item<-items)
      {
        var recordId=ArrayBuffer[Int]()
        for(i<- 0 to (item.Item_Time.size-1))
        {
          if(item.Item_Time(i)<=item.Largest_time)
          {
            recordId+=i
          }
        }
        for(i <-0 to recordId.size-1)
        {
          item.Item_Time.remove(0)
        }
      }
      for(item<-items)
      {
        var recordId2=ArrayBuffer[Int]()
        for(i<- 0 to item.Item_Time.size-1)
        {
          for(item2<-items)
          {

            if(item.Item_Time.size!=0)
            {
              if(item2.Largest_time==item.Item_Time(i))
              {
                recordId2+=i
              }
            }
          }
        }
        for(i <-0 to recordId2.size-1)
        {
          item.Item_Time.remove(0)
        }
      }
      returnnum=1
    }
    else
    {
      for (item<-items)
      {
        var recordId=ArrayBuffer[Int]()
        for(i<- 0 to (item.Item_Time.size-1))
        {
          if(item.Item_Time(i)<=item.Largest_time)
          {
            recordId+=i
          }
        }
        for(i <-0 to recordId.size-1)
        {
          item.Item_Time.remove(0)
        }
      }
    }
    var V_Empty_Item=0
    var wheredone=true
    for(item<-items)
    {
      if((item.Item_Time.size!=0)&&(wheredone))
      {
        V_Empty_Item+=1
      }
      else
      {
        wheredone=false
      }
    }
    initItem_Find(items)
    var resu=List(returnnum,V_Empty_Item)
    return resu
  }
  def ListUpdate(items:ArrayBuffer[Item],Se:(Int,Int),constrainedTime:Int,Empty_Item:Int): Int =
  {
    for(item<-items)
    {
      if(item.Item_Time.size!=0)
      {
        if(item.logo==Se._1)
        {
          item.Item_Time+=Se._2
          if(Se._2-item.Item_Time(0)>constrainedTime)
          {
            item.Item_Time.remove(0)
          }
        }
      }
    }
    var L_Empty_Item=Empty_Item
    if(Se._1==items(Empty_Item).logo)
    {
      items(Empty_Item).Item_Time+=Se._2
      L_Empty_Item =L_Empty_Item+1
    }
    return L_Empty_Item
  }
  var last_location_S=(-1)//存储发现序列的最后出现位置(倒数)
 /* def countseq_S(Seq:List[(Int,Int)],begin:Int,block:Int,items:ArrayBuffer[Item]): Int =
  {
    last_location_S=(-1)
    Empty_Item=0
    var freq=0
    var windowSize=10
    for(i<-begin to block-1)
    {
  //    ListUpdate(items,Seq(i)._1,Seq(i)._2.toInt,windowSize)
      if(Empty_Item==items.size)
      {
        var flag=ValidateAndEliminate(items,windowSize)
        if(flag)
        {
          freq+=1
          freq_Sum+=1
          last_location_S=block-i
        }
      }
    }
    for(item<-items)
    {
      print(item.logo+",")
    }
    println("序列在本次List中出现："+freq+"次.最后出现的位置是List的倒数第"+last_location_S+"位")
    if(last_location_S==1)
    {
      freq=freq-1
    }
    return freq
  }
  def countseq_C(Seq:List[(Int,Int)],begin:Int,block:Int,items:ArrayBuffer[Item]): ArrayBuffer[Int] =
  {
    var last_location=(-1)//用来存储最后一个符合条件的位置，在S2中将其位置前的元素全部清除
    Empty_Item=0
    var frequency=new ArrayBuffer[Int]()
    var freq=0
    for(i<-0 to Seq.size-1)
    {
    //  ListUpdate(items,Seq(i)._1,Seq(i)._2.toInt,block)
      if(Empty_Item==items.size)
      {
        var flag=ValidateAndEliminate(items,block)
        if(flag)
        {

          freq+=1
          freq_Sum+=1
          last_location=i
        }
      }
    }
    frequency+=freq
    frequency+=last_location-1
    return frequency
  }*/
 /* var CLastOne: List[(Int,Int)]=List((-1,-1))
  def FindSeq_C(Seq:List[(Int,Int)],CHalfSize:Int,SSize:Int): Int ={
    var CFirstOne: List[(Int,Int)]=Seq take(CHalfSize)
    var C:List[(Int,Int)]=CLastOne++CFirstOne
    println("C的序列是"+C)
    var c_star_position=0
    if(last_location_S<=CHalfSize&&last_location_S!=(-1))
    {
      c_star_position=CHalfSize-last_location_S+1
    }
    var C_freq= countseq_C(C,c_star_position,C.size,e)
    CLastOne=Seq drop(SSize-CHalfSize)
    if(C_freq(0)!=0)
    {
      println("在序列C中发现待挖掘序列"+"返回数值"+C_freq(1))
      return C_freq(1)
    }else
    {
      return 0
    }
  }
  val e=init(8,8)//这里是待挖掘项，当前待计数的序列是（8，8）
  def work(Seq:List[(Int,Int)],begin:Int,block:Int):ArrayBuffer[Int]=
  {
    var freq_S= countseq_S(Seq,begin,Seq.size,e)
    var FREQ=ArrayBuffer[Int]()
    return FREQ
  }*/
 def test(d: String): Unit =
 {
   println(" ")
   var logo=List(-1)
   var comingtime=List(-1)
   var numPattern="[0-9]+".r
   var line=numPattern.findAllIn(d)
   var countnum=0
   for(num<-line)
   {
     if(countnum%2==0)
     {
       if(countnum==0)
       {
         logo=List(num.toInt)
       }
       else
       {
         var logo_new=List(num.toInt)
         logo=logo++logo_new
       }
       countnum+=1
     }else
     {
       if(countnum==1)
       {
         comingtime=List(num.toInt)
       }
       else
       {
         var comingtime_new=List(num.toInt)
         comingtime=comingtime++comingtime_new
       }
       countnum += 1
     }
   }
   var S=logo.zip(comingtime)

 }

  def main(args: Array[String]): Unit = {

  /* var logo=new ArrayBuffer[Int]
    var comingtime=new ArrayBuffer[Int]
    val source=Source.fromFile("F:\\test_2.txt","gb2312")
//    val source=Source.fromFile(args(0),"gb2312")
    for(lines <- source.getLines()) {
      var numPattern = "[0-9]+".r
      var line = numPattern.findAllIn(lines.toString())
      var countnum = 0
      for (num <- line) {
          if (countnum % 2 == 0) {
            logo += num.toInt
            countnum += 1
          } else {
            comingtime += num.toInt
            countnum += 1
          }
      }
    }
    var S = logo.zip(comingtime)
    source.close()*/

    val conf:SparkConf = new SparkConf().setAppName("psz")//.setMaster("local[4]")
    val sc:SparkContext = new SparkContext(conf)
    val data=sc.sequenceFile(args(0),classOf[IntWritable],classOf[IntWritable],8).map{case (x, y)=>(x.get(),y.get())}
    var star_time = System.nanoTime()
  //  var rdd=sc.makeRDD(S,4)
    var resrdd=data.mapPartitionsWithIndex{
      (x,iterator)=>{
        var res=List[Int]()
        var freq=0
        var Empty_Item=0
        val eee=init(8,8)//这里是待挖掘项，当前待计数的序列是（8，8）
        while(iterator.hasNext)
          {
            var windowSize=10
            var xnext=iterator.next()
            Empty_Item=ListUpdate(eee,xnext,windowSize,Empty_Item)
            if(Empty_Item==eee.size)
            {
              var flag=ValidateAndEliminate(eee,windowSize)
              if(flag(0)==1)
              {
                freq+=1
              }
              Empty_Item=flag(1)
            }
          }
        res.::(freq).iterator
      }
    }
    var finish_time = System.nanoTime()
    var run_time=finish_time-star_time
    resrdd.collect().foreach(println)
    //println("总共有"+resrdd.count()+"个数据对")
    println("花费时间："+run_time+"")
  }
}
