import java.io.PrintWriter
import java.util.Random
import javax.security.auth.login.{AppConfigurationEntry, Configuration}

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.SequenceFile
import org.apache.hadoop.io._
import org.apache.hadoop.io.SequenceFile.{CompressionType, Writer}
import org.apache.spark.{SparkConf, SparkContext}
object ONCEStreamingSender {
  var time=0
  def makeSeq() : List[(Int,Int)]=
  {
    var Seq: List[(Int,Int)]=List((-1,-1))
    // val now = new Date()
    // var time=now.getTime//runtime test
	//change the upper bound of i to control the length for the generated sequence
    for(i<-0 to 1000)
    {
      val randomNum=(new Random).nextInt(100)
      time+=1
      if(i==0)
        {
          Seq=List((randomNum,time))
        }
      else
        {
          var newSeq=List((randomNum,time))
          Seq=Seq++newSeq
        }
      if(time%1000==0)
        {
          println(time)
        }
    }
    return Seq
  }
  def main(args:Array[String]) =
  {
    val conf:SparkConf = new SparkConf().setAppName("psz").setMaster("local[2]")
    val sc:SparkContext = new SparkContext(conf)
    var send = makeSeq()
    var i=0
	//change the upper bound of i to control the length for the generated sequence
    while (i<600)
      {
        send=makeSeq()++send
        i=i+1
      }
    val data =sc.parallelize(send)
    println(data.count())
    data.saveAsSequenceFile("F:\\test_60w")
    //write to file
    /*new Thread() {
      override def run(): Unit = {
        var writerFile = new PrintWriter(new File("F://test_5.txt"))
        while (true) {
          Thread.sleep(2)
          var send = makeSeq()
          writerFile.print(send)
        }
        writerFile.close()
      }
    }.start()*/

   /* val listener =new ServerSocket(8888)
    while(true)
      {
        val socket=listener.accept()
        new Thread()
        {
          override def run(): Unit = {
            println("Client Link Address and Port"+socket.getInetAddress+":"+socket.getPort)
            var out=new PrintWriter(socket.getOutputStream(),true)
            while(true)
              {
                Thread.sleep(10000)
                var send=makeSeq()+":"
                //write to file
                println(send)
                out.write(send+'\n')
                out.flush()
              }

            socket.close()
          }
        }
          .start()
      }*/
  }
}
