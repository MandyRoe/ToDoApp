package com.example.todoapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_analysis.*


class AnalysisActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
   // private lateinit var doneList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)
       // doneList = arrayListOf<String>()


        val a: ArrayList<String> = arrayListOf<String>()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (todoSnapshot in snapshot.children) {

                    if (todoSnapshot.key!!.contains(uid!!)) {

                        if(todoSnapshot.child("done").getValue() ==true){
                            val doneDate = todoSnapshot.child("doneDate").getValue().toString()
                            a.add(doneDate)

                        }
                    }
                }


                val entries: ArrayList<BarEntry> = ArrayList()
                var doneJAN = 0; var doneFEB = 0; var doneMAR = 0; var doneAPR = 0; var doneMAY = 0; var doneJUN = 0;
                var doneJUL = 0; var doneAUG = 0; var doneSEP = 0; var doneOCT = 0; var doneNOV = 0; var doneDEC = 0;

                for (i in a.indices) {
                    when(a[i].get(5) + "" + a[i].get(6)){
                        "01" -> doneJAN++
                        "02" -> doneFEB++
                        "03" -> doneMAR++
                        "04" -> doneAPR++
                        "05" -> doneMAY++
                        "06" -> doneJUN++
                        "07" -> doneJUL++
                        "08" -> doneAUG++
                        "09" -> doneSEP++
                        "10" -> doneOCT++
                        "11" -> doneNOV++
                        "12" -> doneDEC++

                    }
                    println(a[i])
                }
                entries.add(BarEntry(1f, doneJAN.toFloat()))
                entries.add(BarEntry(2f, doneFEB.toFloat()))
                entries.add(BarEntry(3f, doneMAR.toFloat()))
                entries.add(BarEntry(4f, doneAPR.toFloat()))
                entries.add(BarEntry(5f, doneMAY.toFloat()))
                entries.add(BarEntry(6f, doneJUN.toFloat()))
                entries.add(BarEntry(7f, doneJUL.toFloat()))
                entries.add(BarEntry(8f, doneAUG.toFloat()))
                entries.add(BarEntry(9f, doneSEP.toFloat()))
                entries.add(BarEntry(10f, doneOCT.toFloat()))
                entries.add(BarEntry(11f, doneNOV.toFloat()))
                entries.add(BarEntry(12f, doneDEC.toFloat()))

                val barDataSet = BarDataSet(entries, "")
                barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)


                val data = BarData(barDataSet)
                barChart.data = data


                //hide grid lines
                barChart.axisLeft.setDrawGridLines(false)
                barChart.xAxis.setDrawGridLines(false)
                barChart.xAxis.setDrawAxisLine(false)

                //remove right y-axis
                barChart.axisRight.isEnabled = false

                //remove legend
                barChart.legend.isEnabled = true


                //remove description label
                barChart.description.isEnabled = false


                //add animation
                barChart.animateY(1500)

                barChart.xAxis.granularity = 1f
                barChart.xAxis.setDrawLabels(true)
                barChart.xAxis.labelRotationAngle = +90f
                barChart.xAxis.labelCount

                //draw chart
                barChart.invalidate()




            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

                                                            //https://intensecoder.com/bar-chart-tutorial-in-android-using-kotlin/
    }


/*
    private fun setBarChart() {

        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add(BarEntry(1f, 4f))
        entries.add(BarEntry(2f, 10f))
        entries.add(BarEntry(3f, 2f))
        entries.add(BarEntry(4f, 15f))
        entries.add(BarEntry(5f, 13f))
        entries.add(BarEntry(6f, 2f))

        val barDataSet = BarDataSet(entries, "")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data = BarData(barDataSet)
        barChart.data = data


        //hide grid lines
        barChart.axisLeft.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.setDrawAxisLine(false)

        //remove right y-axis
        barChart.axisRight.isEnabled = false

        //remove legend
        barChart.legend.isEnabled = false


        //remove description label
        barChart.description.isEnabled = false


        //add animation
        barChart.animateY(1500)


        //draw chart
        barChart.invalidate()
    }


    private fun dynamicBarChart() {

        val entries: ArrayList<BarEntry> = ArrayList()
        for (i in a.indices) {
                val done = a[i]
                entries.add(BarEntry(i.toFloat(), done.toFloat()))
            }

        val barDataSet = BarDataSet(entries, "")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data = BarData(barDataSet)
        barChart.data = data


        //hide grid lines
        barChart.axisLeft.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.setDrawAxisLine(false)

        //remove right y-axis
        barChart.axisRight.isEnabled = false

        //remove legend
        barChart.legend.isEnabled = false


        //remove description label
        barChart.description.isEnabled = false


        //add animation
        barChart.animateY(1500)


        //draw chart
        barChart.invalidate()
    }


    //create Array List of done dates for dynamic Bar Chart
    private fun getDoneList(){
        val a: ArrayList<String> = arrayListOf<String>()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (todoSnapshot in snapshot.children) {

                    if (todoSnapshot.key!!.contains(uid!!)) {

                        if(todoSnapshot.child("done").getValue() ==true){
                            val doneDate = todoSnapshot.child("doneDate").getValue().toString()
                            a.add(doneDate)

                        }
                    }
                }
                    println(a)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
*/
}