
/ **  
*     cube, 41063625 (3453), can be permuted to produce two other cubes: 56623104 (3843) and 66430125 (4053).
*     In fact, 41063625 is the smallest cube which has exactly three permutations of its digits which are also cube.
*     Find the smallest cube for which exactly five permutations of its digits are cube. 
*/

import collection.mutable.Map
import collection.mutable.ArrayBuffer


object WhisperCodingTest {
  
  def findMinCub(number : Long, permuteCount :Int): Long ={
   var n :Long = number
   val cubeMap = Map[collection.immutable.Map[Char,Int], ArrayBuffer[Long]]()
   while(n < Long.MaxValue){
     var cube :Long = n*n*n
     val  smap = cube.toString.groupBy(_.toChar).map{ p => (p._1, p._2.length)}
     if(cubeMap.contains(smap)){
       if (cubeMap(smap).size+1 == permuteCount)
         return cubeMap(smap).min
       else
        cubeMap(smap).append(n)
     }else{
       cubeMap(smap) =  ArrayBuffer[Long](n)
     }
   n+=1
   }
   //throw exception
    return -1
  }
}
